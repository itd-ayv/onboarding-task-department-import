package org.example.model;

import java.util.*;

public class Department {
    String id;
    String name;
    List<Department> childDepartments;

    public Department(String id, String name) {
        this.id = id;
        this.name = name;
        this.childDepartments = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addChildDepartment(Department child) {
        this.childDepartments.add(child);
    }

    public List<Department> getChildDepartments() {
        return  childDepartments;
    }
}
