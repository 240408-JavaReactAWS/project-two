import React, { useContext } from 'react'
import { UserContext } from '../../App'
import axios from 'axios'
import { useNavigate } from 'react-router-dom'
interface ICartProps{
  orderItem:{itemId:number,
  stock:number},
  setDisplayQuantity?:React.Dispatch<React.SetStateAction<boolean>>
}

export default function AddToCartButton(props:ICartProps) {
  const user=useContext(UserContext)
  const navigate = useNavigate()

  const addToCart = (e:React.SyntheticEvent) => {
    e.preventDefault()
    
    
    async function saveItemToCart(orderItem:{itemId:number,stock:number}){
      try{
        const axiosMethod = user.cartItems.includes(props.orderItem.itemId) ? axios.patch : axios.post;
          const res= await axiosMethod(`${process.env.REACT_APP_API_URL}/users/${user.userId}/orders/current`,
            orderItem, {
              withCredentials: true, headers: { 'Content-Type': 'application/json', 'userId': user.userId} 
            })
            if(res.status==201){
              props.setDisplayQuantity && props.setDisplayQuantity(true)
              user.setCartItems([...user.cartItems, orderItem.itemId])
            }
            console.log('Item added to cart:', res)
      }catch(error){console.error(error)}
    }
    if (!user.userId) {
      navigate('/login')
    }
    else {
      saveItemToCart(props.orderItem)
    }
  }
    return (
    <button className="btn btn-primary" onClick={addToCart}>Add to Cart</button> 
  )
}
