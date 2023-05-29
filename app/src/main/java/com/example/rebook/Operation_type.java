package com.example.rebook;

public class Operation_type {
    private int Operation_type_id;
    private String Operation_type_name;

    public Operation_type(int operation_type_id, String operation_type_name) {
        Operation_type_id = operation_type_id;
        Operation_type_name = operation_type_name;
    }

    public int getOperation_type_id() {
        return Operation_type_id;
    }

    public String getOperation_type_name() {
        return Operation_type_name;
    }
}
