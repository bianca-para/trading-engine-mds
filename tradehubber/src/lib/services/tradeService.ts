import { API_BASE_URL } from './config';

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
  status: 'PENDING' | 'COMPLETED' | 'CANCELLED';
  executedAt: string;
}

export const tradeService = {
  async createTrade(tradeRequest: TradeRequest): Promise<TradeResponse> {
    const response = await fetch(`${API_BASE_URL}/api/trades`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
      body: JSON.stringify(tradeRequest),
    });

    if (!response.ok) {
      throw new Error('Failed to create trade');
    }

    return response.json();
  },

  async getTradesByAsset(symbol: string): Promise<TradeResponse[]> {
    const response = await fetch(`${API_BASE_URL}/api/trades/asset/${symbol}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
    });

    if (!response.ok) {
      throw new Error('Failed to get trades by asset');
    }

    return response.json();
  },

  async getTradesByBuyer(buyerId: string): Promise<TradeResponse[]> {
    const response = await fetch(`${API_BASE_URL}/api/trades/buyer/${buyerId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
    });

    if (!response.ok) {
      throw new Error('Failed to get trades by buyer');
    }

    return response.json();
  },

  async getTradesBySeller(sellerId: string): Promise<TradeResponse[]> {
    const response = await fetch(`${API_BASE_URL}/api/trades/seller/${sellerId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
    });

    if (!response.ok) {
      throw new Error('Failed to get trades by seller');
    }

    return response.json();
  },

  async getTradesByDateRange(from: string, to: string): Promise<TradeResponse[]> {
    const response = await fetch(`${API_BASE_URL}/api/trades/date-range`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
      body: JSON.stringify({ from, to }),
    });

    if (!response.ok) {
      throw new Error('Failed to get trades by date range');
    }

    return response.json();
  },

  async getTradesByMinPrice(minPrice: number): Promise<TradeResponse[]> {
    const response = await fetch(`${API_BASE_URL}/api/trades/min-price/${minPrice}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
    });

    if (!response.ok) {
      throw new Error('Failed to get trades by min price');
    }

    return response.json();
  },

  async getUserTrades(): Promise<TradeResponse[]> {
    const response = await fetch(`${API_BASE_URL}/api/trades/user`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
    });

    if (!response.ok) {
      throw new Error('Failed to get user trades');
    }

    return response.json();
  },
}; 