// src/pages/Trading.tsx
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ArrowUp, ArrowDown, Activity, Compass, Zap } from "lucide-react";
import { cn } from "@/lib/utils";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import AuthModal from "@/components/AuthModal";
import TradingViewChart from "@/components/TradingViewChart";
import OrderBook from "@/components/OrderBook";
import OrderForm from "@/components/OrderForm";
import MarketNews from "@/components/MarketNews";
import PriceChip from "@/components/PriceChip";
import { assetService, AssetResponse } from "@/lib/services/assetService";
import { orderService, OrderRequest } from "@/lib/services/orderService";
import { tradeService } from "@/lib/services/tradeService";
import { useToast } from "@/components/ui/use-toast";
import { userService } from "@/lib/services/userService";

const Trading = () => {
  const { assetId } = useParams<{ assetId: string }>();
  const numericAssetId = Number(assetId);
  const [isAuthenticated, setIsAuthenticated] = useState(
      () => {
        return localStorage.getItem("isAuthenticated") === "true";
      }
  );
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);
  const [authModalTab, setAuthModalTab] = useState<"login" | "signup">("login");
  const [marketData, setMarketData] = useState<AssetResponse | null>(null);
  const [recentTrades, setRecentTrades] = useState<any[]>([]);
  const { toast } = useToast();

  // Attach token if present
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) userService.setAuthToken(token);
  }, []);

  // Fetch asset & trades every 5s
  useEffect(() => {
    if (isNaN(numericAssetId)) return;

    const fetchMarketData = async () => {
      try {
        const asset = await assetService.getAsset(numericAssetId);
        setMarketData(asset);

        // const trades = await tradeService.getTradesByAsset(numericAssetId);
        // setRecentTrades(trades);
      } catch (err) {
        console.error(err);
        toast({
          title: "Error",
          description: "Failed to fetch market data",
          variant: "destructive",
        });
      }
    };

    fetchMarketData();
    const iv = setInterval(fetchMarketData, 5000);
    return () => clearInterval(iv);
  }, [numericAssetId, toast]);

  const handleLogin = () => {
    setAuthModalTab("login");
    setIsAuthModalOpen(true);
  };
  const handleSignup = () => {
    setAuthModalTab("signup");
    setIsAuthModalOpen(true);
  };
  const handleLogout = () => {
    setIsAuthenticated(false);
    localStorage.removeItem("token");
    localStorage.removeItem("isAuthenticated");
    localStorage.removeItem("userEmail");
    userService.setAuthToken("");
  };
  const handleAuthSuccess = () => setIsAuthenticated(true);

  const handlePlaceOrder = async (orderData: OrderRequest) => {
    if (!isAuthenticated) {
      toast({
        title: "Authentication Required",
        description: "Please log in to place orders",
        variant: "destructive",
      });
      return;
    }
    try {
      await orderService.createOrder(orderData);
      toast({ title: "Success", description: "Order placed successfully" });

      // refresh marketData
      if (numericAssetId) {
        const updated = await assetService.getAsset(numericAssetId);
        setMarketData(updated);
      }
    } catch (err) {
      console.error(err);
      toast({
        title: "Error",
        description: "Failed to place order",
        variant: "destructive",
      });
    }
  };

  if (!marketData) {
    return (
        <div className="flex flex-col min-h-screen">
          <Navbar
              isAuthenticated={isAuthenticated}
              onLogin={handleLogin}
              onSignup={handleSignup}
              onLogout={handleLogout}
          />
          <main className="flex-1 bg-background flex items-center justify-center">
            <div className="text-center">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4" />
              <p className="text-muted-foreground">Loading market data…</p>
            </div>
          </main>
          <Footer />
        </div>
    );
  }

  return (
      <div className="flex flex-col min-h-screen">
        <Navbar
            isAuthenticated={isAuthenticated}
            onLogin={handleLogin}
            onSignup={handleSignup}
            onLogout={handleLogout}
        />
        <main className="flex-1 bg-background">
          <div className="container px-4 py-8 mx-auto">
            {/* Header */}
            <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-6 gap-4">
              <div>
                <h1 className="text-3xl font-bold flex items-center gap-2">
                  {marketData.symbol}
                  {marketData.price > 0 ? (
                      <ArrowUp className="text-trade-buy h-5 w-5" />
                  ) : (
                      <ArrowDown className="text-trade-sell h-5 w-5" />
                  )}
                </h1>
                <p className="text-muted-foreground">{marketData.name}</p>
              </div>
              <div className="flex flex-wrap items-center gap-4">
                <div className="flex flex-col items-end">
                  <PriceChip value={marketData.price} change={0} size="lg" />
                  <div className="text-xs text-muted-foreground mt-1">
                    Updated just now
                  </div>
                </div>
              </div>
            </div>

            {/* Main */}
            <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
              {/* Chart */}
              <div className="lg:col-span-3 space-y-6">
                <div className="bg-card border rounded-lg overflow-hidden">
                  <div className="border-b p-4 flex justify-between items-center">
                    <Tabs defaultValue="chart">
                      <TabsList>
                        <TabsTrigger value="chart" className="flex items-center">
                          <Activity className="h-4 w-4 mr-2" />
                          Chart
                        </TabsTrigger>
                        <TabsTrigger value="depth" className="flex items-center">
                          <Compass className="h-4 w-4 mr-2" />
                          Depth
                        </TabsTrigger>
                        <TabsTrigger value="trades" className="flex items-center">
                          <Zap className="h-4 w-4 mr-2" />
                          Trades
                        </TabsTrigger>
                      </TabsList>
                      <div className="flex items-center space-x-2">
                        <Button variant="outline" size="sm">1h</Button>
                        <Button variant="outline" size="sm">4h</Button>
                        <Button variant="outline" size="sm" className="bg-secondary">1D</Button>
                        <Button variant="outline" size="sm">1W</Button>
                      </div>
                    </Tabs>
                  </div>
                  <div className="h-[500px]">
                    <TradingViewChart symbol={marketData.symbol} height={500} />
                  </div>
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <OrderBook assetId={numericAssetId} />
                  <MarketNews symbol={marketData.symbol} />
                </div>
              </div>

              {/* Order form */}
              <div className="space-y-6">
                <OrderForm
                    symbol={marketData.symbol}
                    currentPrice={marketData.price}
                    isAuthenticated={isAuthenticated}
                    onPlaceOrder={handlePlaceOrder}
                    className="sticky top-20"
                />
                {!isAuthenticated && (
                    <div
                        className={cn(
                            "p-4 border border-primary/20 rounded-lg bg-primary/5",
                            "text-center space-y-3 animate-fade-in"
                        )}
                    >
                      <h3 className="font-medium">Trading requires an account</h3>
                      <p className="text-sm text-muted-foreground">
                        Create an account or sign in to start trading on {marketData.symbol}
                      </p>
                      <div className="flex gap-2">
                        <Button onClick={handleSignup} className="flex-1">
                          Sign Up
                        </Button>
                        <Button onClick={handleLogin} variant="outline" className="flex-1">
                          Login
                        </Button>
                      </div>
                    </div>
                )}
              </div>
            </div>
          </div>
        </main>
        <Footer />
        <AuthModal
            isOpen={isAuthModalOpen}
            onClose={() => setIsAuthModalOpen(false)}
            onSuccess={handleAuthSuccess}
            defaultTab={authModalTab}
        />
      </div>
  );
};

export default Trading;
