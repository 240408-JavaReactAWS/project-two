import React, {createContext, useContext, useState, useEffect} from 'react';
import logo from './logo.svg';
import './App.css';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import LoginPage from './components/pages/LoginPage';
import { IUser } from './interfaces/IUser';

interface contextInterface {
  user: IUser,
  setUser: React.Dispatch<React.SetStateAction<IUser>>
}

export const UserContext = createContext<contextInterface>({user: {}, setUser: () => {}})

function App() {

  const [user, setUser] = useState<IUser>({})

  return (
    <div className="content">
      <header className="header"></header>
      <UserContext.Provider value={{user, setUser}}>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
          </Routes>
        </BrowserRouter>
      </UserContext.Provider>
    </div>
  );
}

export default App;
