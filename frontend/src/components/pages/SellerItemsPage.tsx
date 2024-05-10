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
          
          const response=await axios.get(process.env.REACT_APP_API_URL + `/users/${seller.userId}/items`, {
            withCredentials: true, headers: { 'Content-Type': 'application/json', 'userId': seller.userId} 
          })
          if(response.status==200){
            setSellersItems(response.data)
            setLoading(false)
          }
          if(response.status==403){
            navigate("/")
          }
          if(response.status == 404){
            setSellersItems([])
            setLoading(false)
          }

        }catch(error){
          console.error(error)
          //navigate("/login")
          setLoading(false)
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
        <div className="container w-90 h-100 d-flex align-items-center justify-content-center" style={{backgroundColor: "#fcead6"}}>

        {!addItem &&< button className='btn btn-primary' z-index={3} onClick={formToggle}>Add Item</button>}<br/>
        {addItem && <ItemForm addToItems={addToItems}/>}
        {addItem &&< button className='btn btn-primary' z-index={3} onClick={formToggle}>Collapse</button>}<br/>
        
        <div  className="row row-cols-1 row-cols-md-3 g-4 d-flex align-items-center justify-content-center flex-wrap" style={{ width: '80%'}}>
      
        {sellersItems.map((item:IItem)=>{
          return(
            <div className='sellersItems' key={item.itemId}>
              <ItemCard item={item} type={DisplayType.OWNED} setSellersItems={setSellersItems}  />
            </div>
          )}) }
          </div>
      </div> 
      </div>
       } 
     </div>
  )
}
