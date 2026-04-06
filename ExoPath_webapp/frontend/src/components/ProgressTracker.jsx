import { useState, useEffect } from "react";
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import '../stylesheets/ProgressTracker.css';
import { API_URL } from "../utils/constants";

const localizer = momentLocalizer(moment);

function ProgressTracker({ userId }) {
    const [sessions, setSessions] = useState([]);
    const [passiveScores, setPassiveScores] = useState([]);

    const [stats, setStats] = useState({
        daysSinceStroke: 0,
        daysSinceRehabStart: 0,
        sessionsThisWeek: 0,
        sessionsThisMonth: 0,
        totalMinutes: 0
    });
    const [strokeDate, setStrokeDate] = useState(null);
    const [rehabStartDate, setRehabStartDate] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch(`${API_URL}/patients/${userId}/progress`);
                const data = await response.json();

                const sessionsArray = Object.entries(data.sessions || {}).map(([day, duration]) => ({
                    day,
                    duration: parseFloat(duration)
                }));

                const scoresArray = Object.entries(data.passiveScores || {}).map(([day, score]) => ({
                    day,
                    score: parseInt(score)
                }));

                setSessions(sessionsArray);
                setPassiveScores(scoresArray);
                setStrokeDate(data.strokeDate ? new Date(data.strokeDate) : null);
                setRehabStartDate(data.rehabStartDate ? new Date(data.rehabStartDate) : null);
                calculateStats(sessionsArray,
                    data.strokeDate ? new Date(data.strokeDate) : null,
                    data.rehabStartDate ? new Date(data.rehabStartDate) : null);
            } catch (error) {
                console.error("Error fetching patient data:", error);
                setSessions([]);
                setPassiveScores([])
            }
        };
        fetchData();
    }, [userId]);

    const calculateStats = (sessions, strokeDate, rehabStartDate) => {
        if (!strokeDate || !rehabStartDate) return;

        const now = new Date();

        const daysSinceStroke = Math.floor((now - strokeDate) / (1000 * 60 * 60 * 24));
        const daysSinceRehabStart = Math.floor((now - rehabStartDate) / (1000 * 60 * 60 * 24));

        const startOfWeek = moment().startOf('week').toDate();
        const startOfMonth = moment().startOf('month').toDate();

        const sessionsThisWeek = sessions.filter(s => new Date(s.day) >= startOfWeek).length;
        const sessionsThisMonth = sessions.filter(s => new Date(s.day) >= startOfMonth).length;
        const totalMinutes = sessions.reduce((sum, session) => sum + session.duration, 0).toFixed(1);

        setStats({
            daysSinceStroke,
            daysSinceRehabStart,
            sessionsThisWeek,
            sessionsThisMonth,
            totalMinutes
        });
    };

    const exerciseEvents = sessions.map(session => ({
        start: new Date(session.day),
        end: new Date(session.day),
        title: `Exercise: ${session.duration} min`,
        allDay: true,
        type: 'exercise',
        duration: session.duration
    }));

    const passiveEvents = passiveScores.map(score => ({
        start: new Date(score.day),
        end: new Date(score.day),
        title: `Passive: ${score.score}%`,
        allDay: true,
        type: 'passive',
        score: score.score
    }));

    const calendarEvents = [...exerciseEvents, ...passiveEvents];

    const eventStyleGetter = (event) => {
        let backgroundColor;

        if (event.type == 'exercise') {
            backgroundColor = '#bdd7ff';
            let duration = event.duration;
            if (duration >= 60) backgroundColor = '#4CAF50';
            else if (duration >= 30) backgroundColor = '#4A90E2';
            else if (duration >= 15) backgroundColor = '#7FB3FF';
        } else {
            backgroundColor = '#FFECB3';
        }

        return {
            style: {
                backgroundColor,
                borderRadius: '4px',
                opacity: 0.8,
                color: 'black',
                border: '0px',
                display: 'block',
            }
        };
    };

    return (
        <div className="progress-tracker">
            <h3>My Rehabilitation Progress</h3>
            <div className="progress-contents">
                <div className="stats-container">
                    <div className="stat-card">
                        <h3>{stats.daysSinceStroke}</h3>
                        <p>Days since stroke</p>
                    </div>
                    <div className="stat-card">
                        <h3>{stats.daysSinceRehabStart}</h3>
                        <p>Days in rehabilitation</p>
                    </div>
                    <div className="stat-card highlight-card">
                        <h3>{stats.sessionsThisWeek}</h3>
                        <p>Sessions this week</p>
                    </div>
                    <div className="stat-card">
                        <h3>{stats.sessionsThisMonth}</h3>
                        <p>Sessions this month</p>
                    </div>
                    <div className="stat-card highlight-card">
                        <h3>{stats.totalMinutes}</h3>
                        <p>Total minutes exercised</p>
                    </div>
                </div>

                <div className="calendar-container">
                    <div className="calendar-legend">
                        <div className="legend-item">
                            <span className="legend-color" style={{ backgroundColor: '#bdd7ff' }}></span>
                            <span>1-14 min</span>
                        </div>
                        <div className="legend-item">
                            <span className="legend-color" style={{ backgroundColor: '#7FB3FF' }}></span>
                            <span>15-29 min</span>
                        </div>
                        <div className="legend-item">
                            <span className="legend-color" style={{ backgroundColor: '#4A90E2' }}></span>
                            <span>30-59 min</span>
                        </div>
                        <div className="legend-item">
                            <span className="legend-color" style={{ backgroundColor: '#4CAF50' }}></span>
                            <span>60+ min</span>
                        </div>
                        <div className="legend-item">
                            <span className="legend-color" style={{ backgroundColor: '#FFECB3' }}></span>
                            <span>Passive Mode Score</span>
                        </div>
                    </div>
                    <Calendar
                        localizer={localizer}
                        events={calendarEvents}
                        startAccessor="start"
                        endAccessor="end"
                        style={{ height: 500 }}
                        views={['month']}
                        defaultView="month"
                        eventPropGetter={eventStyleGetter}
                    />

                </div>
            </div>
        </div>
    );
}

export default ProgressTracker;