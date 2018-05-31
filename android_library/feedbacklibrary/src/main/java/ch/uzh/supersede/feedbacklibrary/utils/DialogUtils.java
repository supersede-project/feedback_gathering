package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.activities.FeedbackActivity;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;

public class DialogUtils {

    private DialogUtils(){
    }

    /**
     * This method prompts the user a simple information dialog.
     *
     * @param activity   the activity
     * @param messages   the message(s) to display
     * @param cancelable the cancelable
     */
    public static void showInformationDialog(@NonNull final Activity activity, @NonNull String[] messages, boolean cancelable) {
        DialogUtils.DataDialog d = DialogUtils.DataDialog.newInstance(new ArrayList<>(Arrays.asList(messages)));
        d.setCancelable(cancelable);
        d.show(activity.getFragmentManager(), "dataDialog");
    }

    /**
     * Dialog with a close button for displaying a simple string message, e.g., if a given input field is not valid.
     */
    public static class DataDialog extends DialogFragment {
        public static DataDialog newInstance(ArrayList<String> messages) {
            DataDialog f = new DataDialog();
            Bundle args = new Bundle();
            args.putStringArrayList("messages", messages);
            f.setArguments(args);
            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            List<String> messages = getArguments().getStringArrayList("messages");
            StringBuilder message = new StringBuilder("");
            if (messages != null) {
                for (String s : messages) {
                    message.append(s).append(" \n");
                }
            }
            builder.setMessage(message.toString()).setNegativeButton(getResources().getString(ch.uzh.supersede.feedbacklibrary.R.string.dialog_close), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            return builder.create();
        }
    }
}
