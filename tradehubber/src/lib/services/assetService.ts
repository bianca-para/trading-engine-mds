// src/lib/services/assetService.ts
import axios from 'axios';
import { API_BASE_URL } from './config';

export interface AssetRequest {
  symbol: string;
  name: string;
  description: string;
  currentPrice: number;
}

// match exactly the JSON your backend returns
export interface AssetResponse {
  assetId: number;
  symbol: string;
  name: string;
  price: number;
}

export const assetService = {
  async createAsset(assetRequest: AssetRequest): Promise<AssetResponse> {
    const resp = await axios.post<AssetResponse>(`${API_BASE_URL}/asset`, assetRequest);
    return resp.data;
  },

  async getAllAssets(): Promise<AssetResponse[]> {
    const resp = await fetch(`${API_BASE_URL}/asset`, {
      headers: { 'Content-Type': 'application/json' },
    });
    if (!resp.ok) throw new Error('Failed to fetch assets');
    return resp.json();
  },

  async getAsset(id: number): Promise<AssetResponse> {
    const resp = await axios.get<AssetResponse>(`${API_BASE_URL}/order/asset/${id}`);
    return resp.data;
  },

  async updateAsset(id: number, assetRequest: AssetRequest): Promise<AssetResponse> {
    const resp = await axios.put<AssetResponse>(`${API_BASE_URL}/asset/${id}`, assetRequest);
    return resp.data;
  },

  async deleteAsset(id: number): Promise<void> {
    await axios.delete(`${API_BASE_URL}/asset/${id}`);
  },
};
