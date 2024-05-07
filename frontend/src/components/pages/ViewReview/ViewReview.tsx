import React from 'react';
import './ViewReview.css';
import { IReview } from '../../../interfaces/IReview';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faStar } from '@fortawesome/free-solid-svg-icons'

function ViewReview(review: IReview) {
    return (
        <div className="card">
            <div className="card-body">
                <h3 className="card-title">Rating: 
                    <div className="rating">
                        {Array(review.rating).fill(<FontAwesomeIcon icon={faStar} />)}
                    </div>
                </h3>
                
                <h3 className="card-text">Review: {review.reviewText}</h3>
                <h3 className="card-text">Date Posted: {review.reviewDate}</h3>
            </div>
        </div>
    );
}

// this is the old code from before we added bootstrap card styling -- use if desired
{/*
function ViewReview(review: IReview) {
    return (
        <div className="review">
            
            <h3>Rating: 
                <div className="rating">
                    {Array(review.rating).fill(<FontAwesomeIcon icon={faStar} />)}
                </div>
            </h3>
            
            <h3>Review: {review.reviewText}</h3>
            <h3>Date Posted: {review.reviewDate}</h3>
        </div>
    );
}
*/}

export default ViewReview;