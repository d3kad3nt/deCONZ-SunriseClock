package org.d3kad3nt.sunriseClock.deviceControl;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.controls.Control;
import android.service.controls.ControlsProviderService;
import android.service.controls.DeviceTypes;
import android.service.controls.actions.ControlAction;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.util.FlowUtil;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.function.Consumer;

@RequiresApi(api = Build.VERSION_CODES.R)
public class ControlService extends ControlsProviderService {

    @NonNull
    @Override
    public Flow.Publisher<Control> createPublisherForAllAvailable() {
        Context context = getBaseContext();
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        String app_name = context.getResources().getString(R.string.app_name);
        Control control =
            new Control.StatelessBuilder("sunrise-test1", pendingIntent).setTitle("Test 1").setSubtitle("Subtitle")
                .setStructure(app_name).setDeviceType(DeviceTypes.TYPE_LIGHT).build();
        List<Control> list = List.of(control);
        return FlowUtil.publishList(list);
    }

    @NonNull
    @Override
    public Flow.Publisher<Control> createPublisherFor(@NonNull final List<String> controlIds) {
        Context context = getBaseContext();
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Control control =
            new Control.StatelessBuilder("sunrise-test1", pendingIntent).setTitle("Test 1").setSubtitle("Subtitle")
                .setStructure("Test Structure").setDeviceType(DeviceTypes.TYPE_LIGHT).build();
        List<Control> list = List.of(control);
        return FlowUtil.publishList(list);
    }

    @Override
    public void performControlAction(@NonNull final String controlId, @NonNull final ControlAction action,
                                     @NonNull final Consumer<Integer> consumer) {

    }
}
