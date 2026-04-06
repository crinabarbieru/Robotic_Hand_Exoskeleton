import { API_URL } from "../utils/constants";

export async function addPatient(patientData) {
    const response = await fetch(`${API_URL}/patients`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(patientData),
    });

    const result = await response.json(); 
  
    if (!response.ok) {
      throw new Error(result.message || 'Failed to add patient');
    }
  
    return result;
  }
  