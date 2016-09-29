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

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.matthias.feedbacklibrary.utils.Utils;
import com.example.matthias.feedbacklibrary.views.AnnotateImageView;
import com.example.matthias.feedbacklibrary.views.StickerImageView;
import com.example.matthias.feedbacklibrary.views.StickerView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Activity for annotating the screenshot
 */
public class AnnotateImageActivity extends AppCompatActivity implements ColorPickerDialog.OnColorChangeDialogListener {
    private boolean blackModeOn = false;
    private int oldPaintStrokeColor;
    private int oldPaintFillColor;
    private AnnotateImageView annotateImageView;
    private int selectedSticker;

    private void addSticker() {
        hideAllControlItems((RelativeLayout) findViewById(R.id.supersede_feedbacklibrary_annotate_image_layout));
        StickerImageView sticker = new StickerImageView(this);
        sticker.setImageDrawable(getResources().getDrawable(selectedSticker));
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.supersede_feedbacklibrary_annotate_image_layout);
        if (relativeLayout != null) {
            relativeLayout.addView(sticker);
        }
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

    private void drawStickerOnCanvas() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.supersede_feedbacklibrary_annotate_image_layout);
        if (relativeLayout != null) {
            // Hide all control items
            hideAllControlItems(relativeLayout);

            // Convert the ViewGroup, i.e., the supersede_feedbacklibrary_annotate_picture_layout into a bitmap
            relativeLayout.measure(View.MeasureSpec.makeMeasureSpec(annotateImageView.getBitmapWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(annotateImageView.getBitmapHeight(), View.MeasureSpec.EXACTLY));
            relativeLayout.layout(0, 0, relativeLayout.getMeasuredWidth(), relativeLayout.getMeasuredHeight());

            Bitmap annotatedBitmap = Bitmap.createBitmap(relativeLayout.getLayoutParams().width, relativeLayout.getLayoutParams().height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(annotatedBitmap);
            relativeLayout.draw(canvas);

            int padding = getResources().getDimensionPixelSize(R.dimen.supersede_feedbacklibrary_annotate_image_layout_padding);
            Bitmap croppedBitmap = Bitmap.createBitmap(annotatedBitmap, padding, padding,
                    annotateImageView.getBitmapWidth() - 2 * padding, annotateImageView.getBitmapHeight() - 2 * padding);
        }
    }

    /**
     * This method hides all the control items for every sticker in the specific viewGroup.
     *
     * @param viewGroup the viewGroup
     */
    private void hideAllControlItems(ViewGroup viewGroup) {
        // Hide all control items
        if (viewGroup != null) {
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof StickerView) {
                    ((StickerView) child).setControlItemsHidden(true);
                }
            }
        }
    }

    /**
     * Initializing the view for the image annotation
     *
     * @param bitmap the bitmap to draw on
     */
    private void initAnnotateImageView(Bitmap bitmap, String imagePath) {
        annotateImageView = new AnnotateImageView(this);

        // Set the bitmap to draw on
        annotateImageView.drawBitmap(bitmap);
        // Add the file of the original image
        annotateImageView.addCroppedImage(new File(imagePath));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri croppedImageUri = result.getUri();
                File croppedImageFile = new File(croppedImageUri.getPath());
                annotateImageView.updateCroppedImageHistory(croppedImageFile);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast toast = Toast.makeText(getApplicationContext(), "Oops. Something went wrong!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.supersede_feedbacklibrary_sticker_item_smiley) {
            setSelectedSticker(R.drawable.icon_smile);
            addSticker();
            return true;
        }
        if (id == R.id.supersede_feedbacklibrary_sticker_item_thumb_up) {
            setSelectedSticker(R.drawable.ic_thumb_up_black_48dp);
            addSticker();
            return true;
        }
        if (id == R.id.supersede_feedbacklibrary_sticker_item_thumb_down) {
            setSelectedSticker(R.drawable.ic_thumb_down_black_48dp);
            addSticker();
            return true;
        }
        if (id == R.id.supersede_feedbacklibrary_sticker_item_dissatisfied) {
            setSelectedSticker(R.drawable.ic_sentiment_dissatisfied_black_48dp);
            addSticker();
            return true;
        }
        if (id == R.id.supersede_feedbacklibrary_sticker_item_neutral) {
            setSelectedSticker(R.drawable.ic_sentiment_neutral_black_48dp);
            addSticker();
            return true;
        }
        if (id == R.id.supersede_feedbacklibrary_sticker_item_satisfied) {
            setSelectedSticker(R.drawable.ic_sentiment_satisfied_black_48dp);
            addSticker();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotate);

        // Register the sticker button for the context menu
        selectedSticker = R.drawable.icon_smile;
        final ImageButton stickerButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_sticker_btn);
        registerForContextMenu(stickerButton);

        String imagePath = getIntent().getStringExtra("imagePath");
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        initAnnotateImageView(bitmap, imagePath);

        setListeners();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.supersede_feedbacklibrary_sticker_btn) {
            if (menu.getClass().getSimpleName().equals("ContextMenuBuilder")) {
                try {
                    Method m = menu.getClass().getSuperclass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    // If an exception occurred, ignore it and only the titles of the items will be shown
                }
            }

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_sticker, menu);
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
                // Hide all control items
                hideAllControlItems(relativeLayout);

                // Convert the ViewGroup, i.e., the supersede_feedbacklibrary_annotate_picture_layout into a bitmap
                relativeLayout.measure(View.MeasureSpec.makeMeasureSpec(annotateImageView.getBitmapWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(annotateImageView.getBitmapHeight(), View.MeasureSpec.EXACTLY));
                relativeLayout.layout(0, 0, relativeLayout.getMeasuredWidth(), relativeLayout.getMeasuredHeight());

                Bitmap annotatedBitmap = Bitmap.createBitmap(relativeLayout.getLayoutParams().width, relativeLayout.getLayoutParams().height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(annotatedBitmap);
                relativeLayout.draw(canvas);

                int padding = getResources().getDimensionPixelSize(R.dimen.supersede_feedbacklibrary_annotate_image_layout_padding);
                Bitmap croppedBitmap = Bitmap.createBitmap(annotatedBitmap, padding, padding,
                        annotateImageView.getBitmapWidth() - 2 * padding, annotateImageView.getBitmapHeight() - 2 * padding);

                String annotatedImagePath = Utils.saveBitmapToInternalStorage(getApplicationContext(), "imageDir", FeedbackActivity.IMAGE_NAME, croppedBitmap, Context.MODE_PRIVATE, Bitmap.CompressFormat.PNG, 100);
                Intent intent = new Intent();
                intent.putExtra("annotatedImagePath", annotatedImagePath);
                setResult(RESULT_OK, intent);
            }
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setListeners() {
        final ImageButton colorPickerButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_color_picker_btn);
        final Button blurButton = (Button) findViewById(R.id.supersede_feedbacklibrary_blur_btn);
        final Button fillButton = (Button) findViewById(R.id.supersede_feedbacklibrary_fill_btn);
        final Button blackButton = (Button) findViewById(R.id.supersede_feedbacklibrary_black_btn);
        final ImageButton undoButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_undo_btn);
        final ImageButton redoButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_redo_btn);
        final ImageButton penButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_pen_btn);
        final ImageButton rectangleButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_rectangle_btn);
        final ImageButton circleButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_circle_btn);
        final ImageButton lineButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_line_btn);
        final ImageButton arrowButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_arrow_btn);
        final Button eraseButton = (Button) findViewById(R.id.supersede_feedbacklibrary_erase_btn);
        final ImageButton cropButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_crop_btn);
        final ImageButton stickerButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_sticker_btn);

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
        if (eraseButton != null && blackButton != null) {
            eraseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (annotateImageView.getMode() == AnnotateImageView.Mode.ERASER) {
                        annotateImageView.setMode(AnnotateImageView.Mode.DRAW);
                        blackButton.setEnabled(true);
                        eraseButton.setText(R.string.supersede_feedbacklibrary_off_string);
                    } else {
                        annotateImageView.setMode(AnnotateImageView.Mode.ERASER);
                        blackButton.setEnabled(false);
                        eraseButton.setText(R.string.supersede_feedbacklibrary_on_string);
                    }
                }
            });
        }
        if (cropButton != null) {
            cropButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.supersede_feedbacklibrary_annotate_image_layout);
                    if (relativeLayout != null) {
                        // Hide all control items
                        hideAllControlItems(relativeLayout);

                        // Convert the ViewGroup, i.e., the supersede_feedbacklibrary_annotate_picture_layout into a bitmap
                        relativeLayout.measure(View.MeasureSpec.makeMeasureSpec(annotateImageView.getBitmapWidth(), View.MeasureSpec.EXACTLY),
                                View.MeasureSpec.makeMeasureSpec(annotateImageView.getBitmapHeight(), View.MeasureSpec.EXACTLY));
                        relativeLayout.layout(0, 0, relativeLayout.getMeasuredWidth(), relativeLayout.getMeasuredHeight());

                        Bitmap annotatedBitmap = Bitmap.createBitmap(relativeLayout.getWidth(), relativeLayout.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(annotatedBitmap);
                        relativeLayout.draw(canvas);

                        int padding = getResources().getDimensionPixelSize(R.dimen.supersede_feedbacklibrary_annotate_image_layout_padding);
                        Bitmap croppedBitmap = Bitmap.createBitmap(annotatedBitmap, padding, padding,
                                annotateImageView.getBitmapWidth() - 2 * padding, annotateImageView.getBitmapHeight() - 2 * padding);

                        File tempFile = Utils.createTempChacheFile(getApplicationContext(), "crop", ".jpg");
                        if (Utils.saveBitmapToFile(tempFile, croppedBitmap, Bitmap.CompressFormat.JPEG, 100)) {
                            Uri cropInput = Uri.fromFile(tempFile);
                            CropImage.activity(cropInput).setGuidelines(CropImageView.Guidelines.ON).start(AnnotateImageActivity.this);
                        }
                    }
                }
            });
        }
        if (stickerButton != null) {
            stickerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addSticker();
                }
            });
        }
    }

    private void setSelectedSticker(int selectedSticker) {
        this.selectedSticker = selectedSticker;
    }

    public void showColorPickerDialog() {
        ColorPickerDialog dialog = createColorPickerDialog(annotateImageView.getPaintStrokeColor());
        dialog.show(getFragmentManager(), "ColorPickerDialog");
    }

}
