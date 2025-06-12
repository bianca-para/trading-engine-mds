import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

export interface UserRegisterRequest {
  username: string;
  password: string;
  email: string;
  firstName: string;
  lastName: string;
}

export interface UserLoginRequest {
  username: string;
  password: string;
}

export interface UserResponse {
  id: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  createdAt: string;
  updatedAt: string;
}

export interface LoginResponse {
  token: string;
  message: string;
  userId: string;
  username: string;
}

export const userService = {
  async register(userData: UserRegisterRequest): Promise<UserResponse> {
    const response = await axios.post(`${API_BASE_URL}/user`, userData);
    return response.data;
  },

  async login(credentials: UserLoginRequest): Promise<LoginResponse> {
    const response = await axios.post<LoginResponse>(
        `${API_BASE_URL}/user/login`,
        credentials
    );
    return response.data;
  },

  async getUserById(id: string): Promise<UserResponse> {
    const response = await axios.get(`${API_BASE_URL}/user/${id}`);
    return response.data;
  },

  setAuthToken(token: string) {
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } else {
      delete axios.defaults.headers.common['Authorization'];
    }
  }
};
