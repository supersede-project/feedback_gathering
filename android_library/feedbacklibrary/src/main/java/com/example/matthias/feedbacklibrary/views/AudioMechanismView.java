package com.example.matthias.feedbacklibrary.views;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matthias.feedbacklibrary.FeedbackActivity;
import com.example.matthias.feedbacklibrary.R;
import com.example.matthias.feedbacklibrary.models.AudioMechanism;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.utils.Utils;

import java.io.File;
import java.io.IOException;

/**
 * Audio mechanism view
 */
public class AudioMechanismView extends MechanismView {
    int recordAnimationColorStart;
    int recordAnimationColorEnd;
    private String audioFilePath;
    private AudioMechanism audioMechanism;
    private Activity activity;
    private Context applicationContext;
    private boolean isPaused = false;
    private boolean isPlaying = false;
    private boolean isRecording = false;
    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;
    private ImageButton pauseButton;
    private ImageButton playButton;
    private ImageButton recordButton;
    private TextView recordIndicator;
    private ValueAnimator recordIndicatorAnimator;
    private Resources resources;
    private ImageButton stopButton;
    private String tempAudioFilePath;

    public AudioMechanismView(LayoutInflater layoutInflater, Mechanism mechanism, Resources resources, Activity activity, Context applicationContext) {
        super(layoutInflater);
        this.audioMechanism = (AudioMechanism) mechanism;
        this.resources = resources;
        this.activity = activity;
        this.applicationContext = applicationContext;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.audio_feedback_layout, null));
        initView();
    }

    private void initView() {
        ((TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_title)).setText(audioMechanism.getTitle());
        pauseButton = (ImageButton) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_pause);
        setButtonEnabled(pauseButton, false);
        playButton = (ImageButton) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_play);
        setButtonEnabled(playButton, false);
        recordAnimationColorStart = resources.getColor(R.color.supersede_feedbacklibrary_audio_timer_record_indicator_start_animation_color);
        recordAnimationColorEnd = resources.getColor(R.color.supersede_feedbacklibrary_audio_timer_record_indicator_end_animation_color);
        recordButton = (ImageButton) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_record);
        recordIndicator = (TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_timer_record_indicator);
        stopButton = (ImageButton) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_stop);
        setButtonEnabled(stopButton, false);

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausePlaying();
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPaused && !isPlaying && !isRecording) {
                    startPlaying();
                }
                if (isPaused && !isPlaying && !isRecording && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    resumePlaying();
                }
            }
        });
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = Utils.checkSinglePermission(activity, FeedbackActivity.PERMISSIONS_REQUEST_RECORD_AUDIO, Manifest.permission.RECORD_AUDIO, null, null, false);
                if (result) {
                    // Output file
                    File audioDirectory = applicationContext.getDir(Utils.AUDIO_DIR, Context.MODE_PRIVATE);
                    tempAudioFilePath = audioDirectory.getAbsolutePath() + "/" + audioMechanism.getId() + Utils.AUDIO_FILENAME + "." + Utils.AUDIO_EXTENSION;

                    // Setup recorder
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                        @Override
                        public void onInfo(MediaRecorder mr, int what, int extra) {
                            if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                                if (!isPlaying && isRecording) {
                                    onRecordSuccess();
                                    Toast toast = Toast.makeText(applicationContext, "Recording stopped because the limit of " + (int) audioMechanism.getMaxTime() + " reached", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        }
                    });
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mediaRecorder.setOutputFile(tempAudioFilePath);
                    if (((int) audioMechanism.getMaxTime()) >= 1) {
                        mediaRecorder.setMaxDuration(((int) audioMechanism.getMaxTime()) * 1000);
                    } else {
                        mediaRecorder.setMaxDuration(1000);
                    }
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    try {
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        System.out.println("prepare() failed");
                    }
                    mediaRecorder.start();

                    recordIndicator.setVisibility(View.VISIBLE);
                    // Record animation
                    if (recordIndicatorAnimator == null) {
                        recordIndicatorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), recordAnimationColorStart, recordAnimationColorEnd);
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

                    isRecording = true;
                    setButtonEnabled(playButton, false);
                    setButtonEnabled(recordButton, false);
                    setButtonEnabled(stopButton, true);
                }
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying && isRecording) {
                    onRecordSuccess();
                    Toast toast = Toast.makeText(applicationContext, "Stopped recording", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    stopPlaying();
                }
            }
        });
    }

    private void onRecordSuccess() {
        stopRecordAnimation();
        recordIndicator.setVisibility(View.INVISIBLE);
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        audioFilePath = tempAudioFilePath;
        isPaused = false;
        isPlaying = false;
        isRecording = false;
        setButtonEnabled(pauseButton, false);
        setButtonEnabled(playButton, true);
        setButtonEnabled(recordButton, true);
        setButtonEnabled(stopButton, false);
    }

    private void pausePlaying() {
        if (!isPaused && isPlaying && !isRecording && mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPaused = true;
            isPlaying = false;
            setButtonEnabled(pauseButton, false);
            setButtonEnabled(playButton, true);
        }
    }

    private void resumePlaying() {
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
        mediaPlayer.start();
        isPaused = false;
        isPlaying = true;
        setButtonEnabled(pauseButton, true);
        setButtonEnabled(playButton, false);
    }

    private void setButtonEnabled(ImageButton imageButton, boolean enabled) {
        if (imageButton != null) {
            imageButton.setEnabled(enabled);
            imageButton.setAlpha(enabled ? 1.0F : 0.4F);
        }
    }

    private void startPlaying() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopRecordAnimation();
                    isPlaying = false;
                    setButtonEnabled(pauseButton, false);
                    setButtonEnabled(playButton, true);
                    setButtonEnabled(recordButton, true);
                    setButtonEnabled(stopButton, false);
                }
            });
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPaused = false;
            isPlaying = true;
            isRecording = false;
            setButtonEnabled(pauseButton, true);
            setButtonEnabled(playButton, false);
            setButtonEnabled(recordButton, false);
            setButtonEnabled(stopButton, true);
        } catch (IOException e) {
            System.out.println("prepare() failed");
        }
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isPaused = false;
        isPlaying = false;
        isRecording = false;
        setButtonEnabled(pauseButton, false);
        setButtonEnabled(playButton, true);
        setButtonEnabled(recordButton, true);
        setButtonEnabled(stopButton, false);
    }

    private void stopRecordAnimation() {
        if (recordIndicatorAnimator != null && recordIndicatorAnimator.isRunning()) {
            recordIndicatorAnimator.cancel();
            recordIndicator.setTextColor(recordAnimationColorStart);
        }
    }

    @Override
    public void updateModel() {
        audioMechanism.setAudioPath(audioFilePath);
    }
}
