
import { BarChart2, Github, Twitter, Linkedin } from "lucide-react";
import { cn } from "@/lib/utils";
import { Link } from "react-router-dom";

interface FooterProps {
  className?: string;
}

const Footer = ({ className }: FooterProps) => {
  return (
    <footer className={cn("border-t py-12", className)}>
      <div className="container px-4 md:px-6">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div className="md:col-span-1">
            <Link to="/" className="flex items-center">
              <BarChart2 className="h-8 w-8 text-primary" />
              <span className="ml-2 text-xl font-semibold">TradeHub</span>
            </Link>
            <p className="mt-3 text-sm text-muted-foreground">
              Professional trading platform with real-time market data and advanced trading tools.
            </p>
            <div className="mt-6 flex items-center space-x-4">
              <a 
                href="#" 
                className="text-muted-foreground hover:text-foreground transition-colors"
                aria-label="Twitter"
              >
                <Twitter className="h-5 w-5" />
              </a>
              <a 
                href="#" 
                className="text-muted-foreground hover:text-foreground transition-colors"
                aria-label="GitHub"
              >
                <Github className="h-5 w-5" />
              </a>
              <a 
                href="#" 
                className="text-muted-foreground hover:text-foreground transition-colors"
                aria-label="LinkedIn"
              >
                <Linkedin className="h-5 w-5" />
              </a>
            </div>
          </div>
          
          <div className="md:col-span-3 grid grid-cols-1 sm:grid-cols-3 gap-8">
            <nav className="space-y-4">
              <h3 className="text-base font-medium">Platform</h3>
              <ul className="space-y-3">
                <li>
                  <Link to="/markets" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
                    Markets
                  </Link>
                </li>
                <li>
                  <Link to="/trading" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
                    Trading
                  </Link>
                </li>
                <li>
                  <Link to="/news" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
                    News
                  </Link>
                </li>
                <li>
                  <Link to="/portfolio" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
                    Portfolio
                  </Link>
                </li>
              </ul>
            </nav>
            
            <nav className="space-y-4">
              <h3 className="text-base font-medium">Resources</h3>
              <ul className="space-y-3">
                <li>
                  <a href="#" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
                    Documentation
                  </a>
                </li>
                <li>
                  <a href="#" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
                    API Reference
                  </a>
                </li>
                <li>
                  <a href="#" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
                    Learning Resources
                  </a>
                </li>
                <li>
                  <a href="#" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
                    Market Analysis
                  </a>
                </li>
              </ul>
            </nav>
            
            <nav className="space-y-4">
              <h3 className="text-base font-medium">Company</h3>
              <ul className="space-y-3">
                <li>
                  <a href="#" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
                    About
                  </a>
                </li>
                <li>
                  <a href="#" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
                    Blog
                  </a>
                </li>
                <li>
                  <a href="#" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
                    Careers
                  </a>
                </li>
                <li>
                  <a href="#" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
                    Contact
                  </a>
                </li>
              </ul>
            </nav>
          </div>
        </div>
        
        <div className="mt-12 pt-8 border-t text-center sm:flex sm:justify-between sm:text-left">
          <p className="text-sm text-muted-foreground">
            &copy; {new Date().getFullYear()} TradeHub. All rights reserved.
          </p>
          <div className="mt-4 sm:mt-0 flex flex-wrap justify-center sm:justify-end gap-4">
            <a href="#" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
              Terms of Service
            </a>
            <a href="#" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
              Privacy Policy
            </a>
            <a href="#" className="text-sm text-muted-foreground hover:text-foreground transition-colors">
              Cookie Policy
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
