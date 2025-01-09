package org.example;

import java.io.*;
import java.util.*;
import org.example.model.Department;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLStreamException;

public class Main {
    public static void main(String[] args) {
        List<Department> departments = readCSV("department.csv");
        try {
            // Set up the XML writer and output file
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            FileWriter fileWriter = new FileWriter("department.xml");
            XMLStreamWriter writer = factory.createXMLStreamWriter(fileWriter);
            writer.writeStartDocument();
            writer.writeStartElement("NikuDataBus");
            writer.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            writer.writeAttribute("xsi:noNamespaceSchemaLocation", "../xsd/nikuxog_department.xsd");
            writer.writeStartElement("Header");
            writer.writeAttribute("action", "write");
            writer.writeAttribute("externalSource", "NIKU");
            writer.writeAttribute("objectType", "department");
            writer.writeAttribute("version", "15.9");
            writer.writeEndElement();
            writer.writeStartElement("Departments");

            for (Department dept : departments) {
                writeDepartment(writer, dept);
            }
            writer.writeEndElement();
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
            fileWriter.close();
            System.out.println("XML file created successfully!");
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
    }

    // Reads departments from a CSV file and returns a list of Department objects
    public static List<Department> readCSV(String filename) {
        List<Department> departments = new ArrayList<>();
        Map<String, Department> deptMap = new HashMap<>();

        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                System.err.println("File not found in resources: " + filename);
                return departments;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) continue;
                String[] fields = line.split(",");
                if (fields.length >= 2) {
                    String id = fields[1].trim();
                    String name = fields[2].trim();
                    String parentId = fields.length >= 3 ? fields[3].trim() : null;
                    Department dept = new Department(id, name);
                    deptMap.put(id, dept);
                    System.out.println(id);
                    if (parentId != null && deptMap.containsKey(parentId)) {
                        deptMap.get(parentId).addChildDepartment(dept);
                    } else {
                        departments.add(dept);  // If no parent, it's a root level department
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return departments;
    }

    private static void writeDepartment(XMLStreamWriter writer, Department dept) throws XMLStreamException {
        writer.writeStartElement("Department");
        writer.writeAttribute("department_code", dept.getId());
        writer.writeAttribute("dept_manager_code", "admin");
        writer.writeAttribute("entity", "Corporate");
        writer.writeAttribute("short_description", dept.getName());
        writer.writeStartElement("Description");
        writer.writeCharacters(dept.getName());
        writer.writeEndElement();
        writer.writeStartElement("LocationAssociations");
        writer.writeStartElement("LocationAssociation");
        writer.writeAttribute("locationcode", "DE");
        writer.writeEndElement();
        writer.writeEndElement();
        System.out.println(dept);

        for (Department childDept : dept.getChildDepartments()) {
            writeDepartment(writer, childDept);
        }
        writer.writeEndElement();
    }

}