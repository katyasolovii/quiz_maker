package scr;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

public class AdminModeTest {

    private File getCSV() {
        return new File(getClass().getClassLoader()
                .getResource("NZ_test_questions.csv").getFile());
    }

    private File getJSON() {
        return new File(getClass().getClassLoader()
                .getResource("NZ_test_questions.json").getFile());
    }

    @Test
    void testParseCSVLine() {
        String line = "\"Яке з чисел є парним?\",\"17\",\"21\",\"42\",\"55\",3";

        String[] r = AdminMode.parseCSVLine(line);

        assertEquals(6, r.length);
        assertEquals("Яке з чисел є парним?", r[0]);
        assertEquals("42", r[3]);
        assertEquals("3", r[5]);
    }

    @Test
    void testAddCSV() throws Exception {
        File csv = getCSV();
        AdminMode.setCSVFile(csv.getAbsolutePath());

        Scanner sc = new Scanner(String.join("\n",
                "Нове CSV питання?",
                "O1", "O2", "O3", "O4",
                "4"
        ));

        AdminMode.addCSV(sc);

        String content = Files.readString(csv.toPath());

        assertTrue(content.contains("Нове CSV питання?"));
        assertTrue(content.contains("\"O4\""));
        assertTrue(content.contains(",4"));
    }

    @Test
    void testDeleteCSV() throws Exception {
        File csv = getCSV();
        AdminMode.setCSVFile(csv.getAbsolutePath());

        Scanner sc = new Scanner("1\n");

        AdminMode.deleteCSV(sc);

        String content = Files.readString(csv.toPath());

        assertFalse(content.contains("Яке з чисел є парним?"));
        assertTrue(content.contains("Який кут має прямий кут?"));
    }

    @Test
    void testAddJSON() throws Exception {
        File json = getJSON();
        AdminMode.setJSONFile(json.getAbsolutePath());

        Scanner sc = new Scanner(String.join("\n",
                "New JSON Q?",
                "A", "B", "C", "D",
                "2"
        ));

        AdminMode.addJSON(sc);

        JSONArray arr = new JSONArray(Files.readString(json.toPath()));

        assertEquals(3, arr.length());
        assertEquals("New JSON Q?", arr.getJSONObject(2).getString("text"));
    }

    @Test
    void testDeleteJSON() throws Exception {
        File json = getJSON();
        AdminMode.setJSONFile(json.getAbsolutePath());
        Scanner sc = new Scanner("1\n");
        AdminMode.deleteJSON(sc);
        JSONArray arr = new JSONArray(Files.readString(json.toPath()));
        assertEquals(1, arr.length());
        assertEquals("5+7?", arr.getJSONObject(0).getString("text"));
    }
}
