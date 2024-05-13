import { useContext, useEffect, useState } from "react";
import OrderForm from "../../common/OrderForm";
import axios from "axios";
import { UserContext } from "../../../App";
import { IOrder } from "../../../interfaces/IOrder";
import SummaryRow from "./SummaryRow";

function CheckoutPage() {
    
    const { userId } = useContext(UserContext);
    const [order, setOrder] = useState<IOrder>();

    useEffect(() => {
        axios.get(`${process.env.REACT_APP_API_URL}/users/${userId}/orders/current`, {
            withCredentials: true,
            headers: {
                'Content-Type': 'application/json',
                'userId': userId
            }
        })
        .then(response => {
            setOrder(response.data);
        })
        .catch(error => {
            console.log(error);
        })
    },[]);
  
    let setTotal = (order: IOrder) => {
      let total = 0;
      order.orderItems.forEach((orderItem) => {
          let subtotal: number = orderItem.item.price * orderItem.quantity;
          total += subtotal;
      });
      return total;
    }

    const total = order ? setTotal(order) : 0;
    return (
    <div>
      <h1>Checkout</h1>
      <OrderForm />
      <h1>Order Summary</h1>
      {order?.orderItems.map((item, index) => (
        <SummaryRow key={index} item={item}/>
      ))}
      <h1>Total: ${total}</h1>
    </div>
  );
}

export default CheckoutPage;