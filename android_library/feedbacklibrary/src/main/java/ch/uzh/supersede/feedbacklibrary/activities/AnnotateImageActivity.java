package ch.uzh.supersede.feedbacklibrary.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.models.DialogType;
import ch.uzh.supersede.feedbacklibrary.utils.ColorPickerDialog;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;
import ch.uzh.supersede.feedbacklibrary.views.AnnotateImageView;
import ch.uzh.supersede.feedbacklibrary.views.EditImageDialog;
import ch.uzh.supersede.feedbacklibrary.views.StickerAnnotationImageView;
import ch.uzh.supersede.feedbacklibrary.views.AbstractAnnotationView;
import ch.uzh.supersede.feedbacklibrary.views.TextAnnotationImageView;

import static android.graphics.Color.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ScreenshotConstants.*;


/**
 * Activity for annotating the screenshot
 */
public class AnnotateImageActivity extends AppCompatActivity implements ColorPickerDialog.OnColorChangeDialogListener, TextAnnotationImageView.OnTextAnnotationChangedListener, EditImageDialog.OnEditImageListener {
    private static final String TAG = "AnnotateImageActivity";
    private int mechanismViewId = -1;
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

    private EditImageDialog dialog;

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

    public AnnotateImageView getAnnotateImageView() {
        return annotateImageView;
    }

