import axios from 'axios';

// base API_URL
const API_BASE_URL = 'http://localhost:8080/api/v1/';

/**
 * Get all the insurance policies
 * @returns List of policies
 */
export const getPolicies = async () => {
  try {
    const response = await axios.get(API_BASE_URL + 'policies');
    return response.data;
  } catch (error) {
    console.error('Error fetching policies:', error);
    throw error;
  }
};


/**
 * Create a new insurance policy
 * @param {*} policyData 
 * @returns 
 */
export const createPolicy = async (policyData) => {
    try {
      const response = await axios.post(API_BASE_URL + 'policy', policyData);
      return response.data;
    } catch (error) {
      console.error('Error creating policy:', error);
      throw error;
    }
  };
  
  /**
   * Retrieve an insurance policy by its id
   * @param {*} id 
   * @returns 
   */
  export const getPolicyById = async (id) => {
    try {
      const response = await axios.get(API_BASE_URL + `policy/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching policy with ID ${id}:`, error);
      throw error;
    }
  };
  
  /**
   * Updates an insurance policy
   * @param {*} id 
   * @param {*} policyData 
   * @returns 
   */
  export const updatePolicy = async (id, policyData) => {
    try {
      const response = await axios.put(API_BASE_URL + `policy/${id}`, policyData);
      return response.data;
    } catch (error) {
      console.error(`Error updating policy with ID ${id}:`, error);
      throw error;
    }
  };