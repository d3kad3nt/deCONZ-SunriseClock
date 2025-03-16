package org.d3kad3nt.sunriseClock.ui.endpoint.endpointDetail;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import java.util.Optional;
import kotlin.jvm.functions.Function1;
import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.data.repository.SettingsRepository;

public class EndpointDetailViewModel extends ViewModel {

  public static final CreationExtras.Key<EndpointRepository> ENDPOINT_REPOSITORY_KEY =
      new CreationExtras.Key<>() {};
  public static final CreationExtras.Key<SettingsRepository> SETTINGS_REPOSITORY_KEY =
      new CreationExtras.Key<>() {};
  public static final CreationExtras.Key<Long> ENDPOINT_ID_KEY = new CreationExtras.Key<>() {};

  static final ViewModelInitializer<EndpointDetailViewModel> initializer =
      new ViewModelInitializer<>(
          EndpointDetailViewModel.class,
          creationExtras -> {
            EndpointRepository endpointRepository = creationExtras.get(ENDPOINT_REPOSITORY_KEY);
            SettingsRepository settingsRepository = creationExtras.get(SETTINGS_REPOSITORY_KEY);
            Long endpointId = creationExtras.get(ENDPOINT_ID_KEY);
            return new EndpointDetailViewModel(endpointRepository, settingsRepository, endpointId);
          });
  private final EndpointRepository endpointRepository;
  private final SettingsRepository settingsRepository;
  private final long endpointID;
  public LiveData<IEndpointUI> endpointConfig;
  public LiveData<Boolean> selected;

  /**
   * Text that is shown in the endpoint rename dialog. The user types the desired new name into a
   * text field backed by this LiveData.
   */
  public MutableLiveData<String> endpointNameEditText = new MutableLiveData<>();

  public EndpointDetailViewModel(
      @NonNull EndpointRepository endpointRepository,
      @NonNull SettingsRepository settingsRepository,
      long endpointId) {
    super();
    this.endpointRepository = endpointRepository;
    this.settingsRepository = settingsRepository;
    this.endpointID = endpointId;

    this.endpointConfig = getEndpoint(endpointId);

    selected =
        Transformations.map(
            settingsRepository.getActiveEndpointIdAsLivedata(),
            new Function1<>() {
              @Override
              public Boolean invoke(final Optional<Long> aLong) {
                return aLong.isPresent() && aLong.get().equals(endpointId);
              }
            });

    // If the endpoint name changes upstream, we update the name that the user is getting shown in
    // the rename
    // dialog.
    endpointNameEditText =
        (MutableLiveData<String>)
            Transformations.map(
                endpointConfig,
                endpointUI -> {
                  return endpointUI.getName();
                });
  }

  private LiveData<IEndpointUI> getEndpoint(long endpointID) {
    return endpointRepository.getEndpoint(endpointID);
  }

  public void setEndpointNameFromEditText() {
    setEndpointName(endpointNameEditText.getValue());
  }

  public void setEndpointName(String newName) {
    endpointRepository.setName(endpointID, newName);
  }
}
