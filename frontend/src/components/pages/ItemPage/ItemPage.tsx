import React, { useState, useEffect } from 'react';
import { Button, Col, Container, Row } from 'react-bootstrap';
import { useParams, useNavigate } from 'react-router-dom';
import { IReview } from '../../../interfaces/IReview';
import { IItem } from '../../../interfaces/IItem';
import ItemDetails from './ItemDetails';
import ReviewCard from './ReviewCard';
import ReviewForm from './ReviewForm';
import axios from 'axios';

const ItemPage = () => {
  const { itemId } = useParams();
  const [item, setItem] = useState<IItem | null>(null);
  const [reviews, setReviews] = useState<IReview[]>([]);
  const [showReviewForm, setShowReviewForm] = useState(false);
  const [reviewsToShow, setReviewsToShow] = useState(6); // Number of reviews to show initially
  const [reviewsToLoad, setReviewsToLoad] = useState(6); // Number of reviews to load each time
  const navigate = useNavigate();

  useEffect(() => {
    /* Route Back if no itemId */
    if (!itemId) {
          navigate('/');
    }

    // Fetch item data
    axios.get(`${process.env.REACT_APP_API_URL}/items/${itemId}`)
      .then(response => {
        setItem(response.data);
      })
      .catch(error => {
        console.error('Error fetching item data:', error);
      });

    // Fetch reviews data
    axios.get(`${process.env.REACT_APP_API_URL}/items/${itemId}/reviews`)
      .then(response => {
        setReviews(response.data);
      })
      .catch(error => {
        console.error('Error fetching reviews:', error);
      });
  },);


  // Function to handle leaving a review
  const handleLeaveReview = () => {
    setShowReviewForm(true);
  };

  // Function to close the review form
  const handleCloseReviewForm = () => {
    setShowReviewForm(false);
  };

  // Function to load more reviews
  const handleLoadMoreReviews = () => {
    setReviewsToShow((prevReviewsToShow) => prevReviewsToShow + reviewsToLoad);
  };

  return (
    <Container className="mt-5">
      <ItemDetails item={item || null} />
      {/* Reviews */}
      <Row className="mt-3">
        <h3>Reviews</h3>
      </Row>
      <Row>
        <Col>
          {showReviewForm ? (
            <ReviewForm
              itemId={itemId ? parseInt(itemId) : null}
              onClose={handleCloseReviewForm}
              reviews={reviews}
              setReviews={setReviews}
            />
          ) : (
            <Button variant="primary" onClick={handleLeaveReview}>
              Leave a review
            </Button>
          )}
          <Row>
            {reviews.slice(0, reviewsToShow).map((review, index) => (
              <ReviewCard key={index} review={review} />
            ))}
          </Row>
          {/* Display load more button if there are more reviews to load */}
          {reviewsToShow < reviews.length && (
            <div className="text-center mt-3">
              <Button onClick={handleLoadMoreReviews}>Load More</Button>
            </div>
          )}
        </Col>
      </Row>
    </Container>
  );
};

export default ItemPage;