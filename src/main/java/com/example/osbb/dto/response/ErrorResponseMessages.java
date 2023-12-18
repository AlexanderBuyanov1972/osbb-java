package com.example.osbb.dto.response;

import java.util.List;

public class ErrorResponseMessages {
    private List<String> messages;

    public ErrorResponseMessages() {
    }

    public ErrorResponseMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public ErrorResponseMessages setMessages(List<String> messages) {
        this.messages = messages;
        return this;
    }

    @Override
    public String toString() {
        return "ErrorResponseMessages = {" + " messages = " + messages + " }";
    }
}
