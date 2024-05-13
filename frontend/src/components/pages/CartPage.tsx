import React, {useContext} from 'react'
import { Link, useNavigate } from 'react-router-dom'
import 'bootstrap/dist/css/bootstrap.min.css'; // Import Bootstrap CSS
import 'bootstrap/dist/js/bootstrap.bundle.min'; // Import Bootstrap JS
import ItemCard, { DisplayType } from '../common/ItemCard';
import axios from 'axios';
import { useEffect, useState } from 'react';
import { IItem } from '../../interfaces/IItem';
import './Cart.css';
import { UserContext } from '../../App';
import { IOrder } from '../../interfaces/IOrder';
import { create } from 'domain';
import { IUser } from '../../interfaces/IUser';


function CartPage() {


    let context = useContext(UserContext);
    const [cart, setCart] = useState<IOrder>();
    const navigate = useNavigate();
    const [error, setError] = useState<string>('');
    function compare(a: IItem, b: IItem) {
        if (a.name < b.name) {
            return -1;
        }
        if (a.name > b.name) {
            return 1;
        }
        return 0;
    }

    let getTotal = (order: IOrder) => {
        let total = 0;
        order.orderItems.forEach((orderItem) => {
            let subtotal: number = orderItem.item.price * orderItem.quantity;
            total += subtotal;
        });
        return total;
    }


    let getCart = async () => {
        let response = await axios.get(`${process.env.REACT_APP_API_URL}/users/${context.userId}/orders/current`, {
            withCredentials: true,
            headers: { 'Content-Type': 'application/json', 'userId': context.userId }
        }).then((response) => {
            setCart(response.data);
        }).catch((error) => {
            console.error(error);
            console.error('Error fetching items:', error);
            setError('Failed to fetch items.');
        });}
    // get Items out of Order

    let storeCart = () => {
      // generate array of itemIds for Items in Cart
      let itemIds: number[] = [];
      if (cart) {
        cart.orderItems.forEach((orderItem) => {
          itemIds.push(orderItem.item.itemId);
        });
      } else {
        itemIds = [];
      }
      // store in context
      context.setCartItems(itemIds);
    }


    useEffect(() => {
        getCart();
        storeCart();
    }, []);
  return (
    <>
    <header style={{paddingBottom:"40px"}}>
      <h1 style={{fontSize:"5rem", textAlign:"center" }}>Your Cart</h1>
    </header>


    {(cart?.orderItems != null && cart?.orderItems?.length != 0) ?
    <div className="cart-page w-90 container" style={{backgroundColor: "#fcead6", margin:'0 auto', paddingBottom: '16px'}}>

    <div  className="cart-container row row-cols-1" style={{ width: '80%'}}>
        
        {cart?.orderItems?.map((orderItemMap) => (
            <>
            <div className="col cart-item" style={{backgroundColor:"aliceblue"}}>
              <ItemCard key={`item${orderItemMap.item.itemId}`} item={orderItemMap.item} type={DisplayType.CART} itemQuantity={orderItemMap.quantity} isInCart={true} orderId={orderItemMap.orderId}></ItemCard>
            </div>
            </>
            ))}
            
    </div>
    <div className="col cart-summary" style={{backgroundColor:"aliceblue"}}>
      <h2>Cart Summary</h2>
      <div className="row">
        <div className="col">
          <h3>Subtotal</h3>
        </div>
        <div className="col">
          <h3 id="renderTotal">${getTotal(cart)}</h3>
        </div>
      </div>
      <button className="btn btn-primary" onClick={() => navigate('/checkout')}>Proceed to checkout</button>
    </div>
    </div> : 
    <div className="h-100 d-flex align-items-center justify-content-center">
    <h1>No items in cart</h1>
    </div>}
    </>
  )
}

export default CartPage;
