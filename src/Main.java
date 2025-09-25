import java.util.Scanner;

/** Головний клас програми для роботи з числами Люка
 Дозволяє обчислювати послідовність і перевірити, які числа є кубами
 */
public class Main {

    /**
     * Обчислює n-не число Люка за допомогою ітераційного алгоритму
     *
     * @param n порядковий номер
     * @return значення n-го числа Люка
     */
    public static long lucas(int n) {
        if (n == 0) return 2;
        if (n == 1) return 1;

        long a = 2; //L(0)
        long b = 1; //L(1)
        long c = 0;

        //послідовно рахуємо значення до L(n)
        for (int i = 2; i <= n; i++) {
            c = a + b;
            a = b;
            b = c;
        }
        return b;
    }

    /**
     * Точка входу у програму
     * Користувач вводить N, програма обчислює числа Люка від L(0) до L(N-1),
     * виводить їх і позначає ті, які є кубами
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the number of Luke numbers N ");
        int N = sc.nextInt();

        LukeNumber[] numbers = new LukeNumber[N];

        for (int i = 0; i < N; i++) {
            numbers[i] = new LukeNumber(i, lucas(i));
        }

        System.out.println("Luke numbers:");
        for (LukeNumber num : numbers) {
            System.out.print("L(" + num.getIndex() + " ) = " + num.getValue());
            if (num.isCube()) {
                System.out.print(" <-- it's a cube!");
            }
            System.out.println();
        }
        sc.close();
    }

}





