import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { ArrowRight, ChevronDown } from "lucide-react";
import MarketOverview from "@/components/MarketOverview";
import MarketNews from "@/components/MarketNews";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import AuthModal from "@/components/AuthModal";
import TradingViewChart from "@/components/TradingViewChart";
import { userService } from "@/lib/services/userService";

const Index = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(() => {
    return localStorage.getItem('isAuthenticated') === 'true';
  });
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);
  const [authModalTab, setAuthModalTab] = useState<"login" | "signup">("login");

  // Set auth token from localStorage on component mount
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      userService.setAuthToken(token);
    }
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
    localStorage.removeItem('token');
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('userEmail');
    userService.setAuthToken('');
  };

  const handleAuthSuccess = () => {
    setIsAuthenticated(true);
    setIsAuthModalOpen(false);
  };

  return (
    <div className="flex flex-col min-h-screen">
      <Navbar
        isAuthenticated={isAuthenticated}
        onLogin={handleLogin}
        onSignup={handleSignup}
        onLogout={handleLogout}
      />
      
      <main className="flex-1">
        {/* Hero Section */}
        <section className="relative py-16 md:py-24 overflow-hidden">
          <div className="container px-4 md:px-6">
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 items-center">
              <div className="space-y-6 animate-fade-in">
                <div className="inline-flex items-center rounded-full border px-2.5 py-0.5 text-sm font-semibold transition-colors">
                  <span className="text-muted-foreground">Introducing TradeHub Platform</span>
                </div>
                <h1 className="text-4xl md:text-6xl font-bold tracking-tight">
                  Modern Trading Platform for Smart Investors
                </h1>
                <p className="text-xl text-muted-foreground">
                  Real-time market data, interactive charts, and powerful trading tools. All in one place.
                </p>
                <div className="flex flex-col sm:flex-row gap-4">
                  <Button size="lg" className="group" onClick={handleSignup}>
                    Start Trading <ArrowRight className="ml-2 h-5 w-5 transition-transform group-hover:translate-x-1" />
                  </Button>
                  <Button size="lg" variant="outline">
                    <Link to="/markets">Explore Markets</Link>
                  </Button>
                </div>
              </div>
              <div className="relative w-full h-[300px] md:h-[500px] rounded-lg overflow-hidden shadow-xl animate-slide-up">
                <TradingViewChart symbol="BTCUSD" height={500} />
              </div>
            </div>
            
            <div className="mt-16 md:mt-24 flex justify-center">
              <a href="#market-section" className="flex flex-col items-center text-muted-foreground hover:text-foreground transition-colors">
                <span className="text-sm mb-2">Discover More</span>
                <ChevronDown className="animate-bounce h-5 w-5" />
              </a>
            </div>
          </div>
        </section>
        
        {/* Market Overview Section */}
        <section id="market-section" className="py-16 bg-secondary/20">
          <div className="container px-4 md:px-6">
            <MarketOverview />
          </div>
        </section>
        
        {/* Features Section */}
        <section className="py-16 md:py-24">
          <div className="container px-4 md:px-6">
            <div className="text-center max-w-3xl mx-auto mb-12">
              <h2 className="text-3xl font-bold tracking-tight">Why Choose TradeHub</h2>
              <p className="mt-4 text-lg text-muted-foreground">
                Designed for both beginners and professional traders with powerful features
              </p>
            </div>
            
            <div className="grid md:grid-cols-3 gap-8">
              <div className="bg-card border rounded-lg p-6 transition-all hover:shadow-md">
                <div className="h-12 w-12 rounded-full bg-primary/10 flex items-center justify-center mb-4">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="2"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    className="text-primary"
                  >
                    <path d="M3 3v18h18" />
                    <path d="m19 9-5 5-4-4-3 3" />
                  </svg>
                </div>
                <h3 className="text-xl font-semibold mb-2">Real-Time Data</h3>
                <p className="text-muted-foreground">
                  Access live market data and advanced charts to make informed trading decisions.
                </p>
              </div>
              
              <div className="bg-card border rounded-lg p-6 transition-all hover:shadow-md">
                <div className="h-12 w-12 rounded-full bg-primary/10 flex items-center justify-center mb-4">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="2"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    className="text-primary"
                  >
                    <path d="M12 2H2v10h10V2Z" />
                    <path d="M22 12h-4v4h-4v4h8v-8Z" />
                    <path d="M18 2h4v4" />
                    <path d="M18 22v-4" />
                    <path d="M2 18v4h4" />
                    <path d="M2 22V12" />
                    <path d="M18 6V2" />
                    <path d="M6 2v4" />
                  </svg>
                </div>
                <h3 className="text-xl font-semibold mb-2">Advanced Order Book</h3>
                <p className="text-muted-foreground">
                  View detailed order books and market depth to find the best entry and exit points.
                </p>
              </div>
              
              <div className="bg-card border rounded-lg p-6 transition-all hover:shadow-md">
                <div className="h-12 w-12 rounded-full bg-primary/10 flex items-center justify-center mb-4">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="2"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    className="text-primary"
                  >
                    <path d="M20.5 3l-.5 19.5L12 15l-8 7.5L3.5 3H20.5z" />
                  </svg>
                </div>
                <h3 className="text-xl font-semibold mb-2">Smart Trading Engine</h3>
                <p className="text-muted-foreground">
                  Our matching algorithms ensure fast execution and optimal pricing for all trades.
                </p>
              </div>
            </div>
          </div>
        </section>
        
        {/* News Section */}
        <section className="py-16 bg-secondary/20">
          <div className="container px-4 md:px-6">
            <h2 className="text-3xl font-bold tracking-tight mb-8">Latest Market News</h2>
            <MarketNews />
          </div>
        </section>
        
        {/* CTA Section */}
        <section className="py-16 md:py-24">
          <div className="container px-4 md:px-6">
            <div className="rounded-lg bg-primary p-8 md:p-16 relative overflow-hidden">
              <div className="relative z-10 max-w-2xl">
                <h2 className="text-3xl md:text-4xl font-bold text-primary-foreground mb-4">
                  Ready to start trading?
                </h2>
                <p className="text-lg md:text-xl text-primary-foreground/80 mb-8">
                  Join thousands of traders who trust TradeHub for their investment needs. Get started in minutes.
                </p>
                <Button
                  size="lg"
                  variant="outline"
                  className="bg-white text-primary hover:bg-white/90"
                  onClick={handleSignup}
                >
                  Create Free Account
                </Button>
              </div>
              
              {/* Abstract shapes in background */}
              <div className="absolute right-0 top-0 h-full w-1/2 opacity-10">
                <svg viewBox="0 0 400 400" xmlns="http://www.w3.org/2000/svg">
                  <path
                    d="M156.4,339.5c31.8-2.5,59.4-26.8,80.2-48.5c28.3-29.5,40.5-47,56.1-85.1c14-34.3,20.7-75.6,2.3-111  c-18.1-34.8-55.7-58-90.4-72.3c-11.7-4.8-24.1-8.8-36.8-11.5l-0.9-0.9l-0.6,0.6c-27.7-5.8-56.6-6-82.4,3c-38.8,13.6-64,48.8-66.8,90.3c-3,43.9,17.8,88.3,33.7,128.8c5.3,13.5,10.4,27.1,14.9,40.9C77.5,309.9,111,343,156.4,339.5z"
                    fill="currentColor"
                  />
                </svg>
              </div>
            </div>
          </div>
        </section>
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

export default Index;
