package lab8;

import java.net.MalformedURLException;
import java.util.LinkedList;
import java.net.URL;

public class Crawler{

    // выводит список сайтов
    public static void getSites(LinkedList<URLDepthPair> viewedLink)
    {
        for (URLDepthPair c : viewedLink)
            System.out.println(c.toString());
    }

    public static void crawl(String startURL, int maxDepth, int numThreads)
            throws MalformedURLException {

        //Инициализируем пул адресов
        UrlPool pool = new UrlPool(maxDepth);
        //Формируем первый элемент
        URL rootURL = new URL(startURL);
        URLDepthPair urlPair = new URLDepthPair(rootURL, 0);
        //Добавляем в пул
        pool.addPair(urlPair);

        // Запускаем потоки. Количество может задаваться пользователем. 1 - по умолчанию.
        for (int i = 0; i < numThreads; i++) {
            //передаём пул адресов и id (нужен для вывода в консоль) в каждый созданный поток
            CrawlerTask c = new CrawlerTask(i+1,pool);
            // Thread - функциональность отдельного потока
            Thread t = new Thread(c);
            //Запускаем поток
            t.start();
        }

        // Ожидаем завершения работы всех потоков
        while (pool.getWaitCount() != numThreads) {
            try {
                Thread.sleep(100); // 0.1 second
            } catch (InterruptedException ie) {
                System.out.println("Something went wrong!");
            }
        }

        System.out.printf( "\nСписок просмотренных сайтов (%d):", pool.allSitesSeen().size());
        System.out.println();
        // Выводим фул список
        getSites(pool.allSitesSeen());
    }

    public static void main(String[] args) {

        String[] arg = new String[]{"https://mtuci.ru/?", "2", "4"};
        if (arg.length != 3)
        {
            System.out.println("The number of arguments must be 3");
            System.exit(1);
        }

        try {
            crawl(arg[0], Integer.parseInt(arg[1]), Integer.parseInt(arg[2]));
        }
        catch (MalformedURLException e) {
            // Исключение, если неправильно введен URL
            System.err.println("Error: URL is invalid");
            System.exit(1);
        }
        System.exit(0);
    }
}