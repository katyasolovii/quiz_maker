package scr;

import java.util.List;
import java.util.Scanner;

/**
 * Клас, який реалізує режим роботи Студента.
 * <p>
 * Дозволяє студенту:
 * - вводити ПІБ та email,
 * - обирати джерело питань (CSV або JSON),
 * - проходити вікторину,
 * - отримувати результат на пошту.
 * </p>
 *
 * <p>Розробник: Соловій Катерина</p>
 * <p>Дата виконання: 2025-12-02</p>
 */

public class StudentMode {

    /**
     * Конструктор класу StudentMode.
     * Клас не потребує створення об’єкта, всі методи статичні.
     */
    public StudentMode() {}
    
    /**
     * Перевіряє, чи ПІБ є валідним.
     * <p>
     * ПІБ повинен містити лише літери, пробіли, апостроф або дефіс.
     * </p>
     *
     * @param fullName рядок ПІБ
     * @return true, якщо ПІБ валідний, інакше false
     */
    public static boolean isValidFullName(String fullName) {
        if (fullName == null) return false;
        fullName = fullName.trim();
        if (fullName.isEmpty()) return false;
        return fullName.matches("[a-zA-Zа-яА-ЯіІїЇєЄ'\\-\\s]+");
    }

    /**
     * Перевіряє, чи email валідний.
     * <p>
     * Email повинен містити символ "@"
     * та мати хоча б один символ перед "@". 
     * </p>
     *
     * @param email рядок email
     * @return true, якщо email валідний, інакше false
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        email = email.trim();
        if (email.isEmpty()) return false;

        // Перевірка на наявність "@"
        if (!email.contains("@")) return false;

        // Переконаємось, що є хоча б один символ перед "@"
        String localPart = email.substring(0, email.indexOf("@"));
        if (localPart.isEmpty()) return false;

        return true;
    }   

    /**
     * Перевіряє вибір джерела питань.
     * <p>
     * Джерело має бути 1 (CSV) або 2 (JSON).
     * </p>
     *
     * @param input рядок, який ввів користувач
     * @return true, якщо вибір коректний, інакше false
     */
    public static boolean isValidSource(String input) {
        if (input == null) return false;
        input = input.trim();
        if (input.isEmpty()) return false;
        try {
            int number = Integer.parseInt(input);
            return number == 1 || number == 2;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Запускає вікторину для студента.
     * <p>
     * Виводить питання та варіанти відповідей, приймає вибір користувача
     * та підраховує правильні відповіді.
     * </p>
     *
     * @param quiz список питань (кожне питання — масив рядків)
     * @param in об'єкт Scanner для введення користувача
     * @return кількість правильних відповідей
     */
    public static int runQuiz(List<String[]> quiz, Scanner in) {
        int score = 0;
        for (int i = 0; i < quiz.size(); i++) {
            String[] q = quiz.get(i);
            System.out.println("\nПитання " + (i + 1) + ": " + q[0]);
            for (int j = 1; j <= 4; j++) {
                System.out.println(j + ". " + q[j]);
            }

            int answer = 0;
            while (true) {
                System.out.print("Ваш вибір (1-4): ");
                String input = in.nextLine().trim();
                try {
                    answer = Integer.parseInt(input);
                    if (answer >= 1 && answer <= 4) break;
                } catch (Exception e) {}
                System.out.println("Введіть число від 1 до 4.");
            }

            if (answer == Integer.parseInt(q[5])) {
                score++;
            }
        }
        return score;
    }

    /**
     * Основний метод режиму Студента.
     * <p>
     * Послідовно:
     * 1. Запитує ПІБ та перевіряє його.
     * 2. Запитує email та перевіряє його.
     * 3. Дозволяє обрати джерело питань (CSV або JSON).
     * 4. Завантажує тест через QuizGenerator.
     * 5. Запускає вікторину та підраховує результат.
     * 6. Надсилає результат на email.
     * </p>
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void run(String[] args) {
        Scanner in = new Scanner(System.in);

        // Введення ПІБ
        String fullName = "";
        while (true) {
            System.out.println("Введіть ПІБ:");
            fullName = in.nextLine();
            if (isValidFullName(fullName)) break;
            System.out.println("Некоректне ПІБ. Спробуйте ще раз.");
        }

        // Введення email
        String email = "";
        while (true) {
            System.out.println("Введіть email:");
            email = in.nextLine();
            if (isValidEmail(email)) break;
            System.out.println("Некоректний email. Спробуйте ще раз.");
        }

        // Вибір джерела
        int source = 0;
        while (true) {
            System.out.println("Оберіть джерело (1 - CSV, 2 - JSON):");
            String input = in.nextLine();
            if (isValidSource(input)) {
                source = Integer.parseInt(input);
                break;
            } else {
                System.out.println("Введіть 1 або 2.");
            }
        }

        // Завантаження тесту (тут можна вставити свій QuizGenerator)
        String filePath;
        List<String[]> quiz;
        if (source == 1) {
            filePath = ""/Users/katyasolovii/Desktop/quiz_maker/src/main/resources/questions.json"";
            quiz = QuizGenerator.generateQuizCSV(filePath, 10);
        } else {
            filePath = "/Users/katyasolovii/Desktop/quiz_maker/src/main/resources/questions.json";
            quiz = QuizGenerator.generateQuizJSON(filePath, 10);
        }

        // Запуск вікторини
        int score = runQuiz(quiz, in);

        // Надсилання результату
        String subject = "Результат вікторини";
        String body = "Вікторина завершена! " + fullName + ", ваш результат: " + score + " / " + quiz.size();
        EmailSender.sendEmail(email, subject, body);

        System.out.println("Вікторина завершена! " + fullName + ", результат надіслано на пошту.");

        in.close();
    }
}
