package com.example.rebook;

public class Operation {
    private int Operation_id;
    private int Operation_type_id;
    private int Operation_status_id;
    private int User_id;
    private int Book_id;
    private int Payment_amount;
    private String Operation_date;
    private int Payment_method_id;

    public Operation(int operation_id, int operation_type_id,int operation_status_id, int user_id, int book_id, int payment_amount, String operation_date, int payment_method_id) {
        Operation_id = operation_id;
        Operation_type_id = operation_type_id;
        Operation_status_id = operation_status_id;
        User_id = user_id;
        Book_id = book_id;
        Payment_amount = payment_amount;
        Operation_date = operation_date;
        Payment_method_id = payment_method_id;
    }

    public int getOperation_id() {
        return Operation_id;
    }

    public int getOperation_type_id() {
        return Operation_type_id;
    }

    public int getUser_id() {
        return User_id;
    }

    public int getBook_id() {
        return Book_id;
    }

    public int getPayment_amount() {
        return Payment_amount;
    }

    public String getOperation_date() {
        return Operation_date;
    }

    public int getPayment_method_id() {
        return Payment_method_id;
    }

    public int getOperation_status_id() {
        return Operation_status_id;
    }
}
