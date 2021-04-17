import { Category } from './category';

export class CreateCategoryWithParentReq {
    username: string;
    password: string;
    newCategoryEntity: Category;
    parentCategoryId: number | undefined;

    constructor(username: string, password: string, newCategoryEntity: Category, parentCategoryId?: number) {
        this.username = username;
        this.password = password;
        this.newCategoryEntity = newCategoryEntity;
        this.parentCategoryId = parentCategoryId;
    }

    getUsername(): string {
        return this.username;
    }

    getPassword(): string {
        return this.password;
    }

    getNewCategoryEntity(): Category {
        return this.newCategoryEntity;
    }

    getParentCategoryId(): number | undefined {
        return this.parentCategoryId;
    }
}