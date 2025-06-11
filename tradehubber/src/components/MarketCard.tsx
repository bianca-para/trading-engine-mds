
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { cn } from "@/lib/utils";
import { TrendingUp, TrendingDown } from "lucide-react";
import PriceChip from "./PriceChip";

interface MarketCardProps {
  symbol: string;
  name: string;
  price: number;
  change: number;
  volume: number;
  className?: string;
}

const MarketCard = ({
  symbol,
  name,
  price,
  change,
  volume,
  className,
}: MarketCardProps) => {
  const isPositive = change > 0;
  
  return (
    <Card className={cn("overflow-hidden group hover:shadow-md transition-all duration-300", className)}>
      <CardHeader className="pb-2">
        <div className="flex justify-between items-start">
          <div>
            <CardTitle className="flex items-center">
              {symbol}
              {isPositive ? (
                <TrendingUp className="ml-2 w-4 h-4 text-trade-buy" />
              ) : (
                <TrendingDown className="ml-2 w-4 h-4 text-trade-sell" />
              )}
            </CardTitle>
            <CardDescription>{name}</CardDescription>
          </div>
          <PriceChip value={price} change={change} />
        </div>
      </CardHeader>
      <CardContent>
        <div className="text-sm text-muted-foreground">
          Volume: {volume.toLocaleString()}
        </div>
        <div className="mt-3 w-full h-[40px] bg-secondary rounded-md overflow-hidden">
          <div 
            className={cn(
              "h-full transition-all ease-out duration-700",
              isPositive ? "bg-trade-buy/20" : "bg-trade-sell/20"
            )}
            style={{ 
              width: `${Math.min(Math.abs(change * 10), 100)}%`,
              transform: `translateX(${isPositive ? '0%' : `-${100 - Math.min(Math.abs(change * 10), 100)}%`})` 
            }}
          />
        </div>
      </CardContent>
    </Card>
  );
};

export default MarketCard;
