import { Tag } from './tag';
export class CreateTagReq {
    username: string;
    password: string;
    newTag: Tag;

    constructor(username: string, password: string, newTag: Tag) {
        this.username = username;
        this.password = password;
        this.newTag = newTag;
    }

    getUsername(): string {
        return this.username;
    }

    getPassword(): string {
        return this.password;
    }

    getNewTagEntity(): Tag {
        return this.newTag;
    }
}