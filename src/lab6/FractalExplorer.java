package lab6;

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
    private int displaySize, rowsRemaining;
    private JImageDisplay display;
    private FractalGenerator fractal;
    private Rectangle2D.Double range;

    private JButton saveButton = new JButton();
    private JButton resetButton = new JButton();
    private JComboBox myComboBox = new JComboBox();

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

        lab6.FractalGenerator mandelbrotFractal = new lab6.Mandelbrot();
        myComboBox.addItem(mandelbrotFractal);
        lab6.FractalGenerator tricornFractal = new lab6.Tricorn();
        myComboBox.addItem(tricornFractal);
        lab6.FractalGenerator burningShipFractal = new lab6.BurningShip();
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

    //ИЗМЕНИЛИ
    //Вспомогательный метод для вывода фрактала на экран
    private void drawFractal()
    {
        enableUI(false); // отключает элементы интерфейса во время рисования
        rowsRemaining = displaySize; // значение = кол-во строк

        //вызов FractalWorker
        // для каждой строки создаём отдельный рабочий обхект, а затем вызываем для него execute()
        for (int x=0; x<displaySize; x++){
            FractalWorker drawRow = new FractalWorker(x);
            drawRow.execute();
        }
    }

    //НОВОЕ
    // включает и отключает кнопки кнопки с выпадающим списком на основе указанного параметра
    // с помощью метода setEnabled()
    private void enableUI(boolean val) {
        myComboBox.setEnabled(val);
        resetButton.setEnabled(val);
        saveButton.setEnabled(val);
    }

    //Выпадающий список и кнопки
    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {   //выпадающий список реализация
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
            } else if (command.equals("Save")) {

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

                    //обработка возможной ошибки (исключения) которую может вызвать метод write()
                    try {
                        BufferedImage displayImage = display.getImage();
                        javax.imageio.ImageIO.write(displayImage, "png", file);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(display,
                                exception.getMessage(), "Cannot Save Image :(",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                else return;
            }
        }
    }

    //Внутренний класс для обработки события ИЗМЕНЁН
    private class MouseHandler extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            // выход если rowsRemaining - nonzero.
            // добавили, чтобы решить проблему игнорирования событий во время перерисовки
            if (rowsRemaining != 0) {
                return;
            }
            // Получает координату Х дисплея щелчка мыши
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, x);

            // Получает координату Y дисплея щелчка мыши
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, y);

            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            drawFractal();
        }
    }

    //НОВОЕ
    //Создали подкласс SwingWorker, внутренний класс FractalExplorer
    //Вычисление значений цвета для одной строки фрактала
    private class FractalWorker extends SwingWorker<Object, Object> //Object потому что в нашей реализации не будут использоваться
    {
        // для хранения вычисленных значений RGB в строке
        int yCoordinate;
        int[] computedRGBValues;

        //получает строку в качестве параметра
        private FractalWorker(int row) {
            yCoordinate = row;
        }

        //НОВОЕ
        //Выполняет фоновые операции
        //Вместо рисования, как в прошлом варианте, он сохраняет
        //каждое значение RGB в соответствующем элементе целочесленного массива
        protected Object doInBackground() {

            computedRGBValues = new int[displaySize];

            for (int i = 0; i < computedRGBValues.length; i++) {
                double xCoord = fractal.getCoord(range.x, range.x + range.width, displaySize, i);
                double yCoord = fractal.getCoord(range.y, range.y + range.height, displaySize, yCoordinate);
                int iteration = fractal.numIterations(xCoord, yCoord);

                if (iteration == -1){
                    computedRGBValues[i] = 0;
                }

                else {
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    computedRGBValues[i] = rgbColor;
                }
            }
            return null; //потому что метод должен возвращать объект типа Object
        }

        //НОВОЕ
        //Вызывается, когда фоновые задачи сделаны
        //Рисует пиксели, вычисленные в doInBackground()
        protected void done() {
            for (int i = 0; i < computedRGBValues.length; i++) {
                display.drawPixel(i, yCoordinate, computedRGBValues[i]);
            }
            display.repaint(0, 0, yCoordinate, displaySize, 1);

            rowsRemaining--;
            if (rowsRemaining == 0) {
                enableUI(true); // вызов enableUI()
            }
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