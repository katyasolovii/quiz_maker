package scr;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

public class StudentModeEmailTest {

    @Test
    public void testEmailFromFile() throws FileNotFoundException {
        File file = new File("src/test/resources/NZ_test_email.txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",");
            String email = parts[0];
            boolean expected = Boolean.parseBoolean(parts[1]);

            boolean actual = StudentMode.isValidEmail(email);
            assertEquals(expected, actual, "Помилка для email: " + email);
        }

        scanner.close();
    }

    @Test
    public void testEmail() {
        assertTrue(StudentMode.isValidEmail("test@gmail.com"));
        assertTrue(StudentMode.isValidEmail("user.name@gmail.com"));

        assertFalse(StudentMode.isValidEmail("invalidemail"));
        assertFalse(StudentMode.isValidEmail("user@mail.com"));
        assertFalse(StudentMode.isValidEmail(",@example.com"));
        assertFalse(StudentMode.isValidEmail("user@mail"));
        assertFalse(StudentMode.isValidEmail("@gmail.com"));
    }


    @Test
    public void testFullNameFromFile() throws FileNotFoundException {
        File file = new File("src/test/resources/NZ_test_fullname.txt"); 
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",");
            String fullName = parts[0];
            boolean expected = Boolean.parseBoolean(parts[1]);

            boolean actual = StudentMode.isValidFullName(fullName);
            assertEquals(expected, actual, "Помилка для ПІБ: " + fullName);
        }

        scanner.close();
    }

    @Test
    public void testFullName() {
        assertTrue(StudentMode.isValidFullName("John Smith"));
        assertTrue(StudentMode.isValidFullName("Марія Петрівна"));
        assertFalse(StudentMode.isValidFullName("12345"));
        assertFalse(StudentMode.isValidFullName("Анна_Іванова"));
        assertFalse(StudentMode.isValidFullName(""));
    }

    @Test
    public void testSourceFromFile() throws FileNotFoundException {
        File file = new File("src/test/resources/NZ_test_source.txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",");
            String input = parts[0];
            boolean expected = Boolean.parseBoolean(parts[1]);

            boolean actual = StudentMode.isValidSource(input);
            assertEquals(expected, actual, "Помилка для вводу: " + input);
        }

        scanner.close();
    }

    @Test
    public void testSource() {
        assertTrue(StudentMode.isValidSource("1"));
        assertTrue(StudentMode.isValidSource("2"));
        assertFalse(StudentMode.isValidSource("0"));
        assertFalse(StudentMode.isValidSource("3"));
        assertFalse(StudentMode.isValidSource("abc"));
        assertFalse(StudentMode.isValidSource(""));
    }

    @Test
    public void testRunQuiz5Questions() {
        List<String[]> quiz = new ArrayList<>();
        quiz.add(new String[]{"Яке з чисел є парним?", "17", "21", "42", "55", "3"});
        quiz.add(new String[]{"Який кут має прямий кут?", "45°", "60°", "90°", "180°", "3"});
        quiz.add(new String[]{"Яке число є простим?", "21", "29", "39", "49", "2"});
        quiz.add(new String[]{"Чому дорівнює корінь з 144?", "10", "11", "12", "14", "3"});
        quiz.add(new String[]{"Яка формула площі кола?", "2πr", "πr²", "πd", "r²", "2"});

        List<Integer> answers = Arrays.asList(3, 3, 2, 3, 2); 

        StringBuilder sb = new StringBuilder();
        for (int a : answers) sb.append(a).append("\n");
        Scanner simulatedScanner = new Scanner(sb.toString());

        int score = StudentMode.runQuiz(quiz, simulatedScanner);
        assertEquals(5, score, "Рахунок має бути 5");
    }
}
