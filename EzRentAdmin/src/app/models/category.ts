export class Category {
    categoryId: number | undefined;
    categoryName: string | undefined;



    constructor(categoryId?: number, categoryName?: string) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}