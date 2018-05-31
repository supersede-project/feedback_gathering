package ch.uzh.supersede.feedbacklibrary.components.views;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.models.AbstractFeedbackPart;
import ch.uzh.supersede.feedbacklibrary.models.AudioFeedback;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ModelsConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.PATH_DELIMITER;

public class AudioMechanismView extends AbstractFeedbackPartView implements SeekBar.OnSeekBarChangeListener {
    private int recordAnimationColorStart;
    private int recordAnimationColorEnd;
    private String audioFilePath;
    private AudioFeedback audioFeedback;
    private Activity activity;
    private Context applicationContext;
    private boolean isPaused = false;
    private boolean isPlaying = false;
    private boolean isRecording = false;
    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;
    private ImageView playButton;
    private ImageView recordButton;
    private ImageView clearButton;
    private TextView recordIndicator;
    private ValueAnimator recordIndicatorAnimator;
    private Resources resources;
    private ImageView stopButton;
    private String tempAudioFilePath;
    private int totalDuration;

    private TextView totalDurationLabel;
    private Handler handler;
    private Handler handlerRecorder;
    private SeekBar seekBar;
    private Runnable updateSeekBarTask;
    private Runnable updateSeekBarTaskRecorder;
    private long currentRecordDuration = 1L;

    private MultipleAudioMechanismsListener multipleAudioMechanismsListener;

    public AudioMechanismView(LayoutInflater layoutInflater, AbstractFeedbackPart mechanism, Resources resources, Activity activity, Context applicationContext) {
        super(layoutInflater);
        this.viewOrder = mechanism.getOrder();
        this.audioFeedback = (AudioFeedback) mechanism;
        this.resources = resources;
        this.activity = activity;
        this.multipleAudioMechanismsListener = (MultipleAudioMechanismsListener) activity;
        this.applicationContext = applicationContext;
        setEnclosingLayout(getLayoutInflater().inflate(ch.uzh.supersede.feedbacklibrary.R.layout.mechanism_audio, null));
        initView();
        handler = new Handler();
        updateSeekBarTask = new Runnable() {
            public void run() {
                long mediaPlayerDuration = mediaPlayer.getDuration();
                long currentDuration = mediaPlayer.getCurrentPosition();

                // Displaying time completed playing / total duration time
                String toDisplay = milliSecondsToTimer(currentDuration) + PATH_DELIMITER + milliSecondsToTimer(mediaPlayerDuration);
                totalDurationLabel.setText(toDisplay);

                // Updating progress bar
                int progress = getProgressPercentage(currentDuration, mediaPlayerDuration);
                seekBar.setProgress(progress);

                handler.postDelayed(this, 100);
            }
        };
        handlerRecorder = new Handler();
        updateSeekBarTaskRecorder = new Runnable() {
            public void run() {
                long audioMechanismTotalDuration = ((long) audioFeedback.getMaxTime()) * 1000;
                // Displaying time completed playing / total duration time
                String toDisplay = milliSecondsToTimer(currentRecordDuration * 1000) + PATH_DELIMITER + milliSecondsToTimer(audioMechanismTotalDuration);
                totalDurationLabel.setText(toDisplay);

                // Updating progress bar
                int progress = getProgressPercentage(currentRecordDuration * 1000, audioMechanismTotalDuration);
                seekBar.setProgress(progress);

                ++currentRecordDuration;
                handlerRecorder.postDelayed(this, 1000);
            }
        };
    }

    private void addUpdateSeekBarTask() {
        handler.postDelayed(updateSeekBarTask, 100);
    }

    private void addUpdateSeekBarTaskRecorder() {
        handlerRecorder.postDelayed(updateSeekBarTaskRecorder, 1000);
    }

    /**
     * This method returns the id of the audio mechanism represented by the view.
     *
     * @return the audio mechanism id
     */
    public long getAudioMechanismId() {
        return audioFeedback.getMechanismId();
    }

    private int getProgressPercentage(double currentDuration, double totalDuration) {
        if (totalDuration > 0) {
            return (int) (currentDuration / totalDuration * 100);
        }
        return 0;
    }

    private String getDefaultTotalDurationLabel() {
        return "-/" + milliSecondsToTimer(((long) audioFeedback.getMaxTime()) * 1000);
    }

