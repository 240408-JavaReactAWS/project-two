import React from 'react';
import { Col } from 'react-bootstrap';
import StarRating from './StarRating';
import { IReview } from '../../../interfaces/IReview';

interface Props {
  review: IReview;
}

const ReviewCard: React.FC<Props> = ({ review }) => {
  return (
    <Col lg={6} className="mb-3">
      <div className="border p-3">
        <StarRating rating={review.rating} clickable={false} />
        <p>{review.reviewText}</p>
      </div>
    </Col>
  );
};

export default ReviewCard;
