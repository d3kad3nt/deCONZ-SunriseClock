package org.d3kad3nt.sunriseClock.ui.groupControl;

import android.view.View;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import org.d3kad3nt.sunriseClock.backend.data.model.group.UIGroup;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Status;
import org.d3kad3nt.sunriseClock.backend.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.ui.util.ResourceVisibilityLiveData;
import org.d3kad3nt.sunriseClock.util.LiveDataUtil;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class GroupControlViewModel extends ViewModel {

    public static final CreationExtras.Key<LightRepository> LIGHT_REPOSITORY_KEY = new CreationExtras.Key<>() {};
    public static final CreationExtras.Key<Long> GROUP_ID_KEY = new CreationExtras.Key<>() {};

    static final ViewModelInitializer<GroupControlViewModel> initializer =
            new ViewModelInitializer<>(GroupControlViewModel.class, creationExtras -> {
                LightRepository lightRepository = creationExtras.get(LIGHT_REPOSITORY_KEY);
                Long groupId = creationExtras.get(GROUP_ID_KEY);
                assert lightRepository != null;
                assert groupId != null;
                return new GroupControlViewModel(lightRepository, groupId);
            });

    private final LightRepository lightRepository;
    private final long groupId;
    public final LiveData<Resource<UIGroup>> group;

    /** Whether the loading indicator should be shown by the fragment. */
    public final ResourceVisibilityLiveData loadingIndicatorVisibility;

    /** Whether the loading indicator of the swipeRefreshLayout should be shown by the fragment. */
    public final MediatorLiveData<Boolean> swipeRefreshing = new MediatorLiveData<>(false);

    public GroupControlViewModel(LightRepository lightRepository, long groupId) {
        super();
        LogUtil.setPrefix("GroupId %d: ", groupId);
        this.lightRepository = lightRepository;
        this.groupId = groupId;

        this.group = getGroup(groupId);

        loadingIndicatorVisibility = new ResourceVisibilityLiveData(View.VISIBLE)
                .setLoadingVisibility(View.VISIBLE)
                .setSuccessVisibility(View.INVISIBLE)
                .setErrorVisibility(View.INVISIBLE)
                .addVisibilityProvider(group);
    }

    public void refreshGroup() {
        LogUtil.i("User requested refresh of group.");

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

    public void setGroupOnState(boolean newState) {
        LogUtil.i("User requested the group's state to be set to %s.", newState);

        LiveDataUtil.observeOnce(group, groupResource -> {
            if (groupResource == null || groupResource.getStatus() == Status.LOADING) {
                return;
            }
            LiveData<EmptyResource> state = lightRepository.setGroupOnState(groupId, newState);
            loadingIndicatorVisibility.addVisibilityProvider(state);
        });
    }

    public LiveData<Resource<UIGroup>> getGroup(long groupId) {
        return lightRepository.getGroup(groupId);
    }
}