    private void initView() {
        ((TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_title)).setText(R.string.audio_hint);
        recordAnimationColorStart = resources.getColor(R.color.black);
        recordAnimationColorEnd = resources.getColor(R.color.accent);
        recordIndicator = (TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_timer_record_indicator);
        seekBar = (SeekBar) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_seekbar);

        final String startTotalDurationLabel = getDefaultTotalDurationLabel();
        totalDurationLabel = (TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_timer_total_duration);
        totalDurationLabel.setText(startTotalDurationLabel);

        initClearButton();
        initPlayButton();
        initRecordButton();
        initStopButton();
    }

    private void initStopButton() {
        stopButton = (ImageView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_stop);
        setButtonEnabled(stopButton, false);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();

                if (!isPlaying && isRecording) {
                    onRecordSuccess();
                    Toast toast = Toast.makeText(applicationContext, applicationContext
                            .getResources()
                            .getString(R.string.audio_stopped_recording_text), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    stopPlaying();
                }

                playButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);
            }
        });
    }

    private void initRecordButton() {
        recordButton = (ImageView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_record);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();

                removeUpdateSeekBarTask();
                removeUpdateSeekBarTaskRecorder();

                // Output file
                File audioFile = applicationContext.getDir(AUDIO_DIR, Context.MODE_PRIVATE);
                tempAudioFilePath = audioFile.getAbsolutePath() + PATH_DELIMITER + audioFeedback.getMechanismId() + AUDIO_FILENAME + "." + AUDIO_EXTENSION;

                initMediaRecorder();
                initRecordIndicatorAnimator();

                isRecording = true;
                setButtonEnabled(playButton, false);
                setButtonEnabled(recordButton, false);
                setButtonEnabled(stopButton, true);
                setButtonEnabled(clearButton, true);

                multipleAudioMechanismsListener.onRecordStart(audioFeedback.getMechanismId());

                seekBar.setOnSeekBarChangeListener(null);
                seekBar.setEnabled(false);
                seekBar.setProgress(0);
                seekBar.setMax(100);
                currentRecordDuration = 1L;
                addUpdateSeekBarTaskRecorder();
            }
        });
    }

    private void initMediaRecorder() {
        // Setup recorder
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mediaRecorder, int waringType, int extra) {
                if ((waringType == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) && (!isPlaying && isRecording)) {
                    onRecordSuccess();
                    Toast toast = Toast.makeText(applicationContext, applicationContext
                            .getResources()
                            .getString(R.string.audio_maximum_length_reached_text, audioFeedback.getMaxTime()), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(tempAudioFilePath);
        if (audioFeedback.getMaxTime() >= 1) {
            mediaRecorder.setMaxDuration(((int) audioFeedback.getMaxTime()) * 1000);
        } else {
            mediaRecorder.setMaxDuration(1000);
        }
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(AUDIO_MECHANISM_VIEW_TAG, "prepare() failed");
        }
        mediaRecorder.start();
    }

    private void initRecordIndicatorAnimator() {
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
    }

    private void initPlayButton() {
        playButton = (ImageView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_play);
        setButtonEnabled(playButton, false);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();

                if (!isPaused && !isPlaying && !isRecording) {
                    startPlaying();
                    playButton.setImageResource(R.drawable.ic_pause_black_48dp);
                } else if (isPaused && !isPlaying && !isRecording && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    resumePlaying();

                } else if (!isPaused && isPlaying && mediaPlayer != null) {
                    pausePlaying();
                    playButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);
                }
            }
        });
    }

    private void initClearButton() {
        clearButton = (ImageView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_audio_player_button_clear);
        setButtonEnabled(clearButton, false);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFocus();

                stopRecordAnimation();

                removeUpdateSeekBarTaskRecorder();
                removeUpdateSeekBarTask();

                tempAudioFilePath = null;
                audioFilePath = null;

                currentRecordDuration = 1L;
                recordIndicator.setVisibility(View.INVISIBLE);

                if (mediaRecorder != null) {
                    mediaRecorder.stop();
                    mediaRecorder.reset();
                    mediaRecorder = null;
                }

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer = null;
                }

                isPaused = false;
                isPlaying = false;
                isRecording = false;

                setButtonEnabled(clearButton, false);
                setButtonEnabled(playButton, false);
                setButtonEnabled(stopButton, false);
                setButtonEnabled(recordButton, true);

                seekBar.setProgress(0);
                totalDurationLabel.setText(getDefaultTotalDurationLabel());
                playButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);
            }
        });
    }

    private void clearFocus() {
        if (activity.getCurrentFocus() != null) {
            activity
                    .getCurrentFocus()
                    .clearFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity
                        .getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
    }

    private String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString;

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if necessary
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        return finalTimerString + minutes + ":" + secondsString;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //empty
    }

    private void onRecordSuccess() {
        stopRecordAnimation();
        removeUpdateSeekBarTaskRecorder();
        currentRecordDuration = 1L;
        recordIndicator.setVisibility(View.INVISIBLE);
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        audioFilePath = tempAudioFilePath;
        isPaused = false;
        isPlaying = false;
        isRecording = false;
        setButtonEnabled(playButton, true);
        setButtonEnabled(recordButton, true);
        setButtonEnabled(stopButton, false);

        // Release the old media player
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        // Prepare the new media player
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlaying = false;
                    setButtonEnabled(playButton, true);
                    setButtonEnabled(recordButton, true);
                    setButtonEnabled(stopButton, false);
                    removeUpdateSeekBarTask();
                    resetToStartState();
                    addUpdateSeekBarTask();
                    playButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);
                }
            });
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            totalDuration = mediaPlayer.getDuration();

            multipleAudioMechanismsListener.onRecordStop();

            seekBar.setEnabled(true);
            seekBar.setProgress(0);
            seekBar.setMax(100);
            // Updating progress bar
            addUpdateSeekBarTask();
            seekBar.setOnSeekBarChangeListener(this);
        } catch (IOException e) {
            Log.e(AUDIO_MECHANISM_VIEW_TAG, "prepare() failed");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        removeUpdateSeekBarTask();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        removeUpdateSeekBarTask();
        int mediaPlayerDuration = mediaPlayer.getDuration();
        int currentPosition = progressToTimer(seekBar.getProgress(), mediaPlayerDuration);

        // Forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // Update progress bar again
        addUpdateSeekBarTask();
    }

    private void pausePlaying() {
        mediaPlayer.pause();
        isPaused = true;
        isPlaying = false;
    }

    private int progressToTimer(int progress, int totalDuration) {
        int currentDuration;
        totalDuration = (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // Return current duration in milliseconds
        return currentDuration * 1000;
    }

    private void removeUpdateSeekBarTask() {
        handler.removeCallbacks(updateSeekBarTask);
    }

    private void removeUpdateSeekBarTaskRecorder() {
        handlerRecorder.removeCallbacks(updateSeekBarTaskRecorder);
    }

    private void resetToStartState() {
        mediaPlayer.seekTo(0);
        seekBar.setProgress(0);
    }

    private void resumePlaying() {
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
        mediaPlayer.start();
        isPaused = false;
        isPlaying = true;
    }

    /**
     * This method enables/disables all button clicks.
     *
     * @param clickable true to set all buttons clickable, false otherwise
     */
    public void setAllButtonsClickable(boolean clickable) {
        playButton.setClickable(clickable);
        recordButton.setClickable(clickable);
        stopButton.setClickable(clickable);
        seekBar.setEnabled(clickable);
        clearButton.setEnabled(clickable);
    }

    private void setButtonEnabled(ImageView imageButton, boolean enabled) {
        if (imageButton != null) {
            imageButton.setEnabled(enabled);
            imageButton.setAlpha(enabled ? 1.0F : 0.4F);
        }
    }

    private void startPlaying() {
        mediaPlayer.start();
        isPaused = false;
        isPlaying = true;
        isRecording = false;
        setButtonEnabled(recordButton, false);
        setButtonEnabled(stopButton, true);
        setButtonEnabled(clearButton, true);
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            resetToStartState();
        }

        isPaused = false;
        isPlaying = false;
        isRecording = false;
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
        audioFeedback.setAudioPath(audioFilePath);
        audioFeedback.setDuration(totalDuration);
    }


    public interface MultipleAudioMechanismsListener {
        void onRecordStart(long audioMechanismId);

        void onRecordStop();
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof AbstractFeedbackPartView){
            int comparedViewOrder = ((AbstractFeedbackPartView) o).getViewOrder();
            return comparedViewOrder > getViewOrder() ? -1 : comparedViewOrder == getViewOrder() ? 0 : 1;
        }
        return 0;
    }
}
