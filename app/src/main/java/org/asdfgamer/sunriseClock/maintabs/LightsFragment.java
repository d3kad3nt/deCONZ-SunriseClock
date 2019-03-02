package org.asdfgamer.sunriseClock.maintabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.asdfgamer.sunriseClock.R;
import org.asdfgamer.sunriseClock.network.request.DeconzRequestLights;

import java.text.MessageFormat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class LightsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String title;
    private int page;

    private SwipeRefreshLayout swipeRefreshLayout;

    private final static String TAG = "LightsFragment";

    // newInstance constructor for creating fragment with arguments
    static LightsFragment newInstance(int page, String title) {
        LightsFragment fragmentFirst = new LightsFragment();
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
        //TODO: Use deconz to get lights instead of demo data.
        String[] testData = new String[] {"Hans", "Renate", "Günther"};

        View rootView = inflater.inflate(R.layout.fragment_lights, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        RecyclerView.Adapter recyclerviewAdapter = new LightsAdapter(testData);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerviewAdapter);

        swipeRefreshLayout = rootView.findViewById(R.id.refresh_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        /* Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used. */
        swipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(true);

                loadRecyclerViewData();
            }
        });

        return rootView;
    }

    @Override
    public void onRefresh() {
        Log.i(TAG, "Starting refresh.");
        loadRecyclerViewData();
    }

    private void loadRecyclerViewData() {
        //TODO: Showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setRefreshing(false);
    }
}