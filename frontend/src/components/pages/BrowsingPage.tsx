import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import 'bootstrap/dist/css/bootstrap.min.css'; // Import Bootstrap CSS
import 'bootstrap/dist/js/bootstrap.bundle.min'; // Import Bootstrap JS
import ItemCard, { DisplayType } from '../common/ItemCard';
import axios from 'axios';
import { useEffect, useState } from 'react';
import { IItem } from '../../interfaces/IItem';
const itemsOk: IItem[] = [{
  id: 1,
  name: 'Item 1',
  price: 10,
  description: 'Description 1',
  image: 'Image 1',
  sellerId: 1,
  rating: 5,
  stock: 1,
  datePosted: '2021-10-10'
},{
  id: 2,
  name: 'Item 2',
  price: 20,
  description: 'Description 2',
  image: 'Image 2',
  sellerId: 2,
  rating: 4,
  stock: 2,
  datePosted: '2021-10-11'
},
{
  id: 3,
  name: 'Item 1',
  price: 10,
  description: 'Description 1',
  image: 'Image 1',
  sellerId: 1,
  rating: 5,
  stock: 1,
  datePosted: '2021-10-10'
},{
  id: 4,
  name: 'Item 2',
  price: 20,
  description: 'Description 2',
  image: 'Image 2',
  sellerId: 2,
  rating: 4,
  stock: 2,
  datePosted: '2021-10-11'
},
{
  id: 5,
  name: 'Item 1',
  price: 10,
  description: 'Description 1',
  image: 'Image 1',
  sellerId: 1,
  rating: 5,
  stock: 1,
  datePosted: '2021-10-10'
},{
  id: 6,
  name: 'Item 2',
  price: 20,
  description: 'Description 2',
  image: 'Image 2',
  sellerId: 2,
  rating: 4,
  stock: 2,
  datePosted: '2021-10-11'
},
{
  id: 7,
  name: 'Item 1',
  price: 10,
  description: 'Description 1',
  image: 'Image 1',
  sellerId: 1,
  rating: 5,
  stock: 1,
  datePosted: '2021-10-10'
},{
  id: 8,
  name: 'Item 2',
  price: 20,
  description: 'Description 2',
  image: 'Image 2',
  sellerId: 2,
  rating: 4,
  stock: 2,
  datePosted: '2021-10-11'
}]

function BrowsingPage() {


  const [items, setItems] = useState<IItem[]>(itemsOk);
    const navigate = useNavigate();
    const [error, setError] = useState<string>('');
    function compare(a: IItem, b: IItem) {
        if (a.name < b.name) {
            return -1;
        }
        if (a.name > b.name) {
            return 1;
        }
        return 0;
    }
    let getItems = async () => {
        let response = await axios.get("http://localhost:8080/items", {
            withCredentials: true
        }).then((response) => {
            setItems(response.data);
        }).catch((error) => {
            console.error(error);
            console.error('Error fetching items:', error);
            setError('Failed to fetch items.');
        });}

    useEffect(() => {
        getItems();
    }, []);
  return (
    <div style={{backgroundColor: "#f4dca4"}}>
    <div  className="container row row-cols-1 row-cols-md-3 g-4" style={{ width: '60%', backgroundColor: "aliceblue"}}>
        <h1 style={{color: "blue"}}>Items</h1>
        {items.sort(compare).map((itemMap) => (
            //<ItemCard key={`item${itemMap.id}`} item={itemMap}></ItemCard>
            <ItemCard key={`item${itemMap.id}`} item={itemMap} type={DisplayType.NONOWNED}></ItemCard>
        ))}
    </div>
    </div>
  )
}

export default BrowsingPage
