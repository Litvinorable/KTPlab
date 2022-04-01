package lab1;

public class Palindrome2 {
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String s = args[i]; //присваивание слова переменной s
            s=s.toLowerCase();
            System.out.println(s + " - " + (isPalindrome(s))); //вывод слово - результат isPalindrome
        }
    }

    public static String reverseString(String s) {
        String res = "";                            //создается пустая строка
        for (int i = s.length() - 1; i >= 0; i--) { //обратный цикл от длины-1 до 0
            res += s.charAt(i);                     //запись слова наоборот
        }
        return res;                                 //возвращение обратного слова
    }

    public static boolean isPalindrome(String s) { //проверка на палиндром
        String reversed = reverseString(s);        //присвоение reversed обратного слова
        return s.equals(reversed);                 //сравнение: true - палиндром, false - нет
    }
}
