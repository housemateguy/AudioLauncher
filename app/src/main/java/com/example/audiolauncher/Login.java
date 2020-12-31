package com.example.audiolauncher;

public class Login {
    // fields
    private int LoginID;
    private String Name;
    private String Mail;
    private int Number;

    // constructors
    public Login() {}
    public Login(int id, String Name, String Mail, int Number) {
        this.LoginID = id;
        this.Name = Name;
    this.Mail=Mail;
    this.Number=Number;

    }
    // properties
    public void setID(int id) {
        this.LoginID = id;
    }
    public int getID() {
        return this.LoginID;
    }
    public void setName(String name) {
        this.Name = name;
    }
    public String getName() {
        return this.Name;
    }
    public String getMail() {
        return this.Mail;
    }
    public void setMail(String mail) {
        this.Mail = mail;
    }
    public int getNumber() {
        return this.Number;
    }
    public void setNumber(int num) {
        this.Number = num;
    }

}