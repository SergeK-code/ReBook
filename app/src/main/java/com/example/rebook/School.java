package com.example.rebook;

public class School {
    private int School_id;
    private String School_name;

    public School(int school_id, String school_name) {
        School_id = school_id;
        School_name = school_name;
    }

    public int getSchool_id() {
        return School_id;
    }

    public String getSchool_name() {
        return School_name;
    }
}