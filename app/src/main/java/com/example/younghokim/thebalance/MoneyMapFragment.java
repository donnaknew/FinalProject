package com.example.younghokim.thebalance;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoneyMapFragment extends Fragment {

    public MapFragment mapfragment;
    public MoneyMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        this.mapfragment = (MapFragment)((android.app.Fragment)this.getFragmentManager().getFragments()).getFragmentManager().findFragmentById(R.id.frag_map);
        return v;
    }


}
