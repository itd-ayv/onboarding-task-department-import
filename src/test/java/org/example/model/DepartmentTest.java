package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


public class DepartmentTest {

    private Department department;

    @BeforeEach
    public void setUp() {
        department = new Department("C", "Corporate");
    }

    @Test
    public void testDepartmentCreation() {
        assertNotNull(department);
        assertEquals("C", department.getId());
        assertEquals("Corporate", department.getName());
    }

    @Test
    public void testAddChildDepartment() {
        Department childDepartment = new Department("C", "Corporate");
        department.addChildDepartment(childDepartment);

        List<Department> childDepartments = department.getChildDepartments();
        assertEquals(1, childDepartments.size());
        assertEquals("C", childDepartments.get(0).getId());
        assertEquals("Corporate", childDepartments.get(0).getName());
    }

}
