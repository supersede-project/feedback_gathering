import '../../js/lib/screenshot/canvg.js';


export module CanvasObjectModule {

    export let getObjectColor = function(object:any):string {
        let currentObjectColor;
        if (object.get('type') === 'path-group') {
            for (let path of object.paths) {
                if (path.getFill() != "") {
                    currentObjectColor = path.getFill();
                    break;
                }
            }
        } else if (object.get('type') === 'path' || object.get('type') === 'fabricObject') {
            currentObjectColor = object.getStroke();
        } else if (object.get('type') === 'fillRect') {
            currentObjectColor = object.getFill();
        } else {
            currentObjectColor = object.getFill();
        }

        return currentObjectColor;
    };

    export let setCanvasObjectsMovement = function(lock:boolean, fabricCanvas, cropperTypeObjectIdentifier:string) {
        let objects = fabricCanvas.getObjects();

        for (let i = 0; i < objects.length; i++) {
            if(fabricCanvas.getObjects()[i].get('type') !== cropperTypeObjectIdentifier) {
                fabricCanvas.getObjects()[i].lockMovementX = lock;
                fabricCanvas.getObjects()[i].lockMovementX = lock;
            }
        }
    };

    export let getCircleOptions = function(offsetX:number, offsetY:number, screenshotView, defaultColor:string) {
        return {
            left: offsetX,
            top: offsetY,
            radius: 50,
            hasBorders: screenshotView.hasBordersAndControls,
            hasControls: screenshotView.hasBordersAndControls,
            startAngle: 0,
            type: 'fabricObject',
            endAngle: 2 * Math.PI,
            stroke: defaultColor,
            strokeWidth: screenshotView.defaultStrokeWidth,
            fill: 'transparent'
        }
    };

    /**
     *
     * @param {number} offsetX
     * @param {number} offsetY
     * @param screenshotView
     * @param {string} defaultColor
     * @param {string} fill could be a color or transparent
     * @param {string} type could be fabricObject or fillRect
     * @returns {{left: number; top: number; width: number; height: number; hasBorders: any; hasControls: any; type: string; stroke: string; strokeWidth: number; lockUniScaling: boolean; fill: string}}
     */
    export let getRectOptions = function(offsetX:number, offsetY:number, screenshotView, defaultColor:string, fill:string, type:string) {
      return {
          left: offsetX,
          top: offsetY,
          width: 50,
          height: 50,
          hasBorders: screenshotView.hasBordersAndControls,
          hasControls: screenshotView.hasBordersAndControls,
          type: type,
          stroke: defaultColor,
          strokeWidth: screenshotView.defaultStrokeWidth,
          lockUniScaling: false,
          fill: fill
      }
    };

    export let getCroppingRectOptions = function(cropperTypeObjectIdentifier:string) {
        return {
            fill: 'transparent',
            originX: 'left',
            originY: 'top',
            stroke: '#333',
            strokeDashArray: [4, 4],
            type: cropperTypeObjectIdentifier,
            opacity: 1,
            width: 1,
            height: 1
        }
    };

    /**
     * Converts SVG objects (e.g. from highcharts lib) to a temporary canvas. This enables us to capture also SVG stuff
     * on the screenshot.
     */
    export let svgToCanvas = function(screenshotView:any) {
        let svgElements = screenshotView.elementToCapture.find('svg:not(.jq-star-svg)');

        //replace all svgs with a temp canvas
        svgElements.each(function() {
            let canvas, xml;

            // canvg doesn't cope very well with em font sizes so find the calculated size in pixels and replace it in the element.
            jQuery.each(jQuery(this).find('[style*=em]'), function(index, el) {
                jQuery(this).css('font-size', getStyle(el, 'font-size'));
            });

            canvas = document.createElement("canvas");
            canvas.className = "screenShotTempCanvas";
            //convert SVG into a XML string
            xml = (new XMLSerializer()).serializeToString(this);

            // Removing the name space as IE throws an error
            xml = xml.replace(/xmlns=\"http:\/\/www\.w3\.org\/2000\/svg\"/, '');

            //draw the SVG onto a canvas
            canvg(canvas, xml);
            jQuery(canvas).insertAfter(this);
            //hide the SVG element
            jQuery(this).attr('class', 'temp-hide');
            jQuery(this).hide();
        });
    };

    export let getTemporarySpansCSS = function(select:any) {
        return {
            'position': 'absolute',
            'top': select.offset().top - 30 + 'px',
            'left': select.offset().left + 10 + 'px',
            'width': 'auto',
            'height': '25px',
            'display': 'block',
            'z-index': 9000,
        }
    };

    export let getColorLinkElementCSS = function(defaultColor) {
        return {
            'position': 'absolute',
            'color': defaultColor,
            'width': '16px',
            'height': '16px',
            'opacity': '0',
        }
    };

    export let getCustomiseCornerIconsOptions = function(screenshotView) {
        return {
            settings: {
                borderColor: 'black',
                cornerSize: 24,
                cornerShape: 'rect',
                cornerPadding: 1
            },
            ml: {
                icon: screenshotView.distPath + 'img/ic_delete_black_24px_background.svg'
            },
            mt: {
                icon: screenshotView.distPath + 'img/ic_format_color_fill_black_24px_background.svg'
            }
        }
    };

    let getStyle = function(el, styleProp) {
        let camelize = function (str) {
            return str.replace(/\-(\w)/g, function(str, letter){
                return letter.toUpperCase();
            });
        };

        if (el.currentStyle) {
            return el.currentStyle[camelize(styleProp)];
        } else if (document.defaultView && document.defaultView.getComputedStyle) {
            return document.defaultView.getComputedStyle(el,null)
                .getPropertyValue(styleProp);
        } else {
            return el.style[camelize(styleProp)];
        }
    };
}