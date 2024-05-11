import React, {createContext, useContext, useState, useEffect} from 'react';
import logo from './logo.svg';
import './App.css';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import LoginPage from './components/pages/LoginPage';
import { IUser } from './interfaces/IUser';
import Nav from './components/common/Nav';
import SellerItemsPage from './components/pages/SellerItemsPage';
import BrowsingPage from './components/pages/BrowsingPage';
import CartPage from './components/pages/CartPage';
import RegistrationPage from './components/pages/RegistrationPage/RegistrationPage';
import OrderHistoryPage from './components/pages/OrderHistoryPage';
import ItemPage from './components/pages/ItemPage/ItemPage';
import CheckoutPage from './components/pages/CheckoutPage/CheckoutPage';

interface contextInterface {
  userId: any,
  setUserId: React.Dispatch<React.SetStateAction<any>>,
  cartItems: number[],
  setCartItems: React.Dispatch<React.SetStateAction<number[]>>,
}

export const UserContext = createContext<contextInterface>(
  {userId: null, 
  setUserId: () => {},
  cartItems: [],
  setCartItems: () => {}
})

function App() {

  const [userId, setUserId] = useState<any>(null)
  const [cartItems, setCartItems] = useState<any>([])

  return (
    <div className="content">
      <header className="header"></header>
      <UserContext.Provider value={{userId, setUserId, cartItems, setCartItems}}>
        <BrowserRouter>
        <Nav/>
          <Routes>
            <Route path="/" element={<BrowsingPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegistrationPage />} />
            <Route path="/item/:itemId" element={<ItemPage />} />
            <Route path="/listings" element={<SellerItemsPage />} />
            <Route path="/orders" element={<OrderHistoryPage />} />
            <Route path="/orders/:itemId" element={<OrderHistoryPage />} />
            <Route path="/cart" element={<CartPage />} />
            <Route path="/checkout" element={<CheckoutPage />} />
            {/* <Route path="/order-review" element={<AfterOrderPage />} /> */}
          </Routes>
        </BrowserRouter>
      </UserContext.Provider>
    </div>
  );
}

export default App;
