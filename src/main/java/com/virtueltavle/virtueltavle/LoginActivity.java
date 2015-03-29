package com.virtueltavle.virtueltavle;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.virtueltavle.virtueltavle.Models.Employee;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener{

    Button login;
    EditText password, employeenumber;
    RelativeLayout loadingPanel;
    String pref_employeeNumberKey = "loginActivity_employeeNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(MyApp.data.loggedInUser != null)
        {
            goToMain();
        }

        setContentView(R.layout.activity_login);
        login = (Button)findViewById(R.id.loginBtn);
        login.setOnClickListener(this);

        employeenumber = (EditText)findViewById(R.id.medarbejderNr);
        password = (EditText)findViewById(R.id.password);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);

        employeenumber.setText( MyApp.prefs.getString(pref_employeeNumberKey,""));

        goToMain();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApp.prefs.edit().putString(pref_employeeNumberKey, employeenumber.getText().toString()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Log.i("LogActivity", "loginview click" );

        System.out.print("loginview click");
        if(v== login)
        {
            loadingPanel.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            new AsyncTask() {
                @Override
                protected Object doInBackground(Object... arg0) {
                    try {
                        System.out.print("password: "+password.getText().toString());
                        Log.i("LogActivity", "password: "+password.getText().toString() );
                        Log.i("LogActivity", "employeenumber: "+employeenumber.getText().toString() );

                        Log.i("LogActivity", "MyApp.dataAccess ==null: "+(MyApp.dataAccess==null? "True":"False") );

                        if(password.getText().toString().equals("123")) {
                            Employee user = MyApp.dataAccess.getEmployee(employeenumber.getText().toString());
                            return user;
                        }
                        Log.i("LogActivity", "Invalid password" );

                        return null;

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.print(e.getStackTrace());
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Object result) {
                    loadingPanel.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);
                    if(result == null)
                    {
                        makeToast("Forkert medarbejdernummer eller password");
                        return;
                    }

                    Employee user = (Employee) result;
                    makeToast("Velkommen "+ user.firstName+"!");

                    //Save employee 2 data
                    MyApp.data.loggedInUser = user;
                    MyApp.data.notifyObservatører();
                    MyApp.prefs.edit().putString("loggedInUser", user.employeeNr).commit();

                    //go to next activity
                    goToMain();
                }
            }.execute();
        }
    }

    private void goToMain()
    {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void makeToast(String txt)
    {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }

}
