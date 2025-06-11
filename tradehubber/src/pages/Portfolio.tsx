import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import {
  ArrowUpRight,
  ArrowDownRight,
  DollarSign,
  BarChart3,
  Wallet,
  Clock,
  ChevronRight,
  RefreshCw,
  AlertCircle,
  AreaChart
} from "lucide-react";
import { Link, Navigate } from "react-router-dom";
import { formatRelative } from "date-fns";
import { cn } from "@/lib/utils";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import AuthModal from "@/components/AuthModal";
import PriceChip from "@/components/PriceChip";
import TradingViewChart from "@/components/TradingViewChart";
import { portfolioService } from "@/lib/services/portfolioService";
import { assetService, AssetResponse } from "@/lib/services/assetService";
import { tradeService, TradeResponse } from "@/lib/services/tradeService";
import { useToast } from "@/components/ui/use-toast";
import { userService } from "@/lib/services/userService";

interface AssetPosition {
  id: string;
  symbol: string;
  name: string;
  amount: number;
  price: number;
  value: number;
  costBasis: number;
  pnl: number;
  pnlPercentage: number;
}

interface Transaction {
  id: string;
  type: "buy" | "sell" | "deposit" | "withdrawal";
  symbol?: string;
  amount: number;
  price?: number;
  value: number;
  timestamp: Date;
  status: "completed" | "pending" | "cancelled";
}

