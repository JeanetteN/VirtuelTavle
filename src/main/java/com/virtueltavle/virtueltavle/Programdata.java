package com.virtueltavle.virtueltavle;

import com.virtueltavle.virtueltavle.Models.Employee;
import com.virtueltavle.virtueltavle.Models.VirtualBoard;

import java.io.Serializable;
import java.util.ArrayList;

public class Programdata implements Serializable {
    // Vigtigt: Sæt versionsnummer så objekt kan læses selvom klassen er ændret!
    private static final long serialVersionUID = 12345; // bare et eller andet nr.

//    public String employeeNr = "";
//    public boolean loggedIn = false;
//    public boolean IsCleaningCrew = false;
    public Employee loggedInUser;
    public ArrayList<VirtualBoard> boards;
    public VirtualBoard selectedBoard = null;
    //public ArrayList<String> brugteBogstaver = new ArrayList<String>();
    public transient ArrayList<Runnable> observatører = new ArrayList<Runnable>();

    public String toString() {
        return"medarbejderNr: ";//+ employeeNr + " Gættede bogstaver: " + brugteBogstaver + " indtastning: "+indtastning;
    }

    public void notifyObservatører() {
        for (Runnable r : observatører) r.run();
    }
}
