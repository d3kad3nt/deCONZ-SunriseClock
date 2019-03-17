package org.asdfgamer.sunriseClock.maintabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tomerrosenfeld.customanalogclockview.CustomAnalogClock;

import org.asdfgamer.sunriseClock.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class OverviewFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    static OverviewFragment newInstance(int page, String title) {
        OverviewFragment fragmentFirst = new OverviewFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        CustomAnalogClock customAnalogClock = view.findViewById(R.id.analog_clock);
        customAnalogClock.setFace(R.drawable.ic_clock_face_green);
        customAnalogClock.setScale(2);
        return view;
    }
}