import {AudioMechanism} from '../../models/mechanisms/audio_mechanism';


export class AudioView {
    audioMechanism:AudioMechanism;
    container:JQuery;
    distPath:string;
    recordButton:JQuery;
    pauseButton:JQuery;
    deleteButton:JQuery;
    replayButton:JQuery;
    audioElement:JQuery;

    constructor(audioMechanism:AudioMechanism, container:JQuery, distPath:string) {
        this.audioMechanism = audioMechanism;
        this.container = container;
        this.distPath = distPath;
        this.initElements();
        this.initEvents()
    }

    initElements() {
        this.recordButton = this.container.find('a.record');
        this.pauseButton = this.container.find('a.pause');
        this.deleteButton = this.container.find('a.delete');
        this.replayButton = this.container.find('a.replay');
        this.audioElement = this.container.find('audio.audio');
    }

    initEvents() {
        var myThis = this;
        this.recordButton.on('click', function() {
            Fr.voice.record(false, function () {
                myThis.recordButton.addClass("disabled");

                var activeSrc = myThis.recordButton.find('img').data('active-src');
                myThis.recordButton.find('img').attr('src', activeSrc);

                myThis.container.find(".one").removeClass("disabled");
            });
        });

        this.pauseButton.on('click', function() {
            if (jQuery(this).hasClass("resume")) {
                // record again
                Fr.voice.resume();
                var defaultSrc = jQuery(this).find('img').data('default-src');
                jQuery(this).find('img').attr('src', defaultSrc);

                jQuery(this).removeClass('resume');
                jQuery(this).attr('title', 'Pause');
            } else {
                // stop recording
                var activeSrc = myThis.recordButton.find('img').data('active-src');
                myThis.recordButton.find('img').attr('src', activeSrc);

                Fr.voice.pause();

                jQuery(this).addClass('resume');
                jQuery(this).attr('title', 'Resume');

                var activeSrc = jQuery(this).find('img').data('active-src');
                jQuery(this).find('img').attr('src', activeSrc);
            }
        });

        this.deleteButton.on('click', function() {
            myThis.restore();
        });

        this.replayButton.on('click', function() {
            Fr.voice.export(function (url) {
                myThis.audioElement.attr("src", url);
                myThis.audioElement[0].play();
            }, "URL");
        });

        this.container.find('a.download').on('click', function() {
           myThis.downloadAudio();
        });
    }

    restore(){
        this.recordButton.removeClass("disabled");
        this.container.find('.one').addClass("disabled");
        Fr.voice.stop();
    }

    getBlob(callback:any) {
        Fr.voice.export(function (blob) {
            callback(blob);
        }, "blob");
    }

    downloadAudio() {
        Fr.voice.export(function (url) {
            jQuery("<a href='" + url + "' download='MyRecording_3.wav'></a>")[0].click();
        }, "URL");
    }
}












