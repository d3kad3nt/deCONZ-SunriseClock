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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.function.Consumer;
import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.backend.data.model.UIEndpointEntity;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.backend.data.model.group.UIGroup;
import org.d3kad3nt.sunriseClock.backend.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Status;
import org.d3kad3nt.sunriseClock.backend.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.backend.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.util.AsyncJoin;
import org.d3kad3nt.sunriseClock.util.ExtendedPublisher;
import org.d3kad3nt.sunriseClock.util.LiveDataUtil;
import org.d3kad3nt.sunriseClock.util.LogUtil;
import org.jetbrains.annotations.Contract;

@RequiresApi(api = Build.VERSION_CODES.R)
public class ControlService extends ControlsProviderService {

    // This indicates if the device has to be unlocked to interact with the device Controls.
    // TODO: This could be specified in a setting.
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
     * <p>This occurs for example when a new Device Control should be added.
     *
     * @return A Flow which returns Controls for all Lights
     */
    @NonNull
    @Override
    public Flow.Publisher<Control> createPublisherForAllAvailable() {
        final Context context = getBaseContext();
        final Intent intent = new Intent();
        // The given Flags are always necessary
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        final ExtendedPublisher<Control> flow = new ExtendedPublisher<>(true);
        final LiveData<List<IEndpointUI>> allEndpoints = getEndpointRepository().getAllEndpoints();
        final AsyncJoin asyncHelper = new AsyncJoin();

        LiveDataUtil.observeOnce(allEndpoints, new AsyncJoin.AsyncObserver<>(asyncHelper) {
            @Override
            public void onChanged(final List<IEndpointUI> endpoints) {
                for (final IEndpointUI endpoint : endpoints) {
                    final LiveData<Resource<List<UILight>>> lightResources =
                            getLightRepository().getLightsForEndpoint(endpoint.getId());
                    lightResources.observeForever(getObserver(endpoint, lightResources));
                    final LiveData<Resource<List<UIGroup>>> groupResources =
                            getLightRepository().getGroupsForEndpoint(endpoint.getId());
                    groupResources.observeForever(getObserver(endpoint, groupResources));
                }
                asyncHelper.removeAsyncTask(this);
            }

            @NonNull
            @Contract("_, _ -> new")
            private <T extends UIEndpointEntity> AsyncJoin.AsyncObserver<Resource<List<T>>> getObserver(
                    final IEndpointUI endpoint, final LiveData<Resource<List<T>>> lightResources) {
                return new AsyncJoin.AsyncObserver<>(asyncHelper) {
                    @Override
                    public void onChanged(final Resource<List<T>> listResource) {
                        switch (listResource.getStatus()) {
                            case SUCCESS:
                                for (final T light : listResource.getData()) {
                                    ControlService.this.endpointNames.put(
                                            light.getId(), endpoint.getStringRepresentation());
                                    flow.publish(getStatelessControl(light, pendingIntent));
                                }
                                removeObserver(this, lightResources, asyncHelper);
                            case ERROR:
                                LogUtil.w(
                                        "Error occurred while loading Lights of Endpoint %s for DeviceControl",
                                        endpoint.getStringRepresentation());
                                removeObserver(this, lightResources, asyncHelper);
                                break;
                            case LOADING:
                                break;
                        }
                    }
                };
            }
        });
        asyncHelper.executeWhenJoined(() -> flow.complete());
        return flow;
    }

    /**
     * This Method gets called be the Android Device Controls when the state of one ore multiple Device Controls should
     * be given.
     *
     * <p>This is for example the case when the device Control View is opened and a Control from this App is used.
     *
     * @param controlIds The IDs of the device controls that
     * @return A Flow of Control Elements that is updated when the state of a Light is changed.
     */
    @NonNull
    @Override
    public Flow.Publisher<Control> createPublisherFor(@NonNull final List<String> controlIds) {
        final ExtendedPublisher<Control> flow = getFlow(controlIds);
        for (final String controlId : controlIds) {
            // TODO: remove match statements
            if (controlId.startsWith(UIGroup.getPrefix())) {
                observeGroupChanges(controlId, flow);
            } else if (controlId.startsWith(UILight.getPrefix())) {
                observeLightChanges(controlId, flow);
            } else {
                throw new IllegalStateException("Invalid Type: " + controlId);
            }
        }
        return flow;
    }

    private void observeGroupChanges(final String groupID, final ExtendedPublisher<Control> flow) {
        final LiveData<Resource<UIGroup>> groupLiveData = getUIGroup(groupID);
        groupLiveData.observeForever(resource -> {
            if (resource.getStatus() == Status.LOADING) {
                return;
            } else if (resource.getStatus() == Status.ERROR) {
                return;
            }
            flow.publish(this.getStatefulGroupControl(resource));
        });
    }

