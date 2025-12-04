package scr;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class QuizGeneratorTest {
    @Test
    public void testParseCSVLineManual() {
        String line = "\"Question, with comma\",Option1,Option2,Option3,Option4,2";
        String[] parts = QuizGenerator.parseCSVLine(line);

        assertEquals(6, parts.length);
        assertEquals("Question, with comma", parts[0]);
        assertEquals("Option1", parts[1]);
        assertEquals("Option2", parts[2]);
        assertEquals("Option3", parts[3]);
        assertEquals("Option4", parts[4]);
        assertEquals("2", parts[5]);
    }

    @Test
    public void testParseCSVLineFromFile() {
        String filePath = "src/test/resources/NZ_test_csv_line.txt";

        try {
            String line = Files.readString(Paths.get(filePath)).trim();
            String[] parts = QuizGenerator.parseCSVLine(line);

            assertEquals(6, parts.length);
            assertEquals("Capital of France?", parts[0]);
            assertEquals("Paris", parts[1]);
            assertEquals("Lyon", parts[2]);
            assertEquals("Nice", parts[3]);
            assertEquals("Bordeaux", parts[4]);
            assertEquals("1", parts[5]);

        } catch (Exception e) {
            fail("Помилка при читанні файлу або розборі CSV: " + e.getMessage());
        }
    }

    @Test
    public void testReadCSV() {
        String filePath = "src/test/resources/NZ_test_readCSV.txt";

        try {
            List<String[]> questions = QuizGenerator.readCSV(filePath);
            assertEquals(2, questions.size(), "Файл повинен містити 2 питання");
            String[] q1 = questions.get(0);
            assertEquals(6, q1.length, "Кожне питання повинно містити 6 полів");

            assertEquals("Capital of Germany?", q1[0]);
            assertEquals("Berlin", q1[1]);
            assertEquals("Munich", q1[2]);
            assertEquals("Hamburg", q1[3]);
            assertEquals("Bonn", q1[4]);
            assertEquals("1", q1[5]);

        } catch (Exception e) {
            fail("Помилка при тестуванні readCSV(): " + e.getMessage());
        }
    }

    @Test
    public void testReadJSON() {
        String filePath = "src/test/resources/NZ_test_readJSON.json";

        try {
            List<String[]> questions = QuizGenerator.readJSON(filePath);

            assertEquals(2, questions.size(), "Файл JSON повинен містити 2 питання");
            String[] q1 = questions.get(0);
            assertEquals(6, q1.length, "Кожне питання повинно містити 6 полів");

            assertEquals("Capital of France?", q1[0]);
            assertEquals("Paris", q1[1]);
            assertEquals("Lyon", q1[2]);
            assertEquals("Marseille", q1[3]);
            assertEquals("Nice", q1[4]);
            assertEquals("1", q1[5]);

        } catch (Exception e) {
            fail("Помилка при тестуванні readJSON(): " + e.getMessage());
        }
    }

}
