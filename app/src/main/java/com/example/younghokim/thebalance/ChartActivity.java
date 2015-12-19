package com.example.younghokim.thebalance;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.younghokim.thebalance.util.DBManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ChartActivity extends Fragment {

    ListView monthList;
    ArrayList<String> monthBalanceList;
    ArrayAdapter<String> monthAdapter;
    Calendar calendar = Calendar.getInstance();
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_chart, container, false);
        monthBalanceList = new ArrayList<String>();
        monthAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, monthBalanceList);
        monthList = (ListView)v.findViewById(R.id.monthBalanceListView);
        monthList.setAdapter(monthAdapter);
        monthBalanceList.clear();
        for(int i=0;i<12;i++) {
            DBManager.selectAllMonthly(getActivity(), monthBalanceList, "" + calendar.get(Calendar.YEAR) + "-" + (i+1) + "-");
        }
        monthAdapter.notifyDataSetChanged();

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.activity_chart, container, false);
        return v;
    }
}
