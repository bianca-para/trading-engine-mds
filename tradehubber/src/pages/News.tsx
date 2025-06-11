import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Search, Calendar, Globe, Filter, Clock, ExternalLink } from "lucide-react";
import { formatRelative } from "date-fns";
import { cn } from "@/lib/utils";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import AuthModal from "@/components/AuthModal";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { userService } from "@/lib/services/userService";

interface NewsItem {
  id: number;
  title: string;
  content: string;
  source: string;
  date: string;
  category: string;
  imageUrl: string;
}

const News = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(() => {
    return localStorage.getItem('isAuthenticated') === 'true';
  });
  const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);
  const [authModalTab, setAuthModalTab] = useState<"login" | "signup">("login");
  const [news, setNews] = useState<NewsItem[]>([]);
  const [filteredNews, setFilteredNews] = useState<NewsItem[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [categoryFilter, setCategoryFilter] = useState<string>("all");
  const [view, setView] = useState<"grid" | "list">("grid");

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

  useEffect(() => {
    const fetchNews = async () => {
      try {
        setIsLoading(true);
        // Simulate API call with mock data
        const mockNews: NewsItem[] = [
          {
            id: 1,
            title: "Bitcoin Surges Past $50,000",
            content: "Bitcoin has reached a new all-time high, surpassing $50,000 for the first time in 2024...",
            source: "Crypto News",
            date: "2024-02-20",
            category: "crypto",
            imageUrl: "https://picsum.photos/800/400"
          },
          {
            id: 2,
            title: "Federal Reserve Announces Interest Rate Decision",
            content: "The Federal Reserve has decided to maintain current interest rates, citing stable inflation...",
            source: "Financial Times",
            date: "2024-02-19",
            category: "economy",
            imageUrl: "https://picsum.photos/800/400"
          },
          {
            id: 3,
            title: "Tech Stocks Rally on AI Boom",
            content: "Technology stocks are experiencing a significant rally as artificial intelligence continues to drive market growth...",
            source: "Tech Daily",
            date: "2024-02-18",
            category: "stocks",
            imageUrl: "https://picsum.photos/800/400"
          }
        ];
        setNews(mockNews);
        setFilteredNews(mockNews);
      } catch (error) {
        console.error('Error fetching news:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchNews();
  }, []);

  useEffect(() => {
    const filtered = news.filter(item => {
      const matchesSearch = item.title.toLowerCase().includes(search.toLowerCase()) ||
                          item.content.toLowerCase().includes(search.toLowerCase());
      const matchesCategory = categoryFilter === "all" || item.category === categoryFilter;
      return matchesSearch && matchesCategory;
    });
    setFilteredNews(filtered);
  }, [search, categoryFilter, news]);

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
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 gap-4">
            <div>
              <h1 className="text-3xl font-bold">News</h1>
              <p className="text-muted-foreground">Stay updated with the latest financial and market news</p>
            </div>
            
            <div className="flex items-center gap-2">
              <Tabs value={view} onValueChange={(value) => setView(value as "grid" | "list")} className="w-[180px]">
                <TabsList className="grid w-full grid-cols-2">
                  <TabsTrigger value="grid">Grid</TabsTrigger>
                  <TabsTrigger value="list">List</TabsTrigger>
                </TabsList>
              </Tabs>
            </div>
          </div>
          
          <div className="flex flex-col lg:flex-row gap-6">
            <div className="w-full lg:w-3/4">
              {/* Filters */}
              <div className="flex flex-col md:flex-row justify-between gap-4 mb-6">
                <div className="flex-1 max-w-md relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    type="text"
                    placeholder="Search news..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    className="pl-9"
                  />
                </div>
                
                <div className="flex flex-wrap gap-2">
                  <Select
                    value={categoryFilter}
                    onValueChange={setCategoryFilter}
                  >
                    <SelectTrigger className="w-[150px]">
                      <SelectValue placeholder="Category" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">All Categories</SelectItem>
                      <SelectItem value="economy">Economy</SelectItem>
                      <SelectItem value="markets">Markets</SelectItem>
                      <SelectItem value="commodities">Commodities</SelectItem>
                      <SelectItem value="cryptocurrency">Cryptocurrency</SelectItem>
                      <SelectItem value="technology">Technology</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>
              
              {/* News Articles */}
              {isLoading ? (
                view === "grid" ? (
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    {Array(6).fill(0).map((_, i) => (
                      <div key={i} className="bg-card border rounded-lg overflow-hidden animate-pulse">
                        <div className="h-48 bg-muted"></div>
                        <div className="p-6 space-y-3">
                          <div className="h-4 bg-muted rounded w-1/4"></div>
                          <div className="h-6 bg-muted rounded w-3/4"></div>
                          <div className="h-4 bg-muted rounded w-full"></div>
                          <div className="h-4 bg-muted rounded w-2/3"></div>
                          <div className="h-4 bg-muted rounded w-1/3"></div>
                        </div>
                      </div>
                    ))}
                  </div>
                ) : (
                  <div className="space-y-6">
                    {Array(6).fill(0).map((_, i) => (
                      <div key={i} className="flex flex-col md:flex-row gap-4 bg-card border rounded-lg overflow-hidden animate-pulse">
                        <div className="md:w-1/4 h-48 md:h-auto bg-muted"></div>
                        <div className="p-6 space-y-3 flex-1">
                          <div className="h-4 bg-muted rounded w-1/4"></div>
                          <div className="h-6 bg-muted rounded w-3/4"></div>
                          <div className="h-4 bg-muted rounded w-full"></div>
                          <div className="h-4 bg-muted rounded w-2/3"></div>
                          <div className="h-4 bg-muted rounded w-1/3"></div>
                        </div>
                      </div>
                    ))}
                  </div>
                )
              ) : filteredNews.length === 0 ? (
                <div className="text-center py-12">
                  <h3 className="text-lg font-medium">No news articles found</h3>
                  <p className="text-muted-foreground mt-2">Try adjusting your search or filters</p>
                </div>
              ) : view === "grid" ? (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  {filteredNews.map((item) => (
                    <Card key={item.id} className="overflow-hidden">
                      <img
                        src={item.imageUrl}
                        alt={item.title}
                        className="w-full h-48 object-cover"
                      />
                      <CardHeader>
                        <div className="flex items-center gap-2 mb-2">
                          <span className="text-xs font-medium px-2 py-1 bg-primary/10 text-primary rounded-full">
                            {item.category}
                          </span>
                          <span className="text-xs text-muted-foreground">
                            {item.source}
                          </span>
                        </div>
                        <CardTitle className="line-clamp-2">{item.title}</CardTitle>
                      </CardHeader>
                      <CardContent>
                        <p className="text-sm text-muted-foreground line-clamp-3 mb-4">
                          {item.content}
                        </p>
                        <div className="flex justify-between items-center">
                          <span className="text-xs text-muted-foreground">
                            {new Date(item.date).toLocaleDateString()}
                          </span>
                          <Button variant="outline" size="sm">
                            Read More
                          </Button>
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              ) : (
                <div className="space-y-6">
                  {filteredNews.map((item) => (
                    <Card key={item.id} className="overflow-hidden">
                      <img
                        src={item.imageUrl}
                        alt={item.title}
                        className="w-full h-48 object-cover"
                      />
                      <CardHeader>
                        <div className="flex items-center gap-2 mb-2">
                          <span className="text-xs font-medium px-2 py-1 bg-primary/10 text-primary rounded-full">
                            {item.category}
                          </span>
                          <span className="text-xs text-muted-foreground">
                            {item.source}
                          </span>
                        </div>
                        <CardTitle className="line-clamp-2">{item.title}</CardTitle>
                      </CardHeader>
                      <CardContent>
                        <p className="text-sm text-muted-foreground line-clamp-3 mb-4">
                          {item.content}
                        </p>
                        <div className="flex justify-between items-center">
                          <span className="text-xs text-muted-foreground">
                            {new Date(item.date).toLocaleDateString()}
                          </span>
                          <Button variant="outline" size="sm">
                            Read More
                          </Button>
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              )}
              
              {filteredNews.length > 0 && (
                <div className="mt-12 flex justify-center">
                  <Button variant="outline">Load More Articles</Button>
                </div>
              )}
            </div>
            
            <div className="w-full lg:w-1/4 space-y-6">
              <div className="bg-card border rounded-lg p-6">
                <h3 className="text-lg font-medium mb-4">Popular Categories</h3>
                <div className="space-y-2">
                  {["Economy", "Markets", "Cryptocurrencies", "Technology", "Commodities", "Forex"].map((category) => (
                    <Button
                      key={category}
                      variant="outline"
                      size="sm"
                      className="mr-2 mb-2"
                      onClick={() => setCategoryFilter(category.toLowerCase())}
                    >
                      {category}
                    </Button>
                  ))}
                </div>
              </div>
              
              <div className="bg-card border rounded-lg p-6">
                <h3 className="text-lg font-medium mb-4">Market Pulse</h3>
                <div className="space-y-4">
                  <div className="flex justify-between">
                    <div className="text-sm">S&P 500</div>
                    <div className="font-medium text-trade-buy">+0.87%</div>
                  </div>
                  <div className="flex justify-between">
                    <div className="text-sm">Nasdaq</div>
                    <div className="font-medium text-trade-buy">+1.24%</div>
                  </div>
                  <div className="flex justify-between">
                    <div className="text-sm">Bitcoin</div>
                    <div className="font-medium text-trade-buy">+2.37%</div>
                  </div>
                  <div className="flex justify-between">
                    <div className="text-sm">Gold</div>
                    <div className="font-medium text-trade-sell">-0.12%</div>
                  </div>
                  <div className="flex justify-between">
                    <div className="text-sm">Crude Oil</div>
                    <div className="font-medium text-trade-sell">-1.89%</div>
                  </div>
                </div>
                <div className="mt-4 pt-4 border-t">
                  <Button variant="link" className="p-0 h-auto">
                    View all markets
                  </Button>
                </div>
              </div>
              
              <div className="bg-primary p-6 rounded-lg text-primary-foreground">
                <h3 className="text-lg font-medium mb-2">Premium Analysis</h3>
                <p className="text-sm opacity-90 mb-4">
                  Get exclusive access to in-depth market analysis and expert commentary.
                </p>
                <Button variant="outline" className="w-full bg-white text-primary hover:bg-white/90" onClick={handleSignup}>
                  Upgrade Now
                </Button>
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

export default News;
