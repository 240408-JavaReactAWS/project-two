// TODO
// Styling
// Form validation
// Testing eventually
import React, { useState, useContext } from 'react';
import axios from 'axios';
import { IOrder } from '../../interfaces/IOrder';
import { IUser } from '../../interfaces/IUser';
import { UserContext } from '../../App';
import { Button, InputGroup, Form, FormControl, FormGroup, FormLabel, FormText } from 'react-bootstrap';
import './OrderForm.css';

const OrderForm: React.FC = () => {
  const { userId } = useContext(UserContext);
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [shippingStreetAddress, setShippingStreetAddress] = useState('');
  const [shippingCity, setShippingCity] = useState('');
  const [shippingState, setShippingState] = useState('');
  const [shippingCountry, setShippingCountry] = useState('');
  const [shippingZipCode, setShippingZipCode] = useState('');
  const [billingStreetAddress, setBillingStreetAddress] = useState('');
  const [billingCity, setBillingCity] = useState('');
  const [billingState, setBillingState] = useState('');
  const [billingCountry, setBillingCountry] = useState('');
  const [billingZipCode, setBillingZipCode] = useState('');

  const handleCopyAddress = () => {
    setBillingStreetAddress(shippingStreetAddress);
    setBillingCity(shippingCity);
    setBillingState(shippingState);
    setBillingCountry(shippingCountry);
    setBillingZipCode(shippingZipCode);
  }

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const shipToAddress = shippingStreetAddress + ', ' + shippingCity + ', ' + shippingState + ', ' + shippingCountry + ' ' + shippingZipCode;
    const billAddress = billingStreetAddress + ', ' + billingCity + ', ' + billingState + ', ' + billingCountry + ' ' + billingZipCode;

    axios.patch(`${process.env.REACT_APP_API_URL})/users/${userId}/orders/checkout`, {
      status: 'Approved',
      shipToAddress,
      billAddress
    })
      .then(response => { console.log(response); })
      .catch(error => { alert(error) });
}

  return (
    <>
      <Form onSubmit={handleSubmit}>
        <FormGroup>
          <FormLabel>Shipping Information</FormLabel>
          <InputGroup className="mb-3">
            <FormControl
              placeholder="First Name"
              aria-label="First Name"
              aria-describedby="basic-addon1"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
            />
          </InputGroup>
          <InputGroup className="mb-3">
            <FormControl
              placeholder="Last Name"
              aria-label="Last Name"
              aria-describedby="basic-addon1"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
            />
          </InputGroup>
          <InputGroup className="mb-3">
            <FormControl
              placeholder="Street Address"
              aria-label="Street Address"
              aria-describedby="basic-addon1"
              value={shippingStreetAddress}
              onChange={(e) => setShippingStreetAddress(e.target.value)}
            />
          </InputGroup>
          <InputGroup className="mb-3">
            <FormControl
              placeholder="City"
              aria-label="City"
              aria-describedby="basic-addon1"
              value={shippingCity}
              onChange={(e) => setShippingCity(e.target.value)}
            />
          </InputGroup>
          <InputGroup className="mb-3">
            <FormControl
              placeholder="State"
              aria-label="State"
              aria-describedby="basic-addon1"
              value={shippingState}
              onChange={(e) => setShippingState(e.target.value)}
            />
          </InputGroup>
          <InputGroup className="mb-3">
            <FormControl
              placeholder="Country"
              aria-label="Country"
              aria-describedby="basic-addon1"
              value={shippingCountry}
              onChange={(e) => setShippingCountry(e.target.value)}
            />
          </InputGroup>
          <InputGroup className="mb-3">
            <FormControl
              placeholder="Zip Code"
              aria-label="Zip Code"
              aria-describedby="basic-addon1"
              value={shippingZipCode}
              onChange={(e) => setShippingZipCode(e.target.value)}
            />
          </InputGroup>
        </FormGroup>
        <FormGroup>
          <Button onClick={handleCopyAddress}>Set to same as Shipping Address</Button>
          <FormLabel>Billing Information</FormLabel>
            <InputGroup className="mb-3">
            <FormControl
              placeholder="Card Number"
              aria-label="Card Number"
              aria-describedby="basic-addon1"
            />
            </InputGroup>
            <InputGroup className="mb-3">
            <FormControl
              placeholder="Cardholder Name"
              aria-label="Cardholder Name"
              aria-describedby="basic-addon1"
            />
            </InputGroup>
            <InputGroup className="mb-3">
            <FormControl
              placeholder="Expiration Date"
              aria-label="Expiration Date"
              aria-describedby="basic-addon1"
            />
            </InputGroup>
            <InputGroup className="mb-3">
            <FormControl
              placeholder="Security Code"
              aria-label="Security Code"
              aria-describedby="basic-addon1"
            />
            </InputGroup>
            <InputGroup className="mb-3">
            <FormControl
              placeholder="Street Address"
              aria-label="Street Address"
              aria-describedby="basic-addon1"
              value={billingStreetAddress}
              onChange={(e) => setBillingStreetAddress(e.target.value)}
            />
          </InputGroup>
          <InputGroup className="mb-3">
            <FormControl
              placeholder="City"
              aria-label="City"
              aria-describedby="basic-addon1"
              value={billingCity}
              onChange={(e) => setBillingCity(e.target.value)}
            />
          </InputGroup>
          <InputGroup className="mb-3">
            <FormControl
              placeholder="State"
              aria-label="State"
              aria-describedby="basic-addon1"
              value={billingState}
              onChange={(e) => setBillingState(e.target.value)}
            />
          </InputGroup>
          <InputGroup className="mb-3">
            <FormControl
              placeholder="Country"
              aria-label="Country"
              aria-describedby="basic-addon1"
              value={billingCountry}
              onChange={(e) => setBillingCountry(e.target.value)}
            />
          </InputGroup>
          <InputGroup className="mb-3">
            <FormControl
              placeholder="Zip Code"
              aria-label="Zip Code"
              aria-describedby="basic-addon1"
              value={billingZipCode}
              onChange={(e) => setBillingZipCode(e.target.value)}
            />
          </InputGroup>
        </FormGroup>
        <Button type="submit">Submit</Button>
      </Form>
    </>
  )
}

export default OrderForm;