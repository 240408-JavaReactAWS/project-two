import React, {SyntheticEvent, useState, useEffect, useContext} from 'react';
import { UserContext } from '../../App';

function LoginPage() {
    
    const {username, setUsername} = useContext(UserContext)


  
  
  
    return (
    <div className="login-page">
      <h1>Login Page</h1>
    </div>
  );
}

export default LoginPage;