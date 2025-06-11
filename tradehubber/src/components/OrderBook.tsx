// src/components/OrderBook.tsx
import { useState, useEffect } from "react";
import { orderService, OrderResponse } from "@/lib/services/orderService";
import { toast } from "sonner";

interface OrderBookProps {
  assetId: number;
}

const OrderBook = ({ assetId }: OrderBookProps) => {
  const [asks, setAsks] = useState<OrderResponse[]>([]);
  const [bids, setBids] = useState<OrderResponse[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        setIsLoading(true);
        const orders = await orderService.getOrdersByAsset(assetId);
        const pending = orders.filter((o) => o.status === "PENDING");

        const asksList = pending
            .filter((o) => o.type === "SELL")
            .sort((a, b) => a.price - b.price);

        const bidsList = pending
            .filter((o) => o.type === "BUY")
            .sort((a, b) => b.price - a.price);

        setAsks(asksList);
        setBids(bidsList);
      } catch (error) {
        console.error("Error fetching order book:", error);
        toast.error("Failed to fetch order book", {
          description: "Please try again later.",
        });
      } finally {
        setIsLoading(false);
      }
    };

    fetchOrders();
    const interval = setInterval(fetchOrders, 5000);
    return () => clearInterval(interval);
  }, [assetId]);

  if (isLoading) {
    return (
        <div className="bg-card border rounded-lg p-4">
          <h3 className="text-lg font-semibold mb-4">Order Book</h3>
          <div className="animate-pulse space-y-4">
            <div className="h-4 bg-muted rounded w-3/4"></div>
            <div className="h-4 bg-muted rounded w-1/2"></div>
            <div className="h-4 bg-muted rounded w-2/3"></div>
          </div>
        </div>
    );
  }

  return (
      <div className="bg-card border rounded-lg p-4">
        <h3 className="text-lg font-semibold mb-4">Order Book</h3>
        <div className="mb-4">
          <div className="text-sm text-muted-foreground mb-2">Asks</div>
          <div className="space-y-1">
            {asks.map((order) => (
                <div
                    key={order.orderId}
                    className="flex justify-between items-center p-2 rounded hover:bg-muted/50 cursor-pointer"
                >
                  <span className="text-trade-sell">{order.price.toFixed(2)}</span>
                  <span>{order.quantity.toFixed(4)}</span>
                  <span>{(order.price * order.quantity).toFixed(2)}</span>
                </div>
            ))}
          </div>
        </div>
        <div>
          <div className="text-sm text-muted-foreground mb-2">Bids</div>
          <div className="space-y-1">
            {bids.map((order) => (
                <div
                    key={order.orderId}
                    className="flex justify-between items-center p-2 rounded hover:bg-muted/50 cursor-pointer"
                >
                  <span className="text-trade-buy">{order.price.toFixed(2)}</span>
                  <span>{order.quantity.toFixed(4)}</span>
                  <span>{(order.price * order.quantity).toFixed(2)}</span>
                </div>
            ))}
          </div>
        </div>
      </div>
  );
};

export default OrderBook;
