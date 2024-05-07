import React, { useContext, useEffect, useState } from 'react'
import { UserContext } from '../../App';
import { Link } from 'react-router-dom';

export enum DisplayType {
    OWNED,
    NONOWNED,
    CART
}

interface IItemCardProps {
    item: {
        id: number;
        name: string;
        price: number;
        description: string;
        image: string;
        sellerId: number;
        rating: number;
        quantity: number;
    };
    type: DisplayType;
}
function ItemCard(props : IItemCardProps) {
    const [quantity, setQuantity] = useState(props.item.quantity)
    const plusQuantity = () => {
        setQuantity(quantity + 1)
    }
    const minusQuantity = () => setQuantity(quantity - 1)
    const deleteItem = (e:React.SyntheticEvent) => {
        e.preventDefault()
    }
    const addToCart = (e:React.SyntheticEvent) => {
        e.preventDefault()
    }
    
    
    return (
        props.type === DisplayType.OWNED ? 
        <Link style={{textDecoration:'none', color:'black'}} to={`/item/${props.item.id}`}>
            <div className="card" style={{width: '18rem'}}>
                <img className="card-img-top" src={props.item.image} alt="Card image cap"/>
                <div className="card-body">
                    <h5 className="card-title">{props.item.name}</h5>
                    <p className="card-text">Price: {props.item.price}</p>
                    <p className="card-text">Rating: {props.item.rating}</p>
                    <p className="card-text">Quantity: {props.item.quantity}</p>
                    <button className="btn btn-primary" onClick={deleteItem}>Delete</button>
                    </div>
                </div>
        </Link>
        : props.type === DisplayType.NONOWNED ?
        <Link style={{textDecoration:'none', color:'black'}}to={`/item/${props.item.id}`}>
            <div className="card" style={{width: '18rem'}}>
                <img className="card-img-top" src={props.item.image} alt="Card image cap"/>
                <div className="card-body">
                    <h5 className="card-title">{props.item.name}</h5>
                    <p className="card-text">Price: {props.item.price}</p>
                    <p className="card-text">Rating: {props.item.rating}</p>
                    <p className="card-text">Quantity: {props.item.quantity}</p>
                    <button className="btn btn-primary" onClick={addToCart}>Add to Cart</button>
                </div>
            </div>
        </Link>
            :
            <tr>
                <td><img  style={{height: "100px"}} src={props.item.image} alt="Card image cap"/></td>
                <td>{props.item.name}</td>
                <td>{props.item.price}</td>
                <td>{props.item.rating}</td>
                <td><button className="btn btn-primary" onClick={minusQuantity}>-</button>{quantity}<button className="btn btn-primary" onClick={plusQuantity}>+</button></td>
            </tr>
    )
}

export default ItemCard
