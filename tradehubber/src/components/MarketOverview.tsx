
import { useState, useEffect } from "react";
import { ArrowRight, Sparkles } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import MarketCard from "./MarketCard";
import { Link } from "react-router-dom";

interface Market {
  id: string;
  symbol: string;
  name: string;
  price: number;
  change: number;
  volume: number;
}

interface MarketOverviewProps {
  className?: string;
}

const MarketOverview = ({ className }: MarketOverviewProps) => {
  const [markets, setMarkets] = useState<Market[]>([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState<"all" | "gainers" | "losers">("all");

  useEffect(() => {
    // Simulate loading market data
    setLoading(true);
    
    // Mock market data
    setTimeout(() => {
      const mockMarkets: Market[] = [
        {
          id: "1",
          symbol: "BTC",
          name: "Bitcoin",
          price: 49327.85,
          change: 2.37,
          volume: 28431592034
        },
        {
          id: "2",
          symbol: "ETH",
          name: "Ethereum",
          price: 2583.41,
          change: 3.12,
          volume: 14583920183
        },
        {
          id: "3",
          symbol: "AAPL",
          name: "Apple Inc",
          price: 172.39,
          change: -0.78,
          volume: 74938293
        },
        {
          id: "4",
          symbol: "MSFT",
          name: "Microsoft",
          price: 331.21,
          change: 1.45,
          volume: 26284931
        },
        {
          id: "5",
          symbol: "TSLA",
          name: "Tesla",
          price: 248.19,
          change: -2.12,
          volume: 125743982
        },
        {
          id: "6",
          symbol: "AMZN",
          name: "Amazon",
          price: 129.12,
          change: 0.95,
          volume: 54372981
        },
        {
          id: "7",
          symbol: "GOOG",
          name: "Alphabet",
          price: 135.73,
          change: -0.45,
          volume: 19283746
        },
        {
          id: "8",
          symbol: "META",
          name: "Meta Platforms",
          price: 297.82,
          change: 3.57,
          volume: 21987654
        }
      ];
      
      setMarkets(mockMarkets);
      setLoading(false);
    }, 1000);
  }, []);

  const filteredMarkets = markets.filter((market) => {
    if (filter === "gainers") return market.change > 0;
    if (filter === "losers") return market.change < 0;
    return true;
  });

  return (
    <div className={cn("space-y-6", className)}>
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div>
          <h2 className="text-2xl font-bold tracking-tight">Market Overview</h2>
          <p className="text-muted-foreground">
            Latest prices and trends from global markets
          </p>
        </div>
        
        <div className="flex items-center space-x-2 bg-secondary/30 p-1 rounded-lg">
          <Button
            variant="ghost"
            size="sm"
            onClick={() => setFilter("all")}
            className={cn(
              "rounded-md",
              filter === "all" && "bg-background shadow-sm"
            )}
          >
            All
          </Button>
          <Button
            variant="ghost"
            size="sm"
            onClick={() => setFilter("gainers")}
            className={cn(
              "rounded-md",
              filter === "gainers" && "bg-background shadow-sm"
            )}
          >
            Top Gainers
          </Button>
          <Button
            variant="ghost"
            size="sm"
            onClick={() => setFilter("losers")}
            className={cn(
              "rounded-md",
              filter === "losers" && "bg-background shadow-sm"
            )}
          >
            Top Losers
          </Button>
        </div>
      </div>
      
      {loading ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {Array(8).fill(0).map((_, i) => (
            <div key={i} className="h-40 bg-card border rounded-lg animate-pulse">
              <div className="h-full flex flex-col justify-between p-4">
                <div className="space-y-2">
                  <div className="h-5 bg-muted rounded w-1/3"></div>
                  <div className="h-4 bg-muted rounded w-2/3"></div>
                </div>
                <div className="space-y-2">
                  <div className="h-6 bg-muted rounded w-1/2"></div>
                  <div className="h-4 bg-muted rounded w-full"></div>
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            {filteredMarkets.map((market) => (
              <Link to={`/trading/${market.symbol}`} key={market.id}>
                <MarketCard
                  symbol={market.symbol}
                  name={market.name}
                  price={market.price}
                  change={market.change}
                  volume={market.volume}
                  className="h-full transition-transform hover:-translate-y-1"
                />
              </Link>
            ))}
          </div>
          
          <div className="flex justify-center">
            <Button variant="outline" className="group">
              View all markets
              <ArrowRight className="ml-2 h-4 w-4 transition-transform group-hover:translate-x-1" />
            </Button>
          </div>
        </>
      )}
    </div>
  );
};

export default MarketOverview;
