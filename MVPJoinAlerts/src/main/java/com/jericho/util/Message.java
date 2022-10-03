package com.jericho.util;

import java.util.List;

public class Message {
    private final List<String> messages;

    public Message(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return this.messages;
    }
}
