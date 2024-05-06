import React, {createContext, useContext, useState, useEffect} from 'react';
import logo from './logo.svg';
import './App.css';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import LoginPage from './components/pages/LoginPage';
import { IUser } from './interfaces/IUser';

interface contextInterface {
  username: string,
  setUsername: React.Dispatch<React.SetStateAction<string>>
}

export const UserContext = createContext<contextInterface>(
  {username: "", 
  setUsername: () => {}})

function App() {

  const [username, setUsername] = useState<string>("")

  return (
    <div className="content">
      <header className="header"></header>
      <UserContext.Provider value={{username, setUsername}}>
        <BrowserRouter>
          <Routes>
            {/* <Route path="/" element={<BrowsingPage />} /> */}
            <Route path="/login" element={<LoginPage />} />
            {/* <Route path="/register" element={<RegistrationPage />} /> */}
            {/* <Route path="/item/:id" element={<ItemPage />} /> */}
            {/* <Route path="/listings" element={<SellerItemsPage />} /> */}
            {/* <Route path="/orders" element={<OrderHistoryPage />} /> */}
            {/* <Route path="/cart" element={<CartPage />} /> */}
            {/* <Route path="/checkout" element={<CheckoutPage />} /> */}
            {/* <Route path="/order-review" element={<AfterOrderPage />} /> */}
          </Routes>
        </BrowserRouter>
      </UserContext.Provider>
    </div>
  );
}

export default App;
