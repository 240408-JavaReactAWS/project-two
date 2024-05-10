import React, { useContext, useState } from 'react';
import { Col, Button, Row, Form } from 'react-bootstrap';
import { IItem } from '../../../interfaces/IItem';
import axios from 'axios';
import StarRating from './StarRating';
import { useNavigate } from 'react-router-dom';
import { UserContext } from '../../../App';
import ItemForm from '../../common/ItemForm';

interface Props {
  item: IItem | null;
}

const ItemDetails: React.FC<Props> = ({ item }) => {
    const [quantity, setQuantity] = useState(1);
    const [showForm, setShowForm] = useState(false);
    const navigate = useNavigate();
    const { userId } = useContext(UserContext)

    const handleChangeQuantity = (e: React.ChangeEvent<HTMLInputElement>) => {
        setQuantity(parseInt(e.target.value));
    };

    const handleAddToCart = () => {
        if (item === null) {
            return;
        } else {
            // Make a POST request to add item to cart
            axios.post(`${process.env.REACT_APP_API_URL}/users/${userId}/orders/current`, { itemId: item.itemId, quantity: quantity })
                .then(response => {
                    // Handle success
                    console.log('Item added to cart:', response.data);
                })
                .catch(error => {
                    // Handle error
                    console.error('Error adding item to cart:', error);
                });
        }
    };

    const handleUpdateItem = () => {
      if (item) {
        setShowForm(true);
      } else {
        navigate('/');
      }

    };

    const handleViewRelatedOrders = () => {
      if (item !== null) {
        navigate(`/orders/${item.itemId}`);
      } else {
        navigate('/');
      }
    };


  return (
    <Col>
    {item && (
        <Row>
          <Col lg={6}>
            {/* Item image */}
            <img src={item.image} className="img-fluid" alt="Item Image" />
          </Col>
          <Col lg={6}>
            {/* Item details */}
            <Row>
              <Col>
                <h1><strong>{item.name}</strong></h1>
                {/* If item's seller ID matches the current user's ID */}
                {item.sellerId === userId && (
                  <>
                    <Button onClick={handleUpdateItem} variant="secondary" className="mr-2">Update Item</Button>
                    <Button onClick={handleViewRelatedOrders} variant="info">View Related Orders</Button>
                  </>
                )}
              </Col>
            </Row>

            <Row>
              <Col>
                <h3>${item.price}</h3>
                <p>{item.stock} currently available</p>
                <br/>
                <p>Average Rating of {item.rating}</p>
                <h3><StarRating rating={item.rating}  clickable={false} /></h3>
                <br/>
                <h5>Description</h5>
                <p>{item.description}</p>
                <Form className="mb-3">
                  <Row>
                    {/* Quantity input */}
                    <Col xs={6}>
                      <Form.Group>
                        <Form.Label>Quantity</Form.Label>
                        <Form.Control type="number" value={quantity} onChange={handleChangeQuantity} min={1}  max={item.stock}/>
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
      {showForm && <ItemForm itemId={item?.itemId} />}
    </Col>
  );
};

export default ItemDetails;