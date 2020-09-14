package com.markus.MassMailer.api;

import com.markus.MassMailer.model.mail.MailReference;
import org.springframework.http.HttpStatus;

public class ApiResponse {
    private HttpStatus status;
    private String message;
    private Long mailReferenceId;

    public ApiResponse(MailReference mailReference){
        this.status = HttpStatus.OK;
        this.message = "Mail Job Created";
        this.mailReferenceId = mailReference.getId();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getMailReferenceId() {
        return mailReferenceId;
    }
}
