import React, { useContext, useState } from 'react';
import { Form, Button } from 'react-bootstrap';
import StarRating from './StarRating';
import axios from 'axios';
import { IReview } from '../../../interfaces/IReview';
import { UserContext } from '../../../App';
import './ReviewForm.css';
import { IItem } from '../../../interfaces/IItem';

interface Props {
  item: IItem;
  onClose: () => void;
  reviews: IReview[];
  setReviews: React.Dispatch<React.SetStateAction<IReview[]>>;
}

const ReviewForm: React.FC<Props> = ({ item, onClose, reviews, setReviews }) => {
  const [rating, setRating] = useState<number | null>(null);
  const [text, setText] = useState('');
  const { userId } = useContext(UserContext)

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Handle form submission
    // console.log('Rating:', rating);
    // console.log('Review Text:', text);
    /* Add review to the database */
    if (!userId) {
        alert('You must be logged in to leave a review.');
        return;
    }

    axios.post(`${process.env.REACT_APP_API_URL}/items/${item.itemId}/reviews`, { rating, text }, {
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
            if (error.response.status == 403) {
              userId == item.user.userId ? alert('You cant leave a review cause you own this listing.'): alert('You can leave only 1 review after buying an item.');
            }else{
            console.error('Error adding review:', error);
            }
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
            clickable={true}
            onStarClick={setRating}
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
      <br/>
      {/* Submit and close buttons */}
      <div className="d-flex justify-content-between">
        <Button type="submit" variant="primary">Submit Review</Button>
      </div>
      <br/>
    </Form>
  );
};

export default ReviewForm;