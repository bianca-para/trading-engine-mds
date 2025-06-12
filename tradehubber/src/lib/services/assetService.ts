// src/lib/services/assetService.ts

import { API_BASE_URL } from "./config";

export interface AssetRequest {
  symbol: string;
  name: string;
  description: string;
  currentPrice: number;
}

export interface AssetResponse {
  assetId: number;
  symbol: string;
  name: string;
  price: number;
}

export const assetService = {
  /** Create a new asset (you can leave this with axios if you want auth) */
  async createAsset(assetRequest: AssetRequest): Promise<AssetResponse> {
    const resp = await fetch(`${API_BASE_URL}/asset`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(assetRequest),
    });
    if (!resp.ok) {
      throw new Error(`Failed to create asset (${resp.status})`);
    }
    return resp.json();
  },

  /** Get all assets */
  async getAllAssets(): Promise<AssetResponse[]> {
    const resp = await fetch(`${API_BASE_URL}/asset`, {
      headers: { "Content-Type": "application/json" },
    });
    if (!resp.ok) {
      throw new Error(`Failed to fetch assets (${resp.status})`);
    }
    return resp.json();
  },

  /** Get one asset by its numeric ID */
  async getAsset(id: number): Promise<AssetResponse> {
    const resp = await fetch(`${API_BASE_URL}/asset/${id}`, {
      headers: { "Content-Type": "application/json" },
    });
    if (!resp.ok) {
      const text = await resp.text();
      throw new Error(`Failed to fetch asset ${id} (${resp.status}): ${text}`);
    }
    return resp.json();
  },

  /** Update an asset */
  async updateAsset(id: number, assetRequest: AssetRequest): Promise<AssetResponse> {
    const resp = await fetch(`${API_BASE_URL}/asset/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(assetRequest),
    });
    if (!resp.ok) {
      throw new Error(`Failed to update asset ${id} (${resp.status})`);
    }
    return resp.json();
  },

  /** Delete an asset */
  async deleteAsset(id: number): Promise<void> {
    const resp = await fetch(`${API_BASE_URL}/asset/${id}`, {
      method: "DELETE",
    });
    if (!resp.ok) {
      throw new Error(`Failed to delete asset ${id} (${resp.status})`);
    }
  },
};
