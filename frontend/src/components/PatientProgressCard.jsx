import moment from 'moment';
import '../stylesheets/PatientProgressCard.css';
import '../stylesheets/MiniCalendar.css'


const PatientProgressCard = ({ patient }) => {

    const sessions = patient?.sessions ?
        Object.entries(patient.sessions).map(([date, duration]) => ({
            day: date,
            duration: parseFloat(duration) || 0,
            dateObj: new Date(date)
        })).sort((a, b) => a.dateObj - b.dateObj) :
        [];

    const passiveScores = patient?.passiveScores ?
        Object.entries(patient.passiveScores).map(([date, score]) => ({
            day: date,
            score: parseInt(score) || 0,
            dateObj: new Date(date)
        })).sort((a, b) => a.dateObj - b.dateObj) :
        [];

    const calculateProgressStats = () => {
        if (!patient?.strokeDate || !patient?.rehabStartDate) return null;

        const now = new Date();
        const strokeDate = new Date(patient.strokeDate);
        const rehabStartDate = new Date(patient.rehabStartDate);

        const daysSinceStroke = Math.floor((now - strokeDate) / (1000 * 60 * 60 * 24));
        const daysSinceRehab = Math.floor((now - rehabStartDate) / (1000 * 60 * 60 * 24));

        const startOfWeek = moment().startOf('week').toDate();
        const sessionsThisWeek = sessions.filter(s => new Date(s.day) >= startOfWeek).length;
        const totalMinutes = sessions.reduce((sum, session) => sum + session.duration, 0).toFixed(1);

        return {
            daysSinceStroke,
            daysSinceRehab,
            sessionsThisWeek,
            totalMinutes,
            lastSession: sessions.length > 0 ? new Date(sessions[sessions.length - 1].day) : null
        };
    };

    const stats = calculateProgressStats();
    const formatDate = (date) => date ? moment(date).format('MMM D, YYYY') : 'N/A';

    const MiniCalendar = () => {
        const today = moment();
        const startDate = moment().subtract(2, 'weeks').startOf('week');
        const endDate = today.clone().endOf('week');
        // const endDate = moment().add(1, 'week').endOf('week');

        const days = [];
        let day = startDate.clone();

        while (day <= endDate) {
            days.push(day.clone());
            day.add(1, 'day');
        }

        const getDayColor = (date) => {
            const session = sessions.find(s => moment(s.dateObj).isSame(date, 'day'));
            const passiveScore = passiveScores.find(s => moment(s.dateObj).isSame(date, 'day'));

            if (session && passiveScore) {
                let durationColor = '#bdd7ff';
                if (session.duration >= 60) durationColor = '#4CAF50';
                else if (session.duration >= 30) durationColor = '#4A90E2';
                else if (session.duration >= 15) durationColor = '#7FB3FF';

                let scoreColor = '#FFECB3';

                return {
                    background: `linear-gradient(135deg, ${durationColor} 50%, ${scoreColor} 50%)`
                };
            }

            if (session) {
                return {
                    backgroundColor: session.duration >= 60 ? '#4CAF50' :
                        session.duration >= 30 ? '#4A90E2' :
                            session.duration >= 15 ? '#7FB3FF' : '#bdd7ff'
                };
            }

            if (passiveScore) {
                return {
                    backgroundColor: '#FFECB3'
                };
            }

            return { backgroundColor: 'transparent' };
        };


        return (
            <div className="mini-calendar">
                <div className="calendar-header">
                    {today.format('MMMM YYYY')}
                </div>
                <div className="weekdays">
                    {['S', 'M', 'T', 'W', 'T', 'F', 'S'].map((d, i) => (
                        <div key={i} className="weekday">{d}</div>
                    ))}
                </div>
                <div className="days-grid">
                    {days.map((day, i) => {
                        const isCurrentMonth = day.isSame(today, 'month');
                        const isToday = day.isSame(today, 'day');
                        const session = sessions.find(s => moment(s.dateObj).isSame(day, 'day'));
                        const passiveScore = passiveScores.find(s => moment(s.dateObj).isSame(day, 'day'));
                        const colorStyle = getDayColor(day);

                        return (
                            <div
                                key={patient.userId}
                                className={`day-cell ${isCurrentMonth ? '' : 'other-month'} ${isToday ? 'today' : ''}`}
                                style={colorStyle}
                                title={
                                    [
                                        session ? `Exercise: ${session.duration} mins` : null,
                                        passiveScore ? `Passive: Score ${passiveScore.score}%` : null
                                    ].filter(Boolean).join('\n') || 'No data'
                                }
                            >
                                <span className="day-number">{day.date()}</span>
                            </div>
                        );
                    })}
                </div>
                <div className="calendar-legend">
                    <div className="legend-item">
                        <span className="legend-color" style={{ backgroundColor: '#4CAF50' }}></span>
                        <span>60+ min</span>
                    </div>
                    <div className="legend-item">
                        <span className="legend-color" style={{ backgroundColor: '#4A90E2' }}></span>
                        <span>30-59 min</span>
                    </div>
                    <div className="legend-item">
                        <span className="legend-color" style={{ backgroundColor: '#7FB3FF' }}></span>
                        <span>15-29 min</span>
                    </div>
                    <div className="legend-item">
                        <span className="legend-color" style={{ backgroundColor: '#bdd7ff' }}></span>
                        <span>1-14 min</span>
                    </div>
                    <div className="legend-item">
                        <span className="legend-color" style={{ backgroundColor: '#FFECB3' }}></span>
                        <span>Passive Mode</span>
                    </div>
                </div>
            </div>
        );
    };

    return (
        <div className="progress-card">
            <div className="progress-header">
                <h2>{patient.name}'s Progress</h2>
            </div>

            {stats ? (
                <>
                    <div className="progress-metrics">
                        <div className="metric-group">
                            <div className="metric">
                                <span className="metric-value">{stats.daysSinceStroke}</span>
                                <span className="metric-label">Days Since Stroke</span>
                            </div>
                            <div className="metric">
                                <span className="metric-value">{stats.daysSinceRehab}</span>
                                <span className="metric-label">Days in Rehabilitation</span>
                            </div>
                        </div>

                        <div className="metric-group highlight">
                            <div className="metric">
                                <span className="metric-value">{stats.sessionsThisWeek}</span>
                                <span className="metric-label">Sessions This Week</span>
                            </div>
                            <div className="metric">
                                <span className="metric-value">{stats.totalMinutes}</span>
                                <span className="metric-label">Total Minutes</span>
                            </div>
                        </div>

                        <div className="last-session">
                            <span>Last Session: </span>
                            <span className="last-session-date">
                                {formatDate(stats.lastSession)}
                            </span>
                        </div>
                    </div>

                    <div className="calendar-section">
                        <div className="progress-header">
                            <h2>Recent Activity</h2>
                        </div>
                        <MiniCalendar />
                    </div>
                </>
            ) : (
                <div className="no-data">No progress data available</div>
            )}
        </div>
    );
};

export default PatientProgressCard;