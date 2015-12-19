package com.example.younghokim.thebalance;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.younghokim.thebalance.util.DBManager;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends Fragment {

    ListView mlist;
    LinearLayout calendarArea;
    ArrayList<String> nameList;
    ArrayAdapter<String> baseAdapter;
    Date afterRemove;

//    SQLiteDatabase balanceDB;
//    String balanceDBName = "balanceRanking.db";
//    String balanceTableName = "balanceRankingTable";
//    int balanceDBMode = Context.MODE_PRIVATE;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_calendar, container, false);
        nameList = new ArrayList<String>();
        baseAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, nameList);
        mlist = (ListView)v.findViewById(R.id.listView);
        mlist.setAdapter(baseAdapter);
        nameList.clear();
        DBManager.selectAll(getActivity(), nameList, "now");
//        if(baseAdapter.getCount() > 2){
//            View item = baseAdapter.getView(0, null, mlist);
//            item.measure(0, 0);
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (5.5 * item.getMeasuredHeight()));
//            mlist.setLayoutParams(params);
//        }
        baseAdapter.notifyDataSetChanged();

        this.calendarArea = (LinearLayout)v.findViewById(R.id.calendararea);

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(listener);

        FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
        t.replace(calendarArea.getId(), caldroidFragment);
        t.commit();

        mlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Do you want to delete this balance of list?");

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //String s=String.valueOf(position);
                        //parent.
                        //nameList.indexOf(position);
                        //nameList.get(nameList.indexOf(position));
                        Log.e("checkIndex", nameList.get(nameList.indexOf(position)+1));
 //                       DBManager.removeData(getActivity(), position);
//                        DBManager.selectAll(getActivity(), nameList, formatter.format(afterRemove));
                        //Log.e("check:", formatter.format(afterRemove));
                        dialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                return false;
            }
        });
        // Inflate the layout for this fragment
        return v;
    }


    //element for calendar view
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    final CaldroidListener listener = new CaldroidListener() {

        @Override
        public void onSelectDate(Date date, View view) {
            Toast.makeText(getActivity(), formatter.format(date),
                    Toast.LENGTH_SHORT).show();
            nameList.clear();
            DBManager.selectAll(getActivity(), nameList, formatter.format(date));
            afterRemove = new Date();
            afterRemove = date;
//            if(baseAdapter.getCount() > 2){
//                View item = baseAdapter.getView(0, null, mlist);
//                item.measure(0, 0);
//                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (5.5 * item.getMeasuredHeight()));
//                mlist.setLayoutParams(params);
//            }
            baseAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChangeMonth(int month, int year) {
//            String text = "month: " + month + " year: " + year;
//            Toast.makeText(getActivity(), text,
//                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLongClickDate(Date date, View view) {
            Toast.makeText(getActivity(),
                    "Long click " + formatter.format(date),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCaldroidViewCreated() {
//            Toast.makeText(getActivity(),
//                    "Caldroid view is created",
//                    Toast.LENGTH_SHORT).show();
        }
    };
}
