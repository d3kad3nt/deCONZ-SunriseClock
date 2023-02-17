package org.d3kad3nt.sunriseClock.deviceControl;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.controls.Control;
import android.service.controls.ControlsProviderService;
import android.service.controls.DeviceTypes;
import android.service.controls.actions.ControlAction;
import android.util.Log;

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
        final Context context = getBaseContext();
        final EndpointRepository endpointRepository = EndpointRepository.getInstance(context);
        final LightRepository lightRepository = LightRepository.getInstance(context);
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        ExtendedPublisher<Control> flow = new ExtendedPublisher<>(true);
        AsyncJoinHelper asyncHelper = new AsyncJoinHelper();
        for (String controlId : controlIds) {
            LiveData<Resource<UILight>> lightLiveData = getUILight(controlId, lightRepository);
            lightLiveData.observeForever(new AsyncJoinHelper.Observer<>(asyncHelper) {
                @Override
                public void onChanged(final Resource<UILight> resource) {
                    Control.StatefulBuilder builder = new Control.StatefulBuilder(controlId, pendingIntent);
                    AsyncJoinHelper asyncPublish = new AsyncJoinHelper();
                    builder.setDeviceType(DeviceTypes.TYPE_LIGHT);
                    if (resource.getStatus() == Status.LOADING) {
                        return;
                    } else if (resource.getStatus() == Status.ERROR) {
                        return;
                    }
                    UILight light = resource.getData();
                    LiveData<IEndpointUI> endpointUILiveData = endpointRepository.getEndpoint(light.getEndpointId());
                    endpointUILiveData.observeForever(new AsyncJoinHelper.Observer<>(asyncPublish) {
                        @Override
                        public void onChanged(final IEndpointUI iEndpointUI) {
                            //TODO use endpoint.getName, when it is merged
                            builder.setStructure(iEndpointUI.getStringRepresentation());
                            endpointUILiveData.removeObserver(this);
                            asyncPublish.removeAsyncTask(this);
                        }
                    });
                    builder.setTitle(light.getName());
                    if (light.getIsOn()) {
                        builder.setStatus(Control.STATUS_OK);
                    } else {
                        builder.setStatus(Control.STATUS_DISABLED);
                    }
                    asyncPublish.executeWhenJoined(() -> {
                        flow.publish(builder.build());
                        Log.d(TAG, "Publish control");
                    });
                }
            });
        }
        asyncHelper.executeWhenJoined(() -> flow.complete());
        return flow;
    }

    @Override
    public void performControlAction(@NonNull final String controlId, @NonNull final ControlAction action,
                                     @NonNull final Consumer<Integer> consumer) {

    }

    @NonNull
    private String getControlId(@NonNull UILight light) {
        return String.format("sunrise-%s-%s", light.getEndpointId(), light.getLightId());
    }

    private LiveData<Resource<UILight>> getUILight(@NonNull String controlId,
                                                   @NonNull LightRepository lightRepository) {
        long lightID = Long.parseLong(controlId.split("-")[2]);
        return lightRepository.getLight(lightID);
    }
}
