import React from 'react';

interface Props {
  rating: number;
  clickable: boolean;
  onStarClick?: (rating: number) => void;
}

const StarRating: React.FC<Props> = ({ rating, clickable, onStarClick }) => {
  const stars = [];
  for (let i = 0; i < 5; i++) {
    stars.push(
      <span
        key={i}
        style={{ color: i < rating ? '#ffc107' : '#e4e5e9', cursor: clickable ? 'pointer' : 'default' }}
        onClick={() => clickable && onStarClick && onStarClick(i + 1)}
      >
        {i < Math.floor(rating) || Math.ceil(rating) <= i ? '★' : '☆'}
      </span>
    );
  }

  return <>{stars}</>;
};

export default StarRating;
