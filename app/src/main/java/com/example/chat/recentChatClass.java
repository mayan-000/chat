package com.example.chat;

import java.util.ArrayList;

public class recentChatClass {
    private String userName;
    private String lastMessage;
    private int numberOfUnreadMessage = 0;
    private String dateTime;
    private String image;

    public recentChatClass() {}

    public recentChatClass(String userName, String lastMessage, int numberOfUnreadMessage,
                           String timeDate, String image) {
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.numberOfUnreadMessage = numberOfUnreadMessage;
        this.dateTime = timeDate;
        this.image = image;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getNumberOfUnreadMessage() {
        return numberOfUnreadMessage;
    }

    public void setNumberOfUnreadMessage(int numberOfUnreadMessage) {
        this.numberOfUnreadMessage = numberOfUnreadMessage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static void sort(ArrayList<recentChatClass> list){
        list.sort((o1,o2)->
            o2.getDateTime().compareToIgnoreCase(o1.getDateTime())
        );
    }

}
