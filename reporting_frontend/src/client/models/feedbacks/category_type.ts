export class CategoryType {
    key:string;
    text:string;
    language:string;

    constructor(key:string, text:string, language?:string) {
        this.key = key;
        this.text = text;
        this.language = language;
    }
}
