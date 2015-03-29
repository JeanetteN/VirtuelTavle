package com.virtueltavle.virtueltavle;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.virtueltavle.virtueltavle.Models.Message;
import com.virtueltavle.virtueltavle.Models.VirtualBoard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


//public class RoomsList_Frag extends Fragment implements View.OnClickListener  {
//
//    private View root;
//    @Override
//    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
//        root = i.inflate(R.layout.fragmenttest, container, false);
//
//        Log.i("RoomsList_Frag", "Fragment inflated");
//        return root;
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//}



@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RoomsList_Frag extends Fragment implements View.OnClickListener {

    private View root;
    private ListView listView;
    private ArrayList<VirtualBoard> boards;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        root = i.inflate(R.layout.rooms_list__frag, container, false);


        boards = MyApp.dataAccess.getVirtualBoards();
        ArrayAdapter<VirtualBoard> adapter = new ArrayAdapter<VirtualBoard>(getActivity().getApplicationContext(), R.layout.room_list_element, R.id.roomlist_elm_headline, boards) {
            @Override
            public View getView(int position, View cachedView, ViewGroup parent) {

                View view = super.getView(position, cachedView, parent);
                VirtualBoard item = getItem(position);

                TextView headline = (TextView) view.findViewById(R.id.roomlist_elm_headline);
                headline.setText(item.roomNumber);

                Date lastcleaned = item.lastCleaned();
                long hoursDiff = getDateDiff(lastcleaned,Calendar.getInstance().getTime(), TimeUnit.HOURS);

                //DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                TextView text = (TextView) view.findViewById(R.id.roomlist_elm_text);
                //text.setText(df.format(lastcleaned));
                text.setText(hoursDiff + " timer siden");

                ImageView img = (ImageView) view.findViewById(R.id.roomlist_elm_img);
//                if (position % 3 == 2) {
//                    img.setImageResource(R.drawable.red);
//                } else {
//                    img.setImageResource(android.R.drawable.sym_action_email);
//                }
                if(hoursDiff >24) {
                    img.setImageResource(R.drawable.magenta);
                }
                else if(hoursDiff > 20)
                {
                    img.setImageResource(R.drawable.red);
                }
                else if(hoursDiff > 17)
                {
                    img.setImageResource(R.drawable.orange);
                }
                else if(hoursDiff > 12)
                {
                    img.setImageResource(R.drawable.yellow);
                }
                else
                {
                    img.setImageResource(R.drawable.green);
                }


                Message msg = item.getActiveMessage();

                if(msg != null)
                {
                    img.setImageResource(R.drawable.magenta);
                }

                ImageView msgImg = (ImageView) view.findViewById(R.id.roomlist_elm_msg);
                msgImg.setVisibility(msg == null ? View.INVISIBLE : View.VISIBLE);

                ImageView alertImg = (ImageView) view.findViewById(R.id.roomlist_elm_alert);
                alertImg.setVisibility(msg == null || !msg.urgent? View.INVISIBLE : View.VISIBLE);

                return view;
            }
        };



        listView = (ListView) root.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position1, long id) {

                Log.i("RoomLost", "Item clicked, index: " + position1);

                if(position1>= boards.size()) return;


                MyApp.data.selectedBoard = boards.get(position1);

                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContent, new BoardFragment());
                ft.addToBackStack(null);
                ft.commit();

                getFragmentManager().executePendingTransactions();
            }
        });
        listView.setAdapter(adapter);

        return root;
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }




        public void onClick(View v) {
//        System.out.println("Der blev trykket på en knap");
//
//        // Vis et tal der skifter så vi kan se hver gang der trykkes
//        long etTal = System.currentTimeMillis();
//
//        if (v == knap1) {
//
//            knap1.setText("Du trykkede på mig. Tak! \n" + etTal);
//
//        } else if (v == knap2) {
//
//            knap3.setText("Nej nej, tryk på mig i stedet!\n" + etTal);
//
//        } else if (v == knap3) {
//
//            knap2.setText("Hey, hvis der skal trykkes, så er det på MIG!\n" + etTal);
//            // Erstat logoet med en bil
//            ImageView ikon = (ImageView) rod.findViewById(R.id.ikon);
//            ikon.setImageResource(R.drawable.ic_launcher);

        //}

    }
}
