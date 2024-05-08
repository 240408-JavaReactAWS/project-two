import React, { useState, useEffect } from 'react';
import { Button, Col, Container, Row } from 'react-bootstrap';
import { useParams, useNavigate } from 'react-router-dom';
import { IReview } from '../../../interfaces/IReview';
import { IItem } from '../../../interfaces/IItem';
import ItemDetails from './ItemDetails';
import ReviewCard from './ReviewCard';
import ReviewForm from './ReviewForm';
import axios from 'axios';

// Dummy data
const dummyItem: IItem = {
  id: 1,
  sellerId: 123,
  name: 'Dummy Item',
  description: 'This is a dummy item for testing purposes',
  price: 19.99,
  stock: 10,
  image: 'https://m.media-amazon.com/images/I/71VxfcpYsqL._AC_UF894,1000_QL80_.jpg',
  datePosted: '2024-05-10',
  rating: 4.5 // Assuming the rating is out of 5
};

const dummyReviews: IReview[] = [
  {
    id: 1,
    userId: 456,
    itemId: 1,
    rating: 4,
    reviewText: 'Great product!',
    reviewDate: '2024-05-11'
  },
  {
    id: 2,
    userId: 789,
    itemId: 1,
    rating: 5,
    reviewText: 'Amazing quality!',
    reviewDate: '2024-05-12'
  },
  {
    id: 1,
    userId: 456,
    itemId: 1,
    rating: 4,
    reviewText: 'Great product!',
    reviewDate: '2024-05-11'
  },
  {
    id: 2,
    userId: 789,
    itemId: 1,
    rating: 5,
    reviewText: 'Amazing quality!',
    reviewDate: '2024-05-12'
  },
  {
    id: 1,
    userId: 456,
    itemId: 1,
    rating: 4,
    reviewText: 'Great product!',
    reviewDate: '2024-05-11'
  },
  {
    id: 2,
    userId: 789,
    itemId: 1,
    rating: 5,
    reviewText: 'Amazing quality!',
    reviewDate: '2024-05-12'
  },
  {
    id: 1,
    userId: 456,
    itemId: 1,
    rating: 4,
    reviewText: 'Great product!',
    reviewDate: '2024-05-11'
  },
  {
    id: 2,
    userId: 789,
    itemId: 1,
    rating: 5,
    reviewText: 'Amazing quality!',
    reviewDate: '2024-05-12'
  },
  {
    id: 1,
    userId: 456,
    itemId: 1,
    rating: 4,
    reviewText: 'Great product!',
    reviewDate: '2024-05-11'
  },
  {
    id: 2,
    userId: 789,
    itemId: 1,
    rating: 5,
    reviewText: 'Amazing quality!',
    reviewDate: '2024-05-12'
  },
  {
    id: 1,
    userId: 456,
    itemId: 1,
    rating: 4,
    reviewText: 'Great product!',
    reviewDate: '2024-05-11'
  },
  {
    id: 2,
    userId: 789,
    itemId: 1,
    rating: 5,
    reviewText: 'Amazing quality!',
    reviewDate: '2024-05-12'
  },
];

const ItemPage = () => {
  const { itemId } = useParams();
  const [item, setItem] = useState<IItem | null>(null);
  const [reviews, setReviews] = useState<IReview[]>([]);
  const [showReviewForm, setShowReviewForm] = useState(false);
  const [reviewsToShow, setReviewsToShow] = useState(6); // Number of reviews to show initially
  const [reviewsToLoad, setReviewsToLoad] = useState(6); // Number of reviews to load each time
  const navigate = useNavigate();

  /* Testing Purpose */
  useEffect(() => {
    // Set dummy item data
    setItem(dummyItem);

    // Set dummy review data
    setReviews(dummyReviews);

  }, []);

  // Fetch data
  // useEffect(() => {
  //   /* Route Back if no itemId */
  //   if (!itemId) {
  //         navigate('/');
  //   }

  //   // Fetch item data
  //   axios.get(`${process.env.BACKEND_URL}/items/${itemId}`)
  //     .then(response => {
  //       setItem(response.data);
  //     })
  //     .catch(error => {
  //       console.error('Error fetching item data:', error);
  //     });

  //   // Fetch reviews data
  //   axios.get(`${process.env.BACKEND_URL}/items/${itemId}/reviews`)
  //     .then(response => {
  //       setReviews(response.data);
  //     })
  //     .catch(error => {
  //       console.error('Error fetching reviews:', error);
  //     });
  // },);


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
              userId={123} // Set the userId here
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
