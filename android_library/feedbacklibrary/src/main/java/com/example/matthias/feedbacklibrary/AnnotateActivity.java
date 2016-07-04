package com.example.matthias.feedbacklibrary;

import android.app.DialogFragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.matthias.feedbacklibrary.views.AnnotatePictureView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Activity for annotating the screenshot
 */
public class AnnotateActivity extends AppCompatActivity implements ColorPickerDialog.OnColorChangeDialogListener {
    private boolean blackModeOn = false;
    private int oldMPaintColor;
    private Paint mPaint;
    private MaskFilter mBlur;
    private MaskFilter oldMBlur;
    private AnnotatePictureView annotatePictureView;

    public void applyBlurFilter() {
        if (mPaint.getMaskFilter() != mBlur) {
            mPaint.setMaskFilter(mBlur);
            oldMBlur = mBlur;
        } else {
            mPaint.setMaskFilter(null);
            oldMBlur = null;
        }
    }

    private void initMPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotate);

        String imagePath = getIntent().getStringExtra("imagePath");
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        annotatePictureView = new AnnotatePictureView(this, bitmap);
        annotatePictureView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.supersede_feedbacklibrary_annotate_picture_layout);
        relativeLayout.addView(annotatePictureView);

        initMPaint();
        annotatePictureView.setMPaint(mPaint);
        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);

        setListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_annotate, menu);
        return true;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        mPaint.setColor(((ColorPickerDialog) dialog).getChangedColor());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.supersede_feedbacklibrary_action_annotate_accept) {
            Bitmap annotatedBitmap = annotatePictureView.getBitmap();
            String annotatedImagePath = saveToInternalStorage(annotatedBitmap);
            Intent intent = new Intent();
            intent.putExtra("annotatedImagePath", annotatedImagePath);
            setResult(RESULT_OK, intent);
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File myPath = new File(directory, FeedbackActivity.IMAGE_NAME);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(myPath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    public void setListeners() {
        final Button colorButton = (Button) findViewById(R.id.supersede_feedbacklibrary_color_picker_btn);
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });
        final Button blurButton = (Button) findViewById(R.id.supersede_feedbacklibrary_blur_btn);
        blurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPaint.getMaskFilter() != mBlur) {
                    blurButton.setText(R.string.supersede_feedbacklibrary_unblurbutton_text);
                } else {
                    blurButton.setText(R.string.supersede_feedbacklibrary_blurbutton_text);
                }
                applyBlurFilter();
            }
        });
        final Button clearButton = (Button) findViewById(R.id.supersede_feedbacklibrary_black_btn);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!blackModeOn) {
                    oldMPaintColor = mPaint.getColor();
                    oldMBlur = mPaint.getMaskFilter();
                    mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    colorButton.setEnabled(false);
                    clearButton.setText(R.string.supersede_feedbacklibrary_colorbutton_text);
                } else {
                    initMPaint();
                    mPaint.setColor(oldMPaintColor);
                    mPaint.setMaskFilter(oldMBlur);
                    annotatePictureView.setMPaint(mPaint);
                    colorButton.setEnabled(true);
                    clearButton.setText(R.string.supersede_feedbacklibrary_blackbutton_text);
                }
                blackModeOn = !blackModeOn;
            }
        });
    }

    public void showColorPickerDialog() {
        ColorPickerDialog dialog = newInstance(mPaint.getColor());
        dialog.show(getFragmentManager(), "ColorPickerDialog");
    }

    private static ColorPickerDialog newInstance(int mInitialColor) {
        ColorPickerDialog dialog = new ColorPickerDialog();
        Bundle args = new Bundle();
        args.putInt("mInitialColor", mInitialColor);
        dialog.setArguments(args);
        return dialog;
    }

}
