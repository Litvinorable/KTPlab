package lab2;

public class Point3d extends Point2d {
    private double zCoord; // Координата Z
    /** Конструктор инициализации **/
    public Point3d ( double x, double y, double z) {
        super(x,y);
        zCoord = z;
    }

    public Point3d () {         //Конструктор по умолчанию
        this(0, 0, 0);}

    public double getZ () {
        return zCoord;
    } // Возвращение координаты Z

    public void setZ ( double val) {
        zCoord = val;
    } // Установка значения координаты Z

    /** Поиск расстояния между точками **/
    public double distanceTo(Point3d p) {
        return (double)Math.sqrt(Math.pow(this.getX() - p.getX(),2) + Math.pow(this.getY() - p.getY(),2) + Math.pow(this.getZ() - p.getZ(),2));
    }
}
