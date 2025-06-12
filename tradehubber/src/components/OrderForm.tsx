// src/components/OrderForm.tsx

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { cn } from "@/lib/utils";
import { toast } from "sonner";
import { ArrowRight } from "lucide-react";
import { OrderRequest } from "@/lib/services/orderService";

interface OrderFormProps {
  assetId: number;
  symbol: string;
  currentPrice: number;
  isAuthenticated: boolean;
  onPlaceOrder: (orderData: OrderRequest) => Promise<void>;
  className?: string;
}

export default function OrderForm({
                                    assetId,
                                    symbol,
                                    currentPrice,
                                    isAuthenticated,
                                    onPlaceOrder,
                                    className,
                                  }: OrderFormProps) {
  const [orderType, setOrderType] = useState<"market" | "limit">("market");
  const [amount, setAmount] = useState<string>("");
  const [price, setPrice] = useState<string>(currentPrice.toString());
  const [total, setTotal] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);

  // Now each field only updates its own state:
  const handleAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAmount(e.target.value);
  };
  const handlePriceChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPrice(e.target.value);
  };
  const handleTotalChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTotal(e.target.value);
  };

  const handleSubmit = async (side: "buy" | "sell") => {
    if (!isAuthenticated) {
      toast.error("Authentication required", {
        description: "Please sign in to place orders.",
      });
      return;
    }

    const userId = localStorage.getItem("userId");
    if (!userId) {
      toast.error("User not found", {
        description: "Please log in again.",
      });
      return;
    }

    if (!amount || parseFloat(amount) <= 0) {
      toast.error("Invalid amount", {
        description: "Please enter a valid amount.",
      });
      return;
    }
    if (orderType === "limit" && (!price || parseFloat(price) <= 0)) {
      toast.error("Invalid price", {
        description: "Please enter a valid price.",
      });
      return;
    }

    setLoading(true);
    try {
      const now = new Date().toISOString();
      const orderData: OrderRequest = {
        userId,
        assetId,
        quantity: parseFloat(amount),
        price:
            orderType === "market" ? currentPrice : parseFloat(price),
        type: side.toUpperCase() as "BUY" | "SELL",
        createdAt: now,
      };

      await onPlaceOrder(orderData);

      // reset the form (but keep price at current market)
      setAmount("");
      setPrice(currentPrice.toString());
      setTotal("");
    } catch (err) {
      console.error("Error placing order:", err);
      toast.error("Failed to place order", {
        description: "Please try again later.",
      });
    } finally {
      setLoading(false);
    }
  };

  return (
      <div className={cn("rounded-lg border bg-card", className)}>
        <Tabs defaultValue="buy" className="w-full">
          <div className="p-4 border-b">
            <TabsList className="w-full grid grid-cols-2">
              <TabsTrigger
                  value="buy"
                  className="data-[state=active]:bg-trade-buy/10 data-[state=active]:text-trade-buy"
              >
                Buy
              </TabsTrigger>
              <TabsTrigger
                  value="sell"
                  className="data-[state=active]:bg-trade-sell/10 data-[state=active]:text-trade-sell"
              >
                Sell
              </TabsTrigger>
            </TabsList>
            <div className="flex items-center justify-between mt-4">
              <button
                  onClick={() => setOrderType("market")}
                  className={cn(
                      "px-3 py-1 text-sm rounded-md transition-all",
                      orderType === "market"
                          ? "bg-secondary text-foreground font-medium"
                          : "text-muted-foreground hover:bg-secondary/50"
                  )}
              >
                Market
              </button>
              <button
                  onClick={() => setOrderType("limit")}
                  className={cn(
                      "px-3 py-1 text-sm rounded-md transition-all",
                      orderType === "limit"
                          ? "bg-secondary text-foreground font-medium"
                          : "text-muted-foreground hover:bg-secondary/50"
                  )}
              >
                Limit
              </button>
            </div>
          </div>

          {/* BUY */}
          <TabsContent value="buy" className="p-4 space-y-4 animate-fade-in">
            {/* Amount */}
            <div className="space-y-1.5">
              <label className="text-xs text-muted-foreground">
                Amount ({symbol})
              </label>
              <div className="relative">
                <Input
                    type="number"
                    value={amount}
                    onChange={handleAmountChange}
                    placeholder={`Amount of ${symbol}`}
                    className="pr-16"
                />
                <div className="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-muted-foreground">
                  {symbol}
                </div>
              </div>
            </div>

            {/* Limit Price */}
            {orderType === "limit" && (
                <div className="space-y-1.5">
                  <label className="text-xs text-muted-foreground">
                    Price (USD)
                  </label>
                  <div className="relative">
                    <Input
                        type="number"
                        value={price}
                        onChange={handlePriceChange}
                        placeholder="Price per unit"
                        className="pr-16"
                    />
                    <div className="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-muted-foreground">
                      USD
                    </div>
                  </div>
                </div>
            )}

            {/* Total */}
            <div className="space-y-1.5">
              <label className="text-xs text-muted-foreground">
                Total (USD)
              </label>
              <div className="relative">
                <Input
                    type="number"
                    value={total}
                    onChange={handleTotalChange}
                    placeholder="Total amount in USD"
                    className="pr-16"
                />
                <div className="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-muted-foreground">
                  USD
                </div>
              </div>
            </div>

            <Button
                onClick={() => handleSubmit("buy")}
                className="w-full bg-trade-buy hover:bg-trade-buy/90"
                disabled={loading}
            >
              {isAuthenticated ? (
                  <>Buy {symbol} <ArrowRight className="ml-2 h-4 w-4" /></>
              ) : (
                  <>Sign in to trade <ArrowRight className="ml-2 h-4 w-4" /></>
              )}
            </Button>
          </TabsContent>

          {/* SELL */}
          <TabsContent value="sell" className="p-4 space-y-4 animate-fade-in">
            {/* Amount */}
            <div className="space-y-1.5">
              <label className="text-xs text-muted-foreground">
                Amount ({symbol})
              </label>
              <div className="relative">
                <Input
                    type="number"
                    value={amount}
                    onChange={handleAmountChange}
                    placeholder={`Amount of ${symbol}`}
                    className="pr-16"
                />
                <div className="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-muted-foreground">
                  {symbol}
                </div>
              </div>
            </div>

            {/* Limit Price */}
            {orderType === "limit" && (
                <div className="space-y-1.5">
                  <label className="text-xs text-muted-foreground">
                    Price (USD)
                  </label>
                  <div className="relative">
                    <Input
                        type="number"
                        value={price}
                        onChange={handlePriceChange}
                        placeholder="Price per unit"
                        className="pr-16"
                    />
                    <div className="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-muted-foreground">
                      USD
                    </div>
                  </div>
                </div>
            )}

            {/* Total */}
            <div className="space-y-1.5">
              <label className="text-xs text-muted-foreground">
                Total (USD)
              </label>
              <div className="relative">
                <Input
                    type="number"
                    value={total}
                    onChange={handleTotalChange}
                    placeholder="Total amount in USD"
                    className="pr-16"
                />
                <div className="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-muted-foreground">
                  USD
                </div>
              </div>
            </div>

            <Button
                onClick={() => handleSubmit("sell")}
                className="w-full bg-trade-sell hover:bg-trade-sell/90"
                disabled={loading}
            >
              {isAuthenticated ? (
                  <>Sell {symbol} <ArrowRight className="ml-2 h-4 w-4" /></>
              ) : (
                  <>Sign in to trade <ArrowRight className="ml-2 h-4 w-4" /></>
              )}
            </Button>
          </TabsContent>
        </Tabs>
      </div>
  );
}
