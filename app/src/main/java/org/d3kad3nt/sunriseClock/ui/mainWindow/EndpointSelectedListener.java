package org.d3kad3nt.sunriseClock.ui.mainWindow;

import android.view.View;
import android.widget.AdapterView;

public class EndpointSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(parent.getItemAtPosition(position).getClass());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
