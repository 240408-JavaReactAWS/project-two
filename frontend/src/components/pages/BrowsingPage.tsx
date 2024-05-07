import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import 'bootstrap/dist/css/bootstrap.min.css'; // Import Bootstrap CSS
import 'bootstrap/dist/js/bootstrap.bundle.min'; // Import Bootstrap JS
import ItemCard from '../common/ItemCard';
import axios from 'axios';
import { useEffect, useState } from 'react';
import { IItem } from '../../interfaces/IItem';

function BrowsingPage() {


  const [items, setItems] = useState<IItem[]>([]);
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
    <div>
        <table>
        <thead>
            <tr>
                <th>Item Name</th>
                <th>Price</th>
                <th>Image</th>
                <th>Rating</th>
                <th>Stock</th>
            </tr>
        </thead>
        <tbody>
        {items.sort(compare).map((itemMap) => (
            //<ItemCard key={`item${itemMap.id}`} item={itemMap}></ItemCard>
            <tr>
                <td>{itemMap.name}</td>
                <td>{itemMap.price}</td>
                <td>{itemMap.image}</td>
                <td>{itemMap.rating}</td>
                <td>{itemMap.stock}</td>
            </tr>
        ))}
        </tbody>
        </table>
    </div>
  )
}

export default BrowsingPage
