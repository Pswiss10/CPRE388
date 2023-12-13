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

    /**
     * Get the first name of the user
     * @return firstName the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the last name of the user
     * @return lastName the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the email of the user
     * @return email The email for the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the theme of the user
     * @return theme the theme of the user
     */
    public String getTheme() { return theme; }
}
