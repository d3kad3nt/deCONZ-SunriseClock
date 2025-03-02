package org.d3kad3nt.sunriseClock.ui.util;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MaterialDialogFragment extends DialogFragment {

    private View dialogView = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        dialogView = onCreateView(getLayoutInflater(), null, savedInstanceState);

        if (dialogView != null) {
            onViewCreated(dialogView, savedInstanceState);
        }

        return new MaterialAlertDialogBuilder(requireContext()).setView(dialogView).create();
    }

    @Override
    public void onDestroyView() {
        dialogView = null;
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View getView() {
        return this.dialogView;
    }
}
