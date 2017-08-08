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
        this.stopButton = this.container.find('a.stop');
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
            myThis.container.addClass('dirty');
        });

        this.stopButton.on('click', function() {
            Fr.voice.pause();
            Fr.voice.export(function (url) {
                myThis.audioElement.attr("src", url);
            }, "URL");
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
        this.container.removeClass('dirty');
    }
}












