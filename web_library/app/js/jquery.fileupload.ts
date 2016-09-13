export interface JQuery {
    fileUpload(distPath:string):JQuery;
}

export var fileUploadPluginModule = (function ($, window, document) {
    var distPath;
    var dropArea;
    var fileInput;
    var fileTable;
    var currentFiles = [];

    var isAdvancedUpload = function () {
        var div = document.createElement('div');
        return (('draggable' in div) || ('ondragstart' in div && 'ondrop' in div)) && 'FormData' in window && 'FileReader' in window;
    }();

    var addToCurrentFiles = function(files) {
        if(files.length > 1) {
            for(var i = 0; i < files.length; i++) {
                currentFiles.push(files[i]);
            }
        } else {
            var file = files[0];
            currentFiles.push(file);
        }
        showFiles(currentFiles);
    };

    var showFiles = function(files) {
        fileTable.empty();
        for(var i = 0; i < files.length; i++) {
            var fileLi = $('<tr>' +
                '<td>' + files[i].name + '</td>' +
                '<td>' + files[i].type + '</td>' +
                '<td><a class="remove-file" data-index="' + i + '" href="#"><img src="' + distPath + 'img/ic_delete_black_24dp_1x.png" /></a></td>' +
                '</tr>');
            fileTable.append(fileLi);
        }
    };

    $.fn.fileUpload = function (distPathString:string) {
        distPath = distPathString;
        dropArea = this;
        fileInput = this.find('input[type=file]');
        fileTable = this.parent('.attachment-type').find('table.current-files');

        if (isAdvancedUpload) {
            var droppedFiles = false;

            dropArea.on('drag dragstart dragend dragover dragenter dragleave drop', function (e) {
                e.preventDefault();
                e.stopPropagation();
            }).on('dragover dragenter', function () {
                dropArea.addClass('active');
            }).on('dragleave dragend drop', function () {
                dropArea.removeClass('active');
            }).on('drop', function (e) {
                droppedFiles = e.originalEvent.dataTransfer.files;
                addToCurrentFiles(droppedFiles);
            });
        }

        $(document.body).on('click', 'a.remove-file', function() {
            var index = $(this).data('index');
            currentFiles.splice(index, 1);
            showFiles(currentFiles);
        });

        fileInput.on('change', function(e) {
            addToCurrentFiles(e.target.files);
        });

        return this;
    };
});

(function ($, window, document) {
    fileUploadPluginModule($, window, document);
})(jQuery, window, document);

requirejs.config({
    "shim": {
        "fileUpload": ["jquery"]
    }
});