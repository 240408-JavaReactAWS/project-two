import React from 'react';

interface Props {
  rating: number;
  clickable: boolean;
  onStarClick?: (rating: number) => void;
}

const StarRating: React.FC<Props> = ({ rating, clickable, onStarClick }) => {
  const handleClick = (newRating: number) => {
    if (clickable && onStarClick) {
      onStarClick(newRating);
    }
  };

  const stars = [];

  for (let i = 0; i < 5; i++) {
    if (i < rating) {
      stars.push(
        <span
          key={i}
          className={`text-warning ${clickable ? 'cursor-pointer' : ''} star`}
          onClick={() => handleClick(i + 1)}
        >
          &#9733;
        </span>
      );
    } else {
      stars.push(
        <span
          key={i}
          className={`text-secondary ${clickable ? 'cursor-pointer' : ''} star`}
          onClick={() => handleClick(i + 1)}
        >
          &#9734;
        </span>
      );
    }
  }

  return <>{stars}</>;
};

export default StarRating;