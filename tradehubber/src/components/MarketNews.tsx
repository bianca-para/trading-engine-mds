
import { useState, useEffect } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { ExternalLink, Clock } from "lucide-react";
import { cn } from "@/lib/utils";
import { formatRelative } from "date-fns";

interface NewsItem {
  id: string;
  title: string;
  summary: string;
  url: string;
  publishedAt: Date;
  source: string;
  sentiment?: "positive" | "negative" | "neutral";
}

interface MarketNewsProps {
  className?: string;
  symbol?: string;
}

const MarketNews = ({ className, symbol }: MarketNewsProps) => {
  const [news, setNews] = useState<NewsItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Simulate loading news data
    setLoading(true);
    
    // Mock news data (in reality, this would be fetched from an API)
    setTimeout(() => {
      const mockNews: NewsItem[] = [
        {
          id: "1",
          title: "Federal Reserve Signals Potential Rate Cut",
          summary: "The Federal Reserve has indicated it may consider a rate cut in the coming months as inflation pressures ease and economic growth stabilizes.",
          url: "#",
          publishedAt: new Date(Date.now() - 3600000), // 1 hour ago
          source: "Financial Times",
          sentiment: "positive"
        },
        {
          id: "2",
          title: symbol ? `${symbol} Surges on Strong Quarterly Earnings` : "Market Volatility Increases Amid Global Tensions",
          summary: symbol ? `${symbol} reported better-than-expected quarterly earnings, driving the stock price up by 8% in after-hours trading.` : "Global market volatility has increased as geopolitical tensions rise in key regions.",
          url: "#",
          publishedAt: new Date(Date.now() - 8600000), // ~2.4 hours ago
          source: "Bloomberg",
          sentiment: "positive"
        },
        {
          id: "3",
          title: "Tech Sector Leads Market Rally",
          summary: "Technology stocks are leading a broad market rally as investors bet on continued growth in the digital economy.",
          url: "#",
          publishedAt: new Date(Date.now() - 86400000), // 1 day ago
          source: "CNBC",
          sentiment: "positive"
        },
        {
          id: "4",
          title: "Oil Prices Drop on Supply Concerns",
          summary: "Crude oil prices fell sharply today on news of increased production and concerns about global demand.",
          url: "#",
          publishedAt: new Date(Date.now() - 172800000), // 2 days ago
          source: "Reuters",
          sentiment: "negative"
        },
        {
          id: "5",
          title: symbol ? `Analysts Remain Cautious on ${symbol} Despite Rally` : "Economists Warn of Potential Recession Risks",
          summary: symbol ? `Despite recent gains, several analysts maintain a cautious outlook on ${symbol}, citing valuation concerns.` : "Leading economists are warning that several indicators point to increased recession risk in the coming year.",
          url: "#",
          publishedAt: new Date(Date.now() - 259200000), // 3 days ago
          source: "Wall Street Journal",
          sentiment: "negative"
        }
      ];
      
      setNews(mockNews);
      setLoading(false);
    }, 1000);
  }, [symbol]);

  const getSentimentBadgeStyle = (sentiment?: "positive" | "negative" | "neutral") => {
    switch (sentiment) {
      case "positive":
        return "bg-trade-buy/10 text-trade-buy";
      case "negative":
        return "bg-trade-sell/10 text-trade-sell";
      default:
        return "bg-secondary text-muted-foreground";
    }
  };

  return (
    <Card className={cn("", className)}>
      <CardHeader>
        <CardTitle>Market News</CardTitle>
        <CardDescription>
          {symbol ? `Latest news related to ${symbol}` : "Latest financial market news"}
        </CardDescription>
      </CardHeader>
      <CardContent className="space-y-4">
        {loading ? (
          Array(3).fill(0).map((_, i) => (
            <div key={i} className="space-y-2">
              <div className="h-4 bg-muted rounded w-3/4 animate-pulse"></div>
              <div className="h-3 bg-muted rounded w-full animate-pulse"></div>
              <div className="h-3 bg-muted rounded w-2/3 animate-pulse"></div>
            </div>
          ))
        ) : (
          news.map((item) => (
            <div key={item.id} className="border-b last:border-0 pb-4 last:pb-0">
              <div className="flex justify-between items-start mb-1">
                <h3 className="font-medium line-clamp-2 hover:text-primary/90 transition-colors">
                  <a href={item.url} target="_blank" rel="noopener noreferrer" className="flex items-start">
                    {item.title}
                    <ExternalLink className="h-3 w-3 ml-1 mt-1 flex-shrink-0" />
                  </a>
                </h3>
                <span className={cn("text-xs px-2 py-0.5 rounded-full", getSentimentBadgeStyle(item.sentiment))}>
                  {item.sentiment || "neutral"}
                </span>
              </div>
              <p className="text-sm text-muted-foreground line-clamp-2 mb-2">{item.summary}</p>
              <div className="flex items-center text-xs text-muted-foreground">
                <Clock className="h-3 w-3 mr-1" />
                {formatRelative(item.publishedAt, new Date())}
                <span className="mx-2">•</span>
                {item.source}
              </div>
            </div>
          ))
        )}
      </CardContent>
    </Card>
  );
};

export default MarketNews;
