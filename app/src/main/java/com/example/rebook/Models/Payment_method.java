package com.example.rebook.Models;

public class Payment_method {
    private int Payment_method_id;
    private String Payment_method_name;

    public Payment_method(int payment_method_id, String payment_method_name) {
        Payment_method_id = payment_method_id;
        Payment_method_name = payment_method_name;
    }

    public int getPayment_method_id() {
        return Payment_method_id;
    }

    public String getPayment_method_name() {
        return Payment_method_name;
    }

    @Override
    public String toString(){
        return getPayment_method_name();
    }
}
