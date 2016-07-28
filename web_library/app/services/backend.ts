export interface Backend {
    list(): any;
    retrieve(id: number): any;
    create(object: any): any;
    update(id: number, attributes: {}): any;
    destroy(id: number): any;
}