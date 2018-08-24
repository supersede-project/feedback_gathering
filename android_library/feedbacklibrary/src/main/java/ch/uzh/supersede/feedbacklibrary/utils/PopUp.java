package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

import ch.uzh.supersede.feedbacklibrary.R;

public final class PopUp {

    private Context context;
    private String title;
    private String message;
    private String cancelLabel;
    private String okLabel;
    private OnClickListener okClickListener;
    private OnClickListener cancelClickListener;
    private EditText inputText;
    private boolean showOk = true;
    private boolean showCancel = true;
    private String dialogueOutput = null;
    private DialogueOption dialogueOption = null;
    public PopUp(Context context) {
        this.context = context;
    }

    public String getDialogueOutput() {
        return dialogueOutput;
    }

    public DialogueOption getDialogueOption() {
        return dialogueOption;
    }

    public PopUp withTitle(String title) {
        this.title = title;
        return this;
    }

    public PopUp withMessage(String message) {
        this.message = message;
        return this;
    }

    public PopUp withInput(final EditText inputText) {
        this.inputText = inputText;
        this.inputText.setTextColor(Color.WHITE);
        this.inputText.setGravity(Gravity.CENTER);
        Field f;
        try {
            f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(this.inputText, R.drawable.white_cursor);
        } catch (Exception e) {
            //NOP
        }
        return this;
    }

    public PopUp withCustomCancel(String cancelLabel, OnClickListener... clickListener) {
        this.cancelLabel = cancelLabel;
        if (clickListener != null && clickListener.length > 0) {
            this.cancelClickListener = clickListener[0];
        } else {
            this.cancelClickListener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }
        return this;
    }

    public PopUp withCustomOk(String okLabel, OnClickListener... clickListener) {
        this.okLabel = okLabel;
        if (clickListener != null && clickListener.length > 0) {
            this.okClickListener = clickListener[0];
        } else {
            this.okClickListener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };
        }
        return this;
    }

    public PopUp withoutOk() {
        this.showOk = false;
        return this;
    }

    public PopUp withoutCancel() {
        this.showCancel = false;
        return this;
    }

    public PopUp buildAndShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK);

        TextView titleView = new TextView(context);
        // You Can Customise your Title here
        titleView.setText(title);
        titleView.setBackgroundColor(Color.DKGRAY);
        titleView.setPadding(10, 10, 10, 10);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextColor(Color.WHITE);
        titleView.setTextSize(20);

        builder.setCustomTitle(titleView);

        builder.setMessage(message);

        if (inputText != null) {
            builder.setView(inputText);
        }
        if (showOk) {
            if (okClickListener != null) {
                builder.setPositiveButton(okLabel, okClickListener);
            } else {
                builder.setPositiveButton("OK", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        }
        if (showCancel) {
            if (cancelClickListener != null) {
                builder.setNegativeButton(cancelLabel, cancelClickListener);
            } else {
                builder.setNegativeButton("Cancel", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        }
        builder.show();
        return this;
    }

    public enum DialogueOption {
        OK, CANCEL
    }
}
