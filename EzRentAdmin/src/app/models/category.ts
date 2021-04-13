export class Category {
    
    categoryId: number | undefined;
    categoryName: string;
    parentCategory: Category | undefined;
    subCategories: Category[] | undefined;

    constructor(categoryName: string) {
        this.categoryName = categoryName;
        this.subCategories = [];
    }

    getCategoryName(): string {
        return this.categoryName;
    }

    setCategoryName(categoryName: string): void {
        this.categoryName = categoryName;
    }

    getCategoryId(): number | undefined {
        return this.categoryId;
    }

    getParentCategory(): Category | undefined {
        return this.parentCategory;
    }

    setParentCategory(parentCategory: Category): void {
        this.parentCategory = parentCategory;
    }

    getSubCategories(): Category[] | undefined {
        return this.subCategories;
    }

    setSubCategories(subCategories: Category[]): void {
        this.subCategories = subCategories;
    }
}