const Portfolio = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(() => {
    return localStorage.getItem('isAuthenticated') === 'true';
  });
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);
  const [authModalTab, setAuthModalTab] = useState<"login" | "signup">("login");
  const [isLoading, setIsLoading] = useState(true);
  const [positions, setPositions] = useState<AssetPosition[]>([]);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [portfolioValue, setPortfolioValue] = useState(0);
  const [portfolioChange, setPortfolioChange] = useState(0);
  const [cashBalance, setCashBalance] = useState(0);
  const { toast } = useToast();

  // Set auth token from localStorage on component mount
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      userService.setAuthToken(token);
    }
  }, []);

  const handleLogout = () => {
    setIsAuthenticated(false);
    localStorage.removeItem('token');
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('userEmail');
    userService.setAuthToken('');
  };

  const handleAuthSuccess = async () => {
    setIsAuthenticated(true);
    setIsAuthModalOpen(false);
    await loadPortfolioData();
  };

  const loadPortfolioData = async () => {
    try {
      setIsLoading(true);
      const portfolioData = await portfolioService.getPortfolio();
      const trades = await tradeService.getUserTrades();

      setPositions(portfolioData.positions);
      setTransactions(trades);
      setPortfolioValue(portfolioData.totalValue);
      setPortfolioChange(portfolioData.totalChange);
      setCashBalance(portfolioData.cashBalance);
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to load portfolio data",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (isAuthenticated) {
      loadPortfolioData();
    }
  }, [isAuthenticated]);

  if (!isAuthenticated) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar
          isAuthenticated={false}
          onLogin={() => {
            setIsAuthModalOpen(true);
            setAuthModalTab("login");
          }}
          onSignup={() => {
            setIsAuthModalOpen(true);
            setAuthModalTab("signup");
          }}
          onLogout={handleLogout}
        />
        <div className="container mx-auto px-4 py-8">
          <div className="max-w-2xl mx-auto text-center">
            <h1 className="text-4xl font-bold mb-4">Welcome to TradeHubber</h1>
            <p className="text-muted-foreground mb-8">
              Please log in or create an account to view your portfolio.
            </p>
            <div className="space-y-3">
              <Button onClick={() => {
                setIsAuthModalOpen(true);
                setAuthModalTab("login");
              }} className="w-full">
                Login to Your Account
              </Button>
              <Button variant="outline" onClick={() => {
                setIsAuthModalOpen(true);
                setAuthModalTab("signup");
              }} className="w-full">
                Create an Account
              </Button>
            </div>
          </div>
        </div>
        <AuthModal
          isOpen={isAuthModalOpen}
          onClose={() => setIsAuthModalOpen(false)}
          onSuccess={handleAuthSuccess}
          defaultTab={authModalTab}
        />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      <Navbar
        isAuthenticated={isAuthenticated}
        onLogin={() => {
          setIsAuthModalOpen(true);
          setAuthModalTab("login");
        }}
        onSignup={() => {
          setIsAuthModalOpen(true);
          setAuthModalTab("signup");
        }}
        onLogout={handleLogout}
      />
      <div className="container mx-auto px-4 py-8">
        <div className="grid gap-6">
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Portfolio Value</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">${portfolioValue.toFixed(2)}</div>
                <p className={`text-xs ${portfolioChange >= 0 ? 'text-green-500' : 'text-red-500'}`}>
                  {portfolioChange >= 0 ? '+' : ''}{portfolioChange.toFixed(2)}%
                </p>
              </CardContent>
            </Card>
            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Cash Balance</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">${cashBalance.toFixed(2)}</div>
              </CardContent>
            </Card>
          </div>

          <Tabs defaultValue="positions" className="space-y-4">
            <TabsList>
              <TabsTrigger value="positions">Positions</TabsTrigger>
              <TabsTrigger value="transactions">Transactions</TabsTrigger>
            </TabsList>
            <TabsContent value="positions" className="space-y-4">
              {isLoading ? (
                <div className="text-center py-8">Loading positions...</div>
              ) : positions.length === 0 ? (
                <div className="text-center py-8 text-muted-foreground">
                  No positions found. Start trading to build your portfolio.
                </div>
              ) : (
                <div className="grid gap-4">
                  {positions.map((position) => (
                    <Card key={position.assetId}>
                      <CardHeader>
                        <CardTitle className="flex items-center justify-between">
                          <span>{position.assetName}</span>
                          <span className="text-sm font-normal">
                            {position.amount} {position.assetSymbol}
                          </span>
                        </CardTitle>
                      </CardHeader>
                      <CardContent>
                        <div className="grid gap-2">
                          <div className="flex justify-between text-sm">
                            <span className="text-muted-foreground">Current Value</span>
                            <span>${position.value.toFixed(2)}</span>
                          </div>
                          <div className="flex justify-between text-sm">
                            <span className="text-muted-foreground">Cost Basis</span>
                            <span>${position.costBasis.toFixed(2)}</span>
                          </div>
                          <div className="flex justify-between text-sm">
                            <span className="text-muted-foreground">Profit/Loss</span>
                            <span className={position.profitLoss >= 0 ? 'text-green-500' : 'text-red-500'}>
                              ${position.profitLoss.toFixed(2)}
                            </span>
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              )}
            </TabsContent>
            <TabsContent value="transactions" className="space-y-4">
              {isLoading ? (
                <div className="text-center py-8">Loading transactions...</div>
              ) : transactions.length === 0 ? (
                <div className="text-center py-8 text-muted-foreground">
                  No transactions found.
                </div>
              ) : (
                <div className="grid gap-4">
                  {transactions.map((transaction) => (
                    <Card key={transaction.id}>
                      <CardHeader>
                        <CardTitle className="flex items-center justify-between">
                          <span>{transaction.type}</span>
                          <span className="text-sm font-normal">
                            {new Date(transaction.timestamp).toLocaleString()}
                          </span>
                        </CardTitle>
                      </CardHeader>
                      <CardContent>
                        <div className="grid gap-2">
                          <div className="flex justify-between text-sm">
                            <span className="text-muted-foreground">Amount</span>
                            <span>{transaction.amount} {transaction.symbol}</span>
                          </div>
                          <div className="flex justify-between text-sm">
                            <span className="text-muted-foreground">Price</span>
                            <span>${transaction.price.toFixed(2)}</span>
                          </div>
                          <div className="flex justify-between text-sm">
                            <span className="text-muted-foreground">Total</span>
                            <span>${transaction.total.toFixed(2)}</span>
                          </div>
                          <div className="flex justify-between text-sm">
                            <span className="text-muted-foreground">Status</span>
                            <span className={transaction.status === 'COMPLETED' ? 'text-green-500' : 'text-yellow-500'}>
                              {transaction.status}
                            </span>
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              )}
            </TabsContent>
          </Tabs>
        </div>
      </div>
      <AuthModal
        isOpen={isAuthModalOpen}
        onClose={() => setIsAuthModalOpen(false)}
        onSuccess={handleAuthSuccess}
        defaultTab={authModalTab}
      />
    </div>
  );
};

export default Portfolio;
