package org.d3kad3nt.sunriseClock.ui.group.groupDetail;

import android.view.View;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import java.util.Objects;
import org.d3kad3nt.sunriseClock.backend.data.model.group.UIGroup;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Status;
import org.d3kad3nt.sunriseClock.backend.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.ui.util.ResourceVisibilityLiveData;
import org.d3kad3nt.sunriseClock.util.LiveDataUtil;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class GroupDetailViewModel extends ViewModel {

    public static final CreationExtras.Key<LightRepository> LIGHT_REPOSITORY_KEY = new CreationExtras.Key<>() {};

    static final ViewModelInitializer<GroupDetailViewModel> initializer =
            new ViewModelInitializer<>(GroupDetailViewModel.class, creationExtras -> {
                LightRepository lightRepository = Objects.requireNonNull(creationExtras.get(LIGHT_REPOSITORY_KEY));
                return new GroupDetailViewModel(lightRepository);
            });

    private final LightRepository lightRepository;
    private long groupId;

    private LiveData<Resource<UIGroup>> group;
    public final MediatorLiveData<Boolean> swipeRefreshing = new MediatorLiveData<>(false);
    public final ResourceVisibilityLiveData loadingIndicatorVisibility;

    public GroupDetailViewModel(LightRepository lightRepository) {
        this.lightRepository = lightRepository;

        loadingIndicatorVisibility = new ResourceVisibilityLiveData(View.VISIBLE)
                .setLoadingVisibility(View.VISIBLE)
                .setSuccessVisibility(View.INVISIBLE)
                .setErrorVisibility(View.INVISIBLE);
    }

    public LiveData<Resource<UIGroup>> getGroup() {
        return group;
    }

    public void loadGroup(final long groupId) {
        this.groupId = groupId;
        if (group != null) {
            return; // Already loading
        }
        group = lightRepository.getGroup(groupId);
        loadingIndicatorVisibility.addVisibilityProvider(group);
    }

    public void refreshGroup() {
        LogUtil.i("User requested refresh of group.");
        if (groupId != 0) {
            LiveData<EmptyResource> state = lightRepository.refreshGroup(groupId);
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
    }

    public void setGroupOnState(boolean on) {
        LogUtil.i("User requested the group's state to be set to %s.", on);
        LiveDataUtil.observeOnce(group, groupResource -> {
            if (groupResource == null || groupResource.getStatus() == Status.LOADING) {
                return;
            }
            LiveData<EmptyResource> state = lightRepository.setGroupOnState(groupId, on);
            loadingIndicatorVisibility.addVisibilityProvider(state);
        });
    }
}
