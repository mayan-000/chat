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

    public void encrypt(){

        ArrayList<Character> randoms = new ArrayList<>();
        randoms.add('!');randoms.add('@');randoms.add('#');randoms.add('$');randoms.add('%');
        randoms.add('^');randoms.add('&');randoms.add('*');randoms.add('(');randoms.add(')');
        randoms.add('?');randoms.add('?');randoms.add('_');randoms.add('{');randoms.add('}');
        randoms.add('q');randoms.add('w');randoms.add('e');randoms.add('r');randoms.add('t');
        randoms.add('p');randoms.add('o');randoms.add('i');randoms.add('u');randoms.add('y');
        randoms.add('a');randoms.add('s');randoms.add('d');randoms.add('f');randoms.add('g');
        randoms.add('h');randoms.add('j');randoms.add('k');randoms.add('l');randoms.add('z');
        randoms.add('n');randoms.add('b');randoms.add('v');randoms.add('c');randoms.add('x');
        randoms.add('m');randoms.add('.');randoms.add('<');randoms.add('>');randoms.add(':');


        String encrypted = "";

        char [] chars = this.message.toCharArray();


        for (char c : chars) {

            c = (char)((c+1e9));
            c = (char)((c+9015533));
            c = (char)((c+10271));
            c = (char)((c+1244096432));
            c = (char)((c+22));
            c = (char)((c+88499621));


            encrypted += (int) c;

            Random random = new Random();
            int pos = random.nextInt(15);

            encrypted += randoms.get(pos);
        }



        this.message = encrypted;
    }

    public void decrypt(){
        String decrypted = "";

        ArrayList<Character> randoms = new ArrayList<>();
        randoms.add('!');randoms.add('@');randoms.add('#');randoms.add('$');randoms.add('%');
        randoms.add('^');randoms.add('&');randoms.add('*');randoms.add('(');randoms.add(')');
        randoms.add('?');randoms.add('?');randoms.add('_');randoms.add('{');randoms.add('}');
        randoms.add('q');randoms.add('w');randoms.add('e');randoms.add('r');randoms.add('t');
        randoms.add('p');randoms.add('o');randoms.add('i');randoms.add('u');randoms.add('y');
        randoms.add('a');randoms.add('s');randoms.add('d');randoms.add('f');randoms.add('g');
        randoms.add('h');randoms.add('j');randoms.add('k');randoms.add('l');randoms.add('z');
        randoms.add('n');randoms.add('b');randoms.add('v');randoms.add('c');randoms.add('x');
        randoms.add('m');randoms.add('.');randoms.add('<');randoms.add('>');randoms.add(':');


        ArrayList<String> toDecrypt = new ArrayList<>();

        String msg = "";
        Log.d("msg",this.message);
        for (int i = 0; i < this.message.length(); i++) {
            if(randoms.contains(this.message.charAt(i))){
                toDecrypt.add(msg);
//                Log.d("msg",msg);
                msg="";
            }
            else{
                msg+=this.message.charAt(i);
            }
        }





        for (int i = 0; i < toDecrypt.size(); i++) {
            int num = Integer.parseInt(toDecrypt.get(i));

            num = (int) ((num-88499621)%128+128);
            num = (int) ((num-22)%128+128);
            num = (int) ((num-1244096432)%128+128);
            num = (int) ((num-10271)%128+128);
            num = (int) ((num-9015533)%128+128);
            num = (int) ((int) ((num-1e9))%128+128);

            num %= 128;
            decrypted += (char)num;


        }



        this.message = decrypted;
    }
}
