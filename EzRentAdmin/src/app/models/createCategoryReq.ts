import { Category } from './category';

export class CreateCategoryReq {
    username: string;
    password: string;
    newCategoryEntity: Category;

    constructor(username: string, password: string, newCategoryEntity: Category) {
        this.username = username;
        this.password = password;
        this.newCategoryEntity = newCategoryEntity;
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

}