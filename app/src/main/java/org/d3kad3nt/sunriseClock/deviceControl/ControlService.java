package org.d3kad3nt.sunriseClock.deviceControl;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.controls.Control;
import android.service.controls.ControlsProviderService;
import android.service.controls.DeviceTypes;
import android.service.controls.actions.BooleanAction;
import android.service.controls.actions.ControlAction;
import android.service.controls.actions.FloatAction;
import android.service.controls.templates.ControlButton;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.RangeTemplate;
import android.service.controls.templates.ToggleRangeTemplate;
import android.service.controls.templates.ToggleTemplate;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.model.resource.Status;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.util.AsyncJoin;
import org.d3kad3nt.sunriseClock.util.ExtendedPublisher;
import org.d3kad3nt.sunriseClock.util.LiveDataUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.function.Consumer;

@RequiresApi(api = Build.VERSION_CODES.R)
public class ControlService extends ControlsProviderService {

    private static final String TAG = "ControlService";

    //This indicates if the device has to be unlocked to interact with the device Controls.
    //TODO: This could be specified in a setting.
    private static final boolean AUTH_REQUIRED = false;
    @NonNull
    private final Map<Long, String> endpointNames = new HashMap<>();
    private final Map<String, ExtendedPublisher<Control>> controlFlows = new HashMap<>();
    @Nullable
    private EndpointRepository nullableEndpointRepository;
    @Nullable
    private LightRepository nullableLightRepository;

    /**
     * This Method gets called by the Android Device Controls, when all available Controls are listed.
     *
     * This occurs for example when a new Device Control should be added.
     *
     * @return A Flow which returns Controls for all Lights
     */
    @NonNull
    @Override
    public Flow.Publisher<Control> createPublisherForAllAvailable() {
        final Context context = getBaseContext();
        Intent intent = new Intent();
        //Ths given Flags are always necessary
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        ExtendedPublisher<Control> flow = new ExtendedPublisher<>(true);
        LiveData<List<IEndpointUI>> allEndpoints = getEndpointRepository().getAllEndpoints();
        AsyncJoin asyncHelper = new AsyncJoin();

        LiveDataUtil.observeOnce(allEndpoints, new AsyncJoin.Observer<>(asyncHelper) {
            @Override
            public void onChanged(final List<IEndpointUI> endpoints) {
                for (IEndpointUI endpoint : endpoints) {
                    LiveData<Resource<List<UILight>>> lightResources =
                        getLightRepository().getLightsForEndpoint(endpoint.getId());
                    lightResources.observeForever(new AsyncJoin.Observer<>(asyncHelper) {
                        @Override
                        public void onChanged(final Resource<List<UILight>> listResource) {
                            switch (listResource.getStatus()) {
                                case SUCCESS:
                                    for (UILight light : listResource.getData()) {
                                        endpointNames.put(light.getLightId(), endpoint.getStringRepresentation());
                                        flow.publish(getStatelessControl(light, pendingIntent));
                                    }
                                    removeObserver(this, lightResources, asyncHelper);
                                case ERROR:
                                    Log.w(TAG, String.format(
                                        "Error occurred while loading Lights of Endpoint %s for DeviceControl",
                                        endpoint.getStringRepresentation()));
                                    removeObserver(this, lightResources, asyncHelper);
                                    break;
                                case LOADING:
                                    break;
                            }
                        }
                    });
                }
                asyncHelper.removeAsyncTask(this);
            }
        });
        asyncHelper.executeWhenJoined(() -> flow.complete());
        return flow;
    }

    /**
     * This Method gets called be the Android Device Controls when the state of one ore multiple Device Controls
     * should be given.
     *
     * This is for example the case when the device Control View is opened and a Control from this App is used.
     *
     * @param controlIds The IDs of the device controls that
     * @return A Flow of Control Elements that is updated when the state of a Light is changed.
     */
    @NonNull
    @Override
    public Flow.Publisher<Control> createPublisherFor(@NonNull final List<String> controlIds) {
        ExtendedPublisher<Control> flow = getFlow(controlIds);
        for (String controlId : controlIds) {
            observeChanges(controlId, flow);
        }
        return flow;
    }

