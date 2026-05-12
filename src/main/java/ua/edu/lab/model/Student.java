package ua.edu.lab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {
    private Integer id;
    private String fullName;
    private String phone;
    private String email;
    private String studentIdCard;
    private String photo;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getStudentIdCard() { return studentIdCard; }
    public void setStudentIdCard(String studentIdCard) { this.studentIdCard = studentIdCard; }
    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
}