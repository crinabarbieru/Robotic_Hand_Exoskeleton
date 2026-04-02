import React, { useState } from 'react';
import "../stylesheets/PatientFeedback.css";

function PatientFeedback({ onSave, onCancel }) {
    const [rating, setRating] = useState(0);
    const [comment, setComment] = useState("");
    const [hover, setHover] = useState(0);

    const handleSubmit = () => {
        if (rating === 0) return;
        onSave({ rating, comment });
    };

    return (
        <div className="feedback-overlay">
            <div className="feedback-card">
                <h3>Session Complete</h3>
                <p>How would you rate your comfort and performance today?</p>

                <div className="star-rating">
                    {[1, 2, 3, 4, 5].map((star) => (
                        <button
                            key={star}
                            type="button"
                            className={`star-button ${star <= (hover || rating) ? "active" : ""}`}
                            onClick={() => setRating(star)}
                            onMouseEnter={() => setHover(star)}
                            onMouseLeave={() => setHover(rating)}
                        >
                            ★
                        </button>
                    ))}
                </div>

                <div className="feedback-form-section">
                    <label>Additional Comments</label>
                    <textarea
                        placeholder="Any pain, stiffness, or improvements?"
                        value={comment}
                        onChange={(e) => setComment(e.target.value)}
                    />
                </div>

                <div className="feedback-actions">
                    <button className="skip-button" onClick={onCancel}>Skip</button>
                    <button 
                        className="save-feedback-button" 
                        disabled={rating === 0}
                        onClick={handleSubmit}
                    >
                        Save Progress
                    </button>
                </div>
            </div>
        </div>
    );
}

export default PatientFeedback;