package ua.edu.lab.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import ua.edu.lab.model.*;
import javax.servlet.ServletContext;
import java.io.*;
import java.util.*;

public class UniversityRepository {
    private List<Faculty> faculties = new ArrayList<>();
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();

    public synchronized void load(ServletContext context) throws IOException {
        InputStream jsonIs = context.getResourceAsStream("/WEB-INF/data/university.json");
        if (jsonIs != null) {
            faculties = new ArrayList<>(Arrays.asList(jsonMapper.readValue(jsonIs, Faculty[].class)));
        } else {
            InputStream xmlIs = context.getResourceAsStream("/WEB-INF/data/university.xml");
            if (xmlIs != null) {
                faculties = new ArrayList<>(Arrays.asList(xmlMapper.readValue(xmlIs, Faculty[].class)));
            }
        }
    }

    public List<Faculty> getAll() { return faculties; }

    public Optional<Faculty> getFaculty(int id) {
        return faculties.stream().filter(f -> f.getId() == id).findFirst();
    }

    public Optional<Student> getStudent(int fId, int sId) {
        return getFaculty(fId).stream()
                .flatMap(f -> f.getGroups().stream())
                .flatMap(g -> g.getStudents().stream())
                .filter(s -> s.getId() == sId).findFirst();
    }
}