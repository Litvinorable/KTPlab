package lab2;

public class Main {
    public static void main(String[]args){
        System.out.println("Point2d");
        Point2d myPoint = new Point2d ();//создаетточку (0,0)
        Point2d myOtherPoint = new Point2d (8,4);//создаетточку (8,4)
        Point2d aThirdPoint = new Point2d ();//создаетточку (0,0)
        System.out.println(myPoint.equals(myOtherPoint));
        System.out.println(myPoint.equals(aThirdPoint));
        System.out.println(myOtherPoint.equals(aThirdPoint));

        System.out.println(" ");
        System.out.println("Point3d");
        Point3d point3d_1 = new Point3d(0.5,2.0,0.4);
        Point3d point3d_2 = new Point3d(0.5,2.0,0.4);
        Point3d point3d_3 = new Point3d(5.8,1.8,0.5);
        /**Вывод равенства координат между двумя точками **/
        System.out.println(point3d_1.equals(point3d_2));
        System.out.println(point3d_1.equals(point3d_3));
        /**Вывод расстояния с точностью до двух знаков после запятой **/
        System.out.printf("%.2f",point3d_1.distanceTo(point3d_3));

    }
}
