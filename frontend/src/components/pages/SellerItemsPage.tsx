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
    
    const [sellersItems,setSellersItems]=useState<IItem[]>([]);

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

    function handleAdditem(e:React.SyntheticEvent){
      e.preventDefault()
      setAddItem(!addItem)

    }
    
  return (
    <div>
      {loading && <div>...loading</div>}
      {!loading && <div >
        <button className='btn btn-primary' onClick={handleAdditem}>Add Item</button>
        {/* {addItem && <ItemForm sellerId={seller.userId} itemId={null} />} */}
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
