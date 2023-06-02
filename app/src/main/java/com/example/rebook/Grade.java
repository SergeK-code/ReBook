package com.example.rebook;

public class Grade {
    private int Grade_id;
    private String Grade_name;

    public Grade(int grade_id, String grade_name) {
        Grade_id = grade_id;
        Grade_name = grade_name;
    }

    public int getGrade_id() {
        return Grade_id;
    }

    public String getGrade_name() {
        return Grade_name;
    }

    @Override
    public String toString(){
        return this.getGrade_name();
    }
}
