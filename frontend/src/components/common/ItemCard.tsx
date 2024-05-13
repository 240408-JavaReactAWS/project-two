import React, { useContext, useState } from 'react'
import { UserContext } from '../../App';
import { Link, useNavigate } from 'react-router-dom';
import { IItem } from '../../interfaces/IItem';
import axios from 'axios';
import AddToCartButton from './AddToCartButton';

export enum DisplayType {
    OWNED,
    NONOWNED,
    CART
}

interface IItemCardProps {
    item:IItem;
    itemQuantity?:number,
    type: DisplayType,
    setSellersItems?:React.Dispatch<React.SetStateAction<IItem[]>>,
    isInCart?:boolean
    orderId?:number

}
function ItemCard(props : IItemCardProps ) {
    const user=useContext(UserContext)
    const [quantity, setQuantity] = useState(!!props.itemQuantity ? props.itemQuantity:1)
    const [cart,setCart] = useState(user.cartItems.includes(props.item.itemId))

    const plusQuantity = async (e:React.SyntheticEvent) => {
        e.preventDefault()
        try{
            let response=await axios.patch(process.env.REACT_APP_API_URL + `/users/${user.userId}/orders/current`,
             {stock: quantity + 1, itemId: props.item.itemId},
             {withCredentials: true, headers: { 'Content-Type': 'application/json', 'userId': user.userId}})
            if(response.status==403){

            }
            if(response.status==200){
                setQuantity(quantity + 1)
            }
            console.log('Item added to cart:', response)
        } catch(error){
            console.error(error)
        }
    }

    const minusQuantity = async (e:React.SyntheticEvent) => {
        e.preventDefault()
        try{
            let response=await axios.patch(process.env.REACT_APP_API_URL + `/users/${user.userId}/orders/current`,
            {stock: quantity - 1, itemId: props.item.itemId}, {
                withCredentials: true, headers: { 'Content-Type': 'application/json', 'userId': user.userId}
            })
            if(response.status==403){

            }
            if(response.status==200){
                setQuantity(quantity - 1)
                if (quantity - 1 === 0) {
                    setCart(false)
                    user.setCartItems(user.cartItems.filter((itemId) => itemId !== props.item.itemId))
                }
            }
        } catch(error){
            console.error(error)
        }
    }


    const navigate=useNavigate()

    const deleteItem = async (e:React.SyntheticEvent) => {
        e.preventDefault()
        try{
            let response = await axios.delete(process.env.REACT_APP_API_URL + `/items/${props.item.itemId}`, {
                withCredentials: true, headers: { 'Content-Type': 'application/json', 'userId': user.userId}

              })
              if(response.status==403){

              }
              if(response.status==200){
                props.setSellersItems && props.setSellersItems((sellersItem:IItem[]) => {
                    return sellersItem.filter((item) => {
                        return item.itemId !== props.item.itemId;
                    });
                })
              }
        }catch(error){
            console.error(error)
        }


    }

    // const addToCart = (e:React.SyntheticEvent) => {
    //     e.preventDefault()
    //     const
    // //     let saveItemToCart= async ()=>{
    // //         try{
    // //             // let res=await axios.post(process.env.REACT_APP_API_URL + `/users/${user.userId}/orders/current`, {
    // //             //     withCredentials: true, headers: { 'Content-Type': 'application/json', 'userId': user.userId}
    // //             //   })

    // //             //   if(res.status==403){

    // //             //   }
    // //             //   if(res.status==200){
    // //             //    setCart(true);
    // //             //   }
    // //             <AddToCartButton />
    // //         }catch(error){console.error(error)}
    // //     }
    // }


    return (
        props.type === DisplayType.OWNED ?

            <div className="card" style={{maxWidth: '18rem'}}>
                 <Link style={{textDecoration:'none', color:'black'}} to={`/item/${props.item.itemId}`}>
                    <img className="card-img-top "  height={"200px"} src={props.item.image} alt="Card image cap"/>
                    <div className="card-body">
                        <h5 className="card-title">{props.item.name}</h5>
                        <p className="card-text">Price: {props.item.price}</p>
                        <p className="card-text">Rating: {props.item.rating}</p>
                        <p className="card-text">Stock: {props.item.stock}</p>
                        <button className="btn btn-primary" onClick={deleteItem}>Delete</button>
                        </div>
                </Link>
                </div>
        : props.type === DisplayType.NONOWNED ?
            <div className="card" style={{maxWidth: '18rem'}}>
                <Link style={{textDecoration:'none', color:'black'}}to={`/item/${props.item.itemId}`}>
                    <img className="card-img-top" height={"200px"}  src={props.item.image} alt="Card image cap"/>
                    <div className="card-body">
                        <h5 className="card-title">{props.item.name}</h5>
                        <p className="card-text">Price: {props.item.price}</p>
                        <p className="card-text">Rating: {props.item.rating}</p>
                        <p className="card-text">Quantity: {props.item.stock}</p>
                        {!cart && <AddToCartButton setDisplayQuantity={setCart} orderItem={
                            {itemId : props.item.itemId, stock:1}}/>}
                        {cart &&
                        <>
                            <button className="btn btn-primary" onClick={minusQuantity}>-</button>
                            {quantity}
                            <button className="btn btn-primary" onClick={plusQuantity}>+</button>
                        </>}
                    </div>
            </Link>
            </div>
            :
            <tr>
                <td><img  style={{height: "100px"}} src={props.item.image} alt="Card image cap"/></td>
                <td>{props.item.name}</td>
                <td>{props.item.price}</td>
                <td>{props.item.rating}</td>
                <td>
                    <button className="btn btn-primary" onClick={minusQuantity}>-</button>
                    {quantity}
                    <button className="btn btn-primary" onClick={plusQuantity}>+</button>
                </td>
            </tr>
    )
}

export default ItemCard
