package com.example.matthias.feedbacklibrary.utils;

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

import com.example.matthias.feedbacklibrary.FeedbackActivity;
import com.example.matthias.feedbacklibrary.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class with dialogs
 */
public class DialogUtils {
    /**
     * This method creates a progress dialog.
     *
     * @param context              the context
     * @param title                the title of the dialog
     * @param cancelOnTouchOutside cancelOnTouchOutside
     * @return the progress dialog
     */
    public static ProgressDialog createProgressDialog(Context context, String title, boolean cancelOnTouchOutside) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
        return progressDialog;
    }

    /**
     * This method prompts the user a simple information dialog.
     *
     * @param activity the activity
     * @param messages the message(s) to display
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
            builder.setMessage(message.toString()).setNegativeButton(getResources().getString(R.string.supersede_feedbacklibrary_close_string), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            return builder.create();
        }
    }

    /**
     * Dialog asking the user if (s)he wants to give feedback.
     */
    public static class PullFeedbackIntermediateDialog extends DialogFragment {
        public static PullFeedbackIntermediateDialog newInstance(String message, String jsonString, long selectedPullConfigurationIndex, String language) {
            PullFeedbackIntermediateDialog f = new PullFeedbackIntermediateDialog();
            Bundle args = new Bundle();
            args.putString("message", message);
            args.putString("jsonString", jsonString);
            args.putLong("selectedPullConfigurationIndex", selectedPullConfigurationIndex);
            args.putString("language", language);
            f.setArguments(args);
            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity associatedActivity = getActivity();
            AlertDialog.Builder builder = new AlertDialog.Builder(associatedActivity);
            String message = getArguments().getString("message");
            final String jsonString = getArguments().getString("jsonString");
            final long selectedPullConfigurationIndex = getArguments().getLong("selectedPullConfigurationIndex");
            final String language = getArguments().getString("language");

            builder.setMessage(message).setPositiveButton(R.string.supersede_feedbacklibrary_yes_string, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(associatedActivity, FeedbackActivity.class);
                    intent.putExtra(FeedbackActivity.JSON_CONFIGURATION_STRING, jsonString);
                    intent.putExtra(FeedbackActivity.IS_PUSH_STRING, false);
                    intent.putExtra(FeedbackActivity.SELECTED_PULL_CONFIGURATION_INDEX_STRING, selectedPullConfigurationIndex);
                    intent.putExtra(FeedbackActivity.EXTRA_KEY_LANGUAGE, language);
                    associatedActivity.startActivity(intent);
                }
            }).setNegativeButton(R.string.supersede_feedbacklibrary_no_string, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            return builder.create();
        }
    }
}
