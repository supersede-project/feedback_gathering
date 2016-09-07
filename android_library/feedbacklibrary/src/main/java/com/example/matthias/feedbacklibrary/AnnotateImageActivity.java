/**
 * Copyright [2016] [Matthias Scherrer]
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.matthias.feedbacklibrary;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matthias.feedbacklibrary.utils.Utils;
import com.example.matthias.feedbacklibrary.views.AnnotateImageView;
import com.example.matthias.feedbacklibrary.views.StickerAnnotationImageView;
import com.example.matthias.feedbacklibrary.views.StickerAnnotationView;
import com.example.matthias.feedbacklibrary.views.TextAnnotationImageView;
import com.example.matthias.feedbacklibrary.views.TextAnnotationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Activity for annotating the screenshot
 */
public class AnnotateImageActivity extends AppCompatActivity implements ColorPickerDialog.OnColorChangeDialogListener, TextAnnotationView.OnTextAnnotationChangedListener {
    private int mechanismId = -1;

    private boolean blackModeOn = false;
    private int oldPaintStrokeColor;
    private int oldPaintFillColor;
    // Text annotation
    private int textAnnotationCounter;
    private int textAnnotationCounterMaximum;
    // Annotated image view
    private AnnotateImageView annotateImageView;
    // Sticker dialog
    private StickerArrayAdapter stickerArrayAdapter;
    private List<Integer> stickerIcons;
    private List<String> stickerLabels;
    private AlertDialog stickerDialog;