    @Nullable
    private TextAnnotationImageView addTextAnnotation(int imageResourceId) {
        if (textAnnotationCounter <= textAnnotationCounterMaximum) {
            TextAnnotationImageView stickerViewTextAnnotationImageView = new TextAnnotationImageView(this);
            stickerViewTextAnnotationImageView.setOnTextAnnotationChangedListener(this);
            stickerViewTextAnnotationImageView.setAnnotationInputTextHint(getResources().getString(R.string.supersede_feedbacklibrary_text_annotation_dialog_hint));
            stickerViewTextAnnotationImageView.setAnnotationInputTextLabel(getResources().getString(R.string.supersede_feedbacklibrary_text_annotation_dialog_label));
            TextView textView = stickerViewTextAnnotationImageView.getAnnotationNumber();
            if (textView != null) {
                String newAnnotationNumber = Integer.toString(textAnnotationCounter);
                textView.setText(newAnnotationNumber);
                textAnnotationCounter++;
            }
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.supersede_feedbacklibrary_annotate_image_layout);
            if (relativeLayout != null) {
                relativeLayout.addView(stickerViewTextAnnotationImageView);
            }
            return stickerViewTextAnnotationImageView;
        }
        return null;
    }

    private ColorPickerDialog createColorPickerDialog(int mInitialColor) {
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
        Bundle args = new Bundle();
        args.putInt("mInitialColor", mInitialColor);
        colorPickerDialog.setArguments(args);
        return colorPickerDialog;
    }

    private void hideAllControlItems(ViewGroup viewGroup) {
        // Hide all control items
        if (viewGroup != null) {
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof AbstractAnnotationView) {
                    ((AbstractAnnotationView) child).setViewsVisible(false);
                }
            }
        }
    }

    private void initAnnotateImageView(Bitmap bitmap, String originalImagePath) {
        annotateImageView = new AnnotateImageView(this);
        // Set the bitmap to draw on
        annotateImageView.drawBitmap(bitmap);
        // Add the file of the original image
        annotateImageView.addCroppedImage(new File(originalImagePath));
        // Set the background color of the canvas (used for the eraser)

        annotateImageView.setBaseColor(WHITE);
        // Set the mode
        annotateImageView.setMode(AnnotateImageView.Mode.DRAW);
        // Set the drawer
        annotateImageView.setDrawer(AnnotateImageView.Drawer.PEN);
        // Set the paint attributes
        annotateImageView.setPaintStyle(Paint.Style.STROKE);
        annotateImageView.setPaintStrokeColor(RED);
        annotateImageView.setLineCap(Paint.Cap.ROUND);
        annotateImageView.setLineJoin(Paint.Join.ROUND);
        float strokeWidth = getResources().getDisplayMetrics().density < 1.6F ? 6F : 12F;
        annotateImageView.setPaintStrokeWidth(strokeWidth);
        annotateImageView.setPaintFillColor(RED);
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
            if (intent.getBooleanExtra(EXTRA_KEY_HAS_STICKER_ANNOTATIONS, false)) {
                handleStickerAnnotations(intent);
            }
            if (intent.getBooleanExtra(EXTRA_KEY_HAS_TEXT_ANNOTATIONS, false)) {
                handleTextAnnotations(intent);
            }
            hideAllControlItems(relativeLayout);
        }
    }

    private void handleStickerAnnotations(Intent intent) {
        HashMap<Integer, String> allStickerAnnotations = (HashMap<Integer, String>) intent.getSerializableExtra(EXTRA_KEY_ALL_STICKER_ANNOTATIONS);
        for (Map.Entry<Integer, String> entry : allStickerAnnotations.entrySet()) {
            // Array will be of length 6 --> imageResourceId, x, y, width, height, rotation
            String[] split = entry.getValue().split(SEPARATOR);
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

    private void handleTextAnnotations(Intent intent) {
        HashMap<Integer, String> allTextAnnotations = (HashMap<Integer, String>) intent.getSerializableExtra(EXTRA_KEY_ALL_TEXT_ANNOTATIONS);
        SortedSet<Integer> keys = new TreeSet<>(allTextAnnotations.keySet());
        for (Integer key : keys) {
            // Array will be of length 4 --> annotationText, imageResourceId, x, y
            String[] split = (allTextAnnotations.get(key)).split(SEPARATOR);
            TextAnnotationImageView textAnnotationImageView = addTextAnnotation(Integer.valueOf(split[1]));
            if (textAnnotationImageView != null) {
                textAnnotationImageView.setAnnotationInputText(split[0]);
                textAnnotationImageView.setX(Float.valueOf(split[2]));
                textAnnotationImageView.setY(Float.valueOf(split[3]));
            }
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
                Toast toast = Toast.makeText(getApplicationContext(), R.string.supersede_feedbacklibrary_error_text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO [jfo]: remove me
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_annotate);

        Intent intent = getIntent();
        // If mechanismViewId == -1, an error occurred
        mechanismViewId = intent.getIntExtra(EXTRA_KEY_MECHANISM_VIEW_ID, -1);
        if (mechanismViewId != -1) {
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
            Log.e(TAG, "Failed to create the activity. No mechanismViewID provided");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_annotate, menu);
        return true;
    }

    @Override
    public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog) {
        annotateImageView.setPaintStrokeColor(((ColorPickerDialog) dialog).getChangedColor());
        annotateImageView.setPaintFillColor(((ColorPickerDialog) dialog).getChangedColor());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // In both cases whether the action is accepted or cancelled, the temporary stored files need to be deleted
        List<File> tempFiles = annotateImageView.getCroppedImageLists();
        for (int i = 1; i < tempFiles.size(); ++i) {
            if (!tempFiles.get(i).delete()) {
                Log.w(TAG, "File " + tempFiles.get(i).getName() + "could not be deleted.");
            }
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
                    annotatedImagePathWithoutStickers = Utils.saveBitmapToInternalStorage(getApplicationContext(), "imageDir", mechanismViewId + ANNOTATED_IMAGE_NAME_WITHOUT_STICKERS, annotatedBitmapWithoutStickers, Context.MODE_PRIVATE, Bitmap.CompressFormat.PNG, 100);
                }

                // Convert the ViewGroup, i.e., the supersede_feedbacklibrary_annotate_picture_layout into a bitmap (image with stickers)
                relativeLayout.measure(View.MeasureSpec.makeMeasureSpec(annotateImageView.getBitmapWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(annotateImageView.getBitmapHeight(), View.MeasureSpec.EXACTLY));

                relativeLayout.layout(0, 0, relativeLayout.getMeasuredWidth(), relativeLayout.getMeasuredHeight());
                Bitmap annotatedBitmapWithStickers = Bitmap.createBitmap(relativeLayout.getLayoutParams().width, relativeLayout.getLayoutParams().height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(annotatedBitmapWithStickers);
                relativeLayout.draw(canvas);
                Bitmap croppedBitmap = Bitmap.createBitmap(annotatedBitmapWithStickers, 0, 0, annotateImageView.getBitmapWidth(), annotateImageView.getBitmapHeight());
                String annotatedImagePathWithStickers = Utils.saveBitmapToInternalStorage(getApplicationContext(), "imageDir", mechanismViewId + ANNOTATED_IMAGE_NAME_WITH_STICKERS, croppedBitmap, Context.MODE_PRIVATE, Bitmap.CompressFormat.PNG, 100);

                Intent intent = new Intent();
                intent.putExtra(EXTRA_KEY_MECHANISM_VIEW_ID, mechanismViewId);
                intent.putExtra(EXTRA_KEY_ANNOTATED_IMAGE_PATH_WITHOUT_STICKERS, annotatedImagePathWithoutStickers);
                intent.putExtra(EXTRA_KEY_ANNOTATED_IMAGE_PATH_WITH_STICKERS, annotatedImagePathWithStickers);
                intent.putExtra(EXTRA_KEY_HAS_STICKER_ANNOTATIONS, allStickerAnnotations.size() > 0);
                intent.putExtra(EXTRA_KEY_ALL_STICKER_ANNOTATIONS, allStickerAnnotations);
                intent.putExtra(EXTRA_KEY_HAS_TEXT_ANNOTATIONS, allTextAnnotations.size() > 0);
                intent.putExtra(EXTRA_KEY_ALL_TEXT_ANNOTATIONS, allTextAnnotations);
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
                    String value = annotationImageResource + SEPARATOR + getX + SEPARATOR + getY + SEPARATOR + width + SEPARATOR + height + SEPARATOR + rotation;
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

                    int key = Integer.valueOf(textAnnotationView.getAnnotationNumber().getText().toString());
                    // If no text was entered, just set the empty string
                    String annotationInputText = "";
                    if (textAnnotationView.getAnnotationInputText() != null) {
                        annotationInputText = (textAnnotationView.getAnnotationInputText()).trim();
                    }
                    float getX = child.getX();
                    float getY = child.getY();
                    String value = annotationInputText + SEPARATOR + getX + SEPARATOR + getY;
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
                if (child instanceof TextAnnotationImageView) {
                    TextView textView = (((TextAnnotationImageView) child).getAnnotationNumber());
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

        List<View> toRemove = calculateViewsToRemove(relativeLayout);
        for (View view : toRemove) {
            relativeLayout.removeView(view);
        }
        toRemove.clear();

        textAnnotationCounter = 1;
        for (int i = 0; i < relativeLayout.getChildCount(); ++i) {
            View child = relativeLayout.getChildAt(i);
            if (child instanceof TextAnnotationImageView) {
                String newAnnotationNumber = Integer.toString(textAnnotationCounter);
                ((TextAnnotationImageView) child).getAnnotationNumber().setText(newAnnotationNumber);
                textAnnotationCounter++;
            }
            if (textAnnotationCounter > textAnnotationCounterMaximum) {
                break;
            }
        }
    }

    private List<View> calculateViewsToRemove(RelativeLayout relativeLayout) {
        int newBitmapWidth = annotateImageView.getBitmapWidth();
        int newBitmapHeight = annotateImageView.getBitmapHeight();
        float fraction = 0.5f;

        List<View> toRemove = new ArrayList<>();
        for (int i = 0; i < relativeLayout.getChildCount(); ++i) {
            View child = relativeLayout.getChildAt(i);
            if (child instanceof AbstractAnnotationView) {
                // A fraction the sticker should be visible, if not the sticker will be removed
                float deleteThresholdX = child.getWidth() * fraction;
                float deleteThresholdY = child.getHeight() * fraction;
                float x = child.getX();
                float y = child.getY();
                boolean xOk = true;
                boolean yOk = true;

                if (x < 0) {
                    xOk = Math.abs(x) < deleteThresholdX;
                } else if (x >= 0) {
                    xOk = x + deleteThresholdX < newBitmapWidth;
                }
                if (y < 0) {
                    yOk = Math.abs(y) < deleteThresholdY;
                } else if (y >= 0) {
                    yOk = y + deleteThresholdY < newBitmapHeight;
                }

                if (!(xOk && yOk)) {
                    toRemove.add(child);
                }
            }
        }
        return toRemove;
    }

    private void setListeners() {
        Button favoriteButton = (Button) findViewById(R.id.aa_favorite_button);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DialogType.Favorite);
            }
        });

        Button quickEditButton = (Button) findViewById(R.id.aa_quick_edit_button);
        quickEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DialogType.QuickEdit);
            }
        });

        Button colorButton = (Button) findViewById(R.id.aa_color_button);
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DialogType.Color);
            }
        });

        Button cropButton = (Button) findViewById(R.id.aa_crop_button);
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap tempBitmap = annotateImageView.getBitmap();
                Bitmap croppedBitmap = Bitmap.createBitmap(tempBitmap, 0, 0, tempBitmap.getWidth(), tempBitmap.getHeight());

                File tempFile = Utils.createTempChacheFile(getApplicationContext(), "crop", ".jpg");
                if (Utils.saveBitmapToFile(tempFile, croppedBitmap, Bitmap.CompressFormat.JPEG, 100)) {
                    Uri cropInput = Uri.fromFile(tempFile);
                    CropImage.activity(cropInput).setGuidelines(CropImageView.Guidelines.ON).start(AnnotateImageActivity.this);
                }
            }
        });

        final ImageButton undoButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_undo_btn);
        final ImageButton redoButton = (ImageButton) findViewById(R.id.supersede_feedbacklibrary_redo_btn);

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
                    } else if (!annotateImageView.isUndoable()) {
                        undoButton.setEnabled(false);
                        undoButton.setAlpha(0.4F);
                        annotateImageView.setNoActionExecuted(true);
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
                    } else if (!annotateImageView.isRedoable()) {
                        redoButton.setEnabled(false);
                        redoButton.setAlpha(0.4F);
                    }
                }
            });
        }
    }

    private void showDialog(DialogType type) {
        dialog = new EditImageDialog();
        Bundle args = new Bundle();
        args.putString("type", type.name());
        dialog.setArguments(args);

        switch (type) {
            case Favorite:
            case QuickEdit:
            case Color:
                dialog.show(getSupportFragmentManager(), "EditImageDialog");
                break;
            default:
        }
    }

    private void showColorPickerDialog() {
        ColorPickerDialog colorPickerDialog = createColorPickerDialog(annotateImageView.getPaintStrokeColor());
        colorPickerDialog.show(getSupportFragmentManager(), "ColorPickerDialog");
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

    @Override
    public void onColorClicked() {
        dialog.dismiss();
        showColorPickerDialog();
    }

    @Override
    public void onBlackClicked() {
        if (!blackModeOn) {
            annotateImageView.setPaintStrokeColor(oldPaintStrokeColor);
            annotateImageView.setPaintFillColor(oldPaintFillColor);
        } else {
            oldPaintStrokeColor = annotateImageView.getPaintStrokeColor();
            oldPaintFillColor = annotateImageView.getPaintFillColor();
            annotateImageView.setPaintStrokeColor(BLACK);
            annotateImageView.setPaintFillColor(BLACK);
            showColorPickerDialog();
        }
        blackModeOn = !blackModeOn;
        dialog.dismiss();
    }

    public boolean isBlackModeOn() {
        return blackModeOn;
    }

    @Override
    public void onFillClicked() {
        if (annotateImageView.getPaintStyle() == Paint.Style.FILL) {
            annotateImageView.setPaintStyle(Paint.Style.STROKE);
        } else {
            annotateImageView.setPaintStyle(Paint.Style.FILL);
        }
        dialog.dismiss();
    }

    @Override
    public void onPencilClicked() {
        annotateImageView.setDrawer(AnnotateImageView.Drawer.PEN);
        dialog.dismiss();
    }

    @Override
    public void onArrowsClicked() {
        annotateImageView.setDrawer(AnnotateImageView.Drawer.ARROW);
        dialog.dismiss();
    }

    @Override
    public void onTextClicked() {
        addTextAnnotation(R.drawable.ic_comment_black_48dp);
        dialog.dismiss();
    }

    @Override
    public void onSmileyFaceClicked() {
        showStickerDialog();
        dialog.dismiss();
    }

    @Override
    public void onLineClicked() {
        annotateImageView.setDrawer(AnnotateImageView.Drawer.LINE);
        dialog.dismiss();
    }

    @Override
    public void onSquareClicked() {
        annotateImageView.setDrawer(AnnotateImageView.Drawer.RECTANGLE);
        dialog.dismiss();
    }

    private class StickerArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final List<Integer> icons;
        private final List<String> values;

        public StickerArrayAdapter(Context context, List<Integer> icons, List<String> values) {
            super(context, R.layout.utility_sticker, values);
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
                convertView = layoutInflater.inflate(R.layout.utility_sticker, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.supersede_feedbacklibrary_sticker_row_layout_icon);
            TextView textView = (TextView) convertView.findViewById(R.id.supersede_feedbacklibrary_sticker_row_layout_label);
            imageView.setImageResource(icons.get(position));
            textView.setText(values.get(position));
            return convertView;
        }
    }

}
