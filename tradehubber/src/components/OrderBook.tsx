import { useState, useEffect } from "react";
import { API_BASE_URL } from "@/lib/services/config";

interface Order {
  id: number;
  traderId: string;
  symbol: string;
  price: number;
  quantity: number;
  side: string;
  timestamp: string;
}

interface Level {
  price: number;
  orders: Order[];
}

interface OrderBookResponse {
  bids: Level[];
  asks: Level[];
}

interface OrderBookProps {
  symbol: string;
}

export default function OrderBook({ symbol }: OrderBookProps) {
  const [bids, setBids] = useState<Level[]>([]);
  const [asks, setAsks] = useState<Level[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchBook = async () => {
      try {
        const res = await fetch(`http://127.0.0.1:8000/orderbook/${encodeURIComponent(symbol)}`, {
          headers: { "Content-Type": "application/json" },
        });
        if (!res.ok) {
          const text = await res.text();
          throw new Error(`HTTP ${res.status}: ${text}`);
        }
        const data: OrderBookResponse = await res.json();
        setBids(data.bids);
        setAsks(data.asks);
      } catch (err: any) {
        console.error("Error fetching order book:", err);
        setError(err.message);
      }
    };

    fetchBook();
    const id = setInterval(fetchBook, 5000);
    return () => clearInterval(id);
  }, [symbol]);

  if (error) {
    return <p className="text-red-500">Order book error: {error}</p>;
  }

  return (
    <div className="bg-card border rounded-lg p-4">
      <h3 className="text-xl font-semibold mb-4">Order Book: {symbol}</h3>
      <div className="grid grid-cols-2 gap-6">
        {/* Bids */}
        <div>
          <h4 className="text-lg font-medium mb-2 text-trade-buy">Bids</h4>
          <ul className="space-y-1">
            {bids.flatMap((lvl) =>
              lvl.orders.map((o) => (
                <li key={o.id} className="flex justify-between text-green-600">
                  <span>${o.price.toFixed(2)}</span>
                  <span>{o.quantity} {symbol}</span>
                </li>
              ))
            )}
          </ul>
        </div>

        {/* Asks */}
        <div>
          <h4 className="text-lg font-medium mb-2 text-trade-sell">Asks</h4>
          <ul className="space-y-1">
            {asks.flatMap((lvl) =>
              lvl.orders.map((o) => (
                <li key={o.id} className="flex justify-between text-red-600">
                  <span>${o.price.toFixed(2)}</span>
                  <span>{o.quantity} {symbol}</span>
                </li>
              ))
            )}
          </ul>
        </div>
      </div>
    </div>
  );
}
