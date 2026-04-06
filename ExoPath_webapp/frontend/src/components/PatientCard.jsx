import { useState } from 'react';
import '../stylesheets/PatientCard.css';

const PatientCard = ({ patient, onEdit, onDelete, role }) => {
  const [showDeleteDialog, setShowDeleteDialog] = useState(false);
  const [showEditDialog, setShowEditDialog] = useState(false);
  const [editedPatient, setEditedPatient] = useState({ ...patient });
  const [error, setError] = useState("");

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const validateFullName = (fullName) => {
    const regex = /^[a-zA-Z\s-]+$/;
    return regex.test(fullName);
  };

  const handleEditClick = () => {
    setShowEditDialog(true);
  };

  const handleDeleteClick = () => {
    setShowDeleteDialog(true);
  };

  const handleDeleteConfirm = async () => {
    try {
      await onDelete(patient.id);
      setShowDeleteDialog(false);
    } catch (error) {
      console.error("Deletion error:", error);
    }
  };

  const handleDeleteCancel = () => {
    setShowDeleteDialog(false);
  };

  const handleEditConfirm = () => {
    if(editedPatient.name && !validateFullName(editedPatient.name)){
       setError("Full name can only contain letters, spaces, and hyphens");
      return;
    }
    if (onEdit) onEdit(patient.id, editedPatient);
    setShowEditDialog(false);
  };
  const handleEditCancel = () => {
    setEditedPatient({ ...patient });
    setShowEditDialog(false);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditedPatient(prev => ({
      ...prev,
      [name]: value
    }));
  };


  return (
    <div className="patient-card">
      {/* Delete Confirmation Dialog */}
      {showDeleteDialog && (
        <div className="dialog-overlay">
          <div className="confirmation-dialog">
            <h3>Confirm Deletion</h3>
            <p>Are you sure you want to delete this patient?</p>
            <div className="dialog-buttons">
              <button className="cancel-btn" onClick={handleDeleteCancel}>
                Cancel
              </button>
              <button className="confirm-btn" onClick={handleDeleteConfirm}>
                Confirm
              </button>
            </div>
          </div>
        </div>
      )}
      {/* Edit Dialog */}
      {showEditDialog && (
        <div className="dialog-overlay">
          <div className="edit-dialog">
            <h3>Edit Patient Information</h3>
            {error && <div className="error-message">{error}</div>}
            <div className="edit-form">
              <div className="form-group">
                <label>Name:</label>
                <input
                  type="text"
                  name="name"
                  value={editedPatient.name || ''}
                  onChange={handleInputChange}
                />
              </div>
              <div className="form-group">
                <label>Date of Birth:</label>
                <input
                  type="date"
                  name="dateOfBirth"
                  value={editedPatient.dateOfBirth || ''}
                  onChange={handleInputChange}
                />
              </div>
            </div>
            <div className="form-group">
              <label>Stroke Type:</label>
              <select name="strokeType" value={editedPatient.strokeType || ""} onChange={handleInputChange}>
                <option value="Ischemic">Ischemic</option>
                <option value="Hemorrhagic">Hemorrhagic</option>
              </select>
            </div>
            <div className="form-group">
              <label>Stroke Subtype:</label>
              <select name="strokeSubtype" value={editedPatient.strokeSubtype || ""} onChange={handleInputChange}>
                {editedPatient.strokeType == "Ischemic" &&
                  <>
                    <option value="Lacunar">Lacunar</option>
                    <option value="Cardioembolic">Cardioembolic</option>
                    <option value="Large artery atherosclerosis">Large artery atherosclerosis</option>
                    <option value="Other">Other</option>
                  </>
                }
                {editedPatient.strokeType == "Hemorrhagic" &&
                  <>
                    <option value="Subarachnoid">Subarachnoid</option>
                    <option value="Intracerebral">Intracerebral</option>
                  </>
                }
                {editedPatient.strokeType == "" &&
                  <option value="">Choose a stroke type first</option>
                }
              </select>
            </div>
            <div className="form-group">
              <label>Stroke Date:</label>
              <input
                type="date"
                name="strokeDate"
                value={editedPatient.strokeDate || ''}
                onChange={handleInputChange}
              />
            </div>
            <div className="form-group">
              <label>Rehab Start Date:</label>
              <input
                type="date"
                name="rehabStartDate"
                value={editedPatient.rehabStartDate || ''}
                onChange={handleInputChange}
              />
            </div>

            <div className="dialog-buttons">
              <button className="cancel-btn" onClick={handleEditCancel}>
                Cancel
              </button>
              <button className="confirm-btn" onClick={handleEditConfirm}>
                Save Changes
              </button>
            </div>
          </div>
        </div>
      )}
      <div className="card-header">
        <h2>{patient.name || 'Patient Information'}</h2>
        <div className="card-actions">
          <button className="edit-btn" onClick={handleEditClick}>
            Edit
          </button>
          {role == 2 && (
            <button className="delete-btn" onClick={handleDeleteClick}>
              Delete
            </button>
          )}
        </div>
      </div>

      <div className="card-body">
        <div className="info-section">
          <div className="info-row">
            <span className="info-label">Date of Birth:</span>
            <span className="info-value">{formatDate(patient.dateOfBirth)}</span>
          </div>

          <div className="info-row">
            <span className="info-label">Stroke Type:</span>
            <span className="info-value highlight">{patient.strokeType || 'N/A'}</span>
          </div>

          <div className="info-row">
            <span className="info-label">Stroke Subtype:</span>
            <span className="info-value">{patient.strokeSubtype || 'N/A'}</span>
          </div>
        </div>

        <div className="timeline-section">
          <div className="timeline-item">
            <span className="timeline-label">Stroke Date:</span>
            <span className="timeline-date alert">
              {formatDate(patient.strokeDate)}
            </span>
          </div>

          <div className="timeline-item">
            <span className="timeline-label">Rehab Start:</span>
            <span className="timeline-date success">
              {formatDate(patient.rehabStartDate)}
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PatientCard;