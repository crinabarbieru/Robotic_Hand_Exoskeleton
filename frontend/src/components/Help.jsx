import '../stylesheets/Help.css';

function Help() {
    return (
        <div className="help-container">
            <div className="help-header">
                <h1>Help Center</h1>
                <p className="help-subtitle">Find answers to common questions and get support</p>
            </div>

            <div className="help-content">

                <section className="help-section">
                    <h2>Getting Started</h2>
                    <ol className="steps-list">
                        <li>Your supervisor creates your account</li>
                        <li>You login using the temporary username and password provided by your supervisor</li>
                        <li>You set a new password for your account</li>
                        <li>You can now begin your rehabilitation journey</li>
                    </ol>
                </section>

                <section className="help-section">
                    <h2>Page Information</h2>
                    <div className="page-info-grid">
                        <div className="page-info-card">
                            <h3>For Patients</h3>
                            <ul>
                                <li><strong>My Information:</strong> View and update your personal details</li>
                                <li><strong>Perform Exercises:</strong> Access the exercise interface and control the exoskeleton</li>
                                <li><strong>My Progress:</strong> Track your rehabilitation metrics and improvements</li>
                            </ul>
                        </div>
                        <div className="page-info-card">
                            <h3>For Supervisors</h3>
                            <ul>
                                <li><strong>Dashboard:</strong> Overview of all patients and their progress</li>
                                <li><strong>Add Patient:</strong> Register new patients to the system</li>
                                <li><strong>View Patients:</strong> Manage existing patient accounts</li>
                                <li><strong>View Progress:</strong> Analyze detailed rehabilitation data</li>
                            </ul>
                        </div>
                    </div>
                </section>

                <div className="faq-grid">
                    <div className="faq-card">
                        <h3>How do I connect my exoskeleton?</h3>
                        <p>
                            The rehabilitation supervisor sets up the device for you.
                            Make sure both devices are connected to the same WiFi network.
                        </p>
                    </div>

                    <div className="faq-card">
                        <h3>What if my exercises aren't registering?</h3>
                        <p>
                            Check your device connection and ensure proper positioning.
                            Contact support if the issue persists.
                        </p>
                    </div>

                    <div className="faq-card">
                        <h3>How often should I do my exercises?</h3>
                        <p>
                            Follow your prescribed rehabilitation plan. Typically 3-5 sessions
                            per week is recommended.
                        </p>
                    </div>
                </div>

                <div className="support-section">
                    <h2>Need More Help?</h2>
                    <p>
                        Email us at <a href="mailto:support@rehabexo.com" className="support-email">support@rehabexo.com</a>
                    </p>
                    <p className="emergency-contact">
                        For urgent medical issues, contact your healthcare provider immediately.
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Help;