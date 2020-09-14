package com.markus.MassMailer.model.user;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

public class User implements Serializable {
    private final Map<String, Object> data;

    public User(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Object> getData(){
        return data;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + data.get("firstname") + '\'' +
                '}';
    }

    public String getMailAddress() {
        return (String) data.get("email");
    }

    public String getFirstname() {
        return (String) data.get("firstname");
    }

    public String getLastname() {
        return (String) data.get("lastname");
    }

    public String getFullName() {
        return getFirstname() + " " + getLastname();
    }
}
