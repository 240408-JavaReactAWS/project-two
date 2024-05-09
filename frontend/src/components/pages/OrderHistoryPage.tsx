import React, {useContext, useEffect, useState} from 'react'
import { useNavigate } from 'react-router-dom'
import { UserContext } from '../../App'
import axios from 'axios'
import OrderCard from '../common/OrderCard'
import { IOrder } from '../../interfaces/IOrder'

interface Props {
    itemid?: number
}


function OrderHistoryPage(props: Props) {
    const context = useContext(UserContext)
    const navigate = useNavigate()

    const [orders, setOrders] = useState([])

    let getRelatedOrders = async () => {
        try {
            let response = await axios.get(`${process.env.REACT_APP_API_URL}/items/${props.itemid}/orders`, 
            {withCredentials: true, 
                headers:{'userId': context.userId}
            })

            setOrders(response.data)
        } catch (error) {
            console.log(error)
        }
    }

    let getUserOrders = async () => {
        try {
            let response = await axios.get(`${process.env.REACT_APP_API_URL}/users/${context.userId}/orders`, 
            {withCredentials: true, 
                headers:{'userId': context.userId}
            })

            setOrders(response.data)
        } catch (error) {
            console.log(error)
        }
    }

    useEffect(() => {
        if (!context.userId) {
            navigate('/login')
        }   
        if (props.itemid) {
            // if itemid exists show orders for that item
            // axios.get(`${process.env.REACT_APP_API_URL}/items/${itemid}/orders`)
            getRelatedOrders()
        }else {

            // axios.get(`${process.env.REACT_APP_API_URL}/users/${context.userId}/orders`)
            // otherwse show orders of currently logged in user
            getUserOrders()
        }
    }, [context.userId, navigate])



return (
    <div>
            <h1>Order History</h1>
            {orders.map((order: IOrder) => (
                    <OrderCard {...order} />
            ))}
            </div>
)
}

export default OrderHistoryPage