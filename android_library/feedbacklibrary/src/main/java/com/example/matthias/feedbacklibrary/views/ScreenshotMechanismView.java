package com.example.matthias.feedbacklibrary.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matthias.feedbacklibrary.R;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.models.ScreenshotMechanism;

import java.util.HashMap;

/**
 * Screenshot mechanism view
 */
public class ScreenshotMechanismView extends MechanismView {
    private ScreenshotMechanism screenshotMechanism = null;
    // Image annotation
    private int mechanismViewIndex;
    private String annotatedImagePath = null;
    private ImageView screenShotPreviewImageView;
    private HashMap<Integer, String> allStickerAnnotations = null;
    private HashMap<Integer, String> allTextAnnotations = null;
    private Button annotateScreenshotButton;
    private Button deleteScreenshotButton;
    private Bitmap pictureBitmap;
    private String picturePath;
    private String picturePathWithoutStickers;
    private OnImageChangedListener onImageChangedListener;

    public ScreenshotMechanismView(LayoutInflater layoutInflater, Mechanism mechanism, OnImageChangedListener onImageChangedListener, int mechanismViewIndex, String defaultImagePath) {
        super(layoutInflater);
        this.screenshotMechanism = (ScreenshotMechanism) mechanism;
        this.onImageChangedListener = onImageChangedListener;
        this.mechanismViewIndex = mechanismViewIndex;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.screenshot_feedback_layout, null));
        initView(defaultImagePath);
    }

    public HashMap<Integer, String> getAllStickerAnnotations() {
        return allStickerAnnotations;
    }

    public HashMap<Integer, String> getAllTextAnnotations() {
        return allTextAnnotations;
    }

    public Button getAnnotateScreenshotButton() {
        return annotateScreenshotButton;
    }

    public String getAnnotatedImagePath() {
        return annotatedImagePath;
    }

    public Button getDeleteScreenshotButton() {
        return deleteScreenshotButton;
    }

    public int getMaxNumberTextAnnotation() {
        return screenshotMechanism.getMaxNumberTextAnnotation();
    }

    public int getMechanismViewIndex() {
        return mechanismViewIndex;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public String getPicturePathWithoutStickers() {
        return picturePathWithoutStickers;
    }

    public ImageView getScreenShotPreviewImageView() {
        return screenShotPreviewImageView;
    }

    private void initView(String defaultImagePath) {
        View view = getEnclosingLayout();
        boolean isEnabled = false;
        screenShotPreviewImageView = (ImageView) view.findViewById(R.id.supersede_feedbacklibrary_screenshot_imageview);

        // Use the default image path for the screenshot if present
        if (defaultImagePath != null) {
            picturePath = defaultImagePath;
            annotatedImagePath = picturePath;
            pictureBitmap = BitmapFactory.decodeFile(picturePath);
            screenShotPreviewImageView.setBackground(null);
            screenShotPreviewImageView.setImageBitmap(pictureBitmap);
            isEnabled = true;
        }
        // Selecting image
        Button selectScreenshotButton = (Button) view.findViewById(R.id.supersede_feedbacklibrary_select_screenshot_btn);
        selectScreenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageChangedListener.onImageSelect(ScreenshotMechanismView.this);
            }
        });
        // Annotate image
        annotateScreenshotButton = (Button) view.findViewById(R.id.supersede_feedbacklibrary_annotate_screenshot_btn);
        annotateScreenshotButton.setEnabled(isEnabled);
        annotateScreenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageChangedListener.onImageAnnotate(ScreenshotMechanismView.this);
            }
        });
        // Delete image
        deleteScreenshotButton = (Button) view.findViewById(R.id.supersede_feedbacklibrary_remove_screenshot_btn);
        deleteScreenshotButton.setEnabled(isEnabled);
        deleteScreenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureBitmap = null;
                picturePath = null;
                picturePathWithoutStickers = null;
                annotatedImagePath = null;
                allStickerAnnotations = null;
                allTextAnnotations = null;
                annotateScreenshotButton.setEnabled(false);
                deleteScreenshotButton.setEnabled(false);
                screenShotPreviewImageView.setImageBitmap(null);
                screenShotPreviewImageView.setBackgroundResource(R.drawable.camera_picture_big);
            }
        });
        ((TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_screenshot_feedback_title)).setText(screenshotMechanism.getTitle());
    }

    public void setAllStickerAnnotations(HashMap<Integer, String> allStickerAnnotations) {
        this.allStickerAnnotations = allStickerAnnotations;
    }

    public void setAllTextAnnotations(HashMap<Integer, String> allTextAnnotations) {
        this.allTextAnnotations = allTextAnnotations;
    }

    public void setAnnotatedImagePath(String annotatedImagePath) {
        this.annotatedImagePath = annotatedImagePath;
    }

    public void setPictureBitmap(Bitmap pictureBitmap) {
        this.pictureBitmap = pictureBitmap;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public void setPicturePathWithoutStickers(String picturePathWithoutStickers) {
        this.picturePathWithoutStickers = picturePathWithoutStickers;
    }

    @Override
    public void updateModel() {
        screenshotMechanism.setImagePath(annotatedImagePath);
        screenshotMechanism.setAllTextAnnotations(allTextAnnotations);
    }

    public interface OnImageChangedListener {
        void onImageAnnotate(ScreenshotMechanismView screenshotMechanismView);

        void onImageSelect(ScreenshotMechanismView screenshotMechanismView);
    }
}