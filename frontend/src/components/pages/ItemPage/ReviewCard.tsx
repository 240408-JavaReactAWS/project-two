import React, { useState, useEffect } from 'react';
import axios from 'axios';
import StarRating from './StarRating';
import { Card } from 'react-bootstrap';
import { IReview } from '../../../interfaces/IReview';
import './ReviewCard.css';

interface Props {
    review: IReview
}

const ReviewCard: React.FC<Props> = ({ review }) => {
  const [username, setUsername] = useState('');

  useEffect(() => {
    const fetchUsername = async () => {
      try {
        const response = await axios.get(`${process.env.REACT_APP_API_URL}/users/${review.userId}`);
        setUsername(response.data.username);
      } catch (error) {
        console.error('Error fetching username', error);
        setUsername('User');
      }
    };

    if (review.userId) {
        fetchUsername();
    }
  }, [review.userId]);

  return (
    <Card className="review-card">
      <Card.Body>
        <Card.Title>{username}</Card.Title>
        <Card.Subtitle>
          <StarRating rating={review.rating} clickable={false} />
        </Card.Subtitle>
        <br/>
        <Card.Text>
          {review.text}
        </Card.Text>
        <Card.Footer className="text-muted">
          Posted on: {new Date(review.datePosted).toLocaleDateString()}
        </Card.Footer>
      </Card.Body>
    </Card>
  );
}

export default ReviewCard;