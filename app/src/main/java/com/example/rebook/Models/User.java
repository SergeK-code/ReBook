package com.example.rebook.Models;

import java.io.Serializable;

public class User implements Serializable {
    private int User_id;
    private String User_first_name;
    private String User_last_name;
    private int Role_id;
    private String User_dob;
    private String User_gender;
    private String User_address;
    private String User_phone;
    private String User_email;
    private String User_password;
    private int School_id;

    public User(int user_id, String user_first_name, String user_last_name, int role_id, String user_dob, String user_gender, String user_address, String user_phone, String user_email, String user_password, int school_id) {
        User_id = user_id;
        User_first_name = user_first_name;
        User_last_name = user_last_name;
        Role_id = role_id;
        User_dob = user_dob;
        User_gender = user_gender;
        User_address = user_address;
        User_phone = user_phone;
        User_email = user_email;
        User_password = user_password;
        School_id = school_id;
    }

    public int getUser_id() {
        return User_id;
    }

    public String getUser_first_name() {
        return User_first_name;
    }

    public String getUser_last_name() {
        return User_last_name;
    }

    public int getRole_id() {
        return Role_id;
    }

    public String getUser_dob() {
        return User_dob;
    }

    public String getUser_gender() {
        return User_gender;
    }

    public String getUser_address() {
        return User_address;
    }

    public String getUser_phone() {
        return User_phone;
    }

    public String getUser_email() {
        return User_email;
    }

    public String getUser_password() {
        return User_password;
    }

    public int getSchool_id() {
        return School_id;
    }
}
