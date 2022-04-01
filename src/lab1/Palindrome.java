package lab1;
import java.util.Scanner;

public class Palindrome {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in); //объявили сканер
        System.out.print("Введите строку: ");
        String s = in.nextLine(); //считывание строки
        s=s.toLowerCase(); //метод toLowerCase() преобразовывает значение строки в нижний регистр
        System.out.println(s + " - " + (isPalindrome(s))); //вывод слово - результат isPalindrome
        }
    //обратное слово
    public static String reverseString(String s) {
        String res = "";                            //создается пустая строка
        //метод length() возвращает длину строки
        for (int i = s.length() - 1; i >= 0; i--) { //обратный цикл от длины-1 до 0
            //метод charAt(index) возвращает символ по указанному индексу
            res += s.charAt(i);                     //запись слова наоборот
        }
        return res;                                 //возвращение обратного слова
    }
    //проверка на палиндром
    public static boolean isPalindrome(String s) {
        String reversed = reverseString(s);        //присвоение reversed обратного слова
        return s.equals(reversed);                 //сравнение
    }
}
