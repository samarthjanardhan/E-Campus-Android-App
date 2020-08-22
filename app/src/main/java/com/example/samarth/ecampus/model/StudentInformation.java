package com.example.samarth.ecampus.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class StudentInformation implements Comparable<StudentInformation> {

    private String fname,lname,year,div,rollno,phone,email,password,isTeacher;
    ArrayList<Integer> subjects;
    public StudentInformation(){

    }

    public StudentInformation(String fname, String lname, String year, String div,String rollno, String phone, String email, String password, ArrayList<Integer> subjects) {
        this.fname = fname;
        this.lname = lname;
        this.year = year;
        this.div = div;
        this.rollno = rollno;
        this.phone = phone;
        this.email = email;
        this.password=password;
        this.subjects=subjects;
        this.isTeacher="false";
    }

    public String getIsTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(String isTeacher) {
        this.isTeacher=isTeacher;
    }

    public ArrayList<Integer> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<Integer> subjects) {
        this.subjects = subjects;
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

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    @Override
    public int compareTo(@NonNull StudentInformation studentInformation) {
        int roll1=Integer.parseInt(this.rollno);
        int roll2=Integer.parseInt(studentInformation.getRollno());

        if(roll1 > roll2) {
            return 1;
        } else if (roll1 == roll2) {
            return 0;
        } else {
            return -1;
        }
    }
}

