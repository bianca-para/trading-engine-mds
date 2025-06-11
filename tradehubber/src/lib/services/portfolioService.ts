import axios from 'axios';
import { AssetResponse } from './assetService';
import { UserResponse } from './userService';
import { API_BASE_URL } from './config';

export interface PortfolioResponse {
  positions: {
    assetId: number;
    assetName: string;
    assetSymbol: string;
    amount: number;
    value: number;
    costBasis: number;
    profitLoss: number;
  }[];
  totalValue: number;
  totalChange: number;
  cashBalance: number;
}

export const portfolioService = {
  async getPortfolio(): Promise<PortfolioResponse> {
    const response = await fetch(`${API_BASE_URL}/api/portfolio`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
    });

    if (!response.ok) {
      throw new Error('Failed to fetch portfolio');
    }

    return response.json();
  },

  async addAssetToUser(userId: string, assetId: number, quantity: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api/portfolio/assets`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
      body: JSON.stringify({ userId, assetId, quantity }),
    });

    if (!response.ok) {
      throw new Error('Failed to add asset to user');
    }
  },

  async getUserQuantity(userId: string, assetId: number): Promise<number> {
    const response = await fetch(`${API_BASE_URL}/api/portfolio/assets/${assetId}/quantity`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
    });

    if (!response.ok) {
      throw new Error('Failed to get user quantity');
    }

    const data = await response.json();
    return data.quantity;
  },

  async updateAssetQuantity(userId: string, assetId: number, quantityChange: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api/portfolio/assets/${assetId}/quantity`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
      body: JSON.stringify({ userId, quantityChange }),
    });

    if (!response.ok) {
      throw new Error('Failed to update asset quantity');
    }
  },

  async removeAssetFromUser(userId: string, assetId: number): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api/portfolio/assets/${assetId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
    });

    if (!response.ok) {
      throw new Error('Failed to remove asset from user');
    }
  },

  async getAssetsByUserId(userId: string): Promise<any[]> {
    const response = await fetch(`${API_BASE_URL}/api/portfolio/assets`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
    });

    if (!response.ok) {
      throw new Error('Failed to get user assets');
    }

    return response.json();
  },

  async getUsersByAssetId(assetId: number): Promise<UserResponse[]> {
    const response = await axios.get(`${API_BASE_URL}/user-asset/users/${assetId}`);
    return response.data;
  }
}; 