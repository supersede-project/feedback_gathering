package com.example.matthias.feedbacklibrary.views;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.matthias.feedbacklibrary.FeedbackActivity;
import com.example.matthias.feedbacklibrary.R;
import com.example.matthias.feedbacklibrary.models.AudioMechanism;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.utils.Utils;

/**
 * Audio mechanism view
 */
public class AudioMechanismView extends MechanismView {
    private AudioMechanism audioMechanism;
    private Activity activity;
    private ImageButton pauseButton;
    private ImageButton playButton;
    private ImageButton recordButton;
    private TextView recordIndicator;
    private ValueAnimator recordIndicatorAnimator;
    private Resources resources;
    private ImageButton stopButton;

    public AudioMechanismView(LayoutInflater layoutInflater, Mechanism mechanism, Resources resources, Activity activity) {
        super(layoutInflater);
        this.audioMechanism = (AudioMechanism) mechanism;
        this.resources = resources;
        this.activity = activity;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.audio_feedback_layout, null));
        initView();
    }

    /*
    /**
     * This method saves the string content to the internal storage.
     *
     * @param applicationContext the application context
     * @param dirName            the directory name, e.g., "configDir"
     * @param fileName           the name of the file
     * @param str                the file content as a string
     * @param mode               the mode, e.g., Context.MODE_PRIVATE
     * @return true on success, false otherwise
     */
    /*
    public static boolean saveStringContentToInternalStorage(Context applicationContext, String dirName, String fileName, String str, int mode) {
        ContextWrapper cw = new ContextWrapper(applicationContext);
        File directory = cw.getDir(dirName, mode);
        File myPath = new File(directory, fileName);
        try {
            FileWriter out = new FileWriter(myPath);
            out.write(str);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    */

    private void initView() {
        ((TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_title)).setText(audioMechanism.getTitle());
        final int colorStart = resources.getColor(R.color.supersede_feedbacklibrary_audio_timer_record_indicator_start_animation_color);
        final int colorEnd = resources.getColor(R.color.supersede_feedbacklibrary_audio_timer_record_indicator_end_animation_color);

        pauseButton = (ImageButton) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_pause);
        pauseButton.setEnabled(false);
        playButton = (ImageButton) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_play);
        playButton.setEnabled(false);
        recordButton = (ImageButton) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_record);
        recordIndicator = (TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_timer_record_indicator);
        stopButton = (ImageButton) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_stop);
        stopButton.setEnabled(false);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = Utils.checkSinglePermission(activity, FeedbackActivity.PERMISSIONS_REQUEST_RECORD_AUDIO, Manifest.permission.RECORD_AUDIO, null, null, false);
                if (result) {
                    if (recordIndicatorAnimator == null) {
                        recordIndicatorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorStart, colorEnd);
                        recordIndicatorAnimator.setDuration(1000);
                        recordIndicatorAnimator.setRepeatMode(ValueAnimator.REVERSE);
                        recordIndicatorAnimator.setRepeatCount(ValueAnimator.INFINITE);
                        recordIndicatorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                recordIndicator.setTextColor((Integer) animator.getAnimatedValue());
                            }

                        });
                    }
                    if (recordIndicatorAnimator.isStarted() || recordIndicatorAnimator.isRunning()) {
                        recordIndicatorAnimator.cancel();
                    }
                    recordIndicatorAnimator.start();
                    stopButton.setEnabled(true);
                }
            }
        });

        stopButton = (ImageButton) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordIndicatorAnimator != null && recordIndicatorAnimator.isRunning()) {
                    recordIndicatorAnimator.cancel();
                    recordIndicator.setTextColor(colorStart);
                }
            }
        });
    }

    @Override
    public void updateModel() {

    }
}
