/**
 * Created by flo on 14.07.16.
 */
import {Directive, ElementRef, Renderer} from 'angular2/core'

@Directive({
    selector: '[autoGrow]',
    host:{
        '(focus)': 'onFocus()',
        '(blur)' : 'onBlur()',
    }
})
export class AutoGrowDirective{
    el: ElementRef;
    renderer: Renderer;

    constructor(el: ElementRef, renderer: Renderer){
        this.el = el;
        this.renderer = renderer;
    }

    onFocus(){
        this.renderer.setElementStyle(this.el, 'width', '200')
    }

    onBlur(){
        this.renderer.setElementStyle(this.el, 'width', '120')
    }
}