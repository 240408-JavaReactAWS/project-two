import { useContext, useEffect, useState } from "react";
import OrderForm from "../../common/OrderForm";
import axios from "axios";
import { UserContext } from "../../../App";
import { IOrder } from "../../../interfaces/IOrder";
import SummaryRow from "./SummaryRow";

function CheckoutPage() {
    
    const { userId } = useContext(UserContext);
    const [order, setOrder] = useState<IOrder>();
    const [total, setTotal] = useState<number>(0);


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
  
    
    return (
    <div>
      <h1>Checkout</h1>
      <OrderForm />
      <h1>Order Summary</h1>
      {order?.orderItems.map((item, index) => (
        <SummaryRow key={index} item={item} total={total} setTotal={setTotal}/>
      ))}
      <h1>Total: ${total}</h1>
    </div>
  );
}

export default CheckoutPage;