
package com.djokersoft.fthangouts.model;

import com.djokersoft.fthangouts.utils.GlobalValues;

public class Contact {
    private long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private String birthday;


    public Contact() {}

    public Contact(String name, String phoneNumber, String email, String address, String birthday) {
        this.name = name;
        this.phoneNumber = GlobalValues.formatPhoneNumber(phoneNumber);
        this.email = email;
        this.address = address;
        this.birthday = birthday;
    }

    public Contact(long id, String name, String phoneNumber, String email, String address, String birthday) {
        this.id = id;
        this.name = name;
        this.phoneNumber = GlobalValues.formatPhoneNumber(phoneNumber);
        this.email = email;
        this.address = address;
        this.birthday = birthday;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = GlobalValues.formatPhoneNumber(phoneNumber);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}