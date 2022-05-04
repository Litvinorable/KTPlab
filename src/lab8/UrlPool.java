package lab8;

import java.util.HashSet;
import java.util.LinkedList;
import java.net.URL;

public class UrlPool {
    // Список всех сайтов, которые мы успешно просмотрели
    static LinkedList<URLDepthPair> allSitesSeen = new LinkedList<URLDepthPair>();
    // Список всех сайтов, которые мы хотим просмотреть
    static LinkedList<URLDepthPair> notSeenSites = new LinkedList<URLDepthPair>();
    //Максимальная глубина относительно сайта введённого пользователем.
    int maxDepth;
    // Число потоков которые ждут адрес для обработки
    int waitCount;
    //Список хэшированных адресов, которые мы просмотрели.
    //Это позволяет обеспечить уникальность адреса в результирующем списке.
    static HashSet<URL> seenURLs = new HashSet<URL>();

    //Конструктор класса, принимающий на вход значения максимальной глубины
    public UrlPool(int maxDepth) {
        this.maxDepth = maxDepth;
        waitCount = 0;
    }

    //Метод для получения пары для обработки из потока
    //Так как к методу будут обращаться несколько потоков - устанавливаем для него синхронизацию
    public synchronized URLDepthPair getNextPair() {
        // бесконечный цикл, в котором будут сидеть потоки и ждать работу
        while (notSeenSites.size() == 0) {
            try {
                waitCount++;
                wait();
                waitCount--;
            } catch (InterruptedException e) {
            }
        }
        URLDepthPair nextPair = notSeenSites.removeFirst();
        return nextPair;
    }

    //Метод для добавления пары из потока в результирующий список
    //Он обеспечивает уникальность адреса и дополнительную проверку выхода за пределы "глубины"
    public synchronized void addPair(URLDepthPair pair) {
        if (seenURLs.contains(pair.getURL())) {
            return;
        }
        allSitesSeen.add(pair);
        seenURLs.add(pair.getURL());
        if (pair.getDepth() < maxDepth) {
            notSeenSites.add(pair);
            // Уведомляем все приостановленные потоки о том, что пара была добавлена
            notify();
        }
    }

    // Получить количество ожидающих потоков
    public synchronized int getWaitCount() {
        return waitCount;
    }

    // Метод для получения максимальной глубины адресов
    //для ускорения работы потоков путём пропуска адресов выходящих за границу глубины
    public synchronized int getMaxDepth() {
        return maxDepth;
    }

    // Получить все просмотренные URL-адреса
    public LinkedList<URLDepthPair> allSitesSeen() {
        return allSitesSeen;
    }
}