import React, { useContext, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import 'bootstrap/dist/css/bootstrap.min.css'; // Import Bootstrap CSS
import 'bootstrap/dist/js/bootstrap.bundle.min'; // Import Bootstrap JS
import "./Nav.css"; // Import custom CSS
import axios from 'axios';
import { UserContext } from '../../App';
const Nav: React.FC = () => {

  const {username, setUsername} = useContext<any>(UserContext)
  const navigateTo = useNavigate();

  let logout = async() => {
    try {
      let res = await axios.get('http://localhost:8080/users/logout', {
          withCredentials: true, headers: { 'Content-Type': 'application/json', 'username': username}
      })
      if (res.status === 200) {
          console.log("Logout Successful")
          setUsername('')
          navigateTo('/')
      }
    } catch (error) {
      alert("There was an error logging out")
      console.log(error)
    }
  }

  return (
    <>
    <nav className="navbar navbar-expand-lg bg-body-tertiary">
      <div className="container-fluid">
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav">
            <li className="nav-item p-2">
              <Link to="/" className="nav-link">Nile</Link>  
            </li>
            <li className="nav-item p-2">
              <Link to="/" className="nav-link">Browse Products</Link>
            </li>
          {!username &&
            <li className="nav-item p-2">
              <Link to="/login" className="nav-link">Login</Link>
            </li>
          }
            {username && <><li className="nav-item p-2"><Link to="/listings" className="nav-link">My Listings</Link></li>
            <li className="nav-item p-2"><Link to="/orders" className="nav-link">My Orders</Link></li>
            <li className="nav-item p-2"><Link to="/cart" className="nav-link">Cart</Link></li>
            <li className="nav-item p-2"><Link to="/checkout" className="nav-link">Checkout</Link></li>
            <li className="nav-item p-2"><button onClick={logout} className="nav-link">Logout</button></li>
            </>}
          </ul>
        </div>
      </div>
      </nav>
    </>
  )
}

export default Nav;