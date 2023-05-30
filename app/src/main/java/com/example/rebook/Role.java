package com.example.rebook;

public class Role {
    private int Role_id;
    private String Role_name;

    public Role(int role_id, String role_name) {
        Role_id = role_id;
        Role_name = role_name;
    }

    public int getRole_id() {
        return Role_id;
    }

    public String getRole_name() {
        return Role_name;
    }
}
