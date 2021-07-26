package com.example.chat;

import java.util.ArrayList;

public class messageClassNew{
    private String Uid;
    private String date;
    private String time;
    private String message;
    private String type;
    private int read;

    public messageClassNew() {}

    public messageClassNew(messageClass message){
        this.date = message.getDate();
        this.time = message.getTime();
        this.message = message.getMessage();
        this.read = message.getRead();
        this.type = message.getType();
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
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

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public messageClassNew(String uid, String date, String time, String message, String type, int read) {
        Uid = uid;
        this.date = date;
        this.time = time;
        this.message = message;
        this.type = type;
        this.read = read;
    }

    public static void sort(ArrayList<messageClassNew> list){
        list.sort((o1, o2) ->
            o2.getDate().compareToIgnoreCase(o1.getDate())
        );
    }

}
