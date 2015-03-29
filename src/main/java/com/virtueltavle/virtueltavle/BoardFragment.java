package com.virtueltavle.virtueltavle;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.virtueltavle.virtueltavle.Models.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BoardFragment extends Fragment {

    private View root;
    private ListView listView;
    TextView roomNr, hours, time, msg;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        root = i.inflate(R.layout.fragment_board, container, false);
        roomNr =(TextView) root.findViewById(R.id.roomNumber);
        hours =(TextView) root.findViewById(R.id.hours);
        time =(TextView) root.findViewById(R.id.time);
        msg = (TextView) root.findViewById(R.id.message);

        if( MyApp.data.selectedBoard != null)
        {
            roomNr.setText( MyApp.data.selectedBoard.roomNumber);
            Date lastCleaned = MyApp.data.selectedBoard.lastCleaned();

            if(lastCleaned != null)
            {
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                time.setText( df.format(lastCleaned));
            }

            Message activeMessage = MyApp.data.selectedBoard.getActiveMessage();
            if(activeMessage == null)
            {
                msg.setVisibility(View.GONE);
            }
            else
            {
                msg.setVisibility(View.VISIBLE);
                msg.setText(activeMessage.content);

                //Show urgent symbol
            }

        }
        return root;
    }
}
