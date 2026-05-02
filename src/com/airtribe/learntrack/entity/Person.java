package com.airtribe.learntrack.entity;

public class Person {
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    public Person() {
    }

    public Person(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        String safeFirst = firstName == null ? "" : firstName;
        String safeLast = lastName == null ? "" : lastName;
        return (safeFirst + " " + safeLast).trim();
    }

    public String getDisplayName() {
        return "Person #" + id + " - " + getFullName();
    }

    public String getContactLine() {
        if (hasEmail()) {
            return getFullName() + " <" + email + ">";
        }
        return getFullName();
    }

    public boolean hasEmail() {
        return email != null && email.trim().length() > 0;
    }

    public String toString() {
        return getDisplayName() + " | " + getContactLine();
    }
}
