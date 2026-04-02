import { useState, useEffect, useRef } from 'react';
import "../stylesheets/PassiveMode.css";

import { API_URL, ESP32_URL } from '../utils/constants';

function PassiveMode( {userId} ) {
    const [status, setStatus] = useState('idle'); // 'idle', 'calibrating', 'measuring', 'complete'
    const [baselineValue, setBaselineValue] = useState(null);
    const [currentValue, setCurrentValue] = useState(0);
    const [minBendValue, setMinBendValue] = useState(null);
    const [progress, setProgress] = useState(0);
    const [finalResults, setFinalResults] = useState(null);
    const [error, setError] = useState(null);
    const pollingInterval = useRef(null);

    const colors = {
        darkBlue: '#0D274D',
        midBlue: '#32538F',
        lightBlue: '#EFF7FF',
        accentBlue: '#4A90E2',
        secondaryBlue: '#7FB3FF',
        alertRed: '#FF6B6B',
        successGreen: '#4CAF50'
    };

    useEffect(() => {
        return () => {
            if (pollingInterval.current) {
                clearInterval(pollingInterval.current);
            }
        };
    }, []);

    useEffect(() => {
        if (status === 'calibrating') {
            const timer = setInterval(() => {
                setProgress(prev => {
                    const newProgress = prev + (100 / 30); // 3 seconds = 30 intervals of 100ms
                    if (newProgress >= 100) {
                        clearInterval(timer);
                        setStatus('measuring');
                        startValuePolling();
                        return 100;
                    }
                    return newProgress;
                });
            }, 100);
            return () => clearInterval(timer);
        }
    }, [status]);

    const startCalibration = async () => {
        try {
            setStatus('calibrating');
            setProgress(0);
            setError(null);
            setFinalResults(null);
            setMinBendValue(null);

            const response = await fetch(`${ESP32_URL}/calibrate`, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                }
            });

            if (!response.ok) {
                throw new Error('Failed to start calibration');
            }

        } catch (err) {
            setError(err.message);
            setStatus('idle');
        }
    };

    const startValuePolling = () => {
        if (pollingInterval.current) {
            clearInterval(pollingInterval.current);
        }
        pollingInterval.current = setInterval(async () => {
            try {
                const response = await fetch(`${ESP32_URL}/flex-values`);
                if (response.ok) {
                    const data = await response.json();
                    setCurrentValue(data.current);

                    if (status === 'calibrating') {
                        setBaselineValue(data.baseline);
                    } else if (status === 'measuring') {
                        if (data.minBend !== undefined && data.minBend !== -1) {
                            setMinBendValue(data.minBend);
                        }
                    }
                }
            } catch (err) {
                console.error("Error polling flex values:", err);
                setError("Failed to get sensor readings");
            }
        }, 300);
    };

    const stopMeasurement = async () => {
        try {
            
            if (pollingInterval.current) {
                clearInterval(pollingInterval.current);
                pollingInterval.current = null;
            }

            const response = await fetch(`${ESP32_URL}/stop-bend`, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                }
            });

            if (!response.ok) {
                throw new Error('Failed to stop measurement');
            }

            const data = await response.json(); 
            setStatus('complete');
            let percentage = calculateBendPercentage(data.minBend);
            setFinalResults({
                minBend: data.minBend,
                baseline: data.baseline,
                bendPercentage: percentage
            });

            const APIresponse = await fetch(`${API_URL}/patients/${userId}/passive-mode/${percentage}`,{
                 method: 'POST',
                 headers: {
                    'Content-Type': 'application/json',
                }
            });
            if (!APIresponse.ok) {
                throw new Error('Failed to record measurement');
            }

        } catch (err) {
            setError(err.message);
        }
    };

    const calculateBendPercentage = (value = minBendValue) => {
        // Ensure we have valid numbers
        if (typeof baselineValue !== 'number' ||
            typeof value !== 'number' ||
            baselineValue <= 0) {
            return 0;
        }

        // Flex sensors typically decrease in value when bent
        // Adjust sesnor reading, values in range (800, 2047)
        var base = baselineValue - 800;
        var val = value - 800;
        const percentage = ((base - val) / base) * 100;

        // Clamp between 0-100%
        return Math.min(100, Math.max(0, Math.round(percentage)));
    };

    const resetMeasurement = () => {
        if (pollingInterval.current) {
            clearInterval(pollingInterval.current);
            pollingInterval.current = null;
        }

        setStatus('idle');
        setBaselineValue(null);
        setCurrentValue(0);
        setMinBendValue(0);
        setProgress(0);
        setFinalResults(null);
        setError(null);
    };

    const getStatusMessage = () => {
        switch (status) {
            case 'calibrating':
                return 'Calibrating - keep sensor straight';
            case 'measuring':
                return 'Bend the sensor now';
            case 'complete':
                return 'Measurement complete';
            default:
                return 'Ready to begin calibration';
        }
    };

    return (
        <div className='passive-mode'>
            <h2>Passive Mode</h2>
            <div className="passive-mode-container" style={{ backgroundColor: colors.lightBlue }}>
                <div className="status-container" style={{ borderColor: colors.midBlue }}>
                    <div className="status-message" style={{ color: colors.darkBlue }}>
                        {getStatusMessage()}
                    </div>

                    {status === 'calibrating' && (
                        <div className="progress-bar-container">
                            <div
                                className="progress-bar"
                                style={{
                                    width: `${progress}%`,
                                    backgroundColor: colors.accentBlue
                                }}
                            ></div>
                        </div>
                    )}
                </div>

                <div className="sensor-data-container">
                    {/* Only show current value when measuring */}
                    {status === 'measuring' && (
                        <div className="data-row">
                            <span className="data-label">Current Value:</span>
                            <span className="data-value">{currentValue}</span>
                        </div>
                    )}

                    {(baselineValue || finalResults?.baseline) && (
                        <div className="data-row">
                            <span className="data-label">Baseline:</span>
                            <span className="data-value">{finalResults?.baseline || baselineValue}</span>
                        </div>
                    )}

                    {(status === 'measuring' || status === 'complete') && (
                        <>
                            <div className="data-row">
                                <span className="data-label">Max Bend:</span>
                                <span className="data-value">
                                    {finalResults?.minBend || minBendValue}
                                </span>
                            </div>
                            <div className="data-row">
                                <span className="data-label">Bend Percentage:</span>
                                <span className="data-value">
                                    {finalResults?.bendPercentage || calculateBendPercentage()}%
                                </span>
                            </div>
                        </>
                    )}
                </div>

                <div className="button-group">
                    {status === 'idle' ? (
                        <button
                            onClick={startCalibration}
                            style={{ backgroundColor: colors.accentBlue }}
                        >
                            Start Calibration
                        </button>
                    ) : status === 'measuring' ? (
                        <button
                            onClick={stopMeasurement}
                            style={{ backgroundColor: colors.successGreen }}
                        >
                            Stop Measurement
                        </button>
                    ) : status === 'complete' ? (
                        <button
                            onClick={resetMeasurement}
                            style={{ backgroundColor: colors.secondaryBlue }}
                        >
                            New Measurement
                        </button>
                    ) : null}
                </div>

                {error && (
                    <div className="error-message" style={{ color: colors.alertRed }}>
                        {error}
                    </div>
                )}

                <div className="instructions">
                    <h3 style={{ color: colors.midBlue }}>Instructions:</h3>
                    <ol>
                        <li>Click "Start Calibration" and keep the sensor straight for 3 seconds</li>
                        <li>After calibration, bend the sensor to measure flexibility</li>
                        <li>Click "Stop Measurement" when finished to see your results</li>
                    </ol>
                </div>
            </div>
        </div>
    );
}

export default PassiveMode;