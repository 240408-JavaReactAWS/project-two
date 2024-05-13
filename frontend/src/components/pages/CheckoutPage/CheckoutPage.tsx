import { useContext, useEffect, useState } from "react";
import OrderForm from "../../common/OrderForm";
import axios from "axios";
import { UserContext } from "../../../App";
import { IOrder } from "../../../interfaces/IOrder";
import SummaryRow from "./SummaryRow";
import { useNavigate } from "react-router-dom";

function CheckoutPage() {
    
    const { userId } = useContext(UserContext);
    const [order, setOrder] = useState<IOrder>();
    const navigate = useNavigate();

    useEffect(() => {
      let getOrder = async () => {
        await axios.get(`${process.env.REACT_APP_API_URL}/users/${userId}/orders/current`, {
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
      }
      getOrder();
      if (!userId) {
        alert('Please log in to view this page');
        setTimeout(() => {
            navigate('/login');
        }, 1000);
      }

      if (!order || !order?.orderItems || order?.orderItems.length == 0) {
        alert('No items in cart');
        setTimeout(() => {
            navigate('/');
        }, 1000);
      }
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
      {/* If order null unidentified or null or the cart items is empty e*/}
      {(order?.orderItems && order?.orderItems?.length != 0) ?
      order?.orderItems.map((item, index) => (
        <SummaryRow key={index} item={item}/>
      ))
      :
      null
    }
      <h1>Total: ${total}</h1>
    </div>
  );
}

export default CheckoutPage;