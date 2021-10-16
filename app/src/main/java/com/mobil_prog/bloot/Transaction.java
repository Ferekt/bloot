package com.mobil_prog.bloot;

import java.util.Date;

public class Transaction {
    public String sender;
    public int value;
    public String reciever;
    public Date date;

    Transaction(String _sender,int _value,String _reciever,Date _date_time){
        sender = _sender;
        value = _value;
        reciever = _reciever;
        date = _date_time;
    }
}
