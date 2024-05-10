import React, { useContext, useState } from 'react';
import { Form, Button } from 'react-bootstrap';
import StarRating from './StarRating';
import axios from 'axios';
import { IReview } from '../../../interfaces/IReview';
import { UserContext } from '../../../App';
import './ReviewForm.css';

interface Props {
  itemId: number | null;
  onClose: () => void;
  reviews: IReview[];
  setReviews: React.Dispatch<React.SetStateAction<IReview[]>>;
}

const ReviewForm: React.FC<Props> = ({ itemId, onClose, reviews, setReviews }) => {
  const [rating, setRating] = useState<number | null>(null);
  const [text, setText] = useState('');
  const { userId } = useContext(UserContext)

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Handle form submission
    console.log('Rating:', rating);
    console.log('Review Text:', text);
    /* Add review to the database */


    axios.post(`${process.env.REACT_APP_API_URL}/items/${itemId}/reviews`, { rating, text }, {
      withCredentials: true,
      headers: {
        'userId': userId
      }
    })
        .then(response => {
            // Handle success
            console.log('Review added:', response.data);
            setReviews([response.data, ...reviews]);

        })
        .catch(error => {
            // Handle error
            console.error('Error adding review:', error);
        });

    // Close the form
    onClose();
  };

  return (
    <Form onSubmit={handleSubmit} className="review-form">
      {/* Star rating */}
      <Form.Group controlId="rating">
        <Form.Label>Rating</Form.Label>
        <div className="d-flex">
          <StarRating
            rating={rating || 0}
            clickable={true} // Stars are clickable in ReviewForm
            onStarClick={(newRating: number) => setRating(newRating)}
          />
        </div>
      </Form.Group>
      {/* Review text */}
      <Form.Group controlId="reviewText">
        <Form.Label>Review</Form.Label>
        <Form.Control
          as="textarea"
          rows={3}
          value={text}
          onChange={(e) => setText(e.target.value)}
          required
        />
      </Form.Group>
      {/* Submit and close buttons */}
      <div className="d-flex justify-content-between">
        <Button type="submit" variant="primary">Submit Review</Button>
      </div>
    </Form>
  );
};

export default ReviewForm;