package com.virtueltavle.virtueltavle.Models;

import java.util.Date;

public class Message extends Record
{
    public Message(){}

    public Message(Date _date, Employee _author, boolean _urgent,String _content)
    {
        super(_date,_author);
        urgent = _urgent;
        content = _content;
    }

    public boolean urgent = false;
    public String content;
}
