package com.markus.MassMailer.model.mail;

import com.markus.MassMailer.model.user.User;

public class ErrorMessage {
    String errorMessage;
    User user;
    int index;

    public ErrorMessage(String errorMessage, User user, int index) {
        this.errorMessage = errorMessage;
        this.user = user;
        this.index = index;
    }

    @Override
    public String toString() {
        return "Error processing Mail for User " + user.getFullName() +
                " on line " + (index + 1) +
                " Message: " + errorMessage;
    }
}
