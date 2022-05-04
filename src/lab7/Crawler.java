package lab7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedList;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.net.*; //для сокетов

public class Crawler {
    // HTML href тэг, по которому будем ловить ссылки
    static final String HREF_TAG = "<a href=\"http";

    // Список всех сайтов, которые мы успешно просмотрели
    static LinkedList<URLDepthPair> allSitesSeen = new LinkedList<URLDepthPair>();
    // Список всех сайтов, которые мы хотим просмотреть
    static LinkedList<URLDepthPair> notSeenSites = new LinkedList<URLDepthPair>();

    // выводит список сайтов
    public static void getSites(LinkedList<URLDepthPair> viewedLink)
    {
        for (URLDepthPair c : viewedLink)
            System.out.println(c.toString());
    }

    public static void crawl(String startURL, int maxDepth)
            throws MalformedURLException {

        // закидываем начальный сайт в список
        URL rootURL = new URL(startURL);
        URLDepthPair urlPair = new URLDepthPair(rootURL, 0);
        notSeenSites.add(urlPair);

        int depth;
        //Список хэшированных адресов, которые мы просмотрели.
        //это позволяет обеспечить уникальность адреса в результирующем списке.
        HashSet<URL> seenURLs = new HashSet<URL>();
        seenURLs.add(rootURL);

        //Создаём "фабрику" для формирования SSL-сокета.
        SocketFactory socketFactory = SSLSocketFactory.getDefault();

        // Будем выполнять поиск пока есть хоть один не просмотренный сайт.
        // Метод isEmpty() возвращает true, если список не содержит элементов.
        while (!notSeenSites.isEmpty()) {
            // Получаем параметры сайта, с которым будем работать
            URLDepthPair currPair = notSeenSites.removeFirst();
            depth = currPair.getDepth();
            if (depth>maxDepth){
                continue;
            }

            // Инициализируем сокет и отравляем запрос на сайт.
            // сокет соединяет в сети две программы (сервер - клиент)
            Socket socket;
            try {
                // Подключаемся к хосту через порт
                socket = socketFactory.createSocket(currPair.getHost(), 443);
                socket.setSoTimeout(5000); //время ожидания в миллисекундах
                System.out.println("Connecting to " + currPair.getURL());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                // Отправляем HTTP запрос
                out.println("GET " + currPair.getDocPath() + " HTTP/1.1");
                out.println("Host: " + currPair.getHost());
                out.println("Connection: close");
                out.println();
            }
            //В случае ошибки ловим исключения
            //недопустимое имя хоста
            catch (UnknownHostException e) {
                System.err.println("Host "+ currPair.getHost() + " couldn't be determined");
                continue;
            }
            //Ошибка подключения к сокету
            catch (SocketException e) {
                System.err.println("Error with socket connection: " + currPair.getURL() +
                        " - " + e.getMessage());
                continue;
            }
            //Не удалось получить страницу
            catch (IOException e) {
                System.err.println("Couldn't retrieve page at " + currPair.getURL() +
                        " - " + e.getMessage());
                continue;
            }

            String line;
            int lineLength;
            int shiftIdx;
            boolean firstTry = true;
            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while ((line = in.readLine())!=null){
                    // В первой строке содержится результат запроса к сайту
                    if(firstTry){
                        firstTry = false;
                        // если всё хорошо - сообщаем пользователю и продолжаем
                        if(line.equals("HTTP/1.1 200 OK")){
                            System.out.println("Connected successfully!");
                            continue;
                        }else{
                            // иначе переходим к следующей паре.
                            System.out.println("Server return error: " + line);
                            break;
                        }
                    }
                    // Проверка содержит ли строка URL
                    boolean foundFullLink = false;
                    int idx = line.indexOf(HREF_TAG);
                    if (idx > 0) {
                        // Сдвигаемся к началу найденного URL посимвольно считываем его
                        StringBuilder sb = new StringBuilder();
                        shiftIdx = idx + 9;
                        char c = line.charAt(shiftIdx);
                        lineLength = line.length();
                        while ( c != '"' && shiftIdx < lineLength - 1) {
                            sb.append(c);
                            shiftIdx++;
                            c = line.charAt(shiftIdx);
                            // Если находим новую ссылку
                            if (c == '"') {
                                foundFullLink = true;
                            }
                        }
                        // Пытаемся создать новую пару.
                        if(foundFullLink) {
                            //Проверка на корректность URL
                            URL currentURL = new URL(sb.toString());
                            //Проверка на допустимость глубины и является ли сайт уникальным
                            if (!seenURLs.contains(currentURL)){
                                URLDepthPair newPair = new URLDepthPair(currentURL, depth + 1);
                                notSeenSites.add(newPair);
                                seenURLs.add(currentURL);
                            }
                        }
                    }
                }
                //Закрываем сокет
                in.close();
                socket.close();
                //Добавляем в результирующий список
                allSitesSeen.add(currPair);
            }
            catch (IOException e) {
            }
        }
        // Выводим результаты
        System.out.printf( "\nСписок просмотренных сайтов (%d):", allSitesSeen.size());
        System.out.println();
        // Выводим фул список
        getSites(allSitesSeen);
    }

    public static void main(String[] args){
        String[] arg = new String[]{"https://mtuci.ru/?","1"};

        if (arg.length != 2)
        {
            System.out.println("The number of arguments must be 2");
            System.exit(1);
        }

        try {
            crawl(arg[0], Integer.parseInt(arg[1]));
        }
        catch (MalformedURLException e) {
            // Исключение, если неправильно введен URL
            System.err.println("Error: URL is invalid");
            System.exit(1);
        }
        System.exit(0);
    }
}
