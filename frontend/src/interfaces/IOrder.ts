export interface IOrder {
    id: number,
    buyerId: number,
    shipToAddress: string,
    billingAddress: string,
    dateOrdered: string,
    status: string   
}