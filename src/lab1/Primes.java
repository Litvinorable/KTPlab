package lab1;

public class Primes {
    public static void main(String[] args) { //описание класса
        for (int i = 2; i < 101; i++)   //числа от 2 до 100
            if (isPrime(i))             //если простое (true)
                System.out.println(i);  //вывод
    }

    // Проверка на то что число является простым
    public static boolean isPrime(int n) {
        for (int i = 2; i < n; i++)   //перебор
            if (n%i == 0)             //проверка условия
                return false;         //не простое
        return true;                  //простое
    }
}
