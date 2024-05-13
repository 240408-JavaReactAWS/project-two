import React, { useContext, useState } from 'react';
import { Col, Button, Row, Form, Container } from 'react-bootstrap';
import { IItem } from '../../../interfaces/IItem';
import axios from 'axios';
import StarRating from './StarRating';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../../../App';
import ItemForm from '../../common/ItemForm';

interface Props {
  item: IItem;
  setItem: React.Dispatch<React.SetStateAction<IItem | undefined>>;
}

const ItemDetails: React.FC<Props> = ( props ) => {

    const [quantity, setQuantity] = useState(1);
    const [showForm, setShowForm] = useState(false);
    const navigate = useNavigate();
    const { userId, cartItems, setCartItems } = useContext(UserContext)

    const handleChangeQuantity = (e: React.ChangeEvent<HTMLInputElement>) => {
        setQuantity(parseInt(e.target.value));
    };

    const handleAddToCart = () => {
        if (props.item === null) {
            return;
        } else {
            // Make a POST request to add item to cart
            const axiosMethod = cartItems.includes(props.item.itemId)? axios.patch : axios.post;
            axiosMethod(`${process.env.REACT_APP_API_URL}/users/${userId}/orders/current`, { itemId: props.item.itemId, stock: quantity }, 

            {
              withCredentials: true, headers: { 'Content-Type': 'application/json', 'userId': userId} 
            })
                .then(response => {
                    // Handle success
                    console.log('Item added to cart:', response.data);
                    if (response.status === 201) {
                      setCartItems([...cartItems, props.item.itemId]);
                    }
                    if(response.status === 200 && quantity === 0){
                      setCartItems(cartItems.filter((itemId) => itemId !== props.item.itemId));
                    }
                })
                .catch(error => {
                    // Handle error
                    console.error('Error adding item to cart:', error);
                });
        }
    };

    const handleUpdateItem = () => {
      if (props.item) {
        setShowForm(!showForm);
      } else {
        navigate('/');
      }

    };

    const handleViewRelatedOrders = () => {
      if (props.item !== null) {
        navigate(`/orders/${props.item.itemId}`);
      } else {
        navigate('/');
      }
    };


  return (
    <Container>
    {props.item && (
        <Row>
          <Col lg={6}>
            {/* Item image */}
            <img src={props.item.image} className="img-fluid" alt="Item Image" />
          </Col>
          <Col lg={6}>
            {/* Item details */}
            <Row>
              <Col>
                <h1><strong>{props.item.name}</strong></h1>
                {/* If item's seller ID matches the current user's ID */}
                {props.item.user.userId === userId && (
                  <>
                    <Button onClick={handleUpdateItem} variant="secondary" className="mr-2">Update Item</Button>
                    <Button onClick={handleViewRelatedOrders} variant="info">View Related Orders</Button>
                  </>
                )}
              </Col>
            </Row>

            <Row>
              <Col>
                <h3>${props.item.price}</h3>
                <p>{props.item.stock} currently available</p>
                <br/>
                <p>Average Rating of <strong>{props.item.rating}</strong></p>
                <h3><StarRating rating={props.item.rating}  clickable={false} /></h3>
                <br/>
                <h5>Description</h5>
                <p>{props.item.description}</p>
                <Form className="mb-3">
                  <Row>
                    {/* Quantity input */}
                    <Col xs={6}>
                      <Form.Group>
                        <Form.Label>Quantity</Form.Label>
                        <Form.Control type="number" value={quantity} onChange={handleChangeQuantity} min={1}  max={props.item.stock}/>
                      </Form.Group>
                    </Col>
                    {/* Add to cart button */}
                    <Col xs={6} className="d-flex align-items-end">
                      <Button onClick={handleAddToCart} variant="primary" className="w-100">Add to Cart</Button>
                    </Col>
                  </Row>
                </Form>
              </Col>
            </Row>
          </Col>
        </Row>
      )}
      {showForm && <ItemForm item={props.item} setItem={props.setItem} handleUpdateItem={handleUpdateItem} />}
    </Container>
  );
};

export default ItemDetails;