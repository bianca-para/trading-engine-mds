// src/lib/services/orderService.ts
import axios from "axios";
import { API_BASE_URL } from "./config";

export interface OrderRequest {
  userId: string;
  assetId: number;
  quantity: number;
  price: number;
  type: "BUY" | "SELL";
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
  createOrder(orderRequest: OrderRequest) {
    return axios
        .post<OrderResponse>(`${API_BASE_URL}/order`, orderRequest)
        .then((res) => res.data);
  },

  getOrdersByAsset(assetId: number) {
    return axios
        .get<OrderResponse[]>(`${API_BASE_URL}/order/asset/${assetId}`)
        .then((res) => res.data);
  },

  cancelOrder(orderId: number) {
    return axios
        .delete(`${API_BASE_URL}/order/${orderId}`)
        .then((res) => res.data);
  },

  getOrderDetails(orderId: number) {
    return axios
        .get<OrderResponse>(`${API_BASE_URL}/order/${orderId}`)
        .then((res) => res.data);
  },
};
