// src/pages/Markets.tsx
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Search } from "lucide-react";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import AuthModal from "@/components/AuthModal";
import MarketNews from "@/components/MarketNews";
import { assetService, AssetResponse } from "@/lib/services/assetService";
import { userService } from "@/lib/services/userService";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

const Markets = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(() => {
    return localStorage.getItem("isAuthenticated") === "true";
  });
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);
  const [authModalTab, setAuthModalTab] = useState<"login" | "signup">("login");

  const [assets, setAssets] = useState<AssetResponse[]>([]);
  const [loading, setLoading] = useState(true);

  const [searchQuery, setSearchQuery] = useState("");
  const [filter, setFilter] = useState<"all" | "crypto" | "stocks" | "forex">("all");

  // Mount: set auth token
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      userService.setAuthToken(token);
    }
  }, []);

  // Fetch assets
  useEffect(() => {
    const fetchAssets = async () => {
      setLoading(true);
      try {
        const data = await assetService.getAllAssets();
        setAssets(data);
      } catch (error) {
        console.error("Error fetching assets:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchAssets();
  }, []);

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
    userService.setAuthToken("");
  };
  const handleAuthSuccess = () => {
    setIsAuthenticated(true);
    setIsAuthModalOpen(false);
  };

  const filteredAssets = assets.filter((asset) => {
    const nameMatch = asset.name.toLowerCase().includes(searchQuery.toLowerCase());
    const symbolMatch = asset.symbol.toLowerCase().includes(searchQuery.toLowerCase());
    if (!nameMatch && !symbolMatch) return false;
    if (filter !== "all") return false;
    return true;
  });

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
            <h1 className="text-3xl font-bold mb-6">Markets</h1>

            <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
              {/* Left: filters + table */}
              <div className="lg:col-span-3 space-y-6">
                <div className="flex flex-col md:flex-row justify-between gap-4">
                  <div className="relative flex-1 max-w-md">
                    <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                    <Input
                        type="text"
                        placeholder="Search assets..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        className="pl-9"
                    />
                  </div>

                  <Select value={filter} onValueChange={(v) => setFilter(v as any)}>
                    <SelectTrigger className="w-[140px]">
                      <SelectValue placeholder="Filter by" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">All Markets</SelectItem>
                      <SelectItem value="crypto">Cryptocurrencies</SelectItem>
                      <SelectItem value="stocks">Stocks</SelectItem>
                      <SelectItem value="forex">Forex</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                <div className="bg-card border rounded-lg overflow-hidden">
                  <div className="overflow-x-auto">
                    <Table>
                      <TableHeader>
                        <TableRow>
                          <TableHead>Asset</TableHead>
                          <TableHead className="text-right">Price</TableHead>
                          <TableHead className="text-right">Action</TableHead>
                        </TableRow>
                      </TableHeader>
                      <TableBody>
                        {loading ? (
                            <TableRow>
                              <TableCell colSpan={3} className="py-8 text-center">
                                Loading markets...
                              </TableCell>
                            </TableRow>
                        ) : filteredAssets.length === 0 ? (
                            <TableRow>
                              <TableCell colSpan={3} className="py-8 text-center">
                                No assets found.
                              </TableCell>
                            </TableRow>
                        ) : (
                            filteredAssets.map((asset) => (
                                <TableRow key={asset.assetId}>
                                  <TableCell className="px-4 py-4">
                                    <div>
                                      <div className="font-medium">{asset.symbol}</div>
                                      <div className="text-xs text-muted-foreground">
                                        {asset.name}
                                      </div>
                                    </div>
                                  </TableCell>
                                  <TableCell className="px-4 py-4 text-right font-medium">
                                    ${asset.price.toFixed(2)}
                                  </TableCell>
                                  <TableCell className="px-4 py-4 text-right">
                                    <Button variant="outline" size="sm" asChild>
                                      <Link to={`/trading/${asset.symbol}`}>Trade</Link>

                                    </Button>
                                  </TableCell>
                                </TableRow>
                            ))
                        )}
                      </TableBody>
                    </Table>
                  </div>
                </div>
              </div>

              {/* Right: news + account */}
              <div className="space-y-6">
                <MarketNews />

                <div className="bg-card border rounded-lg p-6">
                  <h3 className="text-xl font-medium mb-4">Trading Account</h3>
                  {isAuthenticated ? (
                      <div className="space-y-4">
                        <p className="text-muted-foreground">
                          Your trading account is active.
                        </p>
                        <Button className="w-full">Go to Portfolio</Button>
                      </div>
                  ) : (
                      <div className="space-y-4">
                        <p className="text-muted-foreground">
                          Sign in to start trading.
                        </p>
                        <div className="grid grid-cols-2 gap-3">
                          <Button onClick={handleLogin} variant="outline">
                            Login
                          </Button>
                          <Button onClick={handleSignup}>Sign Up</Button>
                        </div>
                      </div>
                  )}
                </div>
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

export default Markets;
