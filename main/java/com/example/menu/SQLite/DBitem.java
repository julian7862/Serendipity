package com.example.menu.SQLite;

public class DBitem {


    // Labels Table Columns names
    public  static final String KEY_ID = "ID";
    public  static final String KEY_NAME="NAME";
    public  static final String KEY_PID = "PID";
    public  static final String KEY_NOTE = "NOTE";
    public  static final String TIME_STAMP= "TIME_STAMP";
    public  static final String KEY_RATE = "RATE";

    public int id;
    public String name;
    public String pid;
    public String note;
    public String rate;
    public DBitem(){

    }

    public DBitem(String name, String pid, String note) {
        this.name = name;
        this.pid = pid;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVid() {
        return pid;
    }

    public void setVid(String pid) {
        this.pid = pid;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
