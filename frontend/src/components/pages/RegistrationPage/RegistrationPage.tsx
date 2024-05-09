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
import './Registration.css';
import { useNavigate } from 'react-router-dom';


function RegistrationPage() {
    const [formData, setFormData] = useState({
        email: '',
        userName: '',
        password: '',
        firstName: '',
        lastName: '',
        address: '',
        city: '',
        state: '',
        zipCode: '',
        country: ''
    });
    const [message, setMessage] = useState('');
    const navigate = useNavigate();  // React Router hook to navigate


    const handleUserInfo = (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleAddressInfo = (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleRegister = async () => {
        // Check if all fields are filled
        if (!formData.email.trim() || !formData.userName.trim() || !formData.password.trim() ||
            !formData.firstName.trim() || !formData.lastName.trim() || !formData.address.trim() ||
            !formData.city.trim() || !formData.state.trim() || !formData.zipCode.trim() || !formData.country.trim()) {
            setMessage('Please fill out all fields');
            return; // Stop the function if any field is empty
        }
        try {
            const fullAddress = `${formData.address}, ${formData.city}, ${formData.state}, ${formData.zipCode}, ${formData.country}`

            let response = await axios.post('${process.env.REACT_APP_API_URL}/users/register', {
                email: formData.email,
                userName: formData.userName,
                password: formData.password,
                firstName: formData.firstName,
                lastName: formData.lastName,
                address: fullAddress

            }, {
                withCredentials: true, headers: { 'Content-Type': 'application/json' }
            });
            if (response.status === 201) {
                setMessage('User registered successfully');
                navigate('/login')
            }
        } catch (error: any) {
            if (error.response && error.response.status === 409) {
                setMessage('Username already taken');
            } else {
                setMessage('Registration failed: ' + (error.response?.data.message || 'Unknown error'));
            }
        }
    };

    return (
        //This form and styling came from this site https://mdbootstrap.com/docs/react/extended/registration-form/
        <MDBContainer fluid className='h-custom'>
            <MDBRow className='d-flex justify-content-center align-items-center h-100'>
                <MDBCol col='12' className='m-5'>
                    <MDBCard className='card-registration card-registration-2' style={{ borderRadius: '15px' }}>
                        <MDBCardBody className='p-0'>
                            {/* Error Message Display */}
                            {message && (
                                <div className="alert alert-danger" role="alert">
                                    {message}
                                </div>
                            )}
                            <MDBRow>
                                <MDBCol md='6' className='p-5 bg-white'>
                                    <h3 className="fw-normal mb-5" style={{ color: '#4835d4' }}>Registration</h3>
                                    <MDBRow>
                                        <MDBCol md='6'>
                                            <MDBInput wrapperClass='mb-4' label='First Name' size='lg' name='firstName' onChange={handleUserInfo} />
                                        </MDBCol>
                                        <MDBCol md='6'>
                                            <MDBInput wrapperClass='mb-4' label='Last Name' size='lg' name='lastName' onChange={handleUserInfo} />
                                        </MDBCol>
                                    </MDBRow>
                                    <MDBInput wrapperClass='mb-4' label='Username' size='lg' name='userName' onChange={handleUserInfo} />
                                    <MDBRow>
                                        <MDBCol md='6'>
                                            <MDBInput wrapperClass='mb-4' label='Email' size='lg' name='email' onChange={handleUserInfo} />
                                        </MDBCol>
                                        <MDBCol md='6'>
                                            <MDBInput wrapperClass='mb-4' label='Password' size='lg' name='password' type='password' onChange={handleUserInfo} />
                                        </MDBCol>
                                    </MDBRow>
                                </MDBCol>
                                <MDBCol md='6' className='bg-indigo p-5'>
                                    <h3 className="fw-normal mb-5 text-white">Contact Details</h3>
                                    <MDBInput wrapperClass='mb-4' labelClass='text-white' label='Street Address / PO Box ' size='lg' name='address' onChange={handleAddressInfo} />
                                    <MDBRow>
                                        <MDBCol md='5'>
                                            <MDBInput wrapperClass='mb-4' labelClass='text-white' label='Zip Code' size='lg' name='zipCode' onChange={handleAddressInfo} />
                                        </MDBCol>
                                        <MDBCol md='7'>
                                            <MDBInput wrapperClass='mb-4' labelClass='text-white' label='City' size='lg' name='city' onChange={handleAddressInfo} />
                                        </MDBCol>
                                    </MDBRow>
                                    <MDBInput wrapperClass='mb-4' labelClass='text-white' label='Country' size='lg' name='country' onChange={handleAddressInfo} />
                                    <MDBRow>
                                        <MDBCol md='5'>
                                            <MDBInput wrapperClass='mb-4' labelClass='text-white' label='State' size='lg' name='state' onChange={handleAddressInfo} />
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