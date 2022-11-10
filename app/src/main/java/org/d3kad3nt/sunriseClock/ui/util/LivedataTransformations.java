package org.d3kad3nt.sunriseClock.ui.util;

import android.view.View;

import org.d3kad3nt.sunriseClock.data.model.light.BaseLight;
import org.d3kad3nt.sunriseClock.data.remote.common.Resource;
import org.d3kad3nt.sunriseClock.data.remote.common.Status;

public class LivedataTransformations {

    public static Integer visibleWhenLoading(Resource<BaseLight> input) {
        if (input.getStatus() == Status.LOADING) {
            return View.VISIBLE;
        }
        else {
            return View.INVISIBLE;
        }
    }
}