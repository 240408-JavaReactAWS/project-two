import { IOrderItem } from "./IOrderItem";

export interface IOrder {
    orderId: number,
    userId: number,
    status: string, 
    shipToAddress: string,
    billAddress: string,
    dateOrdered: string,
    orderItemsList: IOrderItem[]    
}