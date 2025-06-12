from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from main import matchingEngine, order
from datetime import datetime

app = FastAPI()

# 1) Add CORS middleware **before** any routes
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8090"],  # your React app origin
    allow_credentials=True,
    allow_methods=["*"],    # GET, POST, DELETE, etc.
    allow_headers=["*"],    # Authorization, Content-Type, etc.
)

engine_instance = matchingEngine()

class OrderIn(BaseModel):
    id: int
    traderId: str
    symbol: str
    price: float
    quantity: int
    side: str       # BUY or SELL
    timestamp: datetime

class OrderCancel(BaseModel):
    id: int
    traderId: str
    symbol: str
    side: str

@app.delete("/order")
def cancel_order(data: OrderCancel):
    success = engine_instance.cancelOrder(
        data.symbol, data.id, data.traderId, data.side.upper()
    )
    if not success:
        raise HTTPException(status_code=404, detail="Order not found or already matched")
    return {"status": "cancelled"}

@app.post("/order")
def receive_order(o: OrderIn):
    ord = order(**o.dict())
    trades = engine_instance.receiveOrder(ord)
    return {"trades": trades}

@app.get("/orderbook/{symbol}")
def get_orderbook(symbol: str):
    book = engine_instance.orderBooks.get(symbol)
    if not book:
        return {"bids": [], "asks": []}

    bids = [
        {
            "price": -price,
            "orders": [o.__dict__ for o in book.bidLevels[-price].orders],
        }
        for price in book.bidList
    ]
    asks = [
        {
            "price": price,
            "orders": [o.__dict__ for o in book.askLevels[price].orders],
        }
        for price in book.askList
    ]
    return {"bids": bids, "asks": asks}
