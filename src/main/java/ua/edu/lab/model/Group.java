package ua.edu.lab.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupNumber;
    private int formationYear;

    @JacksonXmlElementWrapper(localName = "students")
    @JacksonXmlProperty(localName = "student")
    private List<Student> students = new ArrayList<>();

    public String getGroupNumber() { return groupNumber; }
    public void setGroupNumber(String groupNumber) { this.groupNumber = groupNumber; }
    public int getFormationYear() { return formationYear; }
    public void setFormationYear(int formationYear) { this.formationYear = formationYear; }
    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }
    private int id;
}