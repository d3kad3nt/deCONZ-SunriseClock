package org.d3kad3nt.sunriseClock.ui.light;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class LightsState extends BaseObservable {

    private boolean error = false;
    private String errorTitle = "";
    private String errorMessage = "";

    public void clearError(){
        this.error = false;
    }

    public void setError(String errorTitle, String errorMessage){
        this.error = true;
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
    }

    @Bindable
    public boolean isError() {
        return error;
    }

    @Bindable
    public String getErrorTitle() {
        return errorTitle;
    }

    @Bindable
    public String getErrorMessage() {
        return errorMessage;
    }
}
