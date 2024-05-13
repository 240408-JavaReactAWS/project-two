import axios from 'axios'
import React, { useEffect, useState, useContext } from 'react'
import { IOrder } from '../../interfaces/IOrder'
import { IOrderItem } from '../../interfaces/IOrderItem'
import { UserContext } from '../../App'
import { IItem } from '../../interfaces/IItem'
import { Card } from 'react-bootstrap'


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
        console.log(props)

        let getItems = () => {
            let orderItems: IOrderItemDisplay[] = []
            props.orderItems.forEach((orderItem) => {
                orderItems.push({item: orderItem.item, quantity: orderItem.quantity})
            })
            setOrderedItems(orderItems)
        }

        getItems()
        totalUp()
    },[])

    return (
        <div className="container">
            <div className="row align-items-start" >
                <div className='col '>
                    <Card >
                        <Card.Body >
                            <Card.Title>Buyer's Info:</Card.Title>
                            <Card.Text>Shipping Address: {props.shipToAddress}</Card.Text>
                            <Card.Text>Billing Address: {props.billAddress}</Card.Text>
                            <Card.Text>Order Status: {props.status}</Card.Text>
                            <Card.Text>Date Ordered: {props.dateOrdered}</Card.Text>
                            <Card.Text>Total Price: {total}</Card.Text>
                        </Card.Body>
                    </Card>
                </div>
                <div className='col'>
                    <Card >
                        <Card.Body>
                            <Card.Title>Items:</Card.Title>
                            <ul className='list-group'>
                                {orderedItems.map((orderedItem) => {
                                    return (
                                        <li className="list-group-item" key={orderedItem.item.itemId}>
                                            <Card.Text>{orderedItem.item.name}</Card.Text>
                                            <Card.Text>Quantity: {orderedItem.quantity}</Card.Text>
                                            <Card.Text>Price: {orderedItem.item.price * orderedItem.quantity}</Card.Text>
                                        </li>
                                    )
                                })}
                            </ul>
                        </Card.Body>
                    </Card>
                </div>
            </div>
        </div>
    )
}

export default OrderCard
