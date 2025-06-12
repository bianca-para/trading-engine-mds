// src/lib/services/tradeService.ts

import axios from "axios";
import { API_BASE_URL } from "./config";

export interface TradeRequest {
  buyerId: string;
  sellerId: string;
  assetId: number;
  quantity: number;
  price: number;
}

export interface TradeResponse {
  id: number;
  buyerId: string;
  sellerId: string;
  assetId: number;
  quantity: number;
  price: number;
  total: number;
  status: "PENDING" | "COMPLETED" | "CANCELLED";
  executedAt: string;
}

export const tradeService = {
  /** Create a new trade */
  async createTrade(tr: TradeRequest): Promise<TradeResponse> {
    const resp = await axios.post<TradeResponse>(
        `${API_BASE_URL}/api/trades`,
        tr
    );
    return resp.data;
  },

  /** Get all trades for a given asset ID */
  async getTradesByAsset(assetId: number): Promise<TradeResponse[]> {
    const resp = await axios.get<TradeResponse[]>(
        `${API_BASE_URL}/api/trades/asset/${assetId}`
    );
    return resp.data;
  },

  /** Get all trades where the current user was the buyer */
  async getTradesByBuyer(buyerId: string): Promise<TradeResponse[]> {
    const resp = await axios.get<TradeResponse[]>(
        `${API_BASE_URL}/api/trades/buyer/${buyerId}`
    );
    return resp.data;
  },

  /** Get all trades where the current user was the seller */
  async getTradesBySeller(sellerId: string): Promise<TradeResponse[]> {
    const resp = await axios.get<TradeResponse[]>(
        `${API_BASE_URL}/api/trades/seller/${sellerId}`
    );
    return resp.data;
  },

  /** Get trades between two dates */
  async getTradesByDateRange(
      from: string,
      to: string
  ): Promise<TradeResponse[]> {
    const resp = await axios.post<TradeResponse[]>(
        `${API_BASE_URL}/api/trades/date-range`,
        { from, to }
    );
    return resp.data;
  },

  /** Get all trades above a minimum price */
  async getTradesByMinPrice(minPrice: number): Promise<TradeResponse[]> {
    const resp = await axios.get<TradeResponse[]>(
        `${API_BASE_URL}/api/trades/min-price/${minPrice}`
    );
    return resp.data;
  },

  /** Get all trades for the currently authenticated user */
  async getUserTrades(): Promise<TradeResponse[]> {
    const resp = await axios.get<TradeResponse[]>(
        `${API_BASE_URL}/api/trades/user`
    );
    return resp.data;
  },
};
