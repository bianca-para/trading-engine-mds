
import { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Search,
  BarChart2,
  Menu,
  X,
  User,
  Bell,
  Sun,
  Moon,
} from "lucide-react";
import { cn } from "@/lib/utils";

interface NavbarProps {
  isAuthenticated: boolean;
  onLogin: () => void;
  onSignup: () => void;
  onLogout: () => void;
  className?: string;
}

const Navbar = ({
  isAuthenticated,
  onLogin,
  onSignup,
  onLogout,
  className,
}: NavbarProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const [darkMode, setDarkMode] = useState(false);
  const location = useLocation();
  
  const toggleDarkMode = () => {
    const newMode = !darkMode;
    setDarkMode(newMode);
    
    if (newMode) {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }
  };

  const NavLink = ({ to, label }: { to: string; label: string }) => {
    const isActive = location.pathname === to;
    
    return (
      <Link
        to={to}
        className={cn(
          "px-3 py-2 rounded-md text-sm font-medium transition-colors",
          isActive
            ? "bg-primary/10 text-primary"
            : "text-muted-foreground hover:text-foreground hover:bg-secondary/50"
        )}
      >
        {label}
      </Link>
    );
  };

  return (
    <nav
      className={cn(
        "sticky top-0 z-50 border-b backdrop-blur-md bg-background/80",
        className
      )}
    >
      <div className="max-w-screen-2xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center space-x-4">
            <Link to="/" className="flex items-center">
              <BarChart2 className="h-8 w-8 text-primary" />
              <span className="ml-2 text-xl font-semibold">TradeNow</span>
            </Link>
            
            <div className="hidden md:block">
              <div className="flex items-center space-x-1">
                <NavLink to="/" label="Home" />
                <NavLink to="/markets" label="Markets" />
         
                <NavLink to="/news" label="News" />
                {isAuthenticated && <NavLink to="/portfolio" label="Portfolio" />}
              </div>
            </div>
          </div>
          
          <div className="hidden md:flex items-center space-x-4">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <Input
                type="text"
                placeholder="Search markets..."
                className="pl-9 w-64 bg-secondary/30 border-secondary focus-visible:ring-offset-0"
              />
            </div>
            
            <Button
              onClick={toggleDarkMode}
              variant="ghost"
              size="icon"
              className="rounded-full"
            >
              {darkMode ? (
                <Sun className="h-5 w-5" />
              ) : (
                <Moon className="h-5 w-5" />
              )}
            </Button>
            
            {isAuthenticated ? (
              <>
                <Button variant="ghost" size="icon" className="rounded-full">
                  <Bell className="h-5 w-5" />
                </Button>
                <Button
                  onClick={onLogout}
                  variant="ghost"
                  className="font-medium"
                >
                  <User className="h-5 w-5 mr-2" />
                  Logout
                </Button>
              </>
            ) : (
              <>
                <Button
                  onClick={onLogin}
                  variant="ghost"
                  className="font-medium"
                >
                  Login
                </Button>
                <Button
                  onClick={onSignup}
                  className="font-medium"
                >
                  Sign Up
                </Button>
              </>
            )}
          </div>
          
          <div className="md:hidden flex items-center">
            <Button
              onClick={() => setIsOpen(!isOpen)}
              variant="ghost"
              size="icon"
              className="rounded-full"
            >
              {isOpen ? (
                <X className="h-6 w-6" />
              ) : (
                <Menu className="h-6 w-6" />
              )}
            </Button>
          </div>
        </div>
      </div>
      
      {/* Mobile menu */}
      <div
        className={cn(
          "md:hidden fixed inset-0 bg-background/95 backdrop-blur-sm z-40 transform transition-transform duration-300 ease-in-out",
          isOpen ? "translate-x-0" : "translate-x-full"
        )}
      >
        <div className="pt-20 px-4 pb-8 space-y-4">
          <Link
            to="/"
            className="block px-3 py-2 rounded-md text-base font-medium hover:bg-secondary"
            onClick={() => setIsOpen(false)}
          >
            Home
          </Link>
          <Link
            to="/markets"
            className="block px-3 py-2 rounded-md text-base font-medium hover:bg-secondary"
            onClick={() => setIsOpen(false)}
          >
            Markets
          </Link>
          <Link
            to="/trading"
            className="block px-3 py-2 rounded-md text-base font-medium hover:bg-secondary"
            onClick={() => setIsOpen(false)}
          >
            Trading
          </Link>
          <Link
            to="/news"
            className="block px-3 py-2 rounded-md text-base font-medium hover:bg-secondary"
            onClick={() => setIsOpen(false)}
          >
            News
          </Link>
          {isAuthenticated && (
            <Link
              to="/portfolio"
              className="block px-3 py-2 rounded-md text-base font-medium hover:bg-secondary"
              onClick={() => setIsOpen(false)}
            >
              Portfolio
            </Link>
          )}
          
          <div className="pt-4 border-t">
            {isAuthenticated ? (
              <Button
                onClick={() => {
                  onLogout();
                  setIsOpen(false);
                }}
                className="w-full justify-center"
              >
                Logout
              </Button>
            ) : (
              <div className="space-y-2">
                <Button
                  onClick={() => {
                    onLogin();
                    setIsOpen(false);
                  }}
                  variant="outline"
                  className="w-full justify-center"
                >
                  Login
                </Button>
                <Button
                  onClick={() => {
                    onSignup();
                    setIsOpen(false);
                  }}
                  className="w-full justify-center"
                >
                  Sign Up
                </Button>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
