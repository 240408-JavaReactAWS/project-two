import React, {SyntheticEvent, useState, useEffect, useContext} from 'react';
import { UserContext } from '../../App';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';


function LoginPage() {
    
  const {username, setUsername} = useContext(UserContext)

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
        let res = await axios.get('http://localhost:8080/users/logout', {
            withCredentials: true, headers: { 'Content-Type': 'application/json', 'username': username}
        })
        if (res.status === 200) {
            console.log("Logout Successful")
            setUsername('')
        }
      } catch (error) {
        alert("There was an error logging out")
        console.log(error)
      }
    }

    if (username !== '') {
      logout()
    }

  }, [])
  
  let login = async() => {
    try {
      let res = await axios.post('http://localhost:8080/users/login', {
          email: email,
          password: password
      }, {
          withCredentials: true, headers: { 'Content-Type': 'application/json', 'username': username}
      })
      if (res.status === 200) {
          console.log("Login Successful")
          setUsername(res.data.username)
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
        showError ? <p className="errorMessage">Email or Password Incorrect!</p> : null

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