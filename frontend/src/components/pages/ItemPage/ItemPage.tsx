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

const ItemPage: React.FC = () => {
  const { itemId } = useParams();
  const [item, setItem] = useState<IItem | null>(null);
  const [reviews, setReviews] = useState<IReview[]>([]);
  const [showReviewForm, setShowReviewForm] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const navigate = useNavigate();

  useEffect(() => {
    if (!itemId) navigate('/');
    const fetchItemAndReviews = async () => {
      try {
        const itemResponse = await axios.get(`${process.env.REACT_APP_API_URL}/items/${itemId}`);
        const reviewsResponse = await axios.get(`${process.env.REACT_APP_API_URL}/items/${itemId}/reviews`);
        setItem(itemResponse.data);
        setReviews(reviewsResponse.data);
        setTotalPages(Math.ceil(reviewsResponse.data.length / 3));
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    fetchItemAndReviews();
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