export interface JQuery {
    fileUpload(distPath: string, maximumTotalFileSize?: number): JQuery;
}

export var fileUploadPluginModule = (function ($, window, document) {
    let distPath;
    var dropArea;
    let fileInput;
    let fileTable;
    this.maximumTotalFileSize = 0;
    let currentFiles = [];
    var plugin;

    let isAdvancedUpload = function () {
        let div = document.createElement('div');
        return (('draggable' in div) || ('ondragstart' in div && 'ondrop' in div)) && 'FormData' in window && 'FileReader' in window;
    }();

    let addToCurrentFiles = function (files) {
        hideFileSizeErrorMessage();
        let currentTotal = getTotalFileSizeForFiles(currentFiles);
        let filesToAddTotal = getTotalFileSizeForFiles(files);
        if(currentTotal + filesToAddTotal > plugin.maximumTotalFileSize) {
            showFileSizeErrorMessage(filesToAddTotal);
            return;
        }

        if (files.length > 1) {
            for (let i = 0; i < files.length; i++) {
                currentFiles.push(files[i]);
            }
        } else {
            let file = files[0];
            currentFiles.push(file);
        }

        currentTotal = getTotalFileSizeForFiles(currentFiles);
        updateFileSizeDisplay(currentTotal);

        showFiles(currentFiles);
    };

    let getTotalFileSizeForFiles = function(files):number {
        let totalFileSize = 0;
        for (let i = 0; i < files.length; i++) {
            totalFileSize += files[i].size;
        }
        return totalFileSize;
    };

    let showFileSizeErrorMessage = function(fileSize:number) {
        let errorText = 'File size(s) (' + formatBytes(fileSize) + ') too big. In total ' + formatBytes(plugin.maximumTotalFileSize) + ' are aloud.';
        dropArea.find('.error').text(errorText).show();
    };

    let hideFileSizeErrorMessage = function() {
        dropArea.find('.error').empty().hide();
    };

    let updateFileSizeDisplay = function (currentTotalFileSize: number) {
        let currentTotalFileSizeString:string = formatBytes(currentTotalFileSize);
        plugin.parent().find('.current-total-file-size').text(currentTotalFileSizeString);
    };

    let formatBytes = function (bytes, decimals?):string {
        if (0 == bytes) return "0 Bytes";
        let c = 1024, d = decimals || 2, e = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"],
            f = Math.floor(Math.log(bytes) / Math.log(c));
        return parseFloat((bytes / Math.pow(c, f)).toFixed(d)) + " " + e[f]
    };

    let showFiles = function (files) {
        fileTable.empty();
        for (let i = 0; i < files.length; i++) {
            let fileLi = $('<tr>' +
                '<td>' + files[i].name + '</td>' +
                '<td>' + files[i].type + '</td>' +
                '<td>' + formatBytes(files[i].size) + '</td>' +
                '<td><a class="remove-file" data-index="' + i + '" href="#"><img src="' + distPath + 'img/ic_delete_black_24dp_1x.png" /></a></td>' +
                '</tr>');
            fileTable.append(fileLi);
        }
    };

    $.fn.fileUpload = function (distPathString: string, maximumTotalFileSize?: number) {
        if (maximumTotalFileSize === null || maximumTotalFileSize === undefined) {
            maximumTotalFileSize = 0;
        }
        plugin = this;
        plugin.currentFiles = currentFiles;
        plugin.maximumTotalFileSize = maximumTotalFileSize;
        distPath = distPathString;
        dropArea = plugin;
        fileInput = plugin.find('input[type=file]');
        fileTable = plugin.parent('.attachment-type').find('table.current-files');

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

        $(document.body).on('click', 'a.remove-file', function (event) {
            event.stopPropagation();
            event.preventDefault();
            let index = $(plugin).data('index');
            currentFiles.splice(index, 1);
            showFiles(currentFiles);
        });

        fileInput.on('change', function (e) {
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