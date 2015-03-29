package com.virtueltavle.virtueltavle.Models;

import java.util.Date;

public class Record
{
    public Record(){}

    public Record(Date _date, Employee _author)
    {
        author= _author;
        date = _date;
    }

    public Date date;
    public Employee author;
}
