import { IUser } from "./IUser"

export interface IItem {
    itemId: number,
    user: IUser,
    name: string,
    description: string,
    price: number,
    stock: number,
    image: string,
    datePosted: string,
    rating: number
}