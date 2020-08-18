package com.trainchatapp.model;

public class User {

    private String id;
    private String username;
    private String facePic;
    private String bodyPic;
    private String search;
    private String description;
    private boolean staff;
    private boolean verified;
    private String firstName;
    private String lastName;

    public String getFacePic() {
        return facePic;
    }

    public void setFacePic(String facePic) {
        this.facePic = facePic;
    }

    public String getBodyPic() {
        return bodyPic;
    }

    public void setBodyPic(String bodyPic) {
        this.bodyPic = bodyPic;
    }

    public User(String id, String username, String facePic, String bodyPic,
                String search, String description, boolean staff, boolean verified,
                String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.facePic = facePic;
        this.bodyPic = bodyPic;
        this.search = search;
        this.description = description;
        this.staff = staff;
        this.verified = verified;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public boolean isStaff() {
        return staff;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", bodyPic='" + bodyPic + '\'' +
                ", facePic='" + facePic + '\'' +
                ", search='" + search + '\'' +
                ", description='" + description + '\'' +
                ", staff=" + staff +
                ", verified=" + verified +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}