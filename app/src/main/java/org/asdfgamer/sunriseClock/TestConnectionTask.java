package org.asdfgamer.sunriseClock;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AlertDialog;

public class TestConnectionTask extends AsyncTask<Void, Void, String> {

    private WeakReference<AlertDialog> alertDialogReference;

    // only retain a weak reference to the alertDialog
    public TestConnectionTask(AlertDialog alertDialog) {
        alertDialogReference = new WeakReference<>(alertDialog);
    }

    @Override
    protected String doInBackground(Void... params) {

        // do some long running task...
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "task finished";
    }

    @Override
    protected void onPostExecute(String result) {

        // get a reference to the alertDialog if it is still there
        AlertDialog alertDialog = alertDialogReference.get();
        if (alertDialog == null || (!alertDialog.isShowing())) return;


        // modify the alertDialog's UI
        alertDialog.setMessage(result);

    }
}

