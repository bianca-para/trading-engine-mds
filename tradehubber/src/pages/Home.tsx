// src/pages/Home.tsx
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import AuthModal from "@/components/AuthModal";
import { assetService, AssetResponse } from "@/lib/services/assetService";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Button } from "@/components/ui/button";

const Home = () => {
  // Auth state
  const [isAuthenticated, setIsAuthenticated] = useState(() => {
    const auth = localStorage.getItem('isAuthenticated');
    const token = localStorage.getItem('token');
    return auth === 'true' && token !== null;
  });
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);
  const [authModalTab, setAuthModalTab] = useState<"login" | "signup">("login");

  // Assets state
  const [assets, setAssets] = useState<AssetResponse[]>([]);
  const [loading, setLoading] = useState(true);

  // Auth effects
  useEffect(() => {
    const token = localStorage.getItem('token');
    // if you have a userService.setAuthToken, call it here
  }, []);

  useEffect(() => {
    const handleStorageChange = () => {
      const auth = localStorage.getItem('isAuthenticated');
      const token = localStorage.getItem('token');
      setIsAuthenticated(auth === 'true' && token !== null);
    };
    window.addEventListener('storage', handleStorageChange);
    handleStorageChange();
    return () => window.removeEventListener('storage', handleStorageChange);
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
    localStorage.removeItem('token');
    localStorage.removeItem('isAuthenticated');
    setIsAuthenticated(false);
    // if you have userService.setAuthToken(''), call it here
  };
  const handleAuthSuccess = () => {
    setIsAuthenticated(true);
    setIsAuthModalOpen(false);
  };

  // Fetch assets on mount
  useEffect(() => {
    const fetchAssets = async () => {
      setLoading(true);
      try {
        const data = await assetService.getAllAssets();
        setAssets(data);
      } catch (err) {
        console.error("Error fetching assets", err);
      } finally {
        setLoading(false);
      }
    };
    fetchAssets();
  }, []);

  return (
      <div className="min-h-screen bg-background">
        <Navbar
            isAuthenticated={isAuthenticated}
            onLogin={handleLogin}
            onSignup={handleSignup}
            onLogout={handleLogout}
        />

        <div className="container mx-auto px-4 py-8">
          <h1 className="text-3xl font-bold mb-4">Markets</h1>

          <div className="rounded-md border">
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
                ) : assets.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={3} className="py-8 text-center">
                        No assets available.
                      </TableCell>
                    </TableRow>
                ) : (
                    assets.map((asset) => (
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
                              <Link to={`/trading/${asset.assetId}`}>Trade</Link>
                            </Button>
                          </TableCell>
                        </TableRow>
                    ))
                )}
              </TableBody>
            </Table>
          </div>
        </div>

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

export default Home;
