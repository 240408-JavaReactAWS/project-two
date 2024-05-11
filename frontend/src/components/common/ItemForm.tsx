// Do we want this form to be a new page, or a modal that pops up when the user clicks "Sell Item"?
import React, { useState, useEffect, useContext } from 'react';
import axios from 'axios';
import { UserContext } from '../../App';
import { Button, InputGroup, Form, FormControl, FormGroup, FormLabel, FormText } from 'react-bootstrap';
import './ItemForm.css';
import { IItem } from '../../interfaces/IItem';

interface IItemFormProps {
  item?: IItem,
  setItem?: React.Dispatch<React.SetStateAction<IItem | undefined>>,
  handleUpdateItem?: () => void,
  addToItems?:(item: IItem) => void
}

const ItemForm: React.FC<IItemFormProps> = ({ item, setItem, handleUpdateItem, addToItems }) => {
  const { userId } = useContext(UserContext);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [price, setPrice] = useState('');
  const [stock, setStock] = useState(0);
  const [image, setImage] = useState('');
  const [datePosted, setDatePosted] = useState('');
  const [errors, setErrors] = useState({
    name: false,
    description: false,
    price: false,
    stock: false,
    image: false
  });

  // Check if item already exists
  useEffect(() => {
    if (item) {
      axios.get(`${process.env.REACT_APP_API_URL}/items/${item.itemId}`, {
        withCredentials: true,
        headers: {
          'Content-Type': 'application/json',
          'userId': userId
        }
      })
        .then(response => {
          const { name, description, price, stock, datePosted, image } = response.data;
          setName(name);
          setDescription(description);
          setPrice(parseFloat(price).toFixed(2));
          setStock(stock);
          setDatePosted(datePosted);
          setImage(image);
        })
        .catch(error => {
          console.log(`Failed to fetch item with id ${item.itemId}: ${error}`);
        });
    }
  }, []);

  // Validate each input
  const validateForm = () => {
    const newErrors = {
      name: !name,
      description: !description,
      // ADJUST MAX PRICE
      price: parseFloat(price) <= 0 || parseFloat(price) >= 50000,
      stock: stock < 0,
      image: !image
    };

    setErrors(newErrors);
    return Object.values(newErrors).every(v => !v);
  };

  // Coerce price to be in dollar format
  const handlePriceChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let input = e.target.value;
    let numericInput = input.replace(/\D/g, '');
    let paddedInput = numericInput.padStart(3, '0');
    let dollarAmount = `${paddedInput.slice(0, -2)}.${paddedInput.slice(-2)}`;
    let dollarAmountWithoutLeadingZeros = dollarAmount === '' ? '' : parseFloat(dollarAmount).toFixed(2);
    setPrice(dollarAmountWithoutLeadingZeros);
  };

  // Decide between PATCH and POST request
  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (validateForm()) {
      const itemData = {
        name,
        description,
        price: parseFloat(price.replace(/,/g, '')),
        stock,
        image,
      };
      const axiosMethod = item ? axios.patch : axios.post;
      const url = item ? `${process.env.REACT_APP_API_URL}/items/${item.itemId}` : `${process.env.REACT_APP_API_URL}/items`;

      axiosMethod(url, itemData, {
        withCredentials: true,
        headers: {
          'Content-Type': 'application/json',
          'userId': userId
        }
      })
        .then(response =>{
           console.log(response)
           if(response.status==200){
             addToItems && addToItems(response.data)
             setItem && setItem(response.data)
           }
          })
        .catch(error => console.log(error));
    } else {
      document.getElementById('formContainer')?.classList.add('shake');
      setTimeout(() => {
        document.getElementById('formContainer')?.classList.remove('shake');
      }, 200);
    }
    handleUpdateItem && handleUpdateItem();
  };

  if (item === null) {
    return (  
      <div id="formContainer">
        <h1>Add more details</h1>
        <h6>Add a photo and some details about your item. You will be able to edit this later.</h6>
        <br/>
        <br/>
        <br/>
        <Form onSubmit={handleSubmit}>
          <FormGroup>
            <FormLabel><h2>Item Name</h2></FormLabel>
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
            />
            <FormText>{name.length}/140</FormText>
          </FormGroup>
          <br/>
          <FormGroup>
            <FormLabel><h2>Description</h2></FormLabel>
            <FormText className="text-muted">
              What makes your item special? Buyers will only see the first few lines unless they expand the description.
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
            />
          </FormGroup>
          <br/>
          <FormGroup>
            <FormLabel><h2>Price</h2></FormLabel>
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
          <br/>
          <FormGroup>
            <FormLabel><h2>Stock</h2></FormLabel>
            <FormControl
              className={`form-control ${errors.image ? 'is-invalid' : ''}`}
              type="number"
              value={stock}
              onChange={e => {
                setStock(parseInt(e.target.value));
                setErrors(prev => ({ ...prev, stock: false }));
              }}
              placeholder="0"
            />
          </FormGroup>
          <br/>
          <FormGroup>
            <FormLabel><h2>Image URL</h2></FormLabel>
            <FormText className="text-muted">
              Provide a link to a photo of your product for buyers to see
            </FormText>
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
          <br/>
          <br/>
          <br/>
          <Button type="submit" className="custom-button">Sell Item</Button>
        </Form>
      </div>
    );
  } else {
    return (
      <div id="formContainer">
        <Form onSubmit={handleSubmit}>
          <FormGroup>
            <FormLabel><h2>Stock</h2></FormLabel>
            <FormControl
              className={`form-control ${errors.image ? 'is-invalid' : ''}`}
              type="number"
              value={stock}
              onChange={e => {
                setStock(parseInt(e.target.value));
                setErrors(prev => ({ ...prev, stock: false }));
              }}
              placeholder="0"
            />
          </FormGroup>
          <br/>
          <br/>
          <br/>
          <Button type="submit" className="custom-button">Sell Item</Button>
        </Form>
      </div>
    );
  }
}

export default ItemForm;