import React, { useState } from 'react';
import './StarRating.css';

interface Props {
  rating: number;
  clickable: boolean;
  onStarClick?: (rating: number) => void;
}

const StarRating: React.FC<Props> = ({ rating, clickable, onStarClick }) => {
  const [hoverRating, setHoverRating] = useState<number>(0);
  const stars = [];

  for (let i = 0; i < 5; i++) {
    const starColor = i < (hoverRating || rating) ? '#ffc107' : '#e4e5e9';
    stars.push(
      <span
        key={i}
        style={{ color: starColor, cursor: clickable ? 'pointer' : 'default' }}
        onMouseEnter={() => clickable && setHoverRating(i + 1)}
        onMouseLeave={() => clickable && setHoverRating(0)}
        onClick={() => clickable && onStarClick && onStarClick(i + 1)}
      >
        {i < Math.floor(rating) || Math.ceil(rating) <= i ? '★' : '☆'}
      </span>
    );
  }

  return <div className="star-rating">{stars}</div>;
};

export default StarRating;
