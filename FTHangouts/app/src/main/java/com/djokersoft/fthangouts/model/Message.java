
package com.djokersoft.fthangouts.model;

public class Message {
    private long id;
    private long contactId;
    private String content;
    private String timestamp;
    private boolean isSent; // true se enviada pelo user, false se recebida


    public Message() {}

    public Message(long contactId, String content, String timestamp, boolean isSent) {
        this.contactId = contactId;
        this.content = content;
        this.timestamp = timestamp;
        this.isSent = isSent;
    }


    public Message(long id, long contactId, String content, String timestamp, boolean isSent) {
        this.id = id;
        this.contactId = contactId;
        this.content = content;
        this.timestamp = timestamp;
        this.isSent = isSent;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", contactId=" + contactId +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", isSent=" + isSent +
                '}';
    }
}