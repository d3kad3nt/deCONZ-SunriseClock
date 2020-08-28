package org.d3kad3nt.sunriseClock.ui.maintabs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.tomerrosenfeld.customanalogclockview.CustomAnalogClock;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.utils.SettingKeys;
import org.d3kad3nt.sunriseClock.utils.SimpleTime;

import java.util.Date;
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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        long alarm = preferences.getLong(SettingKeys.ALARM_TIME.toString(), 0);
        if (alarm != 0 && alarm > (new Date().getTime())) {
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

        TextView textView = view.findViewById(R.id.infoText);
        textView.setText(getResources().getString(R.string.alarm_not_set));
    }

    private void showClock(@NonNull View view, long alarm) {
        CustomAnalogClock customAnalogClock = view.findViewById(R.id.analog_clock);
        customAnalogClock.setFace(R.drawable.ic_clock_face_green);
        customAnalogClock.setScale(2);
        customAnalogClock.setTime(alarm);

        TextView textView = view.findViewById(R.id.infoText);
        SimpleTime time = new SimpleTime(alarm);
        String infoText = String.format(getResources().getString(R.string.alarm_time), time.getTimeOfDay());
        textView.setText(infoText);
    }
}