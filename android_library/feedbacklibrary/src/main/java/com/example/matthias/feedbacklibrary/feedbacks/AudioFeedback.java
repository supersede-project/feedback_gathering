package com.example.matthias.feedbacklibrary.feedbacks;

import java.io.Serializable;

/**
 * Created by Matthias on 25.04.2016.
 */
public class AudioFeedback implements Serializable {
    private String title;

    private String audioPath;

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getAudioPath ()
    {
        return audioPath;
    }

    public void setAudioPath (String audioPath)
    {
        this.audioPath = audioPath;
    }
}
