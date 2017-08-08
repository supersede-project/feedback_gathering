import {AudioMechanism} from '../../models/mechanisms/audio_mechanism';
import {MechanismView} from '../mechanism_view';


export class AudioView implements MechanismView {
    audioMechanism:AudioMechanism;
    container:JQuery;
    distPath:string;
    recordButton:JQuery;
    stopButton:JQuery;
    deleteButton:JQuery;
    replayButton:JQuery;
    stopReplayButton:JQuery;
    audioElement:JQuery;
    recordStart:Date;
    recordInterval:any;
    replayStart:Date;
    replayInterval:any;
    recordTime:number;

    constructor(audioMechanism:AudioMechanism, container:JQuery, distPath:string) {
        this.audioMechanism = audioMechanism;
        this.container = container;
        this.distPath = distPath;
        this.initElements();
        this.initEvents()
    }

    initElements() {
        this.recordButton = this.container.find('a.record');
        this.stopButton = this.container.find('a.stop');
        this.deleteButton = this.container.find('a.delete');
        this.replayButton = this.container.find('a.replay');
        this.stopReplayButton = this.container.find('a.stop-replay');
        this.audioElement = this.container.find('audio.audio');

        this.stopButton.hide();
        this.deleteButton.hide();
        this.replayButton.hide();
        this.stopReplayButton.hide();
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
            myThis.recordStart = new Date();
            myThis.recordInterval = setInterval(function() {
                let seconds:number = (new Date().getTime() - myThis.recordStart.getTime()) / 1000;
                seconds = Math.abs(seconds);
                myThis.recordTime = seconds;
                myThis.container.find('.audio-time').empty().text(myThis.toMMSS(seconds));
            }, 1000);

            myThis.stopButton.show();
            myThis.container.addClass('dirty');
        });

        this.stopButton.on('click', function() {
            Fr.voice.pause();
            Fr.voice.export(function (url) {
                myThis.audioElement.attr("src", url);
            }, "URL");
            myThis.replayButton.show();
            myThis.deleteButton.show();
            myThis.stopButton.hide();

            clearInterval(myThis.recordInterval);
        });

        this.deleteButton.on('click', function() {
            myThis.audioElement.attr("src", null);
            myThis.restore();
            myThis.reset();
        });

        this.replayButton.on('click', function() {
            Fr.voice.export(function (url) {
                myThis.audioElement.attr("src", url);
                myThis.audioElement[0].play();
            }, "URL");

            myThis.stopReplayButton.show();

            myThis.replayStart = new Date();
            myThis.replayInterval = setInterval(function() {
                let seconds:number = (new Date().getTime() - myThis.replayStart.getTime()) / 1000;
                seconds = Math.abs(seconds);

                // make sure the audio element has started to replay before checking whether it's paused
                if(seconds > 1 && myThis.audioElement[0].paused) {
                    clearInterval(myThis.replayInterval);
                    myThis.stopReplayButton.hide();
                }
                if(seconds <= myThis.recordTime) {
                    myThis.container.find('.replay-time').empty().text(myThis.toMMSS(seconds) + " / ");
                }
            }, 1000);
        });

        this.stopReplayButton.on('click', function() {
            myThis.audioElement[0].stop();
            clearInterval(myThis.replayInterval);
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

    reset() {
        clearInterval(this.recordInterval);
        clearInterval(this.replayInterval);
        this.stopButton.hide();
        this.deleteButton.hide();
        this.replayButton.hide();
        this.stopReplayButton.hide();
        this.container.find('.replay-time').empty();
        this.container.find('.audio-time').empty();

        this.container.removeClass('dirty');
    }

    toMMSS(totalSeconds:number) {
        let hours   = Math.floor(totalSeconds / 3600);
        let minutes = Math.floor((totalSeconds - (hours * 3600)) / 60);
        let seconds = totalSeconds - (hours * 3600) - (minutes * 60);

        if (hours   < 10) {hours   = "0"+hours;}
        if (minutes < 10) {minutes = "0"+minutes;}
        if (seconds < 10) {seconds = "0"+seconds;}
        return minutes + ':' + seconds.substring(0, 2);
    }
}












