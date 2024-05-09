import axios from 'axios'
import React, { useEffect } from 'react'
import { IOrder } from '../../interfaces/IOrder'
import { IOrderItem } from '../../interfaces/IOrderItem'

interface IOrderCardProps {
    order: IOrder
    user:string
    orderedItems:IOrderItemDisplay[]
}
interface IOrderItemDisplay {
    id: number,
    quantity: number,
    name: string,
    price: number
}

function OrderCard(props: IOrderCardProps) {
    let total = 0
    for(let i = 0; i < props.orderedItems.length; i++) {
        total += props.orderedItems[i].price * props.orderedItems[i].quantity
    }

  return (
    <div className="container">
        <div className="row align-items-start">
            <div className='col'>
                <p>{props.user}'s Info:</p>
                <p> Shipping Address: {props.order.shipToAddress}</p>
                <p> Billing Address: {props.order.billAddress}</p>
                <p> Order Status: {props.order.status}</p>
                <p> Date Ordered: {props.order.dateOrdered}</p>
                <p>Total Price: {total}</p>
            </div>
            <div className='col'>
                <p>Items:</p>
                <ul className='list-group'>
                    {props.orderedItems.map((item) => {
                    return (
                        <li className="list-group-item" key={item.id}>
                        <p>{item.name}</p>
                        <p>Quantity: {item.quantity}</p>
                        <p>Price: {item.price *item.quantity}</p>
                        </li>
                    )
                    })}
                </ul>
            </div>
        </div>
      
      
    </div>
  )
}

export default OrderCard
