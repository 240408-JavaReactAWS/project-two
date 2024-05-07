import React, {createContext, useContext, useState, useEffect} from 'react';
import logo from './logo.svg';
import './App.css';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import LoginPage from './components/pages/LoginPage';
import { IUser } from './interfaces/IUser';
import Nav from './components/common/Nav';
import RegistrationPage from './components/pages/registration/RegistrationPage';
import ViewReview from './components/pages/ViewReview/ViewReview';

interface contextInterface {
  userId: any,
  setUserId: React.Dispatch<React.SetStateAction<any>>
}

export const UserContext = createContext<contextInterface>(
  {userId: null, 
  setUserId: () => {}})

function App() {

  const [userId, setUserId] = useState<any>(null)
  //below is test code for ViewReview
  // let review = {
  //   id: 1,
  //   userId: 2,
  //   itemId: 3,
  //   rating: 4,
  //   reviewText: "string1",
  //   reviewDate: "string2"
  //   }

  return (
    <div className="content">
      <header className="header"></header>
      <UserContext.Provider value={{userId, setUserId}}>
        <BrowserRouter>
        <Nav/>
          <Routes>
            {/* <Route path="/" element={<BrowsingPage />} /> */}
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegistrationPage />} />
            {/* <Route path="/item/:id" element={<ItemPage />} /> */}
            {/* <Route path="/listings" element={<SellerItemsPage />} /> */}
            {/* <Route path="/orders" element={<OrderHistoryPage />} /> */}
            {/* <Route path="/cart" element={<CartPage />} /> */}
            {/* <Route path="/checkout" element={<CheckoutPage />} /> */}
            {/* <Route path="/order-review" element={<AfterOrderPage />} /> */}

          </Routes>
        </BrowserRouter>

        {/* below is test code for ViewReview */}
        {/* <ViewReview {...review}> </ViewReview> */}
      </UserContext.Provider>
    </div>
  );
}

export default App;
