import { Tag } from './tag';
export class CreateTagReq {
    username: string;
    password: string;
    newTagEntity: Tag;

    constructor(username: string, password: string, newTagEntity: Tag) {
        this.username = username;
        this.password = password;
        this.newTagEntity = newTagEntity;
    }

}