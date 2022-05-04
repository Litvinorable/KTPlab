package lab8;

import java.net.*;

public class URLDepthPair {

    private URL URL;
    private int depth;

    //Конструктор класса для хранения.
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

    //Возвращает объект класса типа URL
    public URL getURL() {
        return URL;
    }

    //Возвращает глубину сайта, относительно введённого
    public int getDepth() {
        return depth;
    }

    //Возвращает имя хоста на сервере
    public String getHost() {
        return URL.getHost();
    }

    //Возвращает имя ресурса у хоста
    public String getDocPath() {
        return URL.getPath();
    }
}