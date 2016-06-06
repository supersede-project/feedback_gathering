export class Parameter {
    key:string;
    value:any;
    editableByUser:boolean;
    defaultValue:string;

    constructor(key:string, value:any, editableByUser?:boolean, defaultValue?:string) {
        this.key = key;
        this.value = value;
        this.editableByUser = editableByUser;
        this.defaultValue = defaultValue;
    }
}
