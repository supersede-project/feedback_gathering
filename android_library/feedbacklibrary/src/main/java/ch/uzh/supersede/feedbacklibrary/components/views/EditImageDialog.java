package ch.uzh.supersede.feedbacklibrary.components.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.activities.AnnotateImageActivity;
import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.models.DialogType;
import ch.uzh.supersede.feedbacklibrary.models.EditImageItem;

public class EditImageDialog extends DialogFragment {

    private List<EditImageItem> items;
    private Map<String, EditImageItem> allImageEditItems;
    private Map<String, EditImageItem> quickEditItems;
    private Map<String, EditImageItem> colorEditItems;

    private OnEditImageListener listener;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            listener = (OnEditImageListener) activity;

            initData();

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnColorChangeDialogListener");
        }
    }

    public EditImageDialog() {
        super();
        items = new ArrayList<>();
        allImageEditItems = new HashMap<>();
        quickEditItems = new HashMap<>();
        colorEditItems = new HashMap<>();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String type = getArguments().getString("type");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_dialog_layout, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.edl_edit_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        switch (DialogType.valueOf(type)) {
            case Favorite:
                getFavorites();
                builder.setTitle(R.string.favorite);
                break;

            case QuickEdit:
                getItems(quickEditItems);
                builder.setTitle(R.string.quick_edit);
                break;

            case Color:
                getItems(colorEditItems);
                builder.setTitle(R.string.color);
                break;
            default:
        }

        recyclerView.setAdapter(new EditImageViewAdapter(items));

        // Setting the view to the custom layout
        builder.setView(view);

        return builder.create();
    }

    private void getItems(Map<String, EditImageItem> data) {
        items.clear();

        for (Map.Entry<String, ?> entry : data.entrySet()) {
            items.add((EditImageItem) entry.getValue());
        }
    }

    private void getFavorites() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ImageEdit", Context.MODE_PRIVATE);
        Map<String, ?> keys = sharedPreferences.getAll();

        items.clear();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            if (entry.getValue().equals(true) && (allImageEditItems.get(entry.getKey()) != null)) {
                items.add(allImageEditItems.get(entry.getKey()));
            }
        }
    }

    private void initData() {

        getQuickEdit();
        getColor();

        allImageEditItems.putAll(quickEditItems);
        allImageEditItems.putAll(colorEditItems);
    }

    private void getColor() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ImageEdit", Context.MODE_PRIVATE);

        EditImageItem color = new EditImageItem(getString(R.string.supersede_feedbacklibrary_colorpickerbutton_text), R.drawable.ic_palette_black_48dp);
        color.setOnClickListener(new EditImageItem.OnClickListener() {
            @Override
            public void onClick() {
                listener.onColorClicked();
            }
        });
        colorEditItems.put(color.getTitle(), color);

        final EditImageItem blur = new EditImageItem(getString(R.string.supersede_feedbacklibrary_blurbutton_text), R.drawable.ic_blur_on_black_48dp);
        final AnnotateImageView annotateImageView = setCorrectFavoritesIfNeed(sharedPreferences, blur);
        blur.setOnClickListener(new EditImageItem.OnClickListener() {
            @Override
            public void onClick() {
                if (annotateImageView.getBlur() <= 0F) {
                    annotateImageView.setOpacity(180);
                    annotateImageView.setBlur(10F);
                } else {
                    annotateImageView.setOpacity(255);
                    annotateImageView.setBlur(0F);
                }

                EditImageDialog.this.dismiss();
            }
        });
        colorEditItems.put(blur.getTitle(), blur);

        EditImageItem fill = new EditImageItem(getString(R.string.supersede_feedbacklibrary_strokebutton_text), R.drawable.ic_format_color_fill_black_48dp);
        setCorrectFillIfNeed(sharedPreferences, annotateImageView, fill);
        fill.setOnClickListener(new EditImageItem.OnClickListener() {
            @Override
            public void onClick() {
                listener.onFillClicked();
            }
        });
        colorEditItems.put(fill.getTitle(), fill);

        EditImageItem black = new EditImageItem(getString(R.string.supersede_feedbacklibrary_blackbutton_text), R.drawable.ic_brightness_1_black_48dp);
        if (!getAnnotateImageActivity().isBlackModeOn()) {
            colorEditItems.put(color.getTitle(), color);
            black.setTitle(getString(R.string.supersede_feedbacklibrary_blackbutton_text));

            boolean isColorFavorite = sharedPreferences.getBoolean(getString(R.string.supersede_feedbacklibrary_colorbutton_text), false);

            if (isColorFavorite) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(getString(R.string.supersede_feedbacklibrary_colorbutton_text), false);
                editor.putBoolean(getString(R.string.supersede_feedbacklibrary_blackbutton_text), true);
                editor.apply();
            }
        } else {
            colorEditItems.remove(color.getTitle());
            black.setTitle(getString(R.string.supersede_feedbacklibrary_colorbutton_text));

            boolean isBlackFavorite = sharedPreferences.getBoolean(getString(R.string.supersede_feedbacklibrary_blackbutton_text), false);

            if (isBlackFavorite) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(getString(R.string.supersede_feedbacklibrary_blackbutton_text), false);
                editor.putBoolean(getString(R.string.supersede_feedbacklibrary_colorbutton_text), true);
                editor.apply();
            }
        }
        black.setOnClickListener(new EditImageItem.OnClickListener() {
            @Override
            public void onClick() {
                listener.onBlackClicked();
            }
        });
        colorEditItems.put(black.getTitle(), black);
    }

    private void setCorrectFillIfNeed(SharedPreferences sharedPreferences, AnnotateImageView annotateImageView, EditImageItem fill) {
        if (annotateImageView.getPaintStyle() == Paint.Style.FILL) {
            fill.setTitle(getString(R.string.supersede_feedbacklibrary_strokebutton_text));
            boolean isFillFavorite = sharedPreferences.getBoolean(getString(R.string.supersede_feedbacklibrary_fillbutton_text), false);

            if (isFillFavorite) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(getString(R.string.supersede_feedbacklibrary_fillbutton_text), false);
                editor.putBoolean(getString(R.string.supersede_feedbacklibrary_strokebutton_text), true);
                editor.apply();
            }
        } else {
            fill.setTitle(getString(R.string.supersede_feedbacklibrary_fillbutton_text));
            boolean isStrokeFavorite = sharedPreferences.getBoolean(getString(R.string.supersede_feedbacklibrary_strokebutton_text), false);

            if (isStrokeFavorite) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(getString(R.string.supersede_feedbacklibrary_strokebutton_text), false);
                editor.putBoolean(getString(R.string.supersede_feedbacklibrary_fillbutton_text), true);
                editor.apply();
            }
        }
    }

    private AnnotateImageActivity getAnnotateImageActivity(){
        if (getActivity() instanceof AnnotateImageActivity) {
            return (AnnotateImageActivity) getActivity();
        } else {
            throw new RuntimeException("Could not get AnnotateImageActivity from current instance.");
        }
    }

    @NonNull
    private AnnotateImageView setCorrectFavoritesIfNeed(SharedPreferences sharedPreferences, EditImageItem blur) {
        final AnnotateImageView annotateImageView = getAnnotateImageActivity().getAnnotateImageView();

        if (annotateImageView.getBlur() <= 0F) {
            blur.setTitle(getString(R.string.supersede_feedbacklibrary_blurbutton_text));
            boolean isUnblurFavorite = sharedPreferences.getBoolean(getString(R.string.supersede_feedbacklibrary_unblurbutton_text), false);

            if (isUnblurFavorite) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(getString(R.string.supersede_feedbacklibrary_unblurbutton_text), false);
                editor.putBoolean(getString(R.string.supersede_feedbacklibrary_blurbutton_text), true);
                editor.apply();
            }
        } else {
            blur.setTitle(getString(R.string.supersede_feedbacklibrary_unblurbutton_text));
            boolean isBlurFavorite = sharedPreferences.getBoolean(getString(R.string.supersede_feedbacklibrary_blurbutton_text), false);

            if (isBlurFavorite) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(getString(R.string.supersede_feedbacklibrary_blurbutton_text), false);
                editor.putBoolean(getString(R.string.supersede_feedbacklibrary_unblurbutton_text), true);
                editor.apply();
            }
        }
        return annotateImageView;
    }

    private void getQuickEdit() {
        EditImageItem pencil = new EditImageItem("Pencil", R.drawable.ic_lead_pencil_black_48dp);
        pencil.setOnClickListener(new EditImageItem.OnClickListener() {
            @Override
            public void onClick() {
                listener.onPencilClicked();
            }
        });
        quickEditItems.put("Pencil", pencil);

        EditImageItem arrows = new EditImageItem("Arrows", R.drawable.ic_call_made_black_48dp);
        arrows.setOnClickListener(new EditImageItem.OnClickListener() {
            @Override
            public void onClick() {
                listener.onArrowsClicked();
            }
        });
        quickEditItems.put("Arrows", arrows);

        EditImageItem square = new EditImageItem("Square", R.drawable.ic_crop_din_black_48dp);
        square.setOnClickListener(new EditImageItem.OnClickListener() {
            @Override
            public void onClick() {
                listener.onSquareClicked();
            }
        });
        quickEditItems.put("Square", square);

        EditImageItem line = new EditImageItem("Line", R.drawable.icon_line);
        line.setOnClickListener(new EditImageItem.OnClickListener() {
            @Override
            public void onClick() {
                listener.onLineClicked();
            }
        });
        quickEditItems.put("Line", line);

        EditImageItem smileyFace = new EditImageItem("Smiley face", R.drawable.ic_insert_emoticon_black_48dp);
        smileyFace.setOnClickListener(new EditImageItem.OnClickListener() {
            @Override
            public void onClick() {
                listener.onSmileyFaceClicked();
            }
        });
        quickEditItems.put("Smiley face", smileyFace);
    }

    public interface OnEditImageListener {
        void onColorClicked();

        void onBlackClicked();

        void onFillClicked();

        void onPencilClicked();

        void onArrowsClicked();

        void onSmileyFaceClicked();

        void onLineClicked();

        void onSquareClicked();
    }

}
