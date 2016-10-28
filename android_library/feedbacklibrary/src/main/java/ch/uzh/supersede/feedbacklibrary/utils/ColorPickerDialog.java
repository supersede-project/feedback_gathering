package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.views.ColorPickerView;

/**
 * Color picker dialog class
 */
public class ColorPickerDialog extends DialogFragment {
    OnColorChangeDialogListener mListener;
    private int changedColor;

    /**
     * This method returns the changed color.
     *
     * @return the changed color
     */
    public int getChangedColor() {
        return changedColor;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (OnColorChangeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnColorChangeDialogListener");
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
                .setPositiveButton(getResources().getString(R.string.supersede_feedbacklibrary_ok_string), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // The user wants to change the color
                        changedColor = cpv.getChangedColor();
                        mListener.onDialogPositiveClick(ColorPickerDialog.this);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.supersede_feedbacklibrary_discard_string), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    public interface OnColorChangeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }
}
