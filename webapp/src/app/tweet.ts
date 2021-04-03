import { Reply } from "./reply";

export interface Tweet {
    id : any;
    avatar : string;
    userName: string;
    time: string;
    tweet: string;
    replies : Reply[];
}