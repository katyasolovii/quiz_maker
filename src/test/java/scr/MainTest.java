package scr;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void testGetUserChoiceCorrectInput() {
        String input = "1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        int choice = Main.getUserChoice(scanner);
        assertEquals(1, choice, "Вибір користувача має бути 1");
    }

    @Test
    public void testGetUserChoice_Admin() {
        String input = "2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        int choice = Main.getUserChoice(scanner);
        assertEquals(2, choice, "Вибір користувача має бути 2"); 
    }

    @Test
    public void testGetUserChoice_Exit() {
        String input = "3\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        int choice = Main.getUserChoice(scanner);
        assertEquals(3, choice, "Вибір користувача має бути 3");
    }

    @Test
    public void testGetUserChoice_InvalidThenValid() {
        String input = "\nabc\n1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        int choice = Main.getUserChoice(scanner);
        assertEquals(1, choice);
    }

    @Test
    public void testHandleChoice_Exit() {
        String[] args = {};
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));
        Main.handleChoice(3, args);
        String output = outContent.toString();
        System.setOut(System.out);
        assert(output.contains("На все добре! До побачення!"));
    }

    @Test
    public void testHandleChoice_Student_Output() {
        String[] args = {};
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));
        Main.handleChoice(1, args);
        String output = outContent.toString();
        System.setOut(System.out);
        assert(output.contains("Ви обрали режим: Студент"));
    }

    @Test
    public void testHandleChoice_Admin_Output() {
        String[] args = {};
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));
        Main.handleChoice(2, args);
        String output = outContent.toString();
        System.setOut(System.out);
        assert(output.contains("Ви обрали режим: Адміністратор"));
    }
}
