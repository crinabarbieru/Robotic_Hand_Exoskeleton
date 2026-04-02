import { API_URL } from "../utils/constants";

export async function deletePatient(id) {
    const response = await fetch(`${API_URL}/patients/${id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      }
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || 'Failed to delete patient');
    }

    return await response.json();
}