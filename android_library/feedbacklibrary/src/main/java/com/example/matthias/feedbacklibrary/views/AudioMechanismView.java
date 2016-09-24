package com.example.matthias.feedbacklibrary.views;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.matthias.feedbacklibrary.R;
import com.example.matthias.feedbacklibrary.models.AudioMechanism;
import com.example.matthias.feedbacklibrary.models.Mechanism;

/**
 * Audio mechanism view
 */
public class AudioMechanismView extends MechanismView {
    private AudioMechanism audioMechanism;
    private ImageButton pauseButton;
    private ImageButton playButton;
    private ImageButton recordButton;
    private TextView recordIndicator;
    private ValueAnimator recordIndicatorAnimator;
    private Resources resources;
    private ImageButton stopButton;

    public AudioMechanismView(LayoutInflater layoutInflater, Mechanism mechanism, Resources resources) {
        super(layoutInflater);
        this.audioMechanism = (AudioMechanism) mechanism;
        this.resources = resources;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.audio_feedback_layout, null));
        initView();
    }

    private void initView() {
        ((TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_title)).setText(audioMechanism.getTitle());
        final int colorStart = resources.getColor(R.color.supersede_feedbacklibrary_audio_timer_record_indicator_start_animation_color);
        final int colorEnd = resources.getColor(R.color.supersede_feedbacklibrary_audio_timer_record_indicator_end_animation_color);

        pauseButton = (ImageButton) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_pause);
        playButton = (ImageButton) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_play);
        recordButton = (ImageButton) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_record);
        recordIndicator = (TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_timer_record_indicator);
        stopButton = (ImageButton) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_stop);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
