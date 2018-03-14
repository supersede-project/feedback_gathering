import { CanvasObjectModule } from "./canvas_object";


export module ColorPickerModule {

    let defaultColorPalette = [
        ["#000", "#444", "#666", "#999", "#ccc", "#eee", "#f3f3f3", "#fff"],
        ["#f00", "#f90", "#ff0", "#0f0", "#0ff", "#00f", "#90f", "#f0f"],
        ["#f4cccc", "#fce5cd", "#fff2cc", "#d9ead3", "#d0e0e3", "#cfe2f3", "#d9d2e9", "#ead1dc"],
        ["#ea9999", "#f9cb9c", "#ffe599", "#b6d7a8", "#a2c4c9", "#9fc5e8", "#b4a7d6", "#d5a6bd"],
        ["#e06666", "#f6b26b", "#ffd966", "#93c47d", "#76a5af", "#6fa8dc", "#8e7cc3", "#c27ba0"],
        ["#c00", "#e69138", "#f1c232", "#6aa84f", "#45818e", "#3d85c6", "#674ea7", "#a64d79"],
        ["#900", "#b45f06", "#bf9000", "#38761d", "#134f5c", "#0b5394", "#351c75", "#741b47"],
        ["#600", "#783f04", "#7f6000", "#274e13", "#0c343d", "#073763", "#20124d", "#4c1130"]
    ];

     export let colorPickerOptions = function (screenshotView: any, currentObjectColor: any, selectedObject: any) {
        return {
            color: currentObjectColor,
            containerClassName: screenshotView.colorPickerCSSClass,
            showPaletteOnly: true,
            togglePaletteOnly: true,
            togglePaletteMoreText: 'more',
            togglePaletteLessText: 'less',
            palette: defaultColorPalette,
            change: function (color) {
                var color = color.toHexString();
                jQuery(this).css('color', color);

                if (selectedObject.get('customType') === 'arrow') {
                    selectedObject.setFill(color);
                    selectedObject.setStroke(color);
                    if (selectedObject.line !== undefined) {
                        selectedObject.line.setFill(color);
                        selectedObject.line.setStroke(color);
                    }
                    if (selectedObject.arrow !== undefined) {
                        selectedObject.arrow.setFill(color);
                        selectedObject.arrow.setStroke(color);
                    }
                    if (selectedObject.circle !== undefined) {
                        selectedObject.circle.setFill(color);
                        selectedObject.circle.setStroke(color);
                    }
                } else if (selectedObject.get('type') === 'path-group') {
                    for (var path of selectedObject.paths) {
                        if (path.getFill() != "") {
                            path.setFill(color);
                        }
                    }
                } else if (selectedObject.get('type') === 'path' || selectedObject.get('type') === 'fabricObject') {
                    selectedObject.setStroke(color);
                } else if (selectedObject.get('type') === 'fillRect') {
                    selectedObject.setStroke(color);
                    selectedObject.setFill(color);
                } else {
                    selectedObject.setFill(color);
                }
                screenshotView.fabricCanvas.renderAll();
            },
            beforeShow: function (color) {
                jQuery(this).spectrum("option", 'color', currentObjectColor);
            }
        }
    };

    export let colorPickerOptionsFreehand = function (screenshotView: any, currentObjectColor: any) {
        return {
            color: currentObjectColor,
            containerClassName: screenshotView.colorPickerCSSClass,
            showPaletteOnly: true,
            togglePaletteOnly: true,
            togglePaletteMoreText: 'more',
            togglePaletteLessText: 'less',
            palette: defaultColorPalette,
            change: function (color) {
                let colorHex = color.toHexString();
                jQuery(this).css('color', colorHex);
                screenshotView.fabricCanvas.freeDrawingBrush.color = colorHex;
                screenshotView.fabricCanvas.renderAll();
            }
        }
    };

    export let colorPickerOptionsForControl = function(screenshotView: any, currentObjectColor: any,
                                                       selectedObject: any, selectedObjectControls:any, target:any) {
        return {
            color: currentObjectColor,
            containerClassName: screenshotView.colorPickerCSSClass,
            showPaletteOnly: true,
            togglePaletteOnly: true,
            togglePaletteMoreText: 'more',
            togglePaletteLessText: 'less',
            palette: defaultColorPalette,
            change: function (color) {
                let hexColor = color.toHexString();
                jQuery(this).css('color', hexColor);

                if (selectedObject.get('customType') === 'arrow') {
                    selectedObject.setFill(hexColor);
                    selectedObject.setStroke(hexColor);
                    if(selectedObject.line !== undefined) {
                        selectedObject.line.setFill(hexColor);
                        selectedObject.line.setStroke(hexColor);
                    }
                    if(selectedObject.arrow !== undefined) {
                        selectedObject.arrow.setFill(hexColor);
                        selectedObject.arrow.setStroke(hexColor);
                    }
                    if(selectedObject.circle !== undefined) {
                        selectedObject.circle.setFill(hexColor);
                        selectedObject.circle.setStroke(hexColor);
                    }
                } else if (selectedObject.get('type') === 'path-group') {
                    for (let path of selectedObject.paths) {
                        if (path.getFill() != "") {
                            path.setFill(hexColor);
                        }
                    }
                } else if (selectedObject.get('type') === 'path' || selectedObject.get('type') === 'fabricObject') {
                    selectedObject.setStroke(hexColor);
                } else if (selectedObject.get('type') === 'fillRect') {
                    selectedObject.setStroke(hexColor);
                    selectedObject.setFill(hexColor);
                } else {
                    selectedObject.setFill(hexColor);
                }

                selectedObjectControls.find('a.color').css('color', hexColor);
                screenshotView.fabricCanvas.renderAll();
                jQuery(this).remove();
            },
            beforeShow: function (color) {
                jQuery(this).spectrum("option", 'color', CanvasObjectModule.getObjectColor(target));
            }
        }
    };
}