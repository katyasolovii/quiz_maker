package scr;

import java.io.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Клас, який реалізує режим адміністратора.
 * <p>
 * Адміністратор може:
 * - переглядати питання з CSV та JSON файлів,
 * - додавати нові питання,
 * - редагувати існуючі питання,
 * - видаляти питання,
 * - керувати базою тестових питань.
 * </p>
 *
 * <p>Розробник: Соловій Катерина</p>
 * <p>Дата виконання: 2025-12-02</p>
 */

public class AdminMode {

    /**
     * Конструктор класу AdminMode.
     * Клас не потребує ініціалізації, всі методи статичні.
     */
    public AdminMode() {}

    /** Шлях до CSV файлу з питаннями */
    private static String CSV_FILE= "/Users/katyasolovii/Desktop/quiz_maker/src/main/resources/questions.csv";

    /** Шлях до JSON файлу з питаннями */
    private static String JSON_FILE = "/Users/katyasolovii/Desktop/quiz_maker/src/main/resources/questions.json";

    /**
     * Основний метод режиму адміністратора.
     * <p>
     * Виводить меню з можливими діями та виконує обрану дію.
     * </p>
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void run(String[] args) {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Режим адміністратора ===");
            System.out.println("1. Переглянути запитання (CSV)");
            System.out.println("2. Переглянути запитання (JSON)");
            System.out.println("3. Додати нове запитання (CSV)");
            System.out.println("4. Додати нове запитання (JSON)");
            System.out.println("5. Змінити запитання (CSV)");
            System.out.println("6. Змінити запитання (JSON)");
            System.out.println("7. Видалити питання (CSV)");
            System.out.println("8. Видалити питання (JSON)");
            System.out.println("9. Вихід");
            System.out.print("> ");

            String input = in.nextLine().trim();
            if (input.isEmpty()) continue;

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть число від 1 до 7.");
                continue;
            }

            if (choice == 1) {
                viewCSV();
            } else if (choice == 2) {
                viewJSON();
            } else if (choice == 3) {
                addCSV(in);
            } else if (choice == 4) {
                addJSON(in);
            } else if (choice == 5) {
                editCSV(in);
            } else if (choice == 6) {
                editJSON(in);
            } else if (choice == 7) {
                deleteCSV(in);
            } else if (choice == 8) {
                deleteJSON(in);
            } else if (choice == 9) {
                System.out.println("Вихід з програми...");
                in.close();
                return;
            } else {
                System.out.println("Будь ласка, введіть число від 1 до 7.");
            }
        }
    }

    /**
     * Переглядає всі питання у CSV файлі.
     * Виводить їх на екран з порядковими номерами.
     */
    public static void viewCSV() {
        System.out.println("\n=== Запитання CSV ===");
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            int index = 1;
            while ((line = br.readLine()) != null) {
                System.out.println(index + ": " + line);
                index++;
            }
        } catch (IOException e) {
            System.out.println("Помилка при читанні CSV: " + e.getMessage());
        }
    }

    /**
     * Додає нове питання у CSV файл.
     * <p>
     * Користувач вводить текст питання, 4 варіанти відповідей та номер правильної відповіді.
     * Питання записується у CSV у форматі:
     * "питання","вар1","вар2","вар3","вар4",правильна_відповідь
     * </p>
     *
     * @param in Scanner для введення користувача
     */
    public static void addCSV(Scanner in) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            System.out.print("Питання: ");
            String question = in.nextLine();
            String[] options = new String[4];
            for (int i = 0; i < 4; i++) {
                System.out.print("Варіант " + (i + 1) + ": ");
                options[i] = in.nextLine();
            }
            System.out.print("Правильна відповідь (1-4): ");
            int answer = Integer.parseInt(in.nextLine());

            String line = "\"" + question + "\",\"" + options[0] + "\",\"" + options[1] + "\",\"" +
                    options[2] + "\",\"" + options[3] + "\"," + answer;
            bw.newLine();
            bw.write(line);
            System.out.println("Запитання додано у CSV.");
        } catch (IOException e) {
            System.out.println("Помилка при записі CSV: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Неправильний формат числа для правильної відповіді.");
        }
    }

    /**
     * Розбиває рядок CSV на частини.
     * <p>
     * Підтримує лапки для значень, що містять коми.
     * Наприклад: "питання з, комою","вар1",... => ["питання з, комою", "вар1", ...]
     * </p>
     *
     * @param line рядок CSV
     * @return масив частин рядка
     */
    public static String[] parseCSVLine(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if (c == ',' && !insideQuotes) {
                parts.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        parts.add(current.toString());
        return parts.toArray(new String[0]);
    }

    /**
     * Редагує питання у CSV файлі.
     * <p>
     * Користувач може змінити текст питання, варіанти відповідей або все питання цілком.
     * Після змін файл оновлюється.
     * </p>
     *
     * @param in Scanner для введення користувача
     */
    public static void editCSV(Scanner in) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Помилка при читанні CSV: " + e.getMessage());
            return;
        }

        if (lines.isEmpty()) {
            System.out.println("CSV файл порожній.");
            return;
        }

        while (true) {
            System.out.println("\n=== Редагування CSV ===");
            for (int i = 0; i < lines.size(); i++) {
                System.out.println((i + 1) + ": " + lines.get(i));
            }

            System.out.print("Яке питання ви хочете змінити (0 для виходу): ");
            int index;
            try {
                index = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Потрібно ввести число.");
                continue;
            }

            if (index == 0) return;
            if (index < 1 || index > lines.size()) {
                System.out.println("Немає питання з таким номером.");
                continue;
            }

            String oldLine = lines.get(index - 1);
            String[] parts = parseCSVLine(oldLine);

            if (parts.length != 6) {
                System.out.println("Неправильний формат CSV. Має бути 6 елементів.");
                continue;
            }

            boolean editing = true;
            while (editing) {
                System.out.println("Виберіть, що змінити:");
                System.out.println("1. Текст питання");
                System.out.println("2. Варіанти + правильна відповідь");
                System.out.println("3. Все питання");
                System.out.println("0. Вийти з редагування");
                System.out.print("> ");

                int editChoice;
                try {
                    editChoice = Integer.parseInt(in.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Потрібно ввести число.");
                    continue;
                }

                if (editChoice == 0) {
                    editing = false;
                } else if (editChoice == 1) {
                    System.out.print("Новий текст питання: ");
                    parts[0] = in.nextLine();
                } else if (editChoice == 2) {
                    for (int i = 1; i <= 4; i++) {
                        System.out.print("Новий варіант " + i + ": ");
                        parts[i] = in.nextLine();
                    }
                    System.out.print("Правильна відповідь (1-4): ");
                    parts[5] = in.nextLine();
                } else if (editChoice == 3) {
                    System.out.print("Новий текст питання: ");
                    parts[0] = in.nextLine();
                    for (int i = 1; i <= 4; i++) {
                        System.out.print("Новий варіант " + i + ": ");
                        parts[i] = in.nextLine();
                    }
                    System.out.print("Правильна відповідь (1-4): ");
                    parts[5] = in.nextLine();
                } else {
                    System.out.println("Будь ласка, введіть 0, 1, 2 або 3.");
                    continue;
                }

                // Збираємо рядок назад у CSV
                String newLine = "\"" + parts[0] + "\",\"" + parts[1] + "\",\"" + parts[2] +
                        "\",\"" + parts[3] + "\",\"" + parts[4] + "\"," + parts[5];
                lines.set(index - 1, newLine);

                // Запис у файл
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
                    for (String l : lines) {
                        bw.write(l);
                        bw.newLine();
                    }
                    System.out.println("Питання змінено успішно!");
                } catch (IOException e) {
                    System.out.println("Помилка при записі CSV: " + e.getMessage());
                }

                editing = false;
            }
        }
    }

    /**
     * Переглядає всі питання у JSON файлі.
     * Виводить текст питання, варіанти відповідей та правильну відповідь.
     */
    public static void viewJSON() {
        System.out.println("\n=== Запитання JSON ===");
        try {
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(JSON_FILE)));
            JSONArray array = new JSONArray(content);
            for (int i = 0; i < array.length(); i++) {
                JSONObject q = array.getJSONObject(i);
                System.out.println((i + 1) + ". " + q.getString("text"));
                JSONArray opts = q.getJSONArray("options");
                for (int j = 0; j < opts.length(); j++) {
                    System.out.println("   " + (j + 1) + ". " + opts.getString(j));
                }
                System.out.println("   Відповідь: " + q.getInt("answer"));
            }
        } catch (Exception e) {
            System.out.println("Помилка при читанні JSON: " + e.getMessage());
        }
    }

    /**
     * Додає нове питання у JSON файл.
     * Користувач вводить текст питання, 4 варіанти та правильну відповідь.
     *
     * @param in Scanner для введення користувача
     */
    public static void addJSON(Scanner in) {
        try {
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(JSON_FILE)));
            JSONArray array = new JSONArray(content);

            System.out.print("Питання: ");
            String question = in.nextLine();
            JSONArray options = new JSONArray();
            for (int i = 0; i < 4; i++) {
                System.out.print("Варіант " + (i + 1) + ": ");
                options.put(in.nextLine());
            }
            System.out.print("Правильна відповідь (1-4): ");
            int answer = Integer.parseInt(in.nextLine());

            JSONObject newQ = new JSONObject();
            newQ.put("text", question);
            newQ.put("options", options);
            newQ.put("answer", answer);

            array.put(newQ);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(JSON_FILE))) {
                bw.write(array.toString(4));
            }

            System.out.println("Запитання додано у JSON.");
        } catch (Exception e) {
            System.out.println("Помилка при записі JSON: " + e.getMessage());
        }
    }

    /**
     * Редагує питання у JSON файлі.
     *
     * @param in Scanner для введення користувача
     */
    public static void editJSON(Scanner in) {
        JSONArray array;
        try {
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(JSON_FILE)));
            array = new JSONArray(content);
        } catch (Exception e) {
            System.out.println("Помилка при читанні JSON: " + e.getMessage());
            return;
        }

        if (array.isEmpty()) {
            System.out.println("JSON файл порожній.");
            return;
        }

        while (true) {
            System.out.println("\n=== Редагування JSON ===");
            for (int i = 0; i < array.length(); i++) {
                JSONObject q = array.getJSONObject(i);
                System.out.println((i + 1) + ". " + q.getString("text"));
            }

            System.out.print("Яке питання ви хочете змінити (0 для виходу): ");
            int index;
            try {
                index = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Потрібно ввести число.");
                continue;
            }

            if (index == 0) return;
            if (index < 1 || index > array.length()) {
                System.out.println("Немає питання з таким номером.");
                continue;
            }

            JSONObject question = array.getJSONObject(index - 1);

            boolean editing = true;
            while (editing) {
                System.out.println("Виберіть, що змінити:");
                System.out.println("1. Тільки текст питання");
                System.out.println("2. Варіанти відповідей та правильну відповідь");
                System.out.println("3. Замінити все новим питанням");
                System.out.println("0. Вийти з редагування");
                System.out.print("> ");

                int editChoice;
                try {
                    editChoice = Integer.parseInt(in.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Потрібно ввести число.");
                    continue;
                }

                if (editChoice == 0) return;
                else if (editChoice == 1) {
                    System.out.print("Новий текст питання: ");
                    question.put("text", in.nextLine());
                } else if (editChoice == 2) {
                    JSONArray newOptions = new JSONArray();
                    for (int i = 0; i < 4; i++) {
                        System.out.print("Новий варіант " + (i + 1) + ": ");
                        newOptions.put(in.nextLine());
                    }
                    question.put("options", newOptions);
                    System.out.print("Правильна відповідь (1-4): ");
                    question.put("answer", Integer.parseInt(in.nextLine()));
                } else if (editChoice == 3) {
                    System.out.print("Нове питання: ");
                    question.put("text", in.nextLine());
                    JSONArray newOptions = new JSONArray();
                    for (int i = 0; i < 4; i++) {
                        System.out.print("Новий варіант " + (i + 1) + ": ");
                        newOptions.put(in.nextLine());
                    }
                    question.put("options", newOptions);
                    System.out.print("Правильна відповідь (1-4): ");
                    question.put("answer", Integer.parseInt(in.nextLine()));
                } else {
                    System.out.println("Будь ласка, введіть 1, 2, 3 або 0.");
                    continue;
                }
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(JSON_FILE))) {
                    bw.write(array.toString(4));
                    System.out.println("Питання змінено успішно!");
                } catch (IOException e) {
                    System.out.println("Помилка при записі JSON: " + e.getMessage());
                }

                editing = false;
            }
        }
    }

    /**
     * Видаляє питання з CSV файлу.
     * Користувач обирає номер питання, після чого воно видаляється.
     *
     * @param in Scanner для введення користувача
     */
    public static void deleteCSV(Scanner in) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Помилка при читанні CSV: " + e.getMessage());
            return;
        }

        if (lines.isEmpty()) {
            System.out.println("CSV файл порожній.");
            return;
        }

        while (true) {
            System.out.println("\n=== Видалення питання CSV ===");
            for (int i = 0; i < lines.size(); i++) {
                System.out.println((i + 1) + ": " + lines.get(i));
            }

            System.out.print("Який номер питання видалити (0 для виходу): ");
            int index;
            try {
                index = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Потрібно ввести число.");
                continue;
            }

            if (index == 0) return;
            if (index < 1 || index > lines.size()) {
                System.out.println("Немає питання з таким номером.");
                continue;
            }

            lines.remove(index - 1);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
                System.out.println("Питання видалено успішно!");
            } catch (IOException e) {
                System.out.println("Помилка при записі CSV: " + e.getMessage());
            }

            return;
        }
    }

    /**
     * Видаляє питання з JSON файлу.
     * Користувач обирає номер питання, після чого воно видаляється.
     *
     * @param in Scanner для введення користувача
     */
    public static void deleteJSON(Scanner in) {
        JSONArray array;
        try {
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(JSON_FILE)));
            array = new JSONArray(content);
        } catch (Exception e) {
            System.out.println("Помилка при читанні JSON: " + e.getMessage());
            return;
        }

        if (array.isEmpty()) {
            System.out.println("JSON файл порожній.");
            return;
        }

        while (true) {
            System.out.println("\n=== Видалення питання JSON ===");
            for (int i = 0; i < array.length(); i++) {
                JSONObject q = array.getJSONObject(i);
                System.out.println((i + 1) + ". " + q.getString("text"));
            }

            System.out.print("Який номер питання видалити (0 для виходу): ");
            int index;
            try {
                index = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Потрібно ввести число.");
                continue;
            }

            if (index == 0) return;
            if (index < 1 || index > array.length()) {
                System.out.println("Немає питання з таким номером.");
                continue;
            }

            array.remove(index - 1);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(JSON_FILE))) {
                bw.write(array.toString(4));
                System.out.println("Питання видалено успішно!");
            } catch (IOException e) {
                System.out.println("Помилка при записі JSON: " + e.getMessage());
            }

            return;
        }
    }

    /**
     * Задає шлях до CSV файлу для тестів або альтернативних даних.
     * <p>
     * Використовується, щоб тести могли працювати з власними файлами,
     * не змінюючи реальні дані програми.
     *
     * @param path новий шлях до CSV файлу
     */
    public static void setCSVFile(String path) {
        CSV_FILE = path;
    }

    /**
     * Задає шлях до JSON файлу для тестів або альтернативних даних.
     * <p>
     * Використовується, щоб тести могли працювати з власними файлами,
     * не змінюючи реальні дані програми.
     *
     * @param path новий шлях до JSON файлу
     */
    public static void setJSONFile(String path) {
        JSON_FILE = path;
    }

}
