export class Tag {
    tagId: number | undefined;
    tagName: string;

    constructor(tagName: string) {
        this.tagName = tagName;
    }

    getTagId(): number | undefined {
        return this.tagId;
    }

    getTagName(): string {
        return this.tagName;
    }

}
