import { useEffect, useState } from "react";
import { getMyPatients } from "../services/ViewPatients";
import PatientCard from "./PatientCard";
import "../stylesheets/ViewPatients.css";
import { deletePatient } from '../services/DeletePatient';
import { editPatient } from "../services/EditPatientInformation";

function ViewPatients({ userId }) {
  const [myPatients, setMyPatients] = useState([]);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setIsLoading(true);
    getMyPatients(userId)
      .then((data) => {
        setMyPatients(data || []);
        setIsLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setIsLoading(false);
      });
  }, [userId]);

  const handleDeletePatient = async (patientId) => {
    try {
      await deletePatient(patientId);
      setMyPatients(prevPatients =>
        prevPatients.filter(patient => patient.id !== patientId)
      );
    } catch (error) {
      console.error("Delete failed:", error);
    }
  };

  const handleEditPatient = async (patientId, updatedData) => {
    try {
      await editPatient(patientId, updatedData);
      setMyPatients(prev => prev.map(p =>
        p.id === patientId ? { ...p, ...updatedData } : p
      ));
    } catch (error) {
      console.error("Edit failed:", error);
    }

  }

  if (isLoading) return <div className="loading-spinner">Loading...</div>;
  if (error) return <div className="error-message">{error}</div>;
  if (myPatients.length === 0) {
    return <div className="no-patients">No patients found for this supervisor.</div>;
  }

  return (
    <div className="view-patients-container">
      <h2>My Patients</h2>
      <div className="patients-grid">
        {myPatients.map(patient => (
          <PatientCard role={2}
            key={patient.id}
            patient={patient}
            onEdit={handleEditPatient}
            onDelete={handleDeletePatient}
          />
        ))}
      </div>
    </div>
  );
}

export default ViewPatients;