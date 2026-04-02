import { useEffect, useState } from "react";
import { useAuth } from "../services/AuthContext";
import { getUserInfo } from "../services/UserInfo";
import { editPatient } from "../services/EditPatientInformation";

import '../stylesheets/UserInfo.css';
import PatientCard from "./PatientCard";


function UserInfo({ userId }) {
  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState("");
  const { user } = useAuth();

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {

        const response = await fetch(`http://localhost:8080/api/patients/${userId}`, {
          method: 'GET',
          credentials: 'include',
          headers: {
            'Accept': 'application/json'
          }
        });

        if (response.status === 403) {
          throw new Error("Access denied - insufficient permissions");
        }

        if (!response.ok) {
          throw new Error(`Failed to fetch: ${response.statusText}`);
        }

        const data = await response.json();
        setUserInfo(data);
      } catch (err) {
        console.error("Fetch error:", err);
        setError(err.message);
      }
    };

    fetchUserInfo();
  }, [userId, user]);

  const handleEditInfo = async (patientId, updatedData) => {
      try {
      const response = await fetch(`http://localhost:8080/api/patients/${patientId}`, {
        method: 'PUT',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        body: JSON.stringify(updatedData)
      });

      if (!response.ok) {
        throw new Error("Update failed");
      }
        setUserInfo(updatedData);
      } catch (error) {
        console.error("Delete failed:", error);
      }
  
    }

  if (error) return <p>Could not find any patient information!</p>;
  if (!userInfo) return <p>Loading...</p>;

  return (
    <div className="user_info_div">
      <h3>My Information</h3>
      <PatientCard patient={userInfo} onEdit={handleEditInfo}></PatientCard>
    </div>
  );
}

export default UserInfo;
