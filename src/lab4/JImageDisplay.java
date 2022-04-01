package lab4;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class JImageDisplay extends JComponent{
    private BufferedImage img;

    //Конструктор для создания объекта
    JImageDisplay(int w, int h){
        img = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);

        Dimension imageDimension = new Dimension(w, h);
        super.setPreferredSize(imageDimension);
    }

    //Выводит на экран
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, img.getWidth(),img.getHeight(), null);
    }

    //Очищение
    public void clearImage()
    {
        int[] blankArray = new int[getWidth() * getHeight()];
        img.setRGB(0, 0, getWidth(), getHeight(), blankArray, 0, 1);
    }

    //Устанавливает пиксель в определенный цвет
    public void drawPixel(int x, int y, int rgbColor)
    {
        img.setRGB(x, y, rgbColor);
    }
}
