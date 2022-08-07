package com.example.labtest2;


public class ContactModal {
    // variables
    private String Name;
    private String PhoneNumber;
    private String Address;
    private String Email;
    private int id;

    // creating getter and setter methods
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // constructor
    public ContactModal(String Name, String PhoneNumber,  String Email, String Address) {
        this.Name = Name;
        this.PhoneNumber = PhoneNumber;
        this.Email = Email;
        this.Address = Address;

    }
}

