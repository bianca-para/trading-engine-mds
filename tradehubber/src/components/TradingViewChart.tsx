
import { useEffect, useRef } from "react";
import { cn } from "@/lib/utils";

interface TradingViewChartProps {
  symbol: string;
  interval?: string;
  theme?: "light" | "dark";
  className?: string;
  containerClassName?: string;
  height?: number;
}

const TradingViewChart = ({
  symbol,
  interval = "D",
  theme = "dark",
  className,
  containerClassName,
  height = 500,
}: TradingViewChartProps) => {
  const containerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!containerRef.current) return;

    const script = document.createElement("script");
    script.src = "https://s3.tradingview.com/tv.js";
    script.async = true;
    script.onload = () => {
      if (typeof window.TradingView !== "undefined" && containerRef.current) {
        new window.TradingView.widget({
          autosize: true,
          symbol: symbol,
          interval: interval,
          timezone: "Etc/UTC",
          theme: theme,
          style: "1",
          locale: "en",
          toolbar_bg: "rgba(0, 0, 0, 0)",
          enable_publishing: false,
          allow_symbol_change: true,
          container_id: containerRef.current.id,
          hide_side_toolbar: false,
          studies: ["Volume@tv-basicstudies"],
          save_image: false,
        });
      }
    };
    
    document.head.appendChild(script);

    return () => {
      if (script.parentNode) {
        script.parentNode.removeChild(script);
      }
    };
  }, [symbol, interval, theme]);

  useEffect(() => {
    // Create a placeholder chart with a pulsing animation until the real chart loads
    const container = containerRef.current;
    if (!container) return;
    
    const createPlaceholder = () => {
      const svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
      svg.setAttribute("width", "100%");
      svg.setAttribute("height", "100%");
      svg.setAttribute("viewBox", "0 0 800 400");
      svg.classList.add("placeholder-chart");
      
      // Create a random stock chart path
      const path = document.createElementNS("http://www.w3.org/2000/svg", "path");
      let d = "M0,300 ";
      for (let i = 1; i <= 800; i += 20) {
        const y = 200 + Math.random() * 200 - 100;
        d += `L${i},${y} `;
      }
      path.setAttribute("d", d);
      path.classList.add("chart-line");
      
      svg.appendChild(path);
      container.appendChild(svg);
      
      return svg;
    };
    
    const placeholder = createPlaceholder();
    
    return () => {
      if (placeholder && placeholder.parentNode) {
        placeholder.parentNode.removeChild(placeholder);
      }
    };
  }, []);

  return (
    <div className={cn("relative overflow-hidden rounded-lg", containerClassName)}>
      <div 
        ref={containerRef}
        id={`tradingview-widget-${symbol.replace(/[^a-zA-Z0-9]/g, '')}`}
        className={cn("w-full", className)}
        style={{ height: height }}
      />
    </div>
  );
};

// Extend Window interface to include TradingView
declare global {
  interface Window {
    TradingView: any;
  }
}

export default TradingViewChart;
