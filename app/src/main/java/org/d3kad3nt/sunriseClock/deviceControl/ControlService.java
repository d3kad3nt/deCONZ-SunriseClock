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
import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.model.resource.Status;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.util.AsyncJoinHelper;
import org.d3kad3nt.sunriseClock.util.ExtendedPublisher;
import org.d3kad3nt.sunriseClock.util.LiveDataUtil;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.function.Consumer;

@RequiresApi(api = Build.VERSION_CODES.R)
public class ControlService extends ControlsProviderService {

    private static final String TAG = "ControlService";

    @NonNull
    @Override
    public Flow.Publisher<Control> createPublisherForAllAvailable() {
        final Context context = getBaseContext();
        final EndpointRepository endpointRepository = EndpointRepository.getInstance(context);
        final LightRepository lightRepository = LightRepository.getInstance(context);
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        ExtendedPublisher<Control> flow = new ExtendedPublisher<>(true);
        LiveData<List<IEndpointUI>> allEndpoints = endpointRepository.getAllEndpoints();
        AsyncJoinHelper asyncHelper = new AsyncJoinHelper();

        LiveDataUtil.observeOnce(allEndpoints, new AsyncJoinHelper.Observer<>(asyncHelper) {
            @Override
            public void onChanged(final List<IEndpointUI> endpoints) {
                for (IEndpointUI endpoint : endpoints) {
                    LiveData<Resource<List<UILight>>> lightResources =
                        lightRepository.getLightsForEndpoint(endpoint.getId());
                    lightResources.observeForever(new AsyncJoinHelper.Observer<>(asyncHelper) {
                        @Override
                        public void onChanged(final Resource<List<UILight>> listResource) {
                            if (listResource.getStatus() == Status.LOADING) {
                                return;
                            }
                            if (listResource.getStatus() == Status.ERROR) {
                                lightResources.removeObserver(this);
                                asyncHelper.removeAsyncTask(this);
                                return;
                            }
                            List<UILight> lights = listResource.getData();
                            for (UILight light : lights) {
                                Control.StatelessBuilder builder =
                                    new Control.StatelessBuilder(getControlId(light), pendingIntent);
                                builder.setTitle(light.getName());
                                //TODO use endpoint.getName, when it is merged
                                builder.setStructure(endpoint.getStringRepresentation());
                                builder.setDeviceType(DeviceTypes.TYPE_LIGHT);
                                flow.publish(builder.build());
                            }
                            lightResources.removeObserver(this);
                            asyncHelper.removeAsyncTask(this);
                        }
                    });
                }
                asyncHelper.removeAsyncTask(this);
            }
        });
        asyncHelper.executeWhenJoined(() -> flow.complete());
        return flow;
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

    private LiveData<Resource<UILight>> getUILight(String controlId, LightRepository lightRepository) {
        long lightID = Long.parseLong(controlId.split("-")[2]);
        return lightRepository.getLight(lightID);
    }
}
