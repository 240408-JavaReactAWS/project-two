import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Button, InputGroup, Form, FormControl } from 'react-bootstrap';
import './ItemForm.css';

interface IItemFormProps {
  itemId?: number;
  sellerId: number;
}

const ItemForm: React.FC<IItemFormProps> = ({ itemId, sellerId }) => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [price, setPrice] = useState('0.00');
  const [stock, setStock] = useState(0);
  const [image, setImage] = useState('');
  const [errors, setErrors] = useState({
    name: false,
    description: false,
    price: false,
    stock: false,
    image: false
  });

  const validateForm = () => {
    const newErrors = {
      name: !name,
      description: !description,
      price: parseFloat(price) <= 0,
      stock: stock === 0,
      image: !image
    };

    setErrors(newErrors);
    // Check if every value in error object is false
    return Object.values(newErrors).every(v => !v);
  };

  // Check if Item already exists
  useEffect(() => {
    if (itemId) {
      // FIX ME: Update URL
      axios.get(`${process.env.BACKEND_URL}/items/${itemId}`)
        .then(response => {
          const { name, description, price, stock, image } = response.data;
          setName(name);
          setDescription(description);
          setPrice(price.toFixed(2));
          setStock(stock);
          setImage(image);
        })
        .catch(error => {
          console.log(`Failed to fetch item with id ${itemId}: ${error}`);
        });
    }
  }, [itemId]);

  // Force price input to be in dollar format
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
      const itemData = { name, description, price: parseFloat(price), stock, image, sellerId };
      const axiosMethod = itemId ? axios.patch : axios.post;
      // FIX ME: Update URL
      const url = itemId ? `${process.env.BACKEND_URL}/items/store/${itemId}` : `${process.env.BACKEND_URL}/items/store`;

      axiosMethod(url, itemData)
        .then(response => console.log(response))
        .catch(error => console.log(error));
    } else {
      // Shake form if validation fails
      document.getElementById('formContainer')?.classList.add('shake');
      setTimeout(() => {
        document.getElementById('formContainer')?.classList.remove('shake');
      }, 200);
    }
  };

  return (
    <div id="formContainer">
      <Form onSubmit={handleSubmit}>
        <Form.Group>
          <Form.Label>Item Name</Form.Label>
          <FormControl
            className={errors.name ? 'error' : ''}
            type="text"
            value={name}
            onChange={e => {
              setName(e.target.value);
              setErrors(prev => ({ ...prev, name: false }));
            }}
            placeholder="Enter item name"
          />
        </Form.Group>

        <Form.Group>
          <Form.Label>Item Description</Form.Label>
          <FormControl
            className={errors.description ? 'error' : ''}
            type="text"
            value={description}
            onChange={e => {
              setDescription(e.target.value);
              setErrors(prev => ({ ...prev, description: false }));
            }}
            placeholder="Enter item description"
          />
        </Form.Group>

        <Form.Group>
          <Form.Label>Price</Form.Label>
          <InputGroup>
            <InputGroup.Text>$</InputGroup.Text>
            <FormControl
              className={errors.price ? 'error' : ''}
              type="text"
              placeholder="0.00"
              value={price}
              onChange={handlePriceChange}
            />
          </InputGroup>
        </Form.Group>

        <Form.Group>
          <Form.Label>Stock</Form.Label>
          <FormControl
            className={errors.stock ? 'error' : ''}
            type="number"
            placeholder="0"
            value={stock}
            onChange={e => {
              setStock(Number(e.target.value));
              setErrors(prev => ({ ...prev, stock: false }));
            }}
          />
        </Form.Group>

        <Form.Group>
          <Form.Label>Image URL</Form.Label>
          <FormControl
            className={errors.image ? 'error' : ''}
            type="text"
            placeholder="http://example.com/image.jpg"
            value={image}
            onChange={e => {
              setImage(e.target.value);
              setErrors(prev => ({ ...prev, image: false }));
            }}
          />
        </Form.Group>

        <Button type="submit" variant="primary">Sell Item</Button>
      </Form>
    </div>
  );
}

export default ItemForm;