    private <T> void removeObserver(final AsyncJoin.Observer<T> observer, @NonNull final LiveData<T> livedata,
                                    @NonNull final AsyncJoin asyncHelper) {
        asyncHelper.removeAsyncTask(observer);
        livedata.removeObserver(observer);
    }

    private @NonNull Control getStatelessControl(@NonNull final UILight light,
                                                 @NonNull final PendingIntent pendingIntent) {
        Control.StatelessBuilder builder = new Control.StatelessBuilder(getControlId(light), pendingIntent);
        builder.setTitle(light.getName());
        //TODO use endpoint.getName, when it is merged
        builder.setStructure(getEndpointName(light.getEndpointId()));
        builder.setDeviceType(DeviceTypes.TYPE_LIGHT);
        return builder.build();
    }

    private void observeChanges(final String lightID, final ExtendedPublisher<Control> flow) {
        LiveData<Resource<UILight>> lightLiveData = getUILight(lightID);
        lightLiveData.observeForever(new Observer<>() {
            @Override
            public void onChanged(final Resource<UILight> resource) {

                if (resource.getStatus() == Status.LOADING) {
                    return;
                } else if (resource.getStatus() == Status.ERROR) {
                    return;
                }
                flow.publish(getStatefulControl(resource));
            }
        });
    }

    private Control getStatefulControl(@NonNull final Resource<UILight> lightResource) {
        UILight light = lightResource.getData();
        Intent intent = new Intent(getNonNullBaseContext(), ControlActivity.class);
        //I don't know what this flag does, but it removes one warning
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("LightName", light.getName());
        intent.putExtra("Light", light.getLightId());
        PendingIntent pendingIntent =
            PendingIntent.getActivity(getNonNullBaseContext(), (int) light.getLightId(), intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Control.StatefulBuilder builder = new Control.StatefulBuilder(getControlId(light), pendingIntent);
        builder.setDeviceType(DeviceTypes.TYPE_LIGHT);
        builder.setSubtitle(getEndpointName(light.getEndpointId()));
        builder.setStructure(getEndpointName(light.getEndpointId()));
        builder.setTitle(light.getName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            builder.setAuthRequired(AUTH_REQUIRED);
        }

        if (lightResource.getStatus() == Status.ERROR || !light.getIsReachable()) {
            builder.setStatus(Control.STATUS_DISABLED);
        } else {
            builder.setStatus(Control.STATUS_OK);
        }
        if (light.getIsDimmable()) {
            ControlButton button = new ControlButton(light.getIsOn(), "Activate");
            RangeTemplate rangeTemplate =
                new RangeTemplate(getControlId(light), (float) 0, 100, light.getBrightness(), 1, "Brightness");
            ControlTemplate template = new ToggleRangeTemplate(getControlId(light), button, rangeTemplate);
            builder.setControlTemplate(template);
        } else if (light.getIsSwitchable()) {
            ControlButton button = new ControlButton(light.getIsOn(), "Activate");
            ControlTemplate template = new ToggleTemplate(getControlId(light), button);
            builder.setControlTemplate(template);
        }
        return builder.build();
    }

    @NonNull
    private ExtendedPublisher<Control> getFlow(List<String> controlIds) {
        String flowKey = String.join(",", controlIds);
        if (!controlFlows.containsKey(flowKey)) {
            controlFlows.put(flowKey, new ExtendedPublisher<>(true));
        }
        //The map always contains the key, because it is checked before this
        return Objects.requireNonNull(controlFlows.get(flowKey));
    }

    /**
     * This gets called by the Android Device Controls, when someone interacts with a device control.
     *
     * @param controlId The Id of the Device Control
     * @param action    The Action that was performed
     * @param consumer  A Consumer that gets informed, when the response gets processed.
     */
    @Override
    public void performControlAction(@NonNull final String controlId, @NonNull final ControlAction action,
                                     @NonNull final Consumer<Integer> consumer) {
        Log.d(TAG, "Received ControlAction request");
        if (action instanceof BooleanAction) {
            // Inform SystemUI that the action has been received and is being processed
            consumer.accept(ControlAction.RESPONSE_OK);
            performBooleanControlAction(controlId, (BooleanAction) action);
        } else if (action instanceof FloatAction) {
            consumer.accept(ControlAction.RESPONSE_OK);
            performFloatControlAction(controlId, (FloatAction) action);
        } else {
            Log.w(TAG, "Unknown Action " + action.getClass().getSimpleName() + " for id " + controlId);
            consumer.accept(ControlAction.RESPONSE_FAIL);
        }
    }

    private void performFloatControlAction(@NonNull final String controlId, @NonNull final FloatAction action) {
        Log.d(TAG, "New brightness Value: " + action.getNewValue() + ", for LightID " + controlId);
        LiveData<EmptyResource> responseLiveData =
            getLightRepository().setBrightness(Long.parseLong(controlId), (int) action.getNewValue());
        //The observer is needed because livedata executes only if it has a observer.
        responseLiveData.observeForever(new Observer<>() {
            @Override
            public void onChanged(final EmptyResource emptyResource) {
                if (!emptyResource.getStatus().equals(Status.LOADING)) {
                    responseLiveData.removeObserver(this);
                }
            }
        });
    }

    public void performBooleanControlAction(@NonNull final String controlId, @NonNull final BooleanAction action) {
        Log.d(TAG, "New On State: " + action.getNewState() + ", for LightID " + controlId);
        LiveData<EmptyResource> responseLiveData =
            getLightRepository().setOnState(Long.parseLong(controlId), action.getNewState());
        //The observer is needed because livedata executes only if it has a observer.
        responseLiveData.observeForever(new Observer<>() {
            @Override
            public void onChanged(final EmptyResource emptyResource) {
                if (!emptyResource.getStatus().equals(Status.LOADING)) {
                    responseLiveData.removeObserver(this);
                }
            }
        });
    }

    @NonNull
    private String getControlId(@NonNull UILight light) {
        return Long.toString(light.getLightId());
    }

    private LiveData<Resource<UILight>> getUILight(@NonNull String controlId) {
        long lightID = Long.parseLong(controlId);
        return getLightRepository().getLight(lightID);
    }

    @NonNull
    private EndpointRepository getEndpointRepository() {
        if (nullableEndpointRepository == null) {
            Context context = getNonNullBaseContext();
            nullableEndpointRepository = EndpointRepository.getInstance(context);
        }
        return nullableEndpointRepository;
    }

    @NonNull
    private LightRepository getLightRepository() {
        if (nullableLightRepository == null) {
            Context context = getNonNullBaseContext();
            nullableLightRepository = LightRepository.getInstance(context);
        }
        return nullableLightRepository;
    }

    @NonNull
    private Context getNonNullBaseContext() {
        Context context = getBaseContext();
        if (context == null) {
            throw new IllegalStateException("No Context could be found");
        }
        return context;
    }

    private void initEndpointNames() {
        LiveDataUtil.observeOnce(getEndpointRepository().getAllEndpoints(), new Observer<>() {
            @Override
            public void onChanged(final List<IEndpointUI> iEndpointUIS) {
                for (IEndpointUI endpoint : iEndpointUIS) {
                    endpointNames.put(endpoint.getId(), endpoint.getStringRepresentation());
                }
                Log.d(TAG, "Added Endpoint Names");
            }
        });
    }

    private String getEndpointName(long endpointID) {
        if (endpointNames.containsKey(endpointID)) {
            return endpointNames.get(endpointID);
        }
        Log.w(TAG, "Endpoint Name for " + endpointID + " not loaded");
        return "No Name";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initEndpointNames();
    }
}
