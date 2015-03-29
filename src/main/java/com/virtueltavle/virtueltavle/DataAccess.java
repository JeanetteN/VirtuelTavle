package com.virtueltavle.virtueltavle;

import android.util.Log;

import com.virtueltavle.virtueltavle.Models.Employee;
import com.virtueltavle.virtueltavle.Models.Message;
import com.virtueltavle.virtueltavle.Models.Record;
import com.virtueltavle.virtueltavle.Models.VirtualBoard;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Jeanette on 18-Mar-15.
 */
public class DataAccess {

    public ArrayList<Employee> getEmployees() {

        ArrayList<Employee> result = new ArrayList<Employee>();
        Log.i("DataAccess","getEmployees called.");
        System.out.println("getEmployees called.");
        try {
            String url = "http://jen-net.dk/VirtuelTavle/employees.json";
            String jsonEmployee = GetURl(url);
            Log.i("DataAccess","json string:"+jsonEmployee);

            JSONObject json = new JSONObject(jsonEmployee);
            JSONArray employees = json.getJSONArray("employees");
            int count = employees.length();
            Log.i("DataAccess","json count:"+count);

            for (int i = 0; i < count; i++) {
                JSONObject employee = employees.getJSONObject(i);
                Employee emp = new Employee();
                emp.cleaningCrew = employee.getBoolean("cleaningCrew");
                emp.employeeNr = employee.getString("employeeNr");
                emp.firstName = employee.getString("firstName");
                emp.lastName = employee.getString("lastName");
                Log.i("DataAccess","Employee: "+emp.employeeNr);

                result.add(emp);
            }
        } catch (Exception e) {
            Log.i("DataAccess",""+e.toString());
            System.out.println("could not parse json: "+e.toString());

        }

        return result;
    }

    public ArrayList<VirtualBoard> getVirtualBoards() {

        if(MyApp.data.boards != null)
        {
            return MyApp.data.boards;
        }

        return CreateDummyData();
    }

    public ArrayList<VirtualBoard> CreateDummyData()
    {
        ArrayList<VirtualBoard> result = new ArrayList<VirtualBoard>();
        result.add(CreateBoard("B220",true,true,9));

        result.add(CreateBoard("A120",true,false,7));
        result.add(CreateBoard("A205",true,false,3));

        result.add(CreateBoard("A204",false,false,26));
        result.add(CreateBoard("A209",false,false,22));
        result.add(CreateBoard("D004",false,false,19));
        result.add(CreateBoard("A201",false,false,15));
        result.add(CreateBoard("R203",false,false,10));
        result.add(CreateBoard("R123",false,false,9));
        result.add(CreateBoard("R202",false,false,8));
        result.add(CreateBoard("B201",false,false,8));
        result.add(CreateBoard("C102",false,false,6));
        result.add(CreateBoard("D003",false,false,5));
        result.add(CreateBoard("C012",false,false,3));
        result.add(CreateBoard("D112",false,false,2));
        result.add(CreateBoard("K130",false,false,0));

        return result;
    }

    private VirtualBoard CreateBoard(String roomNumber, boolean haveActiveMsg, boolean urgent, int hoursFromLastClean) {
        VirtualBoard b1 = new VirtualBoard();
        b1.roomNumber =roomNumber;
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.HOUR, -(48+hoursFromLastClean));

        b1.addRecord(new Record(cal.getTime(), getEmployee("PJO")));
        cal.add(Calendar.HOUR, 10);

        b1.addMessage(new Message(cal.getTime(), getEmployee("JDO"), true, "Blod på gulvet"));
        cal.add(Calendar.HOUR, 15);


        b1.addRecord(new Record(cal.getTime(), getEmployee("PJO")));
        cal.add(Calendar.HOUR, 5);
        b1.addMessage(new Message(cal.getTime(),getEmployee("JDO"),false, "Fyld sæbe dispenser" ));

        cal.add(Calendar.HOUR, 2);

        b1.addRecord(new Record(cal.getTime(), getEmployee("SHA")));

        cal.add(Calendar.HOUR, 4);

        b1.addRecord(new Record(cal.getTime(), getEmployee("AKA")));
        cal.add(Calendar.HOUR, 12);
        b1.addRecord(new Record(cal.getTime(), getEmployee("PJO")));

        if(haveActiveMsg)
        {
            cal = new GregorianCalendar();
            b1.addMessage(new Message(cal.getTime(),getEmployee("ASM"),urgent, "Ekstra lagner" ));
        }
        return b1;
    }

    public static String GetURl(String url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line + "\n");
            line = br.readLine();
        }
        return sb.toString();
    }


    ArrayList<Employee> employees;

    public Employee getEmployee(String employeeNumber) {

        if(employees == null)
        {
            employees = getEmployees();
        }
        for (int i = 0; i< employees.size(); i++)
        {
            Employee emp =employees.get(i);

            if(emp.employeeNr.equals( employeeNumber))
            {
                return emp;
            }
        }

        return null;
    }

}

