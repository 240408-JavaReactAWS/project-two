import { IOrderItem } from "./IOrderItem";

export interface IOrder {
    id: number,
    userId: number,
    status: string, 
    shipToAddress: string,
    billAddress: string,
    dateOrdered: string,
    orderItemsList: IOrderItem[]    
}