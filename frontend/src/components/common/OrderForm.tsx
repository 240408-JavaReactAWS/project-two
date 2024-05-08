import React, { useState, useContext } from 'react';
import axios from 'axios';
import { IOrder } from '../../interfaces/IOrder';
import { IUser } from '../../interfaces/IUser';
import { UserContext } from '../../App';
import { Button, InputGroup, Form, FormControl, FormGroup, FormLabel, FormText } from 'react-bootstrap';
import './OrderForm.css';

const OrderForm: React.FC = () => {
  const { userId } = useContext(UserContext);
  const [shipToAddress, setShipToAddress] = useState('');
  const [billAddress, setBillAddress] = useState('');

  // validate if shipping/billing address is empty


  // patch request to update order status
  // PATCH /users/{userID}/orders/checkout
  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    axios.patch(`${process.env.REACT_APP_API_URL})/users/${userId}/orders/checkout`, {
      status: 'Approved',
      shipToAddress,
      billAddress
    })
      .then(response => { console.log(response); })
      .catch(error => { alert(error) });
}

  // send address as a string

  // display error if quantity requested > stock
  // display error if session user != {userID} in request URL

  return (
    <>

      <div>Shipping Information
        <input>First Name</input>
        <input>Last Name</input>
        <input>Street Address</input>
        <input>City</input>
        <input>State</input>
        <input>Country</input>
        <input>Zip Code</input>
      </div>

      {/* doesn't do anything, for display only */}
      <div>Billing Information
        <input>Card Number</input>
        <input>Name on Card</input>
        <input>Expiration Date</input>
        <input>Security Code</input>
        <p>**same as shipping info button</p>
        <input>Street Address</input>
        <input>City</input>
        <input>State</input>
        <input>Country</input>
        <input>Zip Code</input>
      </div>

      <button>Submit</button>
    </>
  )
}

export default OrderForm;