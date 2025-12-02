package scr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Клас для генерації вікторин із CSV та JSON файлів.
 * Містить методи для читання файлів, випадкового вибору питань
 * та парсингу рядків CSV.
 * 
 * Використовує статичні методи, оскільки немає стану об'єкта.
 * 
 * Розробник: Соловій Катерина
 * Дата виконання: 2025-12-02
 */
public class QuizGenerator {

    /**
     * Конструктор класу QuizGenerator.
     * Не містить логіки, оскільки всі методи статичні.
     */
    public QuizGenerator() {}

    /**
     * Читає CSV файл і повертає список питань.
     *
     * @param filePath шлях до CSV файлу
     * @return список питань у вигляді масиву рядків
     * @throws IOException якщо файл не знайдено або помилка читання
     */
    public static List<String[]> readCSV(String filePath) throws IOException {
        ArrayList<String[]> questions = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        try {
            br.readLine(); // Пропускаємо заголовок
            String line;
            while ((line = br.readLine()) != null) {
                String[] parsed = parseCSVLine(line);
                if (parsed.length == 6) questions.add(parsed);
            }
        } finally {
            br.close();
        }

        return questions;
    }

    /**
     * Читає JSON файл і повертає список питань.
     *
     * @param filePath шлях до JSON файлу
     * @return список питань у вигляді масиву рядків
     * @throws IOException якщо файл не знайдено або помилка читання
     * @throws JSONException якщо JSON некоректний
     */
    public static List<String[]> readJSON(String filePath) throws IOException, JSONException {
        ArrayList<String[]> questions = new ArrayList<>();
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONArray array = new JSONArray(content);

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String[] q = new String[6];
            q[0] = obj.getString("text");
            JSONArray opts = obj.getJSONArray("options");
            for (int j = 0; j < 4; j++) {
                q[j + 1] = opts.getString(j);
            }
            q[5] = String.valueOf(obj.getInt("answer"));
            questions.add(q);
        }

        return questions;
    }

    /**
     * Вибирає випадкові питання зі списку.
     *
     * @param allQuestions список всіх питань
     * @param number кількість випадкових питань для тесту
     * @return список випадкових питань
     */
    public static List<String[]> getRandomQuestions(List<String[]> allQuestions, int number) {
        Collections.shuffle(allQuestions);                  // випадково перемішуємо список
        List<String[]> quizQuestions = new ArrayList<>();
        for (int i = 0; i < Math.min(number, allQuestions.size()); i++) {       // бере min від загальної кількості питань та скільки хочемо взяти, щоб не вийти за межі списку
            quizQuestions.add(allQuestions.get(i));
        }
        return quizQuestions;
    }

    /**
     * Генерує вікторину з CSV файлу.
     *
     * @param filePath шлях до CSV файлу
     * @param numberOfQuestions кількість питань для вікторини
     * @return список випадкових питань
     */
    public static List<String[]> generateQuizCSV(String filePath, int numberOfQuestions) {
        try {
            List<String[]> all = readCSV(filePath);
            return getRandomQuestions(all, numberOfQuestions);
        } catch (IOException e) {
            System.out.println("Помилка при читанні CSV файлу: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Генерує вікторину з JSON файлу.
     *
     * @param filePath шлях до JSON файлу
     * @param numberOfQuestions кількість питань для вікторини
     * @return список випадкових питань
     */
    public static List<String[]> generateQuizJSON(String filePath, int numberOfQuestions) {
        try {
            List<String[]> all = readJSON(filePath);
            return getRandomQuestions(all, numberOfQuestions);
        } catch (IOException | JSONException e) {
            System.out.println("Помилка при читанні JSON файлу: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Парсить рядок CSV з урахуванням лапок для ком.
     *
     * @param line рядок CSV
     * @return масив значень
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
}
