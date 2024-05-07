import axios from 'axios';
import React, {useEffect, useState} from 'react';


function ItemForm(itemId : number | null, sellerId : number) {
    /* State variables for item form */
    const [itemName, setItemName] = useState('');
    const [itemDescription, setItemDescription] = useState('');
    const [itemPrice, setItemPrice] = useState("0.00");
    const [itemQuantity, setItemQuantity] = useState(0);
    const [itemImageURL, setItemImageURL] = useState('');
    const [itemRating, setItemRating] = useState(0);
    const [itemDatePosted, setItemDatePosted] = useState('');
    
    /* Axios GET request to fetch item data if exists*/
    useEffect(() => {
        if (itemId) {
            axios.get(`http://localhost:3000/items/${itemId}`)
            .then((response) => {
                setItemName(response.data.name);
                setItemDescription(response.data.description);
                setItemPrice(response.data.price);
                setItemQuantity(response.data.quantity);
                setItemImageURL(response.data.image);
                setItemRating(response.data.rating);
                setItemDatePosted(response.data.datePosted);
            })
            .catch((error) => {
                console.log(error);
            });
        }
    }, [itemId]);

    /* Function to handle price input */
    const handlePriceChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        let input = e.target.value;
        let numericInput = input.replace(/\D/g, '');
        let paddedInput = numericInput.padStart(3, '0');
        let dollarAmount = `${paddedInput.slice(0, -2)}.${paddedInput.slice(-2)}`;
        let dollarAmountWithoutLeadingZeros = dollarAmount === '' ? '' : parseFloat(dollarAmount).toFixed(2);
        setItemPrice(dollarAmountWithoutLeadingZeros);
    };

    /* Axios POST request to add item */
    const handleSubmit = (e : React.FormEvent) => {
        /* Prevent default form submission */
        e.preventDefault();
        /* Check if all fields are filled out else log and alert*/
        try{
            if (itemName === '' || itemDescription === '' || itemPrice === '' || itemQuantity === 0 || itemImageURL === '') {
                throw new Error('All fields must be filled out');
            }
        }catch (error) {
            console.log(error);
            alert(error);
            return;
        }

        /* Log item data to console  for testing*/
        // console.log({
        //     name: itemName,
        //     description: itemDescription,
        //     price: parseFloat(itemPrice),
        //     quantity: itemQuantity,
        //     image: itemImageURL,
        //     datePosted: itemDatePosted ? itemDatePosted : new Date().toISOString(),
        //     rating: itemRating,
        //     itemId : itemId,
        //     sellerId : sellerId
        // });

        /* Axios POST request to update item if Exitst else Add*/
        if (itemId) {
           axios.patch(`http://localhost:3000/items/${itemId}`, {
                name: itemName,
                description: itemDescription,
                price: parseFloat(itemPrice),
                quantity: itemQuantity,
                image: itemImageURL,
                datePosted: itemDatePosted,
                rating: itemRating,
                id : itemId,
                sellerId : sellerId
            })
            .then((response) => {
                console.log(response);
            })
            .catch((error) => {
                console.log(error);
            });
        } else {
            axios.post('http://localhost:3000/items', {
                name: itemName,
                description: itemDescription,
                price: parseFloat(itemPrice),
                quantity: itemQuantity,
                image: itemImageURL,
                datePosted: new Date().toISOString(),
                rating: itemRating,
                sellerId : sellerId
            })
            .then((response) => {
                console.log(response);
            })
            .catch((error) => {
                console.log(error);
            });
    
    
    }
}

  return (
    <>
        <h2>ITEM FORM</h2>
        <form onSubmit={handleSubmit}>
            <label>Item name</label>
                <input type="text" placeholder={"Item Name"} value={itemName} onChange={(e => setItemName(e.target.value))} />
            <label>Item description</label>
                <input type="text" placeholder={"Item Description"} value={itemDescription}  onChange={(e => setItemDescription(e.target.value))} />
            <label>Item price</label>
                <input type="text" placeholder={"Item Price"} value={itemPrice}  onChange={handlePriceChange} />
            <label>Item quantity</label>
                <input type="number" placeholder={"Item Quantity"} value={itemQuantity} onChange={(e => setItemQuantity(Number(e.target.value)))} />
            <label>Item image URL</label>
                <input type="text" placeholder={"Item Image URL"} value={itemImageURL} onChange={(e => setItemImageURL(e.target.value))}/>

            <button type="submit">Add</button>
        </form>
    </>
  );
}

export default ItemForm;