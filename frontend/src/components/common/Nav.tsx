import React, { useState } from 'react'
import { Link } from 'react-router-dom'
import 'bootstrap/dist/css/bootstrap.min.css'; // Import Bootstrap CSS

const Nav: React.FC = () => {

  const [loggedIn, setLoggedIn] = useState<boolean>(true)

  return (
    <>
    <nav className="navbar navbar-expand-lg bg-body-tertiary">
      <div className="container-fluid">
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav">
            <li className="nav-item">
              <Link to="/" className="nav-link">Nile</Link>  
            </li>
            <li className="nav-item">
              <Link to="/products" className="nav-link">Browse Products</Link>
            </li>
          {!loggedIn &&
            <li className="nav-item">
              <Link to="/login" className="nav-link">Login</Link>
            </li>
          }
            {loggedIn && <><li className="nav-item"><Link to="#" className="nav-link">My Listings</Link></li>
            <li className="nav-item"><Link to="#" className="nav-link">My Orders</Link></li>
            <li className="nav-item"><Link to="#" className="nav-link">Cart</Link></li>
            <li className="nav-item"><Link to="#" className="nav-link">Checkout</Link></li>
            <li className="nav-item"><Link to="#" className="nav-link">Logout</Link></li>
            </>}
          </ul>
        </div>
      </div>
      </nav>
    </>
  )
}

export default Nav;