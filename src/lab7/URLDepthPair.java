package lab7;

import java.net.*;

public class URLDepthPair {
    //переменные для хранения ссылки и её глубины
    private URL URL;
    private int depth;

    //Конструктор класса для хранения
    public URLDepthPair(URL url, int d) throws MalformedURLException {
        URL = new URL(url.toString());
        depth = d;
    }

    //Возвращает строку состоящую из адреса сайта и его глубины
    @Override
    public String toString()
    {
        return "URL: " + URL.toString() + ", Depth: " + depth;
    }

    //Возвращает объект класса типа URL(полный путь до сайта)
    public URL getURL() {
        return URL;
    }

    //Возвращает глубину сайта, относительно введённого
    public int getDepth() {
        return depth;
    }

    //Возвращает имя хоста на сервер
    public String getHost() {
        return URL.getHost();
    }

    //Возвращает имя ресурса у хоста
    public String getDocPath() {
        return URL.getPath();
    }
}