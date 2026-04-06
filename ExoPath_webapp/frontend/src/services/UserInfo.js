import { API_URL } from "../utils/constants";

export async function getUserInfo(userId) {
  const response = await fetch(`${API_URL}/patients/${userId}`, {
    method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
  });

  if (!response.ok) {
    throw new Error("Failed to fetch user information");
  }

  return await response.json(); 
}
