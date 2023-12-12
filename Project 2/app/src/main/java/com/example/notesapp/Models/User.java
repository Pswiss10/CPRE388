package com.example.notesapp.Models;

/**
 * Class to store user information
 */
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String theme;

    /**
     * Constructor for the User class
     * @param id
     * @param firstName
     * @param lastName
     * @param email
     * @param theme
     */
    public User(String id, String firstName, String lastName, String email, String theme) {
       this.id = id;
       this.firstName = firstName;
       this.lastName = lastName;
       this.email = email;
       this.theme = theme;
    }

    /**
     * Get the id of the user
     * @return string, userID
     */
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
