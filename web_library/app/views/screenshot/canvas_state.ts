export class CanvasState {
    src:any;
    width:number;
    height:number;
    shiftTop:number;
    shiftLeft:number;
    zoomFactor:number;

    constructor(src:any, width:number, height:number, shiftTop:number, shiftLeft:number, zoomFactor:number) {
        this.src = src;
        this.width = width;
        this.height = height;
        this.shiftTop = shiftTop;
        this.shiftLeft = shiftLeft;
        this.zoomFactor = zoomFactor;
    }
}



















