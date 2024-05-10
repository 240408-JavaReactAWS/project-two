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
    
    if (!user.userId) {
      navigate('/login')
    }
    async function saveItemToCart(orderItem:{itemId:number,stock:number}){
      try{
          const res= await axios.post(`${process.env.REACT_APP_API_URL}/users/${user.userId}/orders/current`,
            orderItem, {
              withCredentials: true, headers: { 'Content-Type': 'application/json', 'userId': user.userId} 
            })
            if(res.status==200){
              props.setDisplayQuantity && props.setDisplayQuantity(false)
            }

      }catch(error){console.error(error)}
    }
    saveItemToCart(props.orderItem)
  }
    return (
    <button className="btn btn-primary" onClick={addToCart}>Add to Cart</button> 
  )
}
