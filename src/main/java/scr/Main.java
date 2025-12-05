package scr;

import java.util.Scanner;

/**
 * Головний клас програми Quiz Maker.
 * <p>
 * Відповідає за запуск програми, вибір режиму роботи
 * та передачу керування відповідному модулю:
 * Студент або Адміністратор.
 * </p>
 *
 * <p>Приклад запуску:</p>
 * <pre>
 * javac -cp "lib/*:src/main/java" src/main/java/scr/*.java
 * java -cp "lib/*:src/main/java" scr.Main
 * </pre>
 *
 * <p>Розробник: Соловій Катерина</p>
 * <p>Дата виконання: 2025-12-02</p>
 */

public class Main {

    /**
     * Конструктор класу Main.
     * Клас не потребує створення об’єкта, програма запускається через main().
     */
    public Main() {}

    /**
     * Запускає програму та обробляє вибір користувача.
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int choice = getUserChoice(in);          // функція для вибору режиму
        handleChoice(choice, args);             // функція для виконання дії
        in.close();
    }

    /**
     * Отримує вибір користувача.
     * <p>
     * Виводить меню і перевіряє, щоб користувач ввів
     * правильне число від 1 до 3.
     * </p>
     *
     * @param in Scanner для вводу користувача
     * @return вибір користувача (1-Студент, 2-Адміністратор, 3-Вихід)
     */
    public static int getUserChoice(Scanner in) {
        int choice = 0;

        System.out.println("====================================");
        System.out.println("     Вітаємо у Quiz Maker!");
        System.out.println("====================================");

        while (true) {
            System.out.println("\nОберіть режим:");
            System.out.println("1. Студент");
            System.out.println("2. Адміністратор");
            System.out.println("3. Вихід");
            System.out.print("> ");

            String input = in.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Помилка: ви нічого не ввели. Будь ласка, введіть число від 1 до 3.");
                continue;
            }
            try {
                choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 3) break;
                else System.out.println("Невірний вибір. Будь ласка, введіть число від 1 до 3.");
            } catch (NumberFormatException e) {
                System.out.println("Невірний ввід. Будь ласка, введіть число від 1 до 3.");
            }
        }
        return choice;
    }

    /**
     * Обробляє вибір користувача.
     * <p>
     * Запускає відповідний режим роботи або завершує програму.
     * </p>
     *
     * @param choice вибір користувача (1-Студент, 2-Адміністратор, 3-Вихід)
     * @param args аргументи командного рядка
     */
    public static void handleChoice(int choice, String[] args) {
        if (choice == 1) {
            System.out.println("Ви обрали режим: Студент");
            StudentMode.run(args);
        } else if (choice == 2) {
            System.out.println("Ви обрали режим: Адміністратор");
            AdminMode.run(args);
        } else if (choice == 3) {
            System.out.println("На все добре! До побачення!");
        }
    }
}