import React, { useContext, useState } from 'react';
import { Form, Button } from 'react-bootstrap';
import StarRating from './StarRating';
import axios from 'axios';
import { IReview } from '../../../interfaces/IReview';
import { UserContext } from '../../../App';

interface Props {
  itemId: number | null;
  userId: number | null;
  onClose: () => void;
  reviews: IReview[];
  setReviews: React.Dispatch<React.SetStateAction<IReview[]>>;
}

const ReviewForm: React.FC<Props> = ({ itemId, userId, onClose, reviews, setReviews }) => {
  const [rating, setRating] = useState<number | null>(null);
  const [reviewText, setReviewText] = useState('');
  const {userId: contextUserId, setUserId} = useContext(UserContext)

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Handle form submission
    console.log('Rating:', rating);
    console.log('Review Text:', reviewText);
    /* Add review to the database */

    /* QUESTION What is Back end generating time or are WE generating time?
                What is the request Body?
     */
    axios.post(`${process.env.BACKEND_URL}/items/{itemId}/reviews`, { userId, rating, reviewText, reviewDate: new Date().toISOString()})
        .then(response => {
            // Handle success
            console.log('Review added:', response.data);
            setReviews([response.data, ...reviews]);

        })
        .catch(error => {
            // Handle error
            console.error('Error adding review:', error);
            // For testing purposes, add a mock review to the reviews list
            // let mockReview: IReview = {
            //   id: 1,
            //   userId: userId!,
            //   itemId: itemId!,
            //   rating: rating!,
            //   reviewText: reviewText,
            //   reviewDate: new Date().toISOString()
            // };
            // setReviews([mockReview, ...reviews]);
        });

    // Close the form
    onClose();
  };

  return (
    <Form onSubmit={handleSubmit}>
        <div className="d-flex justify-content-end">
        <Button variant="secondary" onClick={onClose}>x</Button>
        </div>
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
          value={reviewText}
          onChange={(e) => setReviewText(e.target.value)}
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