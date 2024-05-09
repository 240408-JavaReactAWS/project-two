import { IItem } from "./IItem";

export interface IOrderItem {
    orderItemId: number,
    orderId: number,
    item: IItem,
    quantity: number
}