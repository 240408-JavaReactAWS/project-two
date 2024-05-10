// TODO
// Styling
// Form validation
// Testing eventually
import React, { useState, useContext } from 'react';
import axios from 'axios';
import { UserContext } from '../../App';
import { Button, InputGroup, Form, FormControl, FormGroup, FormLabel } from 'react-bootstrap';
//import './OrderForm.css';

const OrderForm: React.FC = () => {
  const { userId } = useContext(UserContext);
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [shippingStreetAddress, setShippingStreetAddress] = useState('');
  const [shippingCity, setShippingCity] = useState('');
  const [shippingState, setShippingState] = useState('');
  const [shippingZipCode, setShippingZipCode] = useState('');
  const [shippingCountry, setShippingCountry] = useState('');
  const [CardName, setCardName] = useState('');
  const [CardNumber, setCardNumber] = useState('');
  const [CardExpiration, setCardExpiration] = useState('');
  const [CardCVV, setCardCVV] = useState('');
  const [billingStreetAddress, setBillingStreetAddress] = useState('');
  const [billingCity, setBillingCity] = useState('');
  const [billingState, setBillingState] = useState('');
  const [billingZipCode, setBillingZipCode] = useState('');
  const [billingCountry, setBillingCountry] = useState('');

  const handleCopyAddress = () => {
    setBillingStreetAddress(shippingStreetAddress);
    setBillingCity(shippingCity);
    setBillingState(shippingState);
    setBillingZipCode(shippingZipCode);
    setBillingCountry(shippingCountry);
  }

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const shipToAddress = [shippingStreetAddress, shippingCity, shippingState, shippingZipCode, shippingCountry].join(', ');
    const billAddress = [billingStreetAddress, billingCity, billingState, billingZipCode, billingCountry,CardName,CardNumber,CardExpiration,CardCVV].join(', ');
    console.log(shipToAddress);
    console.log(billAddress);

    if (shipToAddress.length > 255 || billAddress.length > 255) {
      alert("Address too long. Please make sure the address is less than 255 characters.");
      return;
    }

    axios.patch(`${process.env.REACT_APP_API_URL})/users/${userId}/orders/checkout`, {
      shipToAddress,
      billAddress
    }, {
      withCredentials: true,
      headers: {
        'Content-Type': 'application/json',
        'userId': userId
      }
    })
      .then(response => { console.log('Completed order!'); })
      .catch(error => { alert('Failed to complete order') });
}

return (
  <Form onSubmit={handleSubmit}>
    <FormGroup>
      <FormLabel>Shipping Information</FormLabel>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="First Name"
          aria-label="First Name"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        />
        <FormControl
          placeholder="Last Name"
          aria-label="Last Name"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
        />
      </InputGroup>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="Street Address"
          aria-label="Street Address"
          value={shippingStreetAddress}
          onChange={(e) => setShippingStreetAddress(e.target.value)}
        />
      </InputGroup>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="City"
          aria-label="City"
          value={shippingCity}
          onChange={(e) => setShippingCity(e.target.value)}
        />
      </InputGroup>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="State"
          aria-label="State"
          value={shippingState}
          onChange={(e) => setShippingState(e.target.value)}
        />
      </InputGroup>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="Zip Code"
          aria-label="Zip Code"
          value={shippingZipCode}
          onChange={(e) => setShippingZipCode(e.target.value)}
        />
      </InputGroup>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="Country"
          aria-label="Country"
          value={shippingCountry}
          onChange={(e) => setShippingCountry(e.target.value)}
        />
      </InputGroup>
      <Button variant="secondary" onClick={handleCopyAddress}>Copy Shipping Address to Billing</Button>
    </FormGroup>
    <FormGroup>
      <FormLabel>Billing Information</FormLabel>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="Name on Card"
          aria-label="Name on Card"
          value={CardName}
          onChange={(e) => setCardName(e.target.value)}
        />
      </InputGroup>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="Card Number"
          aria-label="Card Number"
          maxLength={16}
          value={CardNumber}
          onChange={(e) => setCardNumber(e.target.value)}
        />
      </InputGroup>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="Expiration Date"
          aria-label="Expiration Date"
          type="date"
          value={CardExpiration}
          onChange={(e) => setCardExpiration(e.target.value)}
        />
      </InputGroup>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="CVV"
          aria-label="CVV"
          type='password'
          maxLength={3}
          value={CardCVV}
          onChange={(e) => setCardCVV(e.target.value)}
        />
      </InputGroup>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="Street Address"
          aria-label="Street Address"
          value={billingStreetAddress}
          onChange={(e) => setBillingStreetAddress(e.target.value)}
        />
      </InputGroup>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="City"
          aria-label="City"
          value={billingCity}
          onChange={(e) => setBillingCity(e.target.value)}
        />
      </InputGroup>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="State"
          aria-label="State"
          value={billingState}
          onChange={(e) => setBillingState(e.target.value)}
        />
      </InputGroup>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="Zip Code"
          aria-label="Zip Code"
          value={billingZipCode}
          onChange={(e) => setBillingZipCode(e.target.value)}
        />
      </InputGroup>
      <InputGroup className="mb-3">
        <FormControl
          placeholder="Country"
          aria-label="Country"
          value={billingCountry}
          onChange={(e) => setBillingCountry(e.target.value)}
        />
      </InputGroup>
    </FormGroup>
    <Button type="submit" variant="primary">Complete Order</Button>
  </Form>
);
};

export default OrderForm;