import axios from 'axios'
import React, { useEffect, useState, useContext } from 'react'
import { IOrder } from '../../interfaces/IOrder'
import { IOrderItem } from '../../interfaces/IOrderItem'
import { UserContext } from '../../App'
import { IItem } from '../../interfaces/IItem'


interface IOrderItemDisplay {
    item: IItem,
    quantity: number
}

function OrderCard(props: IOrder) {

    let userId = useContext(UserContext).userId
    const [orderedItems, setOrderedItems] = useState<IOrderItemDisplay[]>([])
    const [total, setTotal] = useState(0)
    

    function totalUp() {
        for(let i = 0; i < orderedItems.length; i++) {
           setTotal(orderedItems[i].item.price * orderedItems[i].quantity + total)
        }
    }

    useEffect(() => {
        
        let getItems = () => {
            let orderItems: IOrderItemDisplay[] = []
            props.orderItemsList.forEach((orderItem) => {
                orderItems.push({item: orderItem.item, quantity: orderItem.quantity})
            })
            setOrderedItems(orderItems)
        }

        getItems()
        totalUp()
    },[])

    return (
    <div className="container">
        <div className="row align-items-start">
            <div className='col'>
                <p>Buyer's Info:</p>
                <p> Shipping Address: {props.shipToAddress}</p>
                <p> Billing Address: {props.billAddress}</p>
                <p> Order Status: {props.status}</p>
                <p> Date Ordered: {props.dateOrdered}</p>
                <p>Total Price: {total}</p>
            </div>
            <div className='col'>
                <p>Items:</p>
                <ul className='list-group'>
                    {orderedItems.map((orderedItem) => {
                    return (
                        <li className="list-group-item" key={orderedItem.item.itemId}>
                        <p>{orderedItem.item.name}</p>
                        <p>Quantity: {orderedItem.quantity}</p>
                        <p>Price: {orderedItem.item.price * orderedItem.quantity}</p>
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
