import '../stylesheets/About.css'; 

function About(){
  return (
    <div className="about-container">
      <div className="about-header">
        <h1>About ExoPath</h1>
        <p className="subtitle">Empowering Stroke Recovery Through Technology</p>
      </div>

      <div className="about-content">
        <section className="about-section">
          <h2>Our Mission</h2>
          <p>
            ExoPath is a cutting-edge platform designed to assist post-stroke patients in regaining upper limb mobility. 
            By combining robotic exoskeleton technology with personalized exercise plans, we aim to make recovery 
            accessible, measurable, and effective.
          </p>
        </section>

        <div className="features-grid">
          <div className="feature-card">
            <h3>For Patients</h3>
            <ul>
              <li>Control a robotic exoskeleton for guided exercises</li>
              <li>Track the rehabilitation journey in a user-friendly interface</li>
              <li>Measure progress using Passive Mode</li>
            </ul>
          </div>

          <div className="feature-card">
            <h3>For Supervisors</h3>
            <ul>
              <li>Monitor patient progress remotely</li>
              <li>Visualize the patient's recovery progress</li>
              <li>Manage multiple patients efficiently</li>
            </ul>
          </div>
        </div>

        <section className="about-section">
          <h2>Technology & Safety</h2>
          <p>
            Our platform integrates securely with robotic exoskeletons, ensuring smooth operation and patient safety. 
            All data is encrypted and complies with healthcare privacy standards.
          </p>
        </section>

        <div className="contact-section">
          <h2>Have Questions?</h2>
          <p>Reach out to our support team at <span className="contact-email">support@exopath.com</span></p>
        </div>
      </div>
    </div>
  );
};

export default About;