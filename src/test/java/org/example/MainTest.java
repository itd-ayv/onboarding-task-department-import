package org.example;

import org.example.model.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.List;

public class MainTest {

    private static final String TEST_CSV_PATH = "src/test/resources/department_test.csv";

    @BeforeEach
    public void setUp() {
        createTestCSV();
    }


    private void createTestCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_CSV_PATH))) {
            writer.write("#ignore,id,name,parent_id\n");
            writer.write(" ,C,Corporate, \n");
            writer.write(" ,CCN,Corporate Compliance & Audit,C\n");
            writer.write("#,CCM,Corporate Communication,C\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadCSV() {
        List<Department> departments = Main.readCSV("department_test.csv");

        assertNotNull(departments);
        System.out.println(departments.size());
        assertEquals(1, departments.size());

        Department C = departments.stream().filter(d -> d.getId().equals("C")).findFirst().orElse(null);
        assertNotNull(C);
        assertEquals("Corporate", C.getName());

        assertEquals(1, C.getChildDepartments().size());

        Department CCN = C.getChildDepartments().stream().filter(d -> d.getId().equals("CCN")).findFirst().orElse(null);
        assertNotNull(CCN);
        assertEquals("Corporate Compliance & Audit", CCN.getName());
    }

}
