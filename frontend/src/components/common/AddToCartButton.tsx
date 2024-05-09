import React, { useContext } from 'react'
import { UserContext } from '../../App'
import { IOrderItem } from '../../interfaces/IOrderItem'
import axios from 'axios'
interface ICartProps{
    orderItem:IOrderItem

}

export default function AddToCartButton(props:ICartProps) {
  const user=useContext(UserContext)

  async function saveItemToCart(orderItem:IOrderItem){
    try{
        const res= await axios.post(`${process.env.REACT_APP_API_URL}/users/${user.userId}/orders/current`, {
            withCredentials: true, headers: { 'Content-Type': 'application/json', 'userId': user.userId} 
          })
          if(res.status==200){

          }

    }catch(error){console.error(error)}
  }
    return (
    <div>
        <button>Add to Cart</button>
      
    </div>
  )
}
