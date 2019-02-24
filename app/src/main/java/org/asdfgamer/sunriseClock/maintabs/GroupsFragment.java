package org.asdfgamer.sunriseClock.maintabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.asdfgamer.sunriseClock.R;

import java.text.MessageFormat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class GroupsFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    static GroupsFragment newInstance(int page, String title) {
        GroupsFragment fragmentFirst = new GroupsFragment();
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
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        TextView tvLabel = view.findViewById(R.id.tvLabel);
        tvLabel.setText(MessageFormat.format("{0} -- {1}", page, title));
        return view;
    }
}