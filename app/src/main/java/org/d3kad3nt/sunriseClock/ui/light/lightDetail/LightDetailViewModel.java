package org.d3kad3nt.sunriseClock.ui.light.lightDetail;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
import org.d3kad3nt.sunriseClock.backend.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Status;
import org.d3kad3nt.sunriseClock.backend.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.ui.util.BooleanVisibilityLiveData;
import org.d3kad3nt.sunriseClock.ui.util.ResourceVisibilityLiveData;
import org.d3kad3nt.sunriseClock.util.LiveDataUtil;
import org.d3kad3nt.sunriseClock.util.LogUtil;
import org.jetbrains.annotations.Contract;

public class LightDetailViewModel extends ViewModel {

    public static final CreationExtras.Key<LightRepository> LIGHT_REPOSITORY_KEY = new CreationExtras.Key<>() {};
    public static final CreationExtras.Key<Long> LIGHT_ID_KEY = new CreationExtras.Key<>() {};

    static final ViewModelInitializer<LightDetailViewModel> initializer =
            new ViewModelInitializer<>(LightDetailViewModel.class, creationExtras -> {
                LightRepository lightRepository = creationExtras.get(LIGHT_REPOSITORY_KEY);
                Long lightId = creationExtras.get(LIGHT_ID_KEY);
                return new LightDetailViewModel(lightRepository, lightId);
            });
    private final LightRepository lightRepository;
    private final long lightID;
    public LiveData<Resource<UILight>> light;

    /** Whether the loading indicator should be shown by the fragment. */
    public ResourceVisibilityLiveData loadingIndicatorVisibility;

    /** Visual indication that a light is not reachable. */
    public BooleanVisibilityLiveData notReachableCardVisibility;

    /** Whether the loading indicator of the swipeRefreshLayout should be shown by the fragment. */
    public MediatorLiveData<Boolean> swipeRefreshing = new MediatorLiveData<>(false);

    /**
     * Text that is shown in the light rename dialog. The user types the desired new name into a text field backed by
     * this LiveData.
     */
    public MutableLiveData<String> lightNameEditText = new MutableLiveData<>();

    public LightDetailViewModel(@NonNull LightRepository lightRepository, long lightId) {
        super();
        LogUtil.setPrefix("LightID %d: ", lightId);
        this.lightRepository = lightRepository;
        this.lightID = lightId;

        this.light = getLight(lightId);

        loadingIndicatorVisibility = new ResourceVisibilityLiveData(View.VISIBLE)
                .setLoadingVisibility(View.VISIBLE)
                .setSuccessVisibility(View.INVISIBLE)
                .setErrorVisibility(View.INVISIBLE)
                .addVisibilityProvider(light);

        notReachableCardVisibility = new BooleanVisibilityLiveData(View.GONE)
                .setTrueVisibility(View.GONE)
                .setFalseVisibility(View.VISIBLE)
                .addVisibilityProvider(getIsReachable());

        // If the light name changes upstream, we update the name that the user is getting shown in
        // the rename dialog.
        lightNameEditText = (MutableLiveData<String>) Transformations.map(light, uiLightResource -> {
            if (uiLightResource.getStatus() == Status.SUCCESS) {
                return uiLightResource.getData().getName();
            }
            return "";
        });
    }

    public void refreshLight() {
        LogUtil.i("User requested refresh of light.");

        LiveData<EmptyResource> state = lightRepository.refreshLight(lightID);

        swipeRefreshing.addSource(state, emptyResource -> {
            switch (emptyResource.getStatus()) {
                case SUCCESS, ERROR -> {
                    LogUtil.v("Stopping swipeRefresh animation.");
                    swipeRefreshing.setValue(false);
                    swipeRefreshing.removeSource(state);
                }
                case LOADING -> {
                    LogUtil.v("Starting swipeRefresh animation.");
                    swipeRefreshing.setValue(true);
                }
            }
        });
    }

    public void setLightOnState(boolean newState) {
        LogUtil.i("User requested the light's state to be set to %s.", newState);

        LiveDataUtil.observeOnce(light, lightResource -> {
            if (lightResource == null || lightResource.getStatus() == Status.LOADING) {
                return;
            }
            LiveData<EmptyResource> state = lightRepository.setOnState(lightID, newState);
            loadingIndicatorVisibility.addVisibilityProvider(state);
        });
    }

    public void setLightBrightness(int brightness) {
        LogUtil.i("User requested the light's brightness to be set to %d%%.", brightness);

        LiveDataUtil.observeOnce(light, lightResource -> {
            if (lightResource == null || lightResource.getStatus() == Status.LOADING) {
                return;
            }

            // Enable the light if it was disabled.
            if (brightness > 0
                    && !(Objects.requireNonNull(light.getValue()).getData().getIsOn())) {
                LogUtil.d("The brightness was changed while the light was off. Turning on light...");
                setLightOnState(true);
            }
            LiveData<EmptyResource> state = lightRepository.setBrightness(lightID, brightness);
            loadingIndicatorVisibility.addVisibilityProvider(state);
        });
    }

    public void setLightNameFromEditText() {
        setLightName(lightNameEditText.getValue());
    }

    public void setLightName(String newName) {
        LogUtil.i("User requested the light's name to be set to %s.", newName);

        LiveData<EmptyResource> state = lightRepository.setName(lightID, newName);
        loadingIndicatorVisibility.addVisibilityProvider(state);
    }

    private LiveData<Resource<UILight>> getLight(long lightID) {
        return lightRepository.getLight(lightID);
    }

    @NonNull
    @Contract(" -> new")
    private LiveData<Boolean> getIsReachable() {
        return Transformations.map(light, new Function1<>() {
            @Override
            public Boolean invoke(final Resource<UILight> input) {
                if (input.getStatus() == Status.SUCCESS) {
                    return input.getData().getIsReachable();
                }
                return true;
            }
        });
    }
}
