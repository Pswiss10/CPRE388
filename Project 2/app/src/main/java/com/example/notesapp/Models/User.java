package com.example.notesapp.Models;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;

    private String theme;

    public User(String id, String firstName, String lastName, String email, String theme) {
       this.id = id;
       this.firstName = firstName;
       this.lastName = lastName;
       this.email = email;
       this.theme = theme;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getTheme() { return theme; }
}
