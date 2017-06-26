package ch.uzh.supersede.feedbacklibrary.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.models.Mechanism;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotMechanism;

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

    /**
     * This method returns all the sticker annotations.
     *
     * @return all sticker annotations
     */
    public HashMap<Integer, String> getAllStickerAnnotations() {
        return allStickerAnnotations;
    }

    /**
     * This method returns all the text annotations.
     *
     * @return all text annotations
     */
    public HashMap<Integer, String> getAllTextAnnotations() {
        return allTextAnnotations;
    }

    /**
     * This method returns the button for annotating screenshots.
     *
     * @return the annotateScreenshotButton
     */
    public Button getAnnotateScreenshotButton() {
        return annotateScreenshotButton;
    }

    /**
     * This method returns the button for deleting screenshots.
     *
     * @return the deleteScreenshotButton
     */
    public Button getDeleteScreenshotButton() {
        return deleteScreenshotButton;
    }

    /**
     * This method returns the maximum possible number of text annotations.
     *
     * @return the maximum number of possible text annotations
     */
    public int getMaxNumberTextAnnotation() {
        return screenshotMechanism.getMaxNumberTextAnnotation();
    }

    /**
     * This method returns the index of the mechanism view.
     *
     * @return the mechanism view index
     */
    public int getMechanismViewIndex() {
        return mechanismViewIndex;
    }

    /**
     * This method returns the path of the picture.
     *
     * @return the picture path
     */
    public String getPicturePath() {
        return picturePath;
    }

    /**
     * This method returns the path of the picture without stickers.
     *
     * @return the picture path without stickers
     */
    public String getPicturePathWithoutStickers() {
        return picturePathWithoutStickers;
    }

    /**
     * This method returns the preview image view.
     *
     * @return the preview image view
     */
    public ImageView getScreenShotPreviewImageView() {
        return screenShotPreviewImageView;
    }

    private void initView(String defaultImagePath) {
        View view = getEnclosingLayout();
        boolean isEnabled = false;
        screenShotPreviewImageView = (ImageView) view.findViewById(R.id.supersede_feedbacklibrary_screenshot_imageview);
        screenShotPreviewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageChangedListener.onImageClick(ScreenshotMechanismView.this);
            }
        });

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
                screenShotPreviewImageView.setBackgroundResource(R.drawable.ic_folder_open_black_24dp);
            }
        });
        ((TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_screenshot_feedback_title)).setText(screenshotMechanism.getTitle());
    }

    /**
     * This method sets all the sticker annotations.
     *
     * @param allStickerAnnotations all sticker annotations
     */
    public void setAllStickerAnnotations(HashMap<Integer, String> allStickerAnnotations) {
        this.allStickerAnnotations = allStickerAnnotations;
    }

    /**
     * This method sets all the text annotations.
     *
     * @param allTextAnnotations all text annotations
     */
    public void setAllTextAnnotations(HashMap<Integer, String> allTextAnnotations) {
        this.allTextAnnotations = allTextAnnotations;
    }

    /**
     * This method sets the path to the annotated image.
     *
     * @param annotatedImagePath the annotated image path
     */
    public void setAnnotatedImagePath(String annotatedImagePath) {
        this.annotatedImagePath = annotatedImagePath;
    }

    /**
     * This method sets the picture bitmap.
     *
     * @param pictureBitmap the picture bitmap
     */
    public void setPictureBitmap(Bitmap pictureBitmap) {
        this.pictureBitmap = pictureBitmap;
    }

    /**
     * This methods sets the path to the picture.
     *
     * @param picturePath the picture path
     */
    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    /**
     * This methods sets the path to the picture without stickers.
     *
     * @param picturePathWithoutStickers the picture path without stickers
     */
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

        void onImageClick(ScreenshotMechanismView screenshotMechanismView);
    }
}