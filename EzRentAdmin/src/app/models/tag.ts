export class Tag {
    tagId: number | undefined;
    tagName: string | undefined;



    constructor(tagId?: number, tagName?: string) {
        this.tagId = tagId;
        this.tagName = tagName;
    }
}
