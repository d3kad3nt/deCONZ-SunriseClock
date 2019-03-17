package org.asdfgamer.sunriseClock.maintabs;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.asdfgamer.sunriseClock.R;
import org.asdfgamer.sunriseClock.network.lights.DeconzRequestLights;
import org.asdfgamer.sunriseClock.network.lights.DeconzRequestLightsHelper;
import org.asdfgamer.sunriseClock.network.lights.model.Light;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.SimplifiedCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Response;

public class LightsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String title;
    private int page;

    private SwipeRefreshLayout swipeRefreshLayout;

    private final static String TAG = "LightsFragment";
    private RecyclerView recyclerView;

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
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO: Use deconz to get lights instead of demo data.


        View rootView = inflater.inflate(R.layout.fragment_lights, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());

        List<Light> emptyList = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(this.getContext()));
        LightsAdapter recyclerviewAdapter = new LightsAdapter(emptyList, preferences);

        recyclerView = rootView.findViewById(R.id.recycler_view);
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

    private void updateLightData(final RecyclerView.Adapter recyclerviewAdapter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(this.getContext()));
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(preferences.getString("pref_ip", "") + ":" + preferences.getString("pref_port", ""));
        Uri uri = builder.build();//TODO save Uri as string in Preferences that it doesn't has to be created multiple times
        String apiKey = preferences.getString("pref_api_key", "");
        DeconzRequestLights deconzRequestLights = new DeconzRequestLightsHelper(uri, apiKey) {
        };
        deconzRequestLights.getLights(new SimplifiedCallback<List<Light>>() {
            @Override
            public void onSuccess(Response<List<Light>> response) {
                assert response.body() != null;
                Log.i(TAG, response.body().size() + " Lights loaded");
                if (recyclerviewAdapter instanceof LightsAdapter) {
                    ((LightsAdapter) recyclerviewAdapter).updateData(response.body());
                } else {
                    Log.e(TAG, "Given wrong Adapter to updated Light data.");
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError() {
                Log.w(TAG, "Error while loading lamps");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        Log.i(TAG, "Starting refresh.");
        loadRecyclerViewData();
    }

    private void loadRecyclerViewData() {
        //TODO: Showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);
        updateLightData(recyclerView.getAdapter());

    }
}