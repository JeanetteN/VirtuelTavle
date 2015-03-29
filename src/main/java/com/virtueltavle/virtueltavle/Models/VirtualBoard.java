package com.virtueltavle.virtueltavle.Models;

import java.util.ArrayList;
import java.util.Date;

public class VirtualBoard
{
    public String roomNumber = "";
    public ArrayList<Message> messages = new ArrayList<Message>();
    public ArrayList<Record> history = new ArrayList<Record>();

    public void addMessage(Message msg)
    {
        messages.add(0,msg);
    }

    public void addRecord(Record record)
    {
        history.add(0,record);
    }

    public Date lastCleaned()
    {
        if(history.size()>0)
        {
            return history.get(0).date;
        }
        return null;
    }

    public Message getActiveMessage()
    {
        if(messages.size()== 0) return null;

        Message lastMsg = messages.get(0);
        Date lastCleaned =lastCleaned();
        if(lastCleaned == null || lastMsg.date.after(lastCleaned))
        {
            return lastMsg;
        }
        return null;
    }
}
