import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, InputGroup, Form, FormControl, FormGroup, FormLabel, FormText } from 'react-bootstrap';
import './ItemForm.css';

interface IItemFormProps {
  itemId?: number;
  sellerId: number;
}

const ItemForm: React.FC<IItemFormProps> = ({ itemId, sellerId }) => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [price, setPrice] = useState('');
  const [image, setImage] = useState('');
  const [errors, setErrors] = useState({
    name: false,
    description: false,
    price: false,
    image: false
  });

  useEffect(() => {
    if (itemId) {
      // FIX ME: Add correct URL
      axios.get(`${process.env.BACKEND_URL}/items/${itemId}`)
        .then(response => {
          const { name, description, price, image } = response.data;
          setName(name);
          setDescription(description);
          setPrice(parseFloat(price).toFixed(2));
          setImage(image);
        })
        .catch(error => {
          console.log(`Failed to fetch item with id ${itemId}: ${error}`);
        });
    }
  }, [itemId]);

  const validateForm = () => {
    const newErrors = {
      name: !name,
      description: !description,
      // ADJUST MAX PRICE
      price: parseFloat(price) <= 0 || parseFloat(price) >= 50000,
      image: !image
    };

    setErrors(newErrors);
    return Object.values(newErrors).every(v => !v);
  };

  const handlePriceChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let input = e.target.value;
    let numericInput = input.replace(/\D/g, '');
    let paddedInput = numericInput.padStart(3, '0');
    let dollarAmount = `${paddedInput.slice(0, -2)}.${paddedInput.slice(-2)}`;
    let dollarAmountWithoutLeadingZeros = dollarAmount === '' ? '' : parseFloat(dollarAmount).toFixed(2);
    setPrice(dollarAmountWithoutLeadingZeros);
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (validateForm()) {
      const itemData = {
        name,
        description,
        price: parseFloat(price.replace(/,/g, '')),
        image,
        sellerId
      };
      const axiosMethod = itemId ? axios.patch : axios.post;
      // FIX ME: Add correct URLs
      const url = itemId ? `${process.env.BACKEND_URL}/items/store/${itemId}` : `${process.env.BACKEND_URL}/items/store`;

      axiosMethod(url, itemData)
        .then(response => console.log(response))
        .catch(error => console.log(error));
    } else {
      document.getElementById('formContainer')?.classList.add('shake');
      setTimeout(() => {
        document.getElementById('formContainer')?.classList.remove('shake');
      }, 200);
    }
  };

  return (
    <div id="formContainer">
      <h1>Add more details</h1>
      <p>Add a photo and some details about your item. You will be able to edit this later.</p>
      <Form onSubmit={handleSubmit}>
        <FormGroup>
          <FormLabel>Item Name</FormLabel>
          <FormText className="text-muted">
            Include keywords that buyers would use to search for this item.
          </FormText>
          <FormControl
            className={`form-control ${errors.name ? 'is-invalid' : ''}`}
            type="text"
            value={name}
            onChange={e => {
              setName(e.target.value);
              setErrors(prev => ({ ...prev, name: false }));
            }}
            maxLength={140}
            placeholder="Enter item name"
          />
          <FormText>{name.length}/140</FormText>
        </FormGroup>

        <FormGroup>
          <FormLabel>Description</FormLabel>
          <FormText className="text-muted">
            Buyers will only see the first few lines unless they expand the description.
          </FormText>
          <FormControl
            className={`form-control ${errors.description ? 'is-invalid' : ''}`}
            as="textarea"
            value={description}
            onChange={e => {
              setDescription(e.target.value);
              setErrors(prev => ({ ...prev, description: false }));
            }}
            style={{ resize: 'vertical' }}
            placeholder="What makes your item special?"
          />
        </FormGroup>

        <FormGroup>
          <FormLabel>Price</FormLabel>
          <InputGroup>
            <InputGroup.Text>$</InputGroup.Text>
            <FormControl
              className={`form-control ${errors.price ? 'is-invalid' : ''}`}
              type="text"
              value={price}
              onChange={handlePriceChange}
              placeholder="0.00"
            />
            {errors.price && <FormText className="invalid-feedback">
              Price must be between $0.01 and $50,000.00
            </FormText>}
          </InputGroup>
        </FormGroup>

        <FormGroup>
          <FormLabel>Image URL</FormLabel>
          <FormControl
            className={`form-control ${errors.image ? 'is-invalid' : ''}`}
            type="text"
            value={image}
            onChange={e => {
              setImage(e.target.value);
              setErrors(prev => ({ ...prev, image: false }));
            }}
            placeholder="http://example.com/image.jpg"
          />
        </FormGroup>

        <Button type="submit" className="custom-button">Sell Item</Button>
      </Form>
    </div>
  );
}

export default ItemForm;