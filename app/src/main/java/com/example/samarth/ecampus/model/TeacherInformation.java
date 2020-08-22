package com.example.samarth.ecampus.model;

public class TeacherInformation {

    private String fname,lname, desgn, phone,email,password,isTeacher;
    public TeacherInformation(){

    }

    public TeacherInformation(String fname, String lname, String desgn, String phone, String email, String password) {
        this.fname = fname;
        this.lname = lname;
        this.desgn=desgn;
        this.phone = phone;
        this.email = email;
        this.password=password;
        this.isTeacher="true";
    }

    public String getIsTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(String isTeacher) {
        this.isTeacher=isTeacher;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDesgn() {
        return desgn;
    }

    public void setDesgn(String desgn) {
        this.desgn = desgn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
