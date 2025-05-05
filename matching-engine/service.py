from fastapi import FastAPI
from pydantic import BaseModel
from main import matchingEngine, order

app = FastAPI()
engine_instance = matchingEngine()

class OrderIn(BaseModel):
    id: int
    traderId: str
    symbol: str
    price: float
    quantity: int
    side: str  # BUY sau SELL
    timestamp: int

@app.post("/order")
def receive_order(o: OrderIn):
    # convertim în obiectul tău order
    ord = order(**o.dict())
    trades = engine_instance.receiveOrder(ord)
    return {"trades": trades}


@app.get("/orderbook/{symbol}")
def get_orderbook(symbol: str):
    book = engine_instance.orderBooks.get(symbol)
    if not book:
        return {"bids": [], "asks": []}

    bids = [{
        "price": -price,
        "orders": [o.__dict__ for o in book.bidLevels[-price].orders]
    } for price in book.bidList]

    asks = [{
        "price": price,
        "orders": [o.__dict__ for o in book.askLevels[price].orders]
    } for price in book.askList]

    return {"bids": bids, "asks": asks}

