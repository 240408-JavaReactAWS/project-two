import React, { useContext, useEffect, useState } from 'react'
import { UserContext } from '../../App';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import ItemCard, { DisplayType } from '../common/ItemCard';
import { IItem } from '../../interfaces/IItem';
import ItemForm from '../common/ItemForm';

export default function SellerItemsPage() {
    
    const[loading,setLoading]=useState(true);
    const seller=useContext(UserContext)
    const [addItem,setAddItem]=useState(false);
    const items:IItem[]=[
    {
        id: 1,
        sellerId: 1,
        name: "Item 1",
        description: "This is item 1",
        price: 10.99,
        stock: 5,
        image: "item1.jpg",
        datePosted: "2022-01-01",
        rating: 4.5
    },
    {
        id: 2,
        sellerId: 2,
        name: "Item 2",
        description: "This is item 2",
        price: 19.99,
        stock: 10,
        image: "item2.jpg",
        datePosted: "2022-01-02",
        rating: 3.8
    },
    {
        id: 3,
        sellerId: 1,
        name: "Item 3",
        description: "This is item 3",
        price: 14.99,
        stock: 3,
        image: "item3.jpg",
        datePosted: "2022-01-03",
        rating: 4.2
    }]
    
    const [sellersItems,setSellersItems]=useState<IItem[]>(items);

    const navigate = useNavigate()
    useEffect(()=>{
      let getSellersItems=async ()=>{
        try{
          if(seller==null){
            navigate("/login")
          }
          
          const response=await axios.get(process.env.REACT_APP_API_URL + `users/${seller.userId}/items`, {
            withCredentials: true, headers: { 'Content-Type': 'application/json', 'userId': seller.userId} 
          })
          if(response.status==200){
            setSellersItems(response.data)
            setLoading(false)
          }
          if(response.status==403){
            navigate("/")
          }

        }catch(error){
          navigate("/login")
      }}
        
      getSellersItems()    
    },[])

    function formToggle(e:React.SyntheticEvent){
      e.preventDefault()
      setAddItem(!addItem)

    }

    function addToItems(item:IItem){
      setSellersItems([...sellersItems,item])
      setAddItem(!addItem)
    }
    
  return (
    <div>
      {loading && <div>...loading</div>}
      {!loading && <div >
        {!addItem &&< button className='btn btn-primary' onClick={formToggle}>Add Item</button>}
        {addItem && <ItemForm addToItems={addToItems}/>}
        {addItem &&< button className='btn btn-primary' onClick={formToggle}>Collapse</button>}
        {sellersItems.map((item:IItem)=>{
          return(
            <div className='sellersItems' key={item.id}>
              <ItemCard item={item} type={DisplayType.OWNED} setSellersItems={setSellersItems}  />
            </div>
          )}) }
      </div> 
       } 
      
     </div>
  )
}
