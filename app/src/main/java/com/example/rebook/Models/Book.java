package com.example.rebook.Models;

import java.io.Serializable;

public class Book implements Serializable {
    private int Book_id;
    private String Book_name;
    private String Book_isbn;
    private int Category_id;
    private int School_id;
    private int Grade_id;
    private String Book_condition;
    private String Book_image_path;
    private int Book_price;

    public Book(int book_id, String book_name, String book_isbn, int category_id, int school_id, int grade_id, String book_condition, String book_image_path, int book_price) {
        Book_id = book_id;
        Book_name = book_name;
        Book_isbn = book_isbn;
        Category_id = category_id;
        School_id = school_id;
        Grade_id = grade_id;
        Book_condition = book_condition;
        Book_image_path = book_image_path;
        Book_price = book_price;
    }


    public int getBook_id() {
        return Book_id;
    }

    public String getBook_name() {
        return Book_name;
    }

    public String getBook_isbn() {
        return Book_isbn;
    }

    public int getCategory_id() {
        return Category_id;
    }

    public int getSchool_id() {
        return School_id;
    }

    public int getGrade_id() {
        return Grade_id;
    }

    public String getBook_condition() {
        return Book_condition;
    }

    public String getBook_image_path() {
        return Book_image_path;
    }

    public int getBook_price() {
        return Book_price;
    }


    @Override
    public String toString(){
        return this.getBook_name();
    }

}
