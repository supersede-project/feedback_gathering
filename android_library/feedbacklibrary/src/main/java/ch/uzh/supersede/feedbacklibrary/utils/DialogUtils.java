package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;

public final class DialogUtils {

    private DialogUtils() {
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
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
            List<String> messages = getArguments().getStringArrayList("messages");
            String message = "";
            if (messages != null) {
                message = StringUtility.concatWithDelimiter("\n", messages);
            }
            builder.setMessage(message).setNegativeButton(getResources().getString(R.string.dialog_close), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            return builder.create();
        }
    }
}
