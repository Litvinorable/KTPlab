package lab2;

public class Point2d {
    private double xCoord; // Координата X
    private double yCoord; // Координата Y
    // Конструктор инициализации
    public Point2d (double x, double y) {
        xCoord = x;
        yCoord = y;
    }
    // Конструктор по умолчанию
    public Point2d () {
        this(0, 0); //Вызван конструктор с двумя параметрами и определен источник.
    }

    public double getX () { // Возвращение координаты X
        return xCoord;
    } // Возвращение координаты X

    public double getY () { // Возвращение координаты Y
        return yCoord;
    } //Возвращение координаты Y

    public void setX ( double val) { // Установка значения координаты Y
        xCoord = val;
    } //

    public void setY ( double val) { // Установка значения координаты Y
        yCoord = val;
    }

    /** Cравнение двух точек. **/
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if ( !getClass().equals(obj.getClass()) )
            return false;
        Point2d point2d = (Point2d) obj;
        return this.xCoord== point2d.xCoord &&this.yCoord== point2d.yCoord;
    }
}
