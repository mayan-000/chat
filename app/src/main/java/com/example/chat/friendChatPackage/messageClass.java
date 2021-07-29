package com.example.chat.friendChatPackage;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class messageClass {
    private String date;
    private String time;
    private String message;
    private String type;
    private int read;

    public messageClass() {}

    public messageClass(String date, String time, String message, String type, int read) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.read = read;
        this.message = message;
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

    public int getRead() { return read; }

    public void setRead(int read) { this.read = read; }

}
