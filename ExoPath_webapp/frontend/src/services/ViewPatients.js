import { API_URL } from "../utils/constants";

export async function getMyPatients(userId) {
  const response = await fetch(`${API_URL}/supervisor/${userId}/patients`);

  if (!response.ok) {
    throw new Error("Failed to fetch patients information");
  }

  return await response.json(); 
}