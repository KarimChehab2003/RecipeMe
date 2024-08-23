package com.example.jsonexample;

public class User {


    private long id;
    private String name;
    private String email;
    private String password;


    // in case of login


    public User(long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //in case of signup


    public User(String name, String email, String password , DBhelper dbh) {


        this.id = dbh.createNewUser(name,email,password);
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
