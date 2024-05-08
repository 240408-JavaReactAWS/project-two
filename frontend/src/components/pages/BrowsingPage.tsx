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
  image: 'https://www.foodandwine.com/thmb/C8XvnSkIMvz2XewXFDB_JYK-mSU=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Perfect-Sandwich-Bread-FT-RECIPE0723-dace53e15a304942acbc880b0ae34f5a.jpg',
  sellerId: 1,
  rating: 5,
  stock: 1,
  datePosted: '2021-10-10'
},{
  id: 2,
  name: 'Item 2',
  price: 20,
  description: 'Description 2',
  image: 'https://www.foodandwine.com/thmb/C8XvnSkIMvz2XewXFDB_JYK-mSU=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Perfect-Sandwich-Bread-FT-RECIPE0723-dace53e15a304942acbc880b0ae34f5a.jpg',
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
  image: 'https://www.foodandwine.com/thmb/C8XvnSkIMvz2XewXFDB_JYK-mSU=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Perfect-Sandwich-Bread-FT-RECIPE0723-dace53e15a304942acbc880b0ae34f5a.jpg',
  sellerId: 1,
  rating: 5,
  stock: 1,
  datePosted: '2021-10-10'
},{
  id: 4,
  name: 'Item 2',
  price: 20,
  description: 'Description 2',
  image: 'https://www.foodandwine.com/thmb/C8XvnSkIMvz2XewXFDB_JYK-mSU=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Perfect-Sandwich-Bread-FT-RECIPE0723-dace53e15a304942acbc880b0ae34f5a.jpg',
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
  image: 'https://www.foodandwine.com/thmb/C8XvnSkIMvz2XewXFDB_JYK-mSU=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Perfect-Sandwich-Bread-FT-RECIPE0723-dace53e15a304942acbc880b0ae34f5a.jpg',
  sellerId: 1,
  rating: 5,
  stock: 1,
  datePosted: '2021-10-10'
},{
  id: 6,
  name: 'Item 2',
  price: 20,
  description: 'Description 2',
  image: 'https://www.foodandwine.com/thmb/C8XvnSkIMvz2XewXFDB_JYK-mSU=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Perfect-Sandwich-Bread-FT-RECIPE0723-dace53e15a304942acbc880b0ae34f5a.jpg',
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
  image: 'https://www.foodandwine.com/thmb/C8XvnSkIMvz2XewXFDB_JYK-mSU=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Perfect-Sandwich-Bread-FT-RECIPE0723-dace53e15a304942acbc880b0ae34f5a.jpg',
  sellerId: 1,
  rating: 5,
  stock: 1,
  datePosted: '2021-10-10'
},{
  id: 8,
  name: 'Item 2',
  price: 20,
  description: 'Description 2',
  image: 'https://www.foodandwine.com/thmb/C8XvnSkIMvz2XewXFDB_JYK-mSU=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/Perfect-Sandwich-Bread-FT-RECIPE0723-dace53e15a304942acbc880b0ae34f5a.jpg',
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
    <>
    <header style={{paddingBottom:"40px"}}>
      <h1 style={{fontSize:"5rem", textAlign:"center" }}>Items</h1>
    </header>
    <div className="container w-90 h-100 d-flex align-items-center justify-content-center" style={{backgroundColor: "#fcead6"}}>

    <div  className="row row-cols-1 g-4 d-flex align-items-center justify-content-center flex-wrap" style={{ width: '80%'}}>
        
        {items.sort(compare).map((itemMap) => (
            //<ItemCard key={`item${itemMap.id}`} item={itemMap}></ItemCard>
            <div className="col">
              <ItemCard key={`item${itemMap.id}`} item={itemMap} type={DisplayType.NONOWNED}></ItemCard>
            </div>))}
    </div>
    </div>
    </>
  )
}

export default BrowsingPage
