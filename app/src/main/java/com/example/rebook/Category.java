package com.example.rebook;

public class Category {
    private int Category_id;
    private String Category_name;

    public Category(int category_id, String category_name) {
        Category_id = category_id;
        Category_name = category_name;
    }

    public int getCategory_id() {
        return Category_id;
    }

    public String getCategory_name() {
        return Category_name;
    }


    @Override
    public String toString(){
        return this.getCategory_name();
    }

}
