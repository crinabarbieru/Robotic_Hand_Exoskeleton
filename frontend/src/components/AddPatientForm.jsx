import { useState } from "react";
import "../stylesheets/AddPatientForm.css";

function AddPatientForm({ onSubmit }) {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    fullName: "",
    dateOfBirth: "",
    strokeType: "",
    strokeSubtype: "",
    strokeDate: "",
    rehabStartDate: "",
  });

  const [errors, setErrors] = useState({
    username: "",
    fullName: "",
  });

  const [showErrorSummary, setShowErrorSummary] = useState(false);

  const validateUsername = (username) => {
    const regex = /^[a-zA-Z0-9_]+$/;
    return regex.test(username);
  };

  const validateFullName = (fullName) => {
    const regex = /^[a-zA-Z\s-]+$/;
    return regex.test(fullName);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    
    if (name === "username") {
      setErrors(prev => ({ ...prev, username: "" }));
    }
    
    if (name === "fullName") {
      setErrors(prev => ({ ...prev, fullName: "" }));
    }
    
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setShowErrorSummary(false);
    
    let isValid = true;
    const newErrors = {
      username: "",
      fullName: "",
    };
    
    if (!validateUsername(formData.username)) {
      newErrors.username = "Username can only contain letters, numbers, and underscores";
      isValid = false;
    }
    
    if (!validateFullName(formData.fullName)) {
      newErrors.fullName = "Full name can only contain letters, spaces, and hyphens";
      isValid = false;
    }
    
    setErrors(newErrors);
    
    if (isValid && onSubmit) {
      onSubmit(formData);
    } else {
      setShowErrorSummary(true);
    }
  };

  const hasErrors = Object.values(errors).some(error => error !== "");

  return (
    <div className="add-patient-div">
      <h2>Add New Patient</h2>
      <div className="form-container">
        <form className="add-patient-form" onSubmit={handleSubmit}>
          <div className="form-grid">
            {/* Column 1 */}
            <div className="form-column">
              <div className="form-group">
                <label>Username:</label>
                <input type="text" name="username" value={formData.username} onChange={handleChange} required />
              </div>

              <div className="form-group">
                <label>Full Name:</label>
                <input type="text" name="fullName" value={formData.fullName} onChange={handleChange} required />
              </div>

              <div className="form-group">
                <label>Stroke Type:</label>
                <select name="strokeType" value={formData.strokeType} onChange={handleChange} required>
                  <option value="">Select Type</option>
                  <option value="Ischemic">Ischemic</option>
                  <option value="Hemorrhagic">Hemorrhagic</option>
                </select>
              </div>

              <div className="form-group">
                <label>Stroke Date:</label>
                <input type="date" name="strokeDate" value={formData.strokeDate} onChange={handleChange} required />
              </div>
            </div>

            {/* Column 2 */}
            <div className="form-column">
              <div className="form-group">
                <label>Password:</label>
                <input type="password" name="password" value={formData.password} onChange={handleChange} required />
              </div>

              <div className="form-group">
                <label>Date of Birth:</label>
                <input type="date" name="dateOfBirth" value={formData.dateOfBirth} onChange={handleChange} required />
              </div>

              <div className="form-group">
                <label>Stroke Subtype:</label>
                <select name="strokeSubtype" value={formData.strokeSubtype} onChange={handleChange} required>
                  {formData.strokeType == "Ischemic" &&
                    <>
                      <option value="Lacunar">Lacunar</option>
                      <option value="Cardioembolic">Cardioembolic</option>
                      <option value="Large artery atherosclerosis">Large artery atherosclerosis</option>
                      <option value="Other">Other</option>
                    </>
                  }
                  {formData.strokeType == "Hemorrhagic" &&
                    <>
                      <option value="Subarachnoid">Subarachnoid</option>
                      <option value="Intracerebral">Intracerebral</option>
                    </>
                  }
                  {formData.strokeType=="" &&
                  <option value="">Choose a stroke type first</option>
                  }

                </select>
              </div>

              <div className="form-group">
                <label>Rehabilitation Start Date:</label>
                <input type="date" name="rehabStartDate" value={formData.rehabStartDate} onChange={handleChange} required />
              </div>
            </div>
          </div>

          <div className="form-actions">
            <button type="submit" className="form-button">Add Patient</button>
          </div>

          {showErrorSummary && hasErrors && (
            <div className="error-summary">
              <h3>Incorrect inputs:</h3>
              <ul>
                {errors.username && <li>{errors.username}</li>}
                {errors.fullName && <li>{errors.fullName}</li>}
              </ul>
            </div>
          )}

        </form>
      </div>
    </div>
  );
}

export default AddPatientForm;