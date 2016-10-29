define(["require", "exports"], function (require, exports) {
    "use strict";
    var AudioView = (function () {
        function AudioView(audioMechanism, container, distPath) {
            this.audioMechanism = audioMechanism;
            this.container = container;
            this.distPath = distPath;
            this.initElements();
            this.initEvents();
        }
        AudioView.prototype.initElements = function () {
            this.recordButton = this.container.find('a.record');
            this.pauseButton = this.container.find('a.pause');
            this.deleteButton = this.container.find('a.delete');
            this.replayButton = this.container.find('a.replay');
            this.audioElement = this.container.find('audio.audio');
        };
        AudioView.prototype.initEvents = function () {
            var myThis = this;
            this.recordButton.on('click', function () {
                Fr.voice.record(false, function () {
                    myThis.recordButton.addClass("disabled");
                    var activeSrc = myThis.recordButton.find('img').data('active-src');
                    myThis.recordButton.find('img').attr('src', activeSrc);
                    myThis.container.find(".one").removeClass("disabled");
                });
            });
            this.pauseButton.on('click', function () {
                if (jQuery(this).hasClass("resume")) {
                    Fr.voice.resume();
                    var defaultSrc = jQuery(this).find('img').data('default-src');
                    jQuery(this).find('img').attr('src', defaultSrc);
                    jQuery(this).removeClass('resume');
                    jQuery(this).attr('title', 'Pause');
                }
                else {
                    var activeSrc = myThis.recordButton.find('img').data('active-src');
                    myThis.recordButton.find('img').attr('src', activeSrc);
                    Fr.voice.pause();
                    jQuery(this).addClass('resume');
                    jQuery(this).attr('title', 'Resume');
                    var activeSrc = jQuery(this).find('img').data('active-src');
                    jQuery(this).find('img').attr('src', activeSrc);
                }
            });
            this.deleteButton.on('click', function () {
                myThis.restore();
            });
            this.replayButton.on('click', function () {
                Fr.voice.export(function (url) {
                    myThis.audioElement.attr("src", url);
                    myThis.audioElement[0].play();
                }, "URL");
            });
            this.container.find('a.download').on('click', function () {
                myThis.downloadAudio();
            });
        };
        AudioView.prototype.restore = function () {
            this.recordButton.removeClass("disabled");
            this.container.find('.one').addClass("disabled");
            Fr.voice.stop();
        };
        AudioView.prototype.getBlob = function (callback) {
            Fr.voice.export(function (blob) {
                callback(blob);
            }, "blob");
        };
        AudioView.prototype.downloadAudio = function () {
            Fr.voice.export(function (url) {
                jQuery("<a href='" + url + "' download='MyRecording_3.wav'></a>")[0].click();
            }, "URL");
        };
        return AudioView;
    }());
    exports.AudioView = AudioView;
});
//# sourceMappingURL=audio_view.js.map