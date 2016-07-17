package com.example.matthias.feedbacklibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.matthias.feedbacklibrary.views.ColorPickerView;

/**
 * Color picker dialog class
 */
public class ColorPickerDialog extends DialogFragment {

    // Use this instance of the interface to deliver action events
    OnColorChangeDialogListener mListener;
    private int changedColor;

    public int getChangedColor() {
        return changedColor;
    }

    // Override the Fragment.onAttach() method to instantiate the OnColorChangeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the OnColorChangeDialogListener so we can send events to the host
            mListener = (OnColorChangeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement OnColorChangeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        changedColor = getArguments().getInt("mInitialColor");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflating the custom layout
        View view = inflater.inflate(R.layout.color_picker_dialog, null);
        LinearLayout linearLayout = (LinearLayout) view;

        TextView textView = new TextView(view.getContext());
        textView.setText(R.string.supersede_feedbacklibrary_chosencolor_string);
        textView.setTextSize(20F);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(changedColor);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(50, 50, 0, 0);
        textView.setLayoutParams(lp);

        final ColorPickerView cpv = new ColorPickerView(view.getContext(), changedColor, textView);
        cpv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));

        linearLayout.addView(cpv);
        linearLayout.addView(textView);

        // Setting the view to the custom layout
        builder.setView(linearLayout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User wants to change the color
                        changedColor = cpv.getChangedColor();
                        mListener.onDialogPositiveClick(ColorPickerDialog.this);
                    }
                })
                .setNegativeButton("DISCARD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface OnColorChangeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }
}
