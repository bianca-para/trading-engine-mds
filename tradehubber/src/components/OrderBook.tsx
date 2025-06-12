// src/components/OrderBook.tsx

import { useState, useEffect } from "react";
import { orderService, OrderResponse } from "@/lib/services/orderService";
import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from "@/components/ui/table";
import { useToast } from "@/components/ui/use-toast";

interface OrderBookProps {
  assetId: number;
}

const OrderBook = ({ assetId }: OrderBookProps) => {
  const [orders, setOrders] = useState<OrderResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { toast } = useToast();

  useEffect(() => {
    const fetchOrders = async () => {
      setLoading(true);
      setError(null);
      try {
        // assetId may be passed as number or string now
        const data = await orderService.getOrdersByAsset(assetId);
        setOrders(data);
      } catch (err: any) {
        console.error("Error fetching order book:", err);
        const msg = err.response?.data?.message || err.message || "Failed to load orders";
        setError(msg);
        toast({
          title: "Order Book Error",
          description: msg,
          variant: "destructive",
        });
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
    const iv = setInterval(fetchOrders, 5000);
    return () => clearInterval(iv);
  }, [assetId, toast]);

  if (loading) {
    return <p className="p-4 text-center">Loading order book…</p>;
  }
  if (error) {
    return <p className="p-4 text-center text-red-500">{error}</p>;
  }
  if (orders.length === 0) {
    return <p className="p-4 text-center">No orders for this asset.</p>;
  }

  return (
      <div className="bg-card border rounded-lg overflow-x-auto">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>User ID</TableHead>
              <TableHead>Type</TableHead>
              <TableHead>Quantity</TableHead>
              <TableHead>Price</TableHead>
              <TableHead>Status</TableHead>
              <TableHead>Date</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {orders.map((o) => (
                <TableRow key={o.orderId}>
                  <TableCell>{o.userId}</TableCell>
                  <TableCell>{o.type}</TableCell>
                  <TableCell>{o.quantity}</TableCell>
                  <TableCell>${o.price.toFixed(2)}</TableCell>
                  <TableCell>{o.status}</TableCell>
                  <TableCell>
                    {new Date(o.createdAt).toLocaleString(undefined, {
                      dateStyle: "short",
                      timeStyle: "short",
                    })}
                  </TableCell>
                </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
  );
};

export default OrderBook;