    /**
     * This method creates a new sticker annotation.
     *
     * @param imageResourceId the image resource of the sticker
     * @return the sticker or null if no sticker was created
     */
    @Nullable
    private StickerAnnotationImageView addSticker(int imageResourceId) {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.supersede_feedbacklibrary_annotate_image_layout);
        if (relativeLayout != null) {
            hideAllControlItems(relativeLayout);
            StickerAnnotationImageView stickerAnnotationImageView = new StickerAnnotationImageView(this);
            stickerAnnotationImageView.setImageResource(imageResourceId);
            relativeLayout.addView(stickerAnnotationImageView);
            return stickerAnnotationImageView;
        }
        return null;
    }

    /**
     * This method creates a new text annotation.
     *
     * @param imageResourceId the image resource of the annotation
     * @return the text annotation or null if no annotation was created
     */
    @Nullable
    private TextAnnotationImageView addTextAnnotation(int imageResourceId) {
        if (textAnnotationCounter <= textAnnotationCounterMaximum) {
            TextAnnotationImageView stickerViewTextAnnotationImageView = new TextAnnotationImageView(this);
            stickerViewTextAnnotationImageView.setOnTextAnnotationChangedListener(this);
            stickerViewTextAnnotationImageView.setImageResource(imageResourceId);
            stickerViewTextAnnotationImageView.setAnnotationInputTextHint(getResources().getString(R.string.supersede_feedbacklibrary_text_annotation_dialog_hint));
            stickerViewTextAnnotationImageView.setAnnotationInputTextLabel(getResources().getString(R.string.supersede_feedbacklibrary_text_annotation_dialog_label));
            TextView textView = stickerViewTextAnnotationImageView.getAnnotationNumberView();
            if (textView != null) {
                String newAnnotationNumber = Integer.toString(textAnnotationCounter);
                textView.setText(newAnnotationNumber);
                textAnnotationCounter++;
            }
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.supersede_feedbacklibrary_annotate_image_layout);
            if (relativeLayout != null) {
                relativeLayout.addView(stickerViewTextAnnotationImageView);
            }
            if (textAnnotationCounter > textAnnotationCounterMaximum) {
                ImageButton textAnnotationButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_text_comment_btn);
                if (textAnnotationButton != null) {
                    textAnnotationButton.setEnabled(false);
                    textAnnotationButton.setAlpha(0.4F);
                }
            }
            return stickerViewTextAnnotationImageView;
        }
        return null;
    }

    /**
     * This method creates the color picker dialog for the image annotation.
     *
     * @param mInitialColor the initial color
     * @return the color picker dialog
     */
    private ColorPickerDialog createColorPickerDialog(int mInitialColor) {
        ColorPickerDialog dialog = new ColorPickerDialog();
        Bundle args = new Bundle();
        args.putInt("mInitialColor", mInitialColor);
        dialog.setArguments(args);
        return dialog;
    }

    /**
     * This method hides all the control items for every sticker and text annotation in the specific viewGroup.
     *
     * @param viewGroup the viewGroup
     */
    private void hideAllControlItems(ViewGroup viewGroup) {
        // Hide all control items
        if (viewGroup != null) {
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof StickerAnnotationView) {
                    ((StickerAnnotationView) child).setControlItemsHidden(true);
                } else if (child instanceof TextAnnotationView) {
                    ((TextAnnotationView) child).setControlItemsHidden(true);
                }
            }
        }
    }

    /**
     * This method initializes the view for the image a annotation.
     *
     * @param bitmap            the bitmap to draw on
     * @param originalImagePath the path of the original image
     */
    private void initAnnotateImageView(Bitmap bitmap, String originalImagePath) {
        annotateImageView = new AnnotateImageView(this);
        // Set the bitmap to draw on
        annotateImageView.drawBitmap(bitmap);
        // Add the file of the original image
        annotateImageView.addCroppedImage(new File(originalImagePath));
        // Set the background color of the canvas (used for the eraser)
        annotateImageView.setBaseColor(Color.WHITE);
        // Set the mode
        annotateImageView.setMode(AnnotateImageView.Mode.DRAW);
        // Set the drawer
        annotateImageView.setDrawer(AnnotateImageView.Drawer.PEN);
        // Set the paint attributes
        annotateImageView.setPaintStyle(Paint.Style.STROKE);
        annotateImageView.setPaintStrokeColor(Color.RED);
        annotateImageView.setLineCap(Paint.Cap.ROUND);
        annotateImageView.setLineJoin(Paint.Join.ROUND);
        annotateImageView.setPaintStrokeWidth(12F);
        annotateImageView.setPaintFillColor(Color.RED);
        annotateImageView.setOpacity(255);
        annotateImageView.setBlur(0F);
        // Set the text attributes
        annotateImageView.setText("Default text");
        annotateImageView.setFontFamily(Typeface.DEFAULT);
        annotateImageView.setFontSize(32F);
        annotateImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.supersede_feedbacklibrary_annotate_image_layout);
        if (relativeLayout != null) {
            relativeLayout.addView(annotateImageView);
        }
    }

    @SuppressWarnings("unchecked")
    private void initAnnotations(Intent intent) {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.supersede_feedbacklibrary_annotate_image_layout);
        if (relativeLayout != null) {
            if (intent.getBooleanExtra(Utils.EXTRA_KEY_HAS_STICKER_ANNOTATIONS, false)) {
                HashMap<Integer, String> allStickerAnnotations = (HashMap<Integer, String>) intent.getSerializableExtra(Utils.EXTRA_KEY_ALL_STICKER_ANNOTATIONS);
                for (Map.Entry<Integer, String> entry : allStickerAnnotations.entrySet()) {
                    // Array will be of length 6 --> imageResourceId, x, y, width, height, rotation
                    String[] split = entry.getValue().split(Utils.SEPARATOR);
                    StickerAnnotationImageView stickerAnnotationImageView = addSticker(Integer.valueOf(split[0]));
                    if (stickerAnnotationImageView != null) {
                        stickerAnnotationImageView.setX(Float.valueOf(split[1]));
                        stickerAnnotationImageView.setY(Float.valueOf(split[2]));
                        stickerAnnotationImageView.getLayoutParams().width = Integer.valueOf(split[3]);
                        stickerAnnotationImageView.getLayoutParams().height = Integer.valueOf(split[4]);
                        stickerAnnotationImageView.setRotation(Float.valueOf(split[5]));
                    }
                }
            }
            if (intent.getBooleanExtra(Utils.EXTRA_KEY_HAS_TEXT_ANNOTATIONS, false)) {
                HashMap<Integer, String> allTextAnnotations = (HashMap<Integer, String>) intent.getSerializableExtra(Utils.EXTRA_KEY_ALL_TEXT_ANNOTATIONS);
                SortedSet<Integer> keys = new TreeSet<>(allTextAnnotations.keySet());
                for (Integer key : keys) {
                    // Array will be of length 4 --> annotationText, imageResourceId, x, y
                    String[] split = (allTextAnnotations.get(key)).split(Utils.SEPARATOR);
                    TextAnnotationImageView textAnnotationImageView = addTextAnnotation(Integer.valueOf(split[1]));
                    if (textAnnotationImageView != null) {
                        textAnnotationImageView.setAnnotationInputText(split[0]);
                        textAnnotationImageView.setX(Float.valueOf(split[2]));
                        textAnnotationImageView.setY(Float.valueOf(split[3]));
                    }
                }
            }
            hideAllControlItems(relativeLayout);
        }
    }

    private void initStickerLists() {
        stickerIcons = new ArrayList<>();
        stickerLabels = new ArrayList<>();
        stickerIcons.add(R.drawable.icon_smile);
        stickerIcons.add(R.drawable.ic_thumb_up_black_48dp);
        stickerIcons.add(R.drawable.ic_thumb_down_black_48dp);
        stickerIcons.add(R.drawable.ic_sentiment_dissatisfied_black_48dp);
        stickerIcons.add(R.drawable.ic_sentiment_neutral_black_48dp);
        stickerIcons.add(R.drawable.ic_sentiment_satisfied_black_48dp);
        stickerLabels.add(getResources().getString(R.string.supersede_feedbacklibrary_sticker_dialog_smiley_title));
        stickerLabels.add(getResources().getString(R.string.supersede_feedbacklibrary_sticker_dialog_thumb_up_title));
        stickerLabels.add(getResources().getString(R.string.supersede_feedbacklibrary_sticker_dialog_thumb_down_title));
        stickerLabels.add(getResources().getString(R.string.supersede_feedbacklibrary_sticker_dialog_dissatisfied_title));
        stickerLabels.add(getResources().getString(R.string.supersede_feedbacklibrary_sticker_dialog_neutral_title));
        stickerLabels.add(getResources().getString(R.string.supersede_feedbacklibrary_sticker_dialog_satisfied_title));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri croppedImageUri = result.getUri();
                File croppedImageFile = new File(croppedImageUri.getPath());
                annotateImageView.updateCroppedImageHistory(croppedImageFile);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.supersede_feedbacklibrary_error_text), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotate);

        Intent intent = getIntent();
        // If mechanismId == -1, an error occurred
        mechanismId = intent.getIntExtra("mechanismId", -1);
        if (mechanismId != -1) {
            String imagePath = intent.getStringExtra("imagePath");
            textAnnotationCounter = 1;
            // If no maximum is specified, no text annotations are allowed
            textAnnotationCounterMaximum = intent.getIntExtra("textAnnotationCounterMaximum", 0);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            initAnnotateImageView(bitmap, imagePath);
            initAnnotations(intent);
            initStickerLists();
            setListeners();
        } else {
            throw new RuntimeException("no mechanismId provided.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_annotate, menu);
        return true;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        annotateImageView.setPaintStrokeColor(((ColorPickerDialog) dialog).getChangedColor());
        annotateImageView.setPaintFillColor(((ColorPickerDialog) dialog).getChangedColor());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // In both cases whether the action is accepted or cancelled, the temporary stored files need to be deleted
        List<File> tempFiles = annotateImageView.getCroppedImageLists();
        for (int i = 1; i < tempFiles.size(); ++i) {
            tempFiles.get(i).delete();
        }
        tempFiles.clear();

        if (id == R.id.supersede_feedbacklibrary_action_annotate_cancel) {
            super.onBackPressed();
            return true;
        }
        if (id == R.id.supersede_feedbacklibrary_action_annotate_accept) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.supersede_feedbacklibrary_annotate_image_layout);
            if (relativeLayout != null) {
                // Remove all the annotation which are out of bounds
                removeOutOfBoundsAnnotations();
                // Hide all control items
                hideAllControlItems(relativeLayout);
                // Process all the sticker annotations
                HashMap<Integer, String> allStickerAnnotations = processStickerAnnotations(relativeLayout);
                // Process all the text annotations
                HashMap<Integer, String> allTextAnnotations = processTextAnnotations(relativeLayout);

                String annotatedImagePathWithoutStickers = null;
                if (allStickerAnnotations.size() > 0 || allTextAnnotations.size() > 0) {
                    // Get the bitmap (image without stickers if there are any)
                    Bitmap annotatedBitmapWithoutStickers = annotateImageView.getBitmap();
                    annotatedImagePathWithoutStickers = Utils.saveBitmapToInternalStorage(getApplicationContext(), "imageDir", FeedbackActivity.ANNOTATED_IMAGE_NAME_WITHOUT_STICKERS, annotatedBitmapWithoutStickers, Context.MODE_PRIVATE, Bitmap.CompressFormat.PNG, 100);
                }

                // Convert the ViewGroup, i.e., the supersede_feedbacklibrary_annotate_picture_layout into a bitmap (image with stickers)
                relativeLayout.measure(View.MeasureSpec.makeMeasureSpec(annotateImageView.getBitmapWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(annotateImageView.getBitmapHeight(), View.MeasureSpec.EXACTLY));
                relativeLayout.layout(0, 0, relativeLayout.getMeasuredWidth(), relativeLayout.getMeasuredHeight());
                Bitmap annotatedBitmapWithStickers = Bitmap.createBitmap(relativeLayout.getLayoutParams().width, relativeLayout.getLayoutParams().height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(annotatedBitmapWithStickers);
                relativeLayout.draw(canvas);
                int padding = getResources().getDimensionPixelSize(R.dimen.supersede_feedbacklibrary_annotate_image_layout_padding);
                Bitmap croppedBitmap = Bitmap.createBitmap(annotatedBitmapWithStickers, padding, padding,
                        annotateImageView.getBitmapWidth() - 2 * padding, annotateImageView.getBitmapHeight() - 2 * padding);
                String annotatedImagePathWithStickers = Utils.saveBitmapToInternalStorage(getApplicationContext(), "imageDir", FeedbackActivity.ANNOTATED_IMAGE_NAME_WITH_STICKERS, croppedBitmap, Context.MODE_PRIVATE, Bitmap.CompressFormat.PNG, 100);

                Intent intent = new Intent();
                intent.putExtra(Utils.EXTRA_KEY_ANNOTATED_IMAGE_PATH_WITHOUT_STICKERS, annotatedImagePathWithoutStickers);
                intent.putExtra(Utils.EXTRA_KEY_ANNOTATED_IMAGE_PATH_WITH_STICKERS, annotatedImagePathWithStickers);
                intent.putExtra(Utils.EXTRA_KEY_HAS_STICKER_ANNOTATIONS, allStickerAnnotations.size() > 0);
                intent.putExtra(Utils.EXTRA_KEY_ALL_STICKER_ANNOTATIONS, allStickerAnnotations);
                intent.putExtra(Utils.EXTRA_KEY_HAS_TEXT_ANNOTATIONS, allTextAnnotations.size() > 0);
                intent.putExtra(Utils.EXTRA_KEY_ALL_TEXT_ANNOTATIONS, allTextAnnotations);
                setResult(RESULT_OK, intent);
            }
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTextAnnotationDelete() {
        textAnnotationCounter--;
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.supersede_feedbacklibrary_annotate_image_layout);
        if (relativeLayout != null) {
            refreshAnnotationNumber(relativeLayout);
        }
        ImageButton textAnnotationButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_text_comment_btn);
        if (textAnnotationButton != null) {
            textAnnotationButton.setEnabled(true);
            textAnnotationButton.setAlpha(1.0F);
        }
    }

    private HashMap<Integer, String> processStickerAnnotations(ViewGroup viewGroup) {
        HashMap<Integer, String> allStickerAnnotations = new HashMap<>();
        if (viewGroup != null) {
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof StickerAnnotationImageView) {
                    StickerAnnotationImageView stickerAnnotationImageView = (StickerAnnotationImageView) child;

                    int annotationImageResource = stickerAnnotationImageView.getImageResourceId();
                    float getX = child.getX();
                    float getY = child.getY();
                    int width = child.getWidth();
                    int height = child.getHeight();
                    float rotation = child.getRotation();
                    String value = annotationImageResource + Utils.SEPARATOR + getX + Utils.SEPARATOR + getY + Utils.SEPARATOR + width + Utils.SEPARATOR + height + Utils.SEPARATOR + rotation;
                    allStickerAnnotations.put(i, value);
                }
            }
        }

        return allStickerAnnotations;
    }

    private HashMap<Integer, String> processTextAnnotations(ViewGroup viewGroup) {
        HashMap<Integer, String> allTextAnnotations = new HashMap<>();
        if (viewGroup != null) {
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof TextAnnotationImageView) {
                    TextAnnotationImageView textAnnotationView = (TextAnnotationImageView) child;

                    int key = Integer.valueOf(textAnnotationView.getAnnotationNumberView().getText().toString());
                    String annotationInputText = (textAnnotationView.getAnnotationInputText()).trim();
                    int annotationImageResource = textAnnotationView.getImageResourceId();
                    float getX = child.getX();
                    float getY = child.getY();
                    String value = annotationInputText + Utils.SEPARATOR + annotationImageResource + Utils.SEPARATOR + getX + Utils.SEPARATOR + getY;
                    allTextAnnotations.put(key, value);
                }
            }
        }

        return allTextAnnotations;
    }

    private void refreshAnnotationNumber(ViewGroup viewGroup) {
        if (viewGroup != null) {
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof TextAnnotationView) {
                    TextView textView = (((TextAnnotationView) child).getAnnotationNumberView());
                    String newAnnotationNumber = Integer.toString(Integer.valueOf(textView.getText().toString()) - 1);
                    if (Integer.valueOf(newAnnotationNumber) != 0) {
                        textView.setText(newAnnotationNumber);
                    }
                }
            }
        }
    }

    private void removeOutOfBoundsAnnotations() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.supersede_feedbacklibrary_annotate_image_layout);
        if (relativeLayout != null) {
            List<View> toRemove = new ArrayList<>();
            int newBitmapWidth = annotateImageView.getBitmapWidth();
            int newBitmapHeight = annotateImageView.getBitmapHeight();
            float fraction = 0.5f;
            for (int i = 0; i < relativeLayout.getChildCount(); ++i) {
                View child = relativeLayout.getChildAt(i);
                if (child instanceof StickerAnnotationView || child instanceof TextAnnotationView) {
                    // A fraction the sticker should be visible, if not the sticker will be removed
                    float deleteThresholdX = child.getWidth() * fraction;
                    float deleteThresholdY = child.getHeight() * fraction;
                    float x = child.getX();
                    float y = child.getY();

                    boolean xOk = true;
                    boolean yOk = true;
                    if (x < 0) {
                        xOk = Math.abs(x) < deleteThresholdX;
                    } else if (x > 0 && !(x < 0)) {
                        xOk = x + deleteThresholdX < newBitmapWidth;
                    }
                    if (y < 0) {
                        yOk = Math.abs(y) < deleteThresholdY;
                    } else if (y > 0 && !(y < 0)) {
                        yOk = y + deleteThresholdY < newBitmapHeight;
                    }

                    if (!(xOk && yOk)) {
                        toRemove.add(child);
                    }
                }
            }

            for (int i = 0; i < toRemove.size(); ++i) {
                relativeLayout.removeView(toRemove.get(i));
            }
            toRemove.clear();

            textAnnotationCounter = 1;
            for (int i = 0; i < relativeLayout.getChildCount(); ++i) {
                View child = relativeLayout.getChildAt(i);
                if (child instanceof TextAnnotationView) {
                    String newAnnotationNumber = Integer.toString(textAnnotationCounter);
                    ((TextAnnotationImageView) child).getAnnotationNumberView().setText(newAnnotationNumber);
                    textAnnotationCounter++;
                }
                if (textAnnotationCounter > textAnnotationCounterMaximum) {
                    break;
                }
            }
            if (textAnnotationCounter <= textAnnotationCounterMaximum) {
                ImageButton textAnnotationButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_text_comment_btn);
                if (textAnnotationButton != null) {
                    textAnnotationButton.setEnabled(false);
                    textAnnotationButton.setAlpha(0.4F);
                }
            }
        }
    }

    private void setListeners() {
        final ImageButton penButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_pen_btn);
        final ImageButton rectangleButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_rectangle_btn);
        final ImageButton circleButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_circle_btn);
        final ImageButton lineButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_line_btn);
        final ImageButton arrowButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_arrow_btn);
        final ImageButton stickerButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_sticker_btn);
        final ImageButton colorPickerButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_color_picker_btn);
        final ImageButton cropButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_crop_btn);
        final ImageButton textAnnotationButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_text_comment_btn);
        final ImageButton undoButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_undo_btn);
        final ImageButton redoButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_redo_btn);
        final Button blurButton = (Button) findViewById(R.id.supersede_feedbacklibrary_blur_btn);
        final Button fillButton = (Button) findViewById(R.id.supersede_feedbacklibrary_fill_btn);
        final Button blackButton = (Button) findViewById(R.id.supersede_feedbacklibrary_black_btn);

        if (colorPickerButton != null) {
            colorPickerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showColorPickerDialog();
                }
            });
        }
        if (blurButton != null) {
            blurButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(annotateImageView.getBlur() > 0F)) {
                        annotateImageView.setOpacity(180);
                        annotateImageView.setBlur(10F);
                        blurButton.setText(R.string.supersede_feedbacklibrary_unblurbutton_text);
                    } else {
                        annotateImageView.setOpacity(255);
                        annotateImageView.setBlur(0F);
                        blurButton.setText(R.string.supersede_feedbacklibrary_blurbutton_text);
                    }
                }
            });
        }
        if (fillButton != null) {
            fillButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (annotateImageView.getPaintStyle() == Paint.Style.FILL) {
                        annotateImageView.setPaintStyle(Paint.Style.STROKE);
                        fillButton.setText(R.string.supersede_feedbacklibrary_fillbutton_text);
                    } else {
                        annotateImageView.setPaintStyle(Paint.Style.FILL);
                        fillButton.setText(R.string.supersede_feedbacklibrary_strokebutton_text);
                    }
                }
            });
        }
        if (blackButton != null && colorPickerButton != null) {
            blackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!blackModeOn) {
                        oldPaintStrokeColor = annotateImageView.getPaintStrokeColor();
                        oldPaintFillColor = annotateImageView.getPaintFillColor();
                        annotateImageView.setPaintStrokeColor(Color.BLACK);
                        annotateImageView.setPaintFillColor(Color.BLACK);
                        colorPickerButton.setEnabled(false);
                        blackButton.setText(R.string.supersede_feedbacklibrary_colorbutton_text);
                    } else {
                        annotateImageView.setPaintStrokeColor(oldPaintStrokeColor);
                        annotateImageView.setPaintFillColor(oldPaintFillColor);
                        colorPickerButton.setEnabled(true);
                        blackButton.setText(R.string.supersede_feedbacklibrary_blackbutton_text);
                    }
                    blackModeOn = !blackModeOn;
                }
            });
        }
        if (penButton != null) {
            penButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    annotateImageView.setDrawer(AnnotateImageView.Drawer.PEN);
                }
            });
        }
        if (lineButton != null) {
            lineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    annotateImageView.setDrawer(AnnotateImageView.Drawer.LINE);
                }
            });
        }
        if (arrowButton != null) {
            arrowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    annotateImageView.setDrawer(AnnotateImageView.Drawer.ARROW);
                }
            });
        }
        if (undoButton != null && redoButton != null) {
            annotateImageView.setUndoButton(undoButton);
            undoButton.setEnabled(false);
            undoButton.setAlpha(0.4F);
            undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (annotateImageView.isUndoable()) {
                        redoButton.setEnabled(annotateImageView.undo());
                        redoButton.setAlpha(1.0F);
                        if (!annotateImageView.isUndoable()) {
                            undoButton.setEnabled(false);
                            undoButton.setAlpha(0.4F);
                            annotateImageView.setNoActionExecuted(true);
                        }
                    }
                }
            });
            annotateImageView.setRedoButton(redoButton);
            redoButton.setEnabled(false);
            redoButton.setAlpha(0.4F);
            redoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (annotateImageView.isRedoable()) {
                        undoButton.setEnabled(annotateImageView.redo());
                        undoButton.setAlpha(1.0F);
                        if (!annotateImageView.isRedoable()) {
                            redoButton.setEnabled(false);
                            redoButton.setAlpha(0.4F);
                        }
                    }
                }
            });
        }
        if (rectangleButton != null) {
            rectangleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    annotateImageView.setDrawer(AnnotateImageView.Drawer.RECTANGLE);
                }
            });
        }
        if (circleButton != null) {
            circleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    annotateImageView.setDrawer(AnnotateImageView.Drawer.CIRCLE);
                }
            });
        }
        if (cropButton != null) {
            cropButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap tempBitmap = annotateImageView.getBitmap();
                    Bitmap croppedBitmap = Bitmap.createBitmap(tempBitmap, 0, 0, tempBitmap.getWidth(), tempBitmap.getHeight());

                    File tempFile = Utils.createTempChacheFile(getApplicationContext(), "crop", ".jpg");
                    if (Utils.saveBitmapToFile(tempFile, croppedBitmap, Bitmap.CompressFormat.JPEG, 100)) {
                        Uri cropInput = Uri.fromFile(tempFile);
                        CropImage.activity(cropInput).setGuidelines(CropImageView.Guidelines.ON).start(AnnotateImageActivity.this);
                    }
                }
            });
        }
        if (stickerButton != null) {
            stickerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStickerDialog();
                }
            });
        }
        if (textAnnotationButton != null) {
            textAnnotationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addTextAnnotation(R.drawable.ic_comment_black_48dp);
                }
            });
        }
    }

    public void showColorPickerDialog() {
        ColorPickerDialog dialog = createColorPickerDialog(annotateImageView.getPaintStrokeColor());
        dialog.show(getFragmentManager(), "ColorPickerDialog");
    }

    private void showStickerDialog() {
        if (stickerDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.ic_sentiment_satisfied_black_48dp);
            builder.setTitle(getResources().getString(R.string.supersede_feedbacklibrary_sticker_dialog_title));
            builder.setNegativeButton(getResources().getString(R.string.supersede_feedbacklibrary_cancel_string), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            if (stickerArrayAdapter == null) {
                stickerArrayAdapter = new StickerArrayAdapter(builder.getContext(), stickerIcons, stickerLabels);
            }
            builder.setAdapter(stickerArrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addSticker(stickerArrayAdapter.getIcons().get(which));
                }
            });
            builder.setCancelable(false);
            stickerDialog = builder.create();
        }
        stickerDialog.show();
    }

    private class StickerArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final List<Integer> icons;
        private final List<String> values;

        public StickerArrayAdapter(Context context, List<Integer> icons, List<String> values) {
            super(context, R.layout.sticker_row_layout, values);
            this.context = context;
            this.icons = icons;
            this.values = values;
        }

        public final List<Integer> getIcons() {
            return icons;
        }

        public final List<String> getValues() {
            return values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.sticker_row_layout, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.supersede_feedbacklibrary_sticker_row_layout_icon);
            TextView textView = (TextView) convertView.findViewById(R.id.supersede_feedbacklibrary_sticker_row_layout_label);
            imageView.setImageResource(icons.get(position));
            textView.setText(values.get(position));
            return convertView;
        }
    }

}