    private LiveData<Resource<UIGroup>> getUIGroup(final String groupID) {
        final long lightID = Long.parseLong(groupID.replaceFirst("^" + UIGroup.getPrefix(), ""));
        return getLightRepository().getGroup(lightID);
    }

    /**
     * This gets called by the Android Device Controls, when someone interacts with a device control.
     *
     * @param controlId The Id of the Device Control
     * @param action The Action that was performed
     * @param consumer A Consumer that gets informed, when the response gets processed.
     */
    @Override
    public void performControlAction(
            @NonNull final String controlId,
            @NonNull final ControlAction action,
            @NonNull final Consumer<Integer> consumer) {
        LogUtil.d("Received ControlAction request for controlId %s", controlId);
        if (action instanceof BooleanAction) {
            // Inform SystemUI that the action has been received and is being processed
            consumer.accept(ControlAction.RESPONSE_OK);
            performBooleanControlAction(controlId, (BooleanAction) action);
        } else if (action instanceof FloatAction) {
            consumer.accept(ControlAction.RESPONSE_OK);
            performFloatControlAction(controlId, (FloatAction) action);
        } else {
            LogUtil.w("Unknown Action %s for id %s", action.getClass().getSimpleName(), controlId);
            consumer.accept(ControlAction.RESPONSE_FAIL);
        }
    }

    private static <T> void removeObserver(
            final AsyncJoin.AsyncObserver<T> observer,
            @NonNull final LiveData<T> livedata,
            @NonNull final AsyncJoin asyncHelper) {
        asyncHelper.removeAsyncTask(observer);
        livedata.removeObserver(observer);
    }

    private @NonNull Control getStatelessControl(
            @NonNull final UIEndpointEntity light, @NonNull final PendingIntent pendingIntent) {
        final Control.StatelessBuilder builder = new Control.StatelessBuilder(getControlId(light), pendingIntent);
        builder.setTitle(light.getName());
        // TODO use endpoint.getName, when it is merged
        builder.setStructure(getEndpointName(light.getEndpointId()));
        builder.setDeviceType(DeviceTypes.TYPE_LIGHT);
        return builder.build();
    }

    private void observeLightChanges(final String lightID, final ExtendedPublisher<Control> flow) {
        final LiveData<Resource<UILight>> lightLiveData = getUILight(lightID);
        lightLiveData.observeForever(resource -> {
            if (resource.getStatus() == Status.LOADING) {
                return;
            } else if (resource.getStatus() == Status.ERROR) {
                return;
            }
            flow.publish(this.getStatefulLightControl(resource));
        });
    }

