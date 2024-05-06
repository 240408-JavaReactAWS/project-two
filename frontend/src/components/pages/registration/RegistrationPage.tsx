import React, { ChangeEvent, useState } from 'react';
import axios from 'axios';
import {
  MDBBtn,
  MDBContainer,
  MDBRow,
  MDBCol,
  MDBCard,
  MDBCardBody,
  MDBInput
} from 'mdb-react-ui-kit';


function RegistrationPage() {
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [address, setAddress] = useState('');
    const [city, setCity] = useState('');
    const [state, setState] = useState('');
    const [zipCode, setZipCode] = useState('');
    const [country, setCountry] = useState('');
    const [message, setMessage] = useState('');

    const handleUserInfo = (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        switch (name) {
            case 'email': setEmail(value); break;
            case 'username': setUsername(value); break;
            case 'password': setPassword(value); break;
            case 'firstName': setFirstName(value); break;
            case 'lastName': setLastName(value); break;
            default: break;
        }
    };

    const handleAddressInfo = (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        switch (name) {
            case 'address': setAddress(value); break;
            case 'city': setCity(value); break;
            case 'state': setState(value); break;
            case 'zipCode': setZipCode(value); break;
            case 'country': setCountry(value); break;
            default: break;
        }
    };

    const handleRegister = async () => {
        try {
            let response = await axios.post('http://localhost:8080/users/register', {
                email, username, password, firstName, lastName,
                address, city, state, zipCode, country
            }, {
                withCredentials: true, headers: { 'Content-Type': 'application/json', 'username': username}
            });
            if (response.status === 201) {
                setMessage('User registered successfully');

            }
        } catch (error: any) {
            setMessage('Registration failed: ' + error.response.data.message);
        }
    };

    return (
        <MDBContainer fluid className='h-custom'>
            <MDBRow className='d-flex justify-content-center align-items-center h-100'>
                <MDBCol col='12' className='m-5'>
                    <MDBCard className='card-registration card-registration-2' style={{ borderRadius: '15px' }}>
                        <MDBCardBody className='p-0'>
                            <MDBRow>
                                <MDBCol md='6' className='p-5 bg-white'>
                                    <h3 className="fw-normal mb-5" style={{ color: '#4835d4' }}>Registration</h3>
                                    <MDBRow>
                                        <MDBCol md='6'>
                                            <MDBInput wrapperClass='mb-4' label='First Name' size='lg' name='firstName' onChange={handleUserInfo}/>
                                        </MDBCol>
                                        <MDBCol md='6'>
                                            <MDBInput wrapperClass='mb-4' label='Last Name' size='lg' name='lastName' onChange={handleUserInfo}/>
                                        </MDBCol>
                                    </MDBRow>
                                    <MDBInput wrapperClass='mb-4' label='Username' size='lg' name='username' onChange={handleUserInfo}/>
                                    <MDBRow>
                                        <MDBCol md='6'>
                                            <MDBInput wrapperClass='mb-4' label='Email' size='lg' name='email' onChange={handleUserInfo}/>
                                        </MDBCol>
                                        <MDBCol md='6'>
                                            <MDBInput wrapperClass='mb-4' label='Password' size='lg' name='password' type='password' onChange={handleUserInfo}/>
                                        </MDBCol>
                                    </MDBRow>
                                </MDBCol>
                                <MDBCol md='6' className='bg-indigo p-5'>
                                    <h3 className="fw-normal mb-5 text-white">Contact Details</h3>
                                    <MDBInput wrapperClass='mb-4' labelClass='text-white' label='Street + Number' size='lg' name='address' onChange={handleAddressInfo}/>
                                    <MDBRow>
                                        <MDBCol md='5'>
                                            <MDBInput wrapperClass='mb-4' labelClass='text-white' label='Zip Code' size='lg' name='zipCode' onChange={handleAddressInfo}/>
                                        </MDBCol>
                                        <MDBCol md='7'>
                                            <MDBInput wrapperClass='mb-4' labelClass='text-white' label='City' size='lg' name='city' onChange={handleAddressInfo}/>
                                        </MDBCol>
                                    </MDBRow>
                                    <MDBInput wrapperClass='mb-4' labelClass='text-white' label='Country' size='lg' name='country' onChange={handleAddressInfo}/>
                                    <MDBRow>
                                        <MDBCol md='5'>
                                            <MDBInput wrapperClass='mb-4' labelClass='text-white' label='State' size='lg' name='state' onChange={handleAddressInfo}/>
                                        </MDBCol>
                                    
                                    </MDBRow>
                                    <MDBBtn color='light' size='lg' onClick={handleRegister}>Register</MDBBtn>
                                </MDBCol>
                            </MDBRow>
                        </MDBCardBody>
                    </MDBCard>
                </MDBCol>
            </MDBRow>
        </MDBContainer>
    );
}

export default RegistrationPage;
