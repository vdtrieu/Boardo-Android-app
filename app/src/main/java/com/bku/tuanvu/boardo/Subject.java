package com.bku.tuanvu.boardo;

import java.io.Serializable;

/**
 * Created by Tuan Vu on 23/03/2018.
 */

public class Subject implements Serializable{
    private int     id;
    private String  name;
    private String  address;
    private String  time; //for saving, include weekdays(3 bits for 7 days) and periods(binary) and "-"
    private String  week;
    private String  teacher;
    private String  phoneNumber;
    private String  email;
    private String  note;

    public Subject(){}

    public Subject(int id, String name, String address, String time, String week,
                   String teacher, String phoneNumber, String email, String note){
        this.id             = id;
        this.name           = name;
        this.address        = address;
        this.time           = time;
        this.week           = week;
        this.teacher        = teacher;
        this.phoneNumber    = phoneNumber;
        this.email          = email;
        this.note           = note;
    }

    public Subject(String name, String address, String time, String week,
                   String teacher, String phoneNumber, String email, String note){
        this.name           = name;
        this.address        = address;
        this.time           = time;
        this.week           = week;
        this.teacher        = teacher;
        this.phoneNumber    = phoneNumber;
        this.email          = email;
        this.note           = note;
    }

        public Subject(Subject subject){
        this.id         = subject.id;
        this.name       = subject.name;
        this.address    = subject.address;
        this.time       = subject.time;
        this.week       = subject.week;
        this.teacher    = subject.teacher;
        this.phoneNumber = subject.phoneNumber;
        this.email      = subject.email;
        this.note       = subject.note;
    }

    public void setId(int id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setAddress(String address) {this.address = address; }

    public void setTime(String time) {this.time = time;}

    public void setWeek(String week) {this.week = week;}

    public void setTeacher(String teacher) {this.teacher = teacher;}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public void setEmail(String email) { this.email = email;}

    public void setNote(String note){this.note = note;}

    public int getId() {return this.id;}

    public String getName() {return this.name;}

    public String getAddress() {return this.address;}

    public String getTime() {return this.time;}

    public String getWeek() {return this.week;}

    public String getTeacher() {return this.teacher;}

    public String getPhoneNumber() {return this.phoneNumber;}

    public String getEmail() {return this.email;}

    public String getNote() {return this.note;}

    public int periodToInt(int NthClass){
        if(!this.time.isEmpty()) {
            String period;
            int index = 0;
            int counter = 0;
            int beginIndex = -1;
            int endIndex = -1;

            while (index < this.time.length()) {
                if (this.time.charAt(index) == ' ') {
                    counter++;
                    if (counter == 2 * NthClass - 1)
                        beginIndex = index + 1;
                    else if (counter == 2 * NthClass) {
                        endIndex = index;
                        break;
                    }
                }
                index++;
            }

            if (beginIndex == -1)
                return -1;

            if (index < this.time.length())
                period = this.time.substring(beginIndex, endIndex);
            else
                period = this.time.substring(beginIndex);
            int p = 0;
            for (int i = period.length()-1; i>=0;i--){
                p = p + (int)Math.pow(2,period.length()-1-i)*Integer.parseInt(String.valueOf(period.charAt(i)));}
            return p;
        }
        else{
            return -1;
        }
    }

    public int weekdayToInt(int NthClass){
        if(!this.time.isEmpty()){
            String wDay;
            int index = 0;
            int counter = 0;
            int beginIndex = -1;
            int endIndex = -1;

            if(NthClass == 1){
                while(this.time.charAt(++index) != ' ');
                beginIndex = 0;
                endIndex = index;
            }
            else{
                while(index < this.time.length()){
                    if(this.time.charAt(index) == ' '){
                        counter++;
                        if (counter == 2 * (NthClass - 1)){
                            beginIndex = index + 1;
                            int i = beginIndex;
                            while(this.time.charAt(++i) != ' ');
                            endIndex = i;
                            break;
                        }
                    }
                    index++;
                }
            }
            if (beginIndex == -1 || endIndex == -1)
                return -1;

            wDay = this.time.substring(beginIndex,endIndex);
            return Integer.parseInt(wDay);
        }
        else
            return -1;
    }

    public int getTotalClass(){
        int counter = 0;
        for (int i = 0; i < this.time.length(); i++){
            if (this.time.charAt(i) == ' ')
                counter++;
        }
        counter += 1;

        return counter / 2;
    }

    public String toString(){
        return new String(this.name+" "+this.address+" "+ this.time+" "+this.week+" "+
                this.teacher+ " " + this.phoneNumber+ " "+this.email+ " " + this.note);
    }
}