    @NonNull
    private Control getStatefulGroupControl(@NonNull final Resource<UIGroup> lightResource) {
        final Context context = getNonNullBaseContext();
        final UIGroup group = lightResource.getData();
        final Intent intent = new Intent(context, ControlActivity.class);
        // I don't know what this flag does, but it removes one warning
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("LightName", group.getName());
        intent.putExtra("Light", group.getId());
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                context, (int) group.getId(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        final Control.StatefulBuilder builder = new Control.StatefulBuilder(getControlId(group), pendingIntent);
        builder.setDeviceType(DeviceTypes.TYPE_LIGHT);
        builder.setSubtitle(getEndpointName(group.getEndpointId()));
        builder.setStructure(getEndpointName(group.getEndpointId()));
        builder.setTitle(group.getName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            builder.setAuthRequired(AUTH_REQUIRED);
        }

        if (lightResource.getStatus() == Status.ERROR) {
            builder.setStatus(Control.STATUS_DISABLED);
        } else {
            builder.setStatus(Control.STATUS_OK);
        }
        //        if (group.getIsDimmable()) {
        //            final ControlButton button =
        //                    new ControlButton(group.getIsOn(), context.getString(R.string.light_on_state_label));
        //            final RangeTemplate rangeTemplate = new RangeTemplate(
        //                    getControlId(group),
        //                    (float) 0,
        //                    100,
        //                    group.getBrightness(),
        //                    1,
        //                    context.getString(R.string.light_brightness_label));
        //            final ControlTemplate template = new ToggleRangeTemplate(getControlId(group), button,
        // rangeTemplate);
        //            builder.setControlTemplate(template);
        //        } else if (group.getIsSwitchable()) {
        //            final ControlButton button =
        //                    new ControlButton(group.getIsOn(), context.getString(R.string.light_on_state_label));
        //            final ControlTemplate template = new ToggleTemplate(getControlId(group), button);
        //            builder.setControlTemplate(template);
        //        }
        return builder.build();
    }

    @NonNull
    private Control getStatefulLightControl(@NonNull final Resource<UILight> lightResource) {
        final Context context = getNonNullBaseContext();
        final UILight light = lightResource.getData();
        final Intent intent = new Intent(context, ControlActivity.class);
        // I don't know what this flag does, but it removes one warning
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("LightName", light.getName());
        intent.putExtra("Light", light.getId());
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                context, (int) light.getId(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        final Control.StatefulBuilder builder = new Control.StatefulBuilder(getControlId(light), pendingIntent);
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
            final ControlButton button =
                    new ControlButton(light.getIsOn(), context.getString(R.string.light_on_state_label));
            final RangeTemplate rangeTemplate = new RangeTemplate(
                    getControlId(light),
                    (float) 0,
                    100,
                    light.getBrightness(),
                    1,
                    context.getString(R.string.light_brightness_label));
            final ControlTemplate template = new ToggleRangeTemplate(getControlId(light), button, rangeTemplate);
            builder.setControlTemplate(template);
        } else if (light.getIsSwitchable()) {
            final ControlButton button =
                    new ControlButton(light.getIsOn(), context.getString(R.string.light_on_state_label));
            final ControlTemplate template = new ToggleTemplate(getControlId(light), button);
            builder.setControlTemplate(template);
        }
        return builder.build();
    }

    @NonNull
    private ExtendedPublisher<Control> getFlow(final List<String> controlIds) {
        final String flowKey = String.join(",", controlIds);
        if (!this.controlFlows.containsKey(flowKey)) {
            this.controlFlows.put(flowKey, new ExtendedPublisher<>(true));
        }
        // The map always contains the key, because it is checked before this
        return Objects.requireNonNull(this.controlFlows.get(flowKey));
    }

    private void performFloatControlAction(@NonNull final String controlId, @NonNull final FloatAction action) {
        LogUtil.d("New brightness Value: %.3f, for LightID %s", action.getNewValue(), controlId);
        final LiveData<EmptyResource> responseLiveData =
                getLightRepository().setBrightness(Long.parseLong(controlId), (int) action.getNewValue());
        // The observer is needed because livedata executes only if it has a observer.
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
        LogUtil.d("New 'On' State: %b, for LightID %s", action.getNewState(), controlId);
        final LiveData<EmptyResource> responseLiveData =
                getLightRepository().setOnState(Long.parseLong(controlId), action.getNewState());
        // The observer is needed because livedata executes only if it has a observer.
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
    private static String getControlId(@NonNull final UIEndpointEntity light) {
        return light.getUniqueID();
    }

    private LiveData<Resource<UILight>> getUILight(@NonNull final String controlId) {
        final long lightID = Long.parseLong(controlId.replaceFirst("^" + UILight.getPrefix(), ""));
        return getLightRepository().getLight(lightID);
    }

    @NonNull
    private EndpointRepository getEndpointRepository() {
        if (this.nullableEndpointRepository == null) {
            final Context context = getNonNullBaseContext();
            this.nullableEndpointRepository = EndpointRepository.getInstance(context);
        }
        return this.nullableEndpointRepository;
    }

    @NonNull
    private LightRepository getLightRepository() {
        if (this.nullableLightRepository == null) {
            final Context context = getNonNullBaseContext();
            this.nullableLightRepository = LightRepository.getInstance(context);
        }
        return this.nullableLightRepository;
    }

    @NonNull
    private Context getNonNullBaseContext() {
        final Context context = getBaseContext();
        if (context == null) {
            throw new IllegalStateException("No Context could be found");
        }
        return context;
    }

    private void initEndpointNames() {
        LiveDataUtil.observeOnce(getEndpointRepository().getAllEndpoints(), new Observer<>() {
            @Override
            public void onChanged(final List<IEndpointUI> iEndpointUIS) {
                for (final IEndpointUI endpoint : iEndpointUIS) {
                    ControlService.this.endpointNames.put(endpoint.getId(), endpoint.getStringRepresentation());
                }
                LogUtil.d("Updated List of endpoint names");
            }
        });
    }

    private String getEndpointName(final long endpointID) {
        if (this.endpointNames.containsKey(endpointID)) {
            return this.endpointNames.get(endpointID);
        }
        LogUtil.w("Endpoint Name for %d not loaded", endpointID);
        return "No Name";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initEndpointNames();
    }
}
