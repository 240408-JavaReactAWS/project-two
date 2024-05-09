export interface IReview {
    reviewId: number,
    userId?: number,
    itemId: number,
    rating: number,
    text: string,
    datePosted: string
}