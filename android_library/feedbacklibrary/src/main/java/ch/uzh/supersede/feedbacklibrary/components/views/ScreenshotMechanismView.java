package ch.uzh.supersede.feedbacklibrary.components.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.AnnotateImageActivity;
import ch.uzh.supersede.feedbacklibrary.models.AbstractFeedbackPart;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotFeedback;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;

public class ScreenshotMechanismView extends AbstractFeedbackPartView {
    private ScreenshotFeedback screenshotFeedback;
    private ImageView screenShotPreviewImageView;
    private HashMap<Integer, String> allStickerAnnotations;
    private HashMap<Integer, String> allTextAnnotations;
    private Button editButton;
    private Button selectButton;
    private Button deleteButton;
    private Bitmap pictureBitmap;
    private Bitmap pictureBitmapAnnotated;
    private Activity activity;

    public ScreenshotMechanismView(LayoutInflater layoutInflater, Activity activity, AbstractFeedbackPart mechanism, int mechanismViewIndex) {
        super(layoutInflater);
        this.viewOrder = mechanism.getOrder();
        this.activity = activity;
        this.screenshotFeedback = (ScreenshotFeedback) mechanism;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.mechanism_screenshot, null));
        initView();
    }

    private Map<Integer, String> getAllStickerAnnotations() {
        return allStickerAnnotations;
    }

    private Map<Integer, String> getAllTextAnnotations() {
        return allTextAnnotations;
    }

    public void toggleSelectButton(boolean enabled) {
        selectButton.setEnabled(enabled);
        if (enabled) {
            selectButton.setBackground(activity.getResources().getDrawable(R.drawable.blue_button));
        } else {
            selectButton.setBackground(activity.getResources().getDrawable(R.drawable.gray_button));
        }
    }

    public void toggleEditButton(boolean enabled) {
        editButton.setEnabled(enabled);
        if (enabled) {
            editButton.setBackground(activity.getResources().getDrawable(R.drawable.pink_button));
        } else {
            editButton.setBackground(activity.getResources().getDrawable(R.drawable.gray_button));
        }
    }

    public void toggleDeleteButton(boolean enabled) {
        deleteButton.setEnabled(enabled);
        if (enabled) {
            deleteButton.setBackground(activity.getResources().getDrawable(R.drawable.blue_button));
        } else {
            deleteButton.setBackground(activity.getResources().getDrawable(R.drawable.gray_button));
        }
    }

    public ImageView getScreenShotPreviewImageView() {
        return screenShotPreviewImageView;
    }

    public void setAllStickerAnnotations(Map<Integer, String> allStickerAnnotations) {
        this.allStickerAnnotations = new HashMap<>(allStickerAnnotations);
    }

    public void setAllTextAnnotations(Map<Integer, String> allTextAnnotations) {
        this.allTextAnnotations = new HashMap<>(allTextAnnotations);
    }

    private void setPictureBitmap(Bitmap pictureBitmap) {
        this.pictureBitmap = pictureBitmap;
    }

    private Bitmap getPictureBitmap() {
        return pictureBitmap;
    }

    public Bitmap getPictureBitmapAnnotated() {
        return pictureBitmapAnnotated;
    }

    private void setPictureBitmapAnnotated(Bitmap pictureBitmapAnnotated) {
        this.pictureBitmapAnnotated = pictureBitmapAnnotated;
    }

    private void initView() {
        View enclosingLayout = getEnclosingLayout();
        screenShotPreviewImageView = (ImageView) enclosingLayout.findViewById(R.id.supersede_feedbacklibrary_screenshot_imageview);

        pictureBitmap = Utils.loadImageFromDatabase(getLayoutInflater().getContext());
        // Selecting image
        selectButton = (Button) enclosingLayout.findViewById(R.id.supersede_feedbacklibrary_select_screenshot_btn);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageSelect();
            }
        });
        // Annotate image
        editButton = (Button) enclosingLayout.findViewById(R.id.supersede_feedbacklibrary_annotate_screenshot_btn);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageAnnotate(ScreenshotMechanismView.this);
            }
        });
        // Delete image
        deleteButton = (Button) enclosingLayout.findViewById(R.id.supersede_feedbacklibrary_remove_screenshot_btn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageDelete();
            }
        });
        refreshPreview(activity.getApplicationContext());
    }

    @Override
    public void updateModel() {
        //TODO [jfo] what to do here?
    }

    private void onImageAnnotate(ScreenshotMechanismView screenshotMechanismView) {
        Intent intent = new Intent(activity, AnnotateImageActivity.class);
        if (screenshotMechanismView.getAllStickerAnnotations() != null && !screenshotMechanismView.getAllStickerAnnotations().isEmpty()) {
            intent.putExtra(EXTRA_KEY_HAS_STICKER_ANNOTATIONS, true);
            intent.putExtra(EXTRA_KEY_ALL_STICKER_ANNOTATIONS, new HashMap<>(screenshotMechanismView.getAllStickerAnnotations()));
        }
        activity.startActivityForResult(intent, REQUEST_ANNOTATE);
    }

    private void onImageSelect() {
        final Resources res = activity.getResources();
        final CharSequence[] items = {res.getString(R.string.screenshot_choose), res.getString(R.string.dialog_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(res.getString(R.string.screenshot_select));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (!items[item].equals(res.getString(R.string.dialog_cancel))) {
                    if (items[item].equals(res.getString(R.string.screenshot_choose))) {
                        galleryIntent();
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void onImageDelete() {
        Utils.wipeImages(activity);
        pictureBitmap = null;
        allStickerAnnotations = null;
        allTextAnnotations = null;
        refreshPreview(activity.getApplicationContext());
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(intent, REQUEST_PHOTO);
    }

    public void refreshPreview(Context context) {
        screenShotPreviewImageView.setBackground(null);
        if (getPictureBitmap() == null) {
            setPictureBitmap(Utils.loadImageFromDatabase(context));
        }
        setPictureBitmapAnnotated(Utils.loadAnnotatedImageFromDatabase(context));
        if (pictureBitmapAnnotated != null) {
            screenShotPreviewImageView.setImageBitmap(pictureBitmapAnnotated);
        } else if (pictureBitmap != null) {
            screenShotPreviewImageView.setImageBitmap(pictureBitmap);
        } else {
            screenShotPreviewImageView.setImageBitmap(null);
            screenShotPreviewImageView.setBackgroundResource(R.drawable.ic_folder_open_black_24dp);
        }
        toggleButtons();
    }

    private void toggleButtons() {
        boolean imageSelected = pictureBitmap != null || pictureBitmapAnnotated != null;
        toggleDeleteButton(imageSelected);
        toggleEditButton(imageSelected);
        toggleSelectButton(!imageSelected);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof AbstractFeedbackPartView){
            int comparedViewOrder = ((AbstractFeedbackPartView) o).getViewOrder();
            return comparedViewOrder > getViewOrder() ? -1 : comparedViewOrder == getViewOrder() ? 0 : 1;
        }
        return 0;
    }
}