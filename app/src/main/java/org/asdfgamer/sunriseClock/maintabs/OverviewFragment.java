package org.asdfgamer.sunriseClock.maintabs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.tomerrosenfeld.customanalogclockview.CustomAnalogClock;

import org.asdfgamer.sunriseClock.R;
import org.asdfgamer.sunriseClock.utils.SettingKeys;
import org.asdfgamer.sunriseClock.utils.SimpleTime;

import java.util.Objects;

public class OverviewFragment extends Fragment {

    // newInstance constructor for creating fragment with arguments
    static OverviewFragment newInstance(int page, String title) {
        OverviewFragment fragmentFirst = new OverviewFragment();
        Bundle args = new Bundle();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getActivity()));
        long alarm = preferences.getLong(SettingKeys.ALARM_TIME.toString(), 0);
        if (alarm != 0) {
            showClock(view, alarm);
        } else {
            showInfo(view);
        }

        return view;
    }

    private void showInfo(View view) {
        CustomAnalogClock customAnalogClock = view.findViewById(R.id.analog_clock);
        customAnalogClock.setFace(R.drawable.ic_clock_face_grey);
        customAnalogClock.setScale(2);
        long standardTime = new SimpleTime(0, 0).getUnixTime();
        customAnalogClock.setTime(standardTime);
    }

    private void showClock(@NonNull View view, long alarm) {
        CustomAnalogClock customAnalogClock = view.findViewById(R.id.analog_clock);
        customAnalogClock.setFace(R.drawable.ic_clock_face_green);
        customAnalogClock.setScale(2);
        customAnalogClock.setTime(alarm);
    }
}