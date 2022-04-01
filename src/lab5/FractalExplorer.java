package lab5;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import javax.swing.JFileChooser.*;
import javax.swing.filechooser.*;
import javax.imageio.ImageIO.*;
import java.awt.image.*;

public class FractalExplorer
{
    private int displaySize;
    private JImageDisplay display;
    private FractalGenerator fractal;
    private Rectangle2D.Double range;

    public FractalExplorer(int size) {
        displaySize = size;

        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);

    }

    //Инициализирует графический интерфейс
    public void createAndShowGUI()
    {
        display.setLayout(new BorderLayout());
        JFrame myFrame = new JFrame("Fractal Explorer");

        myFrame.add(display, BorderLayout.CENTER);
        JButton resetButton = new JButton("Reset");

        ButtonHandler resetHandler = new ButtonHandler();
        resetButton.addActionListener(resetHandler);

        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComboBox myComboBox = new JComboBox(); //выпадающий список

        FractalGenerator mandelbrotFractal = new Mandelbrot();
        myComboBox.addItem(mandelbrotFractal);
        FractalGenerator tricornFractal = new Tricorn();
        myComboBox.addItem(tricornFractal);
        FractalGenerator burningShipFractal = new BurningShip();
        myComboBox.addItem(burningShipFractal);

        ButtonHandler fractalChooser = new ButtonHandler();
        myComboBox.addActionListener(fractalChooser);

        //разработка пользовательского интерфейса
        JPanel upprePanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal:");
        upprePanel.add(myLabel);
        upprePanel.add(myComboBox);
        myFrame.add(upprePanel, BorderLayout.NORTH);

        JButton saveButton = new JButton("Save");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(saveButton);
        bottomPanel.add(resetButton);
        myFrame.add(bottomPanel, BorderLayout.SOUTH);

        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);

        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setResizable(false);

    }

    //Вспомогательный метод для вывода фрактала на экран
    private void drawFractal()
    {
        /** Циклический просмотр каждого пикселя на дисплее  **/
        for (int x=0; x<displaySize; x++){
            for (int y=0; y<displaySize; y++){

                double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

                //Вычисляем количество итераций
                int iteration = fractal.numIterations(xCoord, yCoord);

                //Если точка не выходит за границы -> красим
                if (iteration == -1){
                    display.drawPixel(x, y, 0);
                } else {
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    display.drawPixel(x, y, rgbColor);
                }

            }
        }
        //обновляет картинку
        display.repaint();
    }

    //
    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        { //выпадающий список реализация
            String command = e.getActionCommand();

            if (e.getSource() instanceof JComboBox) {
                JComboBox src = (JComboBox) e.getSource();
                fractal = (FractalGenerator) src.getSelectedItem();
                fractal.getInitialRange(range);
                drawFractal();

                //добавляем кнопки
            } else if (command.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
            } else if (command.equals("Save Image")) {

                JFileChooser myFileChooser = new JFileChooser();

                FileFilter extensionFilter = new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);
                myFileChooser.setAcceptAllFileFilterUsed(false);

                //выбор пользователем пути с помощью метода showSaveDialog
                int userSelection = myFileChooser.showSaveDialog(display);

                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    //получаем путь с помощью метода getSelectedFile
                    java.io.File file = myFileChooser.getSelectedFile();
                    String file_name = file.toString();

                    //обработка возможной ошибки которую может выхвать метод write()
                    try {
                        BufferedImage displayImage = display.getImage();
                        javax.imageio.ImageIO.write(displayImage, "png", file);
                    } catch (Exception exception) { //показ ошибки через диалоговое окно
                        JOptionPane.showMessageDialog(display,
                                exception.getMessage(), "Cannot Save Image :(",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                else return;
            }
        }
    }

    //Внутренний класс для обработки события
    private class MouseHandler extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);

            int y = e.getY();
            double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            drawFractal();
        }
    }

    //Статический метод для запуска
    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}
