// src/lib/services/orderService.ts

import { API_BASE_URL } from "./config";

export interface OrderRequest {
  userId: string;
  assetId: number;
  quantity: number;
  price: number;
  type: "BUY" | "SELL";
  createdAt: string  ;
}

export interface OrderResponse {
  orderId: number;
  userId: string;
  assetId: number;
  quantity: number;
  price: number;
  type: "BUY" | "SELL";
  status: "PENDING" | "CANCELLED" | "FILLED";
  createdAt: string;
}

export const orderService = {
  /** Place a new order */
  async createOrder(orderRequest: OrderRequest): Promise<OrderResponse> {
    const resp = await fetch(`${API_BASE_URL}/order`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(orderRequest),
    });
    if (!resp.ok) throw new Error(`Failed to create order (${resp.status})`);
    return resp.json();
  },

  /** Get all orders for a given asset */
  async getOrdersByAsset(assetId: number | string): Promise<OrderResponse[]> {
    const resp = await fetch(`http://127.0.0.1:8000/orderbook/BTC`, {
      headers: { "Content-Type": "application/json" },
    });
    if (!resp.ok) {
      const text = await resp.text();
      throw new Error(`Failed to fetch orders (${resp.status}): ${text}`);
    }
    return resp.json();
  },

  /** Cancel an order */
  async cancelOrder(orderId: number): Promise<OrderResponse> {
    const resp = await fetch(`${API_BASE_URL}/order/${orderId}`, {
      method: "DELETE",
    });
    if (!resp.ok) throw new Error(`Failed to cancel order (${resp.status})`);
    return resp.json();
  },

  /** Get details of a single order */
  async getOrderDetails(orderId: number): Promise<OrderResponse> {
    const resp = await fetch(`${API_BASE_URL}/order/${orderId}`, {
      headers: { "Content-Type": "application/json" },
    });
    if (!resp.ok) throw new Error(`Failed to fetch order (${resp.status})`);
    return resp.json();
  },
};
