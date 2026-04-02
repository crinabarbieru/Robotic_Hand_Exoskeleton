import { API_URL } from "../utils/constants";

export async function editPatient(id, updatedData) {
  const response = await fetch(`${API_URL}/patients/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(updatedData)
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || 'Failed to update patient');
  }

  return await response.json();
}