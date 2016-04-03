export class Parameter {
    key:string;
    value:string;
    defaultValue:string;
    editableByUSer:boolean;

    constructor(key:string, value:string, defaultValue:string, editableByUSer:boolean) {
        this.key = key;
        this.value = value;
        this.defaultValue = defaultValue;
        this.editableByUSer = editableByUSer;
    }
}