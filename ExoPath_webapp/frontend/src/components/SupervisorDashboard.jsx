import { useEffect, useState } from 'react';
import { Bar } from 'react-chartjs-2';
import { Chart, registerables } from 'chart.js';
import '../stylesheets/SupervisorDashboard.css'
import { API_URL } from '../utils/constants';

Chart.register(...registerables);


function SupervisorDashboard({ userId }) {
    const [stats, setStats] = useState({
        totalPatients: 0,
        sessionsThisWeek: 0,
        totalMinutes: 0,
        avgSessionDuration: 0,
        ageDistribution: {}
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const response = await fetch(`${API_URL}/supervisor/${userId}/stats`);
                const data = await response.json();
                setStats({
                    totalPatients: data.totalPatients,
                    sessionsThisWeek: data.sessionsThisWeek,
                    totalMinutes: data.totalMinutes,
                    avgSessionDuration: data.avgSessionDuration,
                    ageDistribution: data.ageDistribution || {}
                });
            } catch (error) {
                console.error("Error fetching stats:", error);
            } finally {
                setLoading(false);
            }
        };
        fetchStats();
    }, [userId]);

    const renderAgeDistribution = () => {
        return Object.entries(stats.ageDistribution).map(([ageGroup, count]) => (
            <div key={ageGroup} className="age-group">
                <span className="age-range">{ageGroup}: </span>
                <span className="patient-count">{count} patients</span>
            </div>
        ));
    };

    if (loading) return <div className="loading-spinner">Loading...</div>;
    if (!stats) return <div className="error">Failed to load statistics</div>;

    const ageChartData = {
        labels: Object.keys(stats.ageDistribution),
        datasets: [{
            label: 'Number of Patients',
            data: Object.values(stats.ageDistribution),
            backgroundColor: [
                '#4A90E2', '#7FB3FF', '#32538F', '#0D274D'
            ],
            borderColor: '#0D274D',
            borderWidth: 1,
            borderRadius: 4,
        }]
    };

    const chartOptions = {
        responsive: true,
        scales: {
            y: {
                beginAtZero: true,
                title: {
                    display: true,
                    text: 'Number of Patients'
                }
            },
            x: {
                title: {
                    display: true,
                    text: 'Age Groups'
                }
            }
        },
        plugins: {
            legend: { display: false },
            tooltip: {
                callbacks: {
                    label: (context) => `${context.parsed.y} patients`
                }
            }
        }
    };

    return (
        <div className="dashboard-container">
            <h2>Supervisor Overview</h2>

            <div className='dashboard-contents'>

                <div className="stats-grid">
                    <StatCard
                        title="Total Patients"
                        value={stats.totalPatients}
                    />
                    <StatCard
                        title="Sessions This Week"
                        value={stats.sessionsThisWeek}
                    />
                    <StatCard
                        title="Total Minutes"
                        value={`${stats.totalMinutes}m`}
                    />
                    <StatCard
                        title="Avg. Session Duration"
                        value={`${stats.avgSessionDuration}m`}
                    />
                </div>

                <div className="chart-container">
                    <h2>Patient Age Distribution</h2>
                    <div className="chart-wrapper">
                        <Bar
                            data={ageChartData}
                            options={chartOptions}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};

const StatCard = ({ title, value }) => (
    <div className="stat-card">
        <div className="stat-value">{value}</div>
        <div className="stat-title">{title}</div>
    </div>
);

export default SupervisorDashboard;
