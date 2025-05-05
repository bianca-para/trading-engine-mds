from collections import deque
from datetime import datetime
from bisect import insort


class order:
    def __init__(self, id, traderId, symbol, price, quantity, side, timestamp):
        self.id = id
        self.traderId = traderId
        self.symbol = symbol
        self.price = price
        self.quantity = quantity
        self.side = side
        self.timestamp = timestamp

    def __repr__(self):
        return (f"Trader: {self.traderId}\nOrder {self.id}: {self.side}, {self.symbol}, "
                f"price={self.price}, quantity={self.quantity}, timestamp={self.timestamp}")


class orderbookLevel:
    def __init__(self):
        self.orders = deque()

    def addOrder(self, order):
        self.orders.append(order)

    def removeOrder(self):
        return self.orders.popleft()

    def seeFirstOrder(self):
        return self.orders[0] if self.orders else None

    def isEmpty(self):
        return len(self.orders) == 0


class orderbook:
    def __init__(self):
        self.bidLevels = {}  # price: level
        self.askLevels = {}
        self.bidList = []  # sorted descending (using negative prices)
        self.askList = []  # sorted ascending

    def addOrder(self, order):
        if order.side == 'SELL':
            if order.price not in self.askLevels:
                self.askLevels[order.price] = orderbookLevel()
                insort(self.askList, order.price)
            self.askLevels[order.price].addOrder(order)
        else:
            if order.price not in self.bidLevels:
                self.bidLevels[order.price] = orderbookLevel()
                insort(self.bidList, -order.price)  # store as negative for descending order
            self.bidLevels[order.price].addOrder(order)

    def removeLevel(self, price, side):
        if side == 'SELL':
            if price in self.askLevels:
                del self.askLevels[price]
                self.askList.remove(price)
        else:
            if price in self.bidLevels:
                del self.bidLevels[price]
                self.bidList.remove(-price)


class matchingEngine:
    def __init__(self):
        self.orderBooks = {}

    def receiveOrder(self, order):
        if order.symbol not in self.orderBooks:
            self.orderBooks[order.symbol] = orderbook()
        trades = self.match(order, self.orderBooks[order.symbol])
        if order.quantity > 0:
            self.orderBooks[order.symbol].addOrder(order)
        return trades

    def match(self, order, book):
        trades = []
        if order.side == 'SELL':
            while order.quantity > 0 and book.bidList:
                bestBidPrice = -book.bidList[0]
                if order.price <= bestBidPrice:
                    level = book.bidLevels[bestBidPrice]
                    while order.quantity > 0 and not level.isEmpty():
                        bestBidOrder = level.seeFirstOrder()
                        tradeQuantity = min(bestBidOrder.quantity, order.quantity)
                        trades.append({
                            'buyer': bestBidOrder.traderId,
                            'seller': order.traderId,
                            'symbol': order.symbol,
                            'price': bestBidPrice,
                            'quantity': tradeQuantity,
                            'timestamp': datetime.utcnow().isoformat()
                        })
                        order.quantity -= tradeQuantity
                        bestBidOrder.quantity -= tradeQuantity
                        if bestBidOrder.quantity == 0:
                            level.removeOrder()
                    if level.isEmpty():
                        book.removeLevel(bestBidPrice, 'BUY')
                else:
                    break
        else:  # BUY order
            while order.quantity > 0 and book.askList:
                bestAskPrice = book.askList[0]
                if order.price >= bestAskPrice:
                    level = book.askLevels[bestAskPrice]
                    while order.quantity > 0 and not level.isEmpty():
                        bestAskOrder = level.seeFirstOrder()
                        tradeQuantity = min(bestAskOrder.quantity, order.quantity)
                        trades.append({
                            'buyer': order.traderId,
                            'seller': bestAskOrder.traderId,
                            'symbol': order.symbol,
                            'price': bestAskPrice,
                            'quantity': tradeQuantity,
                            'timestamp': datetime.utcnow().isoformat()
                        })
                        order.quantity -= tradeQuantity
                        bestAskOrder.quantity -= tradeQuantity
                        if bestAskOrder.quantity == 0:
                            level.removeOrder()
                    if level.isEmpty():
                        book.removeLevel(bestAskPrice, 'SELL')
                else:
                    break
        return trades


# Run sample test
engine = matchingEngine()

orders = [
    order(id=1, traderId="T1", symbol="TSLA", price=100, quantity=10, side="BUY", timestamp=1),
    order(id=2, traderId="T2", symbol="TSLA", price=101, quantity=5, side="BUY", timestamp=2),
    order(id=3, traderId="T3", symbol="TSLA", price=99, quantity=7, side="SELL", timestamp=3),
    order(id=4, traderId="T4", symbol="TSLA", price=100, quantity=4, side="SELL", timestamp=4),
    order(id=5, traderId="T5", symbol="TSLA", price=102, quantity=8, side="BUY", timestamp=5),
    order(id=6, traderId="T6", symbol="TSLA", price=98, quantity=6, side="SELL", timestamp=6),
    order(id=7, traderId="T7", symbol="TSLA", price=101, quantity=3, side="SELL", timestamp=7),
    order(id=8, traderId="T8", symbol="TSLA", price=102, quantity=2, side="SELL", timestamp=8),
    order(id=9, traderId="T9", symbol="TSLA", price=99, quantity=10, side="BUY", timestamp=9),
    order(id=10, traderId="T10", symbol="TSLA", price=100, quantity=6, side="SELL", timestamp=10),
    order(id=11, traderId="T11", symbol="TSLA", price=98, quantity=4, side="BUY", timestamp=11),
    order(id=12, traderId="T12", symbol="TSLA", price=97, quantity=8, side="SELL", timestamp=12),
    order(id=13, traderId="T13", symbol="TSLA", price=96, quantity=7, side="SELL", timestamp=13),
    order(id=14, traderId="T14", symbol="TSLA", price=97, quantity=2, side="BUY", timestamp=14),
    order(id=15, traderId="T15", symbol="TSLA", price=103, quantity=5, side="BUY", timestamp=15),
    order(id=16, traderId="T16", symbol="TSLA", price=101, quantity=6, side="SELL", timestamp=16),
    order(id=17, traderId="T17", symbol="TSLA", price=100, quantity=10, side="BUY", timestamp=17),
    order(id=18, traderId="T18", symbol="TSLA", price=104, quantity=2, side="BUY", timestamp=18),
    order(id=19, traderId="T19", symbol="TSLA", price=102, quantity=6, side="SELL", timestamp=19),
    order(id=20, traderId="T20", symbol="TSLA", price=101, quantity=4, side="SELL", timestamp=20),
]

for o in orders:
    trades = engine.receiveOrder(o)
    print(f"Trades after Order {o.id}:", trades)

book = engine.orderBooks["TSLA"]

print("\nFinal OrderBook BIDS:")
for price in book.bidList:
    actual_price = -price
    print(f"Price {actual_price}: {[str(o) for o in book.bidLevels[actual_price].orders]}")

print("\nFinal OrderBook ASKS:")
for price in book.askList:
    print(f"Price {price}: {[str(o) for o in book.askLevels[price].orders]}")
