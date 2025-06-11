
import { cn } from "@/lib/utils";

interface PriceChipProps {
  value: number;
  change: number;
  size?: "sm" | "md" | "lg";
  variant?: "default" | "ghost";
  className?: string;
}

const PriceChip = ({
  value,
  change,
  size = "md",
  variant = "default",
  className,
}: PriceChipProps) => {
  const isPositive = change > 0;
  const isNegative = change < 0;
  const changeColor = isPositive ? "text-trade-buy" : isNegative ? "text-trade-sell" : "text-muted-foreground";
  
  const sizeClasses = {
    sm: "text-xs py-0.5 px-2",
    md: "text-sm py-1 px-2.5",
    lg: "text-base py-1.5 px-3",
  };
  
  const variantClasses = {
    default: "bg-secondary/80 backdrop-blur-sm",
    ghost: "bg-transparent",
  };
  
  return (
    <div
      className={cn(
        "rounded-full font-medium inline-flex items-center space-x-1.5 transition-all",
        sizeClasses[size],
        variantClasses[variant],
        className
      )}
    >
      <span>{value.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
      <span className={cn("transition-colors", changeColor)}>
        {isPositive && "+"}
        {change.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}%
      </span>
    </div>
  );
};

export default PriceChip;
