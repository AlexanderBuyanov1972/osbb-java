package com.example.osbb.dto.response;

import java.util.List;


public class Response {
    private Object data;
    private List<String> messages;


    public Response() {
    }

    public Response(Object data, List<String> messages) {
        this.data = data;
        this.messages = messages;
    }

    public Response(List<String> messages) {
        this.messages = messages;
    }

    public Object getData() {
        return data;
    }

    public List<String> getMessages() {
        return messages;
    }

    public Response setData(Object data) {
        this.data = data;
        return this;
    }

    public Response setMessages(List<String> messages) {
        this.messages = messages;
        return this;
    }

    @Override
    public String toString() {
        return "Response = { data = " + data + ", messages = " + messages + " }";
    }
}
