package com.example.chat;

public class messageClass {
    private String date;
    private String time;
    private String message;
    private String type;

    public messageClass() {}

    public messageClass(String date, String time, String message, String type) {
        this.date = date;
        this.time = time;
        this.message = message;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
