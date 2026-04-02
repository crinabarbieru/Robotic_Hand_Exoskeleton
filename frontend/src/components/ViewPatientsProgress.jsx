import { useEffect, useState } from "react";
import PatientProgressCard from "./PatientProgressCard";
import "../stylesheets/ViewPatients.css";
import { API_URL } from "../utils/constants";

export async function getPatientsProgress(userId) {
  const response = await fetch(`${API_URL}/supervisor/${userId}/patients/progress`);

  if (!response.ok) {
    throw new Error("Failed to fetch patients information");
  }
  
  return await response.json();
}

function ViewPatientsProgress({userId}){
    const [myPatients, setMyPatients] = useState([]); 
    const [error, setError] = useState("");
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        setIsLoading(true);
        getPatientsProgress(userId)
          .then((data) => {
            setMyPatients(data || []); 
            setIsLoading(false);
          })
          .catch((err) => {
            setError(err.message);
            setIsLoading(false);
          });
      }, [userId]);

    if (isLoading) return <div className="loading-spinner">Loading...</div>;
    if (error) return <div className="error-message">{error}</div>;
    if (myPatients.length === 0) {
    return <div className="no-patients">No patients found for this supervisor.</div>;
  } 

  return (
    <div className="view-patients-container">
      <h2>My Patients' Progress</h2>
      <div className="patients-grid">
        {myPatients.map(patient => (
          <PatientProgressCard patient={patient}
          />
        ))}
      </div>
    </div>


  );

}

export default ViewPatientsProgress;