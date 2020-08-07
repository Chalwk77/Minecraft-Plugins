package jericraft.chalwk;

import java.util.List;

public class Message {
    private List<String> messages;

    public Message(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return this.messages;
    }
}
