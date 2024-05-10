import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { IReview } from '../../../interfaces/IReview';
import { IItem } from '../../../interfaces/IItem';
import ItemDetails from './ItemDetails';
import ReviewCard from './ReviewCard';
import ReviewForm from './ReviewForm';
import StarRating from './StarRating';
import { Button, Col, Container, Row } from 'react-bootstrap';
import './ItemPage.css';

// Dummy data
const dummyItem: IItem = {
  itemId: 1,
  sellerId: 123,
  name: 'Dummy Item',
  description: 'This is a dummy item for testing purposes',
  price: 19.99,
  stock: 10,
  image: 'https://m.media-amazon.com/images/I/71VxfcpYsqL._AC_UF894,1000_QL80_.jpg',
  datePosted: '2024-05-10',
  rating: 3.5 // Assuming the rating is out of 5
};


const dummyReviews: IReview[] = [
  {
    reviewId: 1,
    userId: 456,
    itemId: 1,
    rating: 4,
    text: 'Great123 product!',
    datePosted: '2024-05-11'
  },
  {
    reviewId: 2,
    userId: 789,
    itemId: 1,
    rating: 5,
    text: 'Amazing31254525 quality!',
    datePosted: '2024-05-12'
  },
  {
    reviewId: 1,
    userId: 456,
    itemId: 1,
    rating: 4,
    text: 'Great pro36456456duct!',
    datePosted: '2024-05-11'
  },
  {
    reviewId: 2,
    userId: 789,
    itemId: 1,
    rating: 5,
    text: 'Amazing27568568 quality!',
    datePosted: '2024-05-12'
  },
  {
    reviewId: 1,
    userId: 456,
    itemId: 1,
    rating: 4,
    text: 'Great prodfgdfgduct!',
    datePosted: '2024-05-11'
  },
  {
    reviewId: 2,
    userId: 789,
    itemId: 1,
    rating: 5,
    text: 'Amazing q2367hdfdgfuality!',
    datePosted: '2024-05-12'
  },
  {
    reviewId: 1,
    userId: 456,
    itemId: 1,
    rating: 4,
    text: 'Gr2316457457eat product!',
    datePosted: '2024-05-11'
  },
  {
    reviewId: 2,
    userId: 789,
    itemId: 1,
    rating: 5,
    text: 'Ama756889678978zing quality!',
    datePosted: '2024-05-12'
  },
  {
    reviewId: 1,
    userId: 456,
    itemId: 1,
    rating: 4,
    text: 'Great pvsdfsdvdfhfjfjroduct!',
    datePosted: '2024-05-11'
  },
  {
    reviewId: 2,
    userId: 789,
    itemId: 1,
    rating: 5,
    text: 'Amazing q7474574uality!',
    datePosted: '2024-05-12'
  },
  {
    reviewId: 1,
    userId: 456,
    itemId: 1,
    rating: 4,
    text: 'Great product!',
    datePosted: '2024-05-11'
  },
  {
    reviewId: 2,
    userId: 789,
    itemId: 1,
    rating: 5,
    text: 'Amazing 11111111quality!',
    datePosted: '2024-05-12'
  },
];

const ItemPage: React.FC = () => {
  const { itemId } = useParams();
  const [item, setItem] = useState<IItem | null>(null);
  const [reviews, setReviews] = useState<IReview[]>([]);
  const [showReviewForm, setShowReviewForm] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const navigate = useNavigate();

  useEffect(() => {
    setItem(dummyItem);
    setReviews(dummyReviews);
    setTotalPages(Math.ceil(dummyReviews.length / 3));
    // if (!itemId) navigate('/');
    // const fetchItemAndReviews = async () => {
    //   try {
    //     const itemResponse = await axios.get(`${process.env.REACT_APP_API_URL}/items/${itemId}`);
    //     const reviewsResponse = await axios.get(`${process.env.REACT_APP_API_URL}/items/${itemId}/reviews`);
    //     setItem(itemResponse.data);
    //     setReviews(reviewsResponse.data);
    //     setTotalPages(Math.ceil(reviewsResponse.data.length / 3));
    //   } catch (error) {
    //     console.error('Error fetching data:', error);
    //   }
    // };
    // fetchItemAndReviews();
  }, [itemId, navigate]);

  // Leave a review
  const toggleReviewForm = () => setShowReviewForm(prev => !prev);

  // Navigate reviews pages
  const handlePageChange = (newPage: number) => {
    if (newPage > 0 && newPage <= totalPages) {
      setCurrentPage(newPage);
    }
  }

  return (
    <Container className="mt-5">
      <ItemDetails item={item} />
      <br/>
      <Row className="review-row">
        <h4 className="total-rev">{reviews.length} reviews</h4>
        <Button onClick={toggleReviewForm} variant="secondary">
            {showReviewForm ? 'Hide Review Form' : 'Leave a Review'}
          </Button>
          {showReviewForm && <ReviewForm
              itemId={itemId ? parseInt(itemId) : null}
              onClose={toggleReviewForm}
              reviews={reviews}
              setReviews={setReviews}
            />}
        {reviews.slice((currentPage - 1) * 3, currentPage * 3).map((review, index) => (
          <ReviewCard key={index} review={review} />
        ))}
      </Row>
      <br/>
      <Row className="review-nav">
        <Button onClick={() => handlePageChange(1)} disabled={currentPage === 1}>{"<<"}</Button>
        <Button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1}>{"<"}</Button>
        <span>Page {currentPage} of {totalPages}</span>
        <Button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages}>{">"}</Button>
        <Button onClick={() => handlePageChange(totalPages)} disabled={currentPage === totalPages}>{">>"}</Button>
      </Row>
    </Container>
  );
};

export default ItemPage;