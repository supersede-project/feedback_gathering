package com.example.matthias.feedbacklibrary;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.matthias.feedbacklibrary.utils.Utils;
import com.example.matthias.feedbacklibrary.views.AnnotateImageView;

/**
 * Activity for annotating the screenshot
 */
public class AnnotateImageActivity extends AppCompatActivity implements ColorPickerDialog.OnColorChangeDialogListener {
    private boolean blackModeOn = false;
    private int oldPaintStrokeColor;
    private int oldPaintFillColor;

    private AnnotateImageView annotateImageView;

    private static ColorPickerDialog newInstance(int mInitialColor) {
        ColorPickerDialog dialog = new ColorPickerDialog();
        Bundle args = new Bundle();
        args.putInt("mInitialColor", mInitialColor);
        dialog.setArguments(args);
        return dialog;
    }

    /**
     * Initializing the view for the image annotation
     *
     * @param bitmap the bitmap to draw on
     */
    private void initAnnotateImageView(Bitmap bitmap) {
        annotateImageView = new AnnotateImageView(this);

        // Set the bitmap to draw on
        annotateImageView.drawBitmap(bitmap);
        // Set the background color of the canvas (used for the eraser)
        annotateImageView.setBaseColor(Color.GREEN);
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
        annotateImageView.setText("");
        annotateImageView.setFontFamily(Typeface.DEFAULT);
        annotateImageView.setFontSize(32F);

        annotateImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.supersede_feedbacklibrary_annotate_picture_layout);
        if (relativeLayout != null) {
            relativeLayout.addView(annotateImageView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotate);

        String imagePath = getIntent().getStringExtra("imagePath");
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        initAnnotateImageView(bitmap);

        setListeners();
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

        if (id == R.id.supersede_feedbacklibrary_action_annotate_cancel) {
            super.onBackPressed();
            return true;
        }
        if (id == R.id.supersede_feedbacklibrary_action_annotate_accept) {
            Bitmap annotatedBitmap = annotateImageView.getBitmap();
            String annotatedImagePath = Utils.saveImageToInternalStorage(getApplicationContext(), "imageDir", FeedbackActivity.IMAGE_NAME, annotatedBitmap, Context.MODE_PRIVATE, Bitmap.CompressFormat.JPEG, 100);
            Intent intent = new Intent();
            intent.putExtra("annotatedImagePath", annotatedImagePath);
            setResult(RESULT_OK, intent);
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setListeners() {
        final Button colorPickerButton = (Button) findViewById(R.id.supersede_feedbacklibrary_color_picker_btn);
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
                        fillButton.setText("Fill");
                    } else {
                        annotateImageView.setPaintStyle(Paint.Style.FILL);
                        fillButton.setText("Stroke");
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
                        eraseButton.setText("OFF");
                    } else {
                        annotateImageView.setMode(AnnotateImageView.Mode.ERASER);
                        blackButton.setEnabled(false);
                        eraseButton.setText("ON");
                    }
                }
            });
        }
    }

    public void showColorPickerDialog() {
        ColorPickerDialog dialog = newInstance(annotateImageView.getPaintStrokeColor());
        dialog.show(getFragmentManager(), "ColorPickerDialog");
    }

}
