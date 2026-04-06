import { useState, useEffect } from 'react';
import "../stylesheets/ExerciseSession.css"

import { API_URL, ESP32_URL } from '../utils/constants';
import ulnarImg from '../assets/ulnar.png';
import radialImg from '../assets/radial.png';
import allFourImg from '../assets/all_four.png';
import PatientFeedback from './PatientFeedback';

function ExerciseSession({ userId }) {
    const [isRunning, setIsRunning] = useState(false);
    const [startTime, setStartTime] = useState(null);
    const [currentTime, setCurrentTime] = useState(0);
    const [showConfirmation, setShowConfirmation] = useState(false);
    const [confirmationMessage, setConfirmationMessage] = useState('');
    const [exerciseLevel, setExerciseLevel] = useState(1);
    const [selectedMovement, setSelectedMovement] = useState('all-four');
    const [esp32Connected, setEsp32Connected] = useState(false);

    useEffect(() => {
        checkEsp32Connection();
    }, []);

    useEffect(() => {
        let interval;
        if (isRunning) {
            interval = setInterval(() => {
                setCurrentTime(Math.floor((new Date() - startTime) / 1000));
            }, 1000);
        }
        return () => clearInterval(interval);
    }, [isRunning, startTime]);

    const checkEsp32Connection = async () => {
        try {
            const response = await fetch(`${ESP32_URL}/`);
            if (response.ok) {
                setEsp32Connected(true);
            }
        } catch (error) {
            setEsp32Connected(false);
            console.error("ESP32 connection error:", error);
        }
    };

    const sendToEsp32 = async (level, movement) => {
        if (!esp32Connected) return;

        try {
            const response = await fetch(`${ESP32_URL}/message`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `level=${level}&movement=${movement}`
            });
            console.log('ESP32 response:', await response.text());
        } catch (error) {
            console.error('Error sending to ESP32:', error);
        }
    };

    const handleStart = () => {
        // if (esp32Connected) {
            setIsRunning(true);
            setStartTime(new Date());
            setCurrentTime(0);
            setShowConfirmation(false);
            sendToEsp32(exerciseLevel, selectedMovement);
        // }
    };

    const handleStop = async () => {
        const endTime = new Date();
        const durationInMs = endTime - startTime;
        const durationInMinutes = durationInMs / (1000 * 60);
        const roundedDuration = Math.round(durationInMinutes * 10) / 10;
        setIsRunning(false);
        // sendToEsp32("stop", "none");

        // TEMPORARY COMPONENT: Feedback form to collect patient input after exercise session ends
        // To be integrated with the backend and database in the future for storing patient feedback alongside exercise session data

        // try {
        //     const response = await fetch(`${API_URL}/patients/${userId}/exercise`, {
        //         method: 'POST',
        //         headers: {
        //             'Content-Type': 'application/json',
        //         },
        //         body: JSON.stringify({
        //             userId: userId,
        //             date: startTime.toISOString(),
        //             duration: roundedDuration,
        //             movement: selectedMovement
        //         })
        //     });

        //     if (!response.ok) {
        //         throw new Error('Network response was not ok');
        //     }

        //     const data = await response.text();
        //     setConfirmationMessage(`Exercise session saved successfully! Duration: ${formatTime(currentTime)}, Movement: ${selectedMovement}`);
        //     setShowConfirmation(true);
        //     console.log('Data sent successfully:', data);
        // } catch (error) {
        //     setConfirmationMessage('Failed to save exercise session. Please try again.');
        //     setShowConfirmation(true);
        //     console.error('Error sending data:', error);
        // }
    };

    const handleLevelChange = (e) => {
        const newLevel = parseInt(e.target.value);
        setExerciseLevel(newLevel);
        // if (isRunning) {
        //     sendToEsp32(newLevel);
        // }
    };

    const formatTime = (seconds) => {
        const mins = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
    };

    return (
        <div className='exercise-container'>
            <div className='exercise-container-contents'>
                <h2>Begin Rehabilitation Exercises</h2>

                <section className='usage-instructions'>
                    <h3>How to use:</h3>
                    <ol>
                        <li>Review the movement patterns below.</li>
                        <li>Select your target movement (Radial, Ulnar, or All Fingers).</li>
                        <li>Set the intensity level using the slider.</li>
                        <li>Press <b>Start</b> to begin. Press <b>Stop</b> to finish and save your progress.</li>
                    </ol>
                </section>

                <div className="movement-gallery">
                    <div className="gallery-item">
                        <img src={radialImg} alt="Radial Movement" />
                        <p><b>Radial (Index + Middle)</b><br />Focuses on precision and pinching.</p>
                    </div>
                    <div className="gallery-item">
                        <img src={ulnarImg} alt="Ulnar Movement" />
                        <p><b>Ulnar (Ring + Little)</b><br />Focuses on power and stabilization.</p>
                    </div>
                    <div className="gallery-item">
                        <img src={allFourImg} alt="All Fingers" />
                        <p><b>All Four Fingers</b><br />Primary synergy for gross object grasping.</p>
                    </div>
                </div>

                <div className="exercise-session-container">

                    <div className="form-section">
                        <h4>Select Movement Pattern</h4>
                        <div className="movement-options">
                            {['radial', 'ulnar', 'all-four'].map((pattern) => (
                                <label key={pattern} className="radio-label">
                                    <input
                                        type="radio"
                                        value={pattern}
                                        checked={selectedMovement === pattern}
                                        onChange={(e) => setSelectedMovement(e.target.value)}
                                        disabled={isRunning}
                                    />
                                    {pattern.charAt(0).toUpperCase() + pattern.slice(1)}
                                </label>
                            ))}
                        </div>
                    </div>


                    <div className="level-selection">
                        <label htmlFor="exercise-level">Exercise Level:</label>
                        <div className="level-slider-container">
                            <input
                                type="range"
                                id="exercise-level"
                                min="1"
                                max="10"
                                value={exerciseLevel}
                                onChange={handleLevelChange}
                                className="level-slider"
                                // disabled={!esp32Connected | isRunning}
                                disabled={isRunning}
                            />
                            <span className="level-value">{exerciseLevel}</span>
                        </div>
                        {/* {!esp32Connected && (
                            <div className="connection-warning">
                                Warning: ESP32 device not connected
                            </div>
                        )} */}
                    </div>

                    <div className="timer-display">
                        {formatTime(currentTime)}
                    </div>

                    <div className="button-container">
                        {!isRunning ? (
                            <button onClick={handleStart} className="start-button">
                                Start Exercise
                            </button>
                        ) : (
                            <button onClick={handleStop} className="stop-button">
                                Stop Exercise
                            </button>
                        )}
                    </div>

                    {showConfirmation && (
                        <div className={`confirmation-message ${confirmationMessage.includes('successfully') ? 'success' : 'error'}`}>
                            {confirmationMessage}
                        </div>
                    )}
                </div>
                {/* <PatientFeedback onSave={(feedback) => {
                    console.log("Feedback saved:", feedback);
                }} onCancel={() => {
                    console.log("Feedback skipped");
                }}></PatientFeedback> */}
            </div>
        </div>
    );
}

export default ExerciseSession;