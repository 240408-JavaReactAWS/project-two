import React, {SyntheticEvent, useState, useEffect, useContext} from 'react';
import { UserContext } from '../../App';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';


function LoginPage() {
    
  const {userId, setUserId} = useContext(UserContext)

  const navigate = useNavigate()

  const [email, setEmail] = useState<string>('')
  const [password, setPassword] = useState<string>('')
  const [showError, setShowError] = useState(false)

  let changeEmail = (e: SyntheticEvent) => {
    setEmail((e.target as HTMLInputElement).value)
  }

  let changePassword = (e: SyntheticEvent) => {
    setPassword((e.target as HTMLInputElement).value)
  }

  useEffect(() => { 
    let logout = async() => {
      try {
        let res = await axios.get(process.env.REACT_APP_API_URL + '/users/logout', {
            withCredentials: true, headers: { 'Content-Type': 'application/json', 'userId': userId}
        })
        if (res.status === 200) {
            console.log("Logout Successful")
            setUserId(-1)
        }
      } catch (error) {
        alert("There was an error logging out")
        console.log(error)
      }     
    }

    if (userId !== null) {
      //logout()
      setUserId(null);
    }

  }, [])
  
  let login = async() => {
    try {
      let res = await axios.post(process.env.REACT_APP_API_URL + '/users/login', {
          email: email,
          password: password
      }, {
          withCredentials: true, headers: { 'Content-Type': 'application/json' }
      })
      if (res.status === 200) {
          console.log("Login Successful")
          setUserId(res.data.userId)
          navigate('/')
      }
    } catch (error) {
      setShowError(true)
    }

    setEmail('')
    setPassword('')
  }
  
  return (
    <div className="loginPage">
      <h1>Login Page</h1>
      <div className="loginBody">
        
        {showError ? <p className="errorMessage">Email or Password Incorrect!</p> : null}

        <input type="text" value={email} placeholder="Email Address" onChange={changeEmail} />
        <input type="password" value={password} placeholder="Password" onChange={changePassword} />

        <div className="buttonSet">
          <button className="loginButton" onClick={login}>Login</button>
          <button className="loginButton" onClick={() => navigate('/register')}>Create New Account</button>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;