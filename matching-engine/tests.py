import unittest
from datetime import datetime
from main import order, matchingEngine

class TestMatchingEngine(unittest.TestCase):
    def setUp(self):
        self.engine = matchingEngine()

    def test_simple_match(self):
        o1 = order(1, "T1", "AAPL", 100, 5, "BUY", 1)
        o2 = order(2, "T2", "AAPL", 100, 5, "SELL", 2)
        self.engine.receiveOrder(o1)
        trades2 = self.engine.receiveOrder(o2)

        self.assertEqual(len(trades2), 1)
        self.assertEqual(trades2[0]['quantity'], 5)
        self.assertEqual(trades2[0]['price'], 100)

    def test_self_trade_prevention(self):
        o1 = order(1, "T1", "AAPL", 105, 5, "BUY", 1)
        o2 = order(2, "T1", "AAPL", 104, 5, "BUY", 2)
        o3 = order(3, "T1", "AAPL", 104, 5, "SELL", 3)
        self.engine.receiveOrder(o1)
        self.engine.receiveOrder(o2)
        trades = self.engine.receiveOrder(o3)

        self.assertEqual(len(trades), 0)

    def test_partial_fill(self):
        o1 = order(1, "T1", "AAPL", 100, 10, "BUY", 1)
        o2 = order(2, "T2", "AAPL", 100, 4, "SELL", 2)
        self.engine.receiveOrder(o1)
        trades2 = self.engine.receiveOrder(o2)

        self.assertEqual(len(trades2), 1)
        self.assertEqual(trades2[0]['quantity'], 4)

        book = self.engine.orderBooks["AAPL"]
        remaining_order = book.bidLevels[100].seeFirstOrder()
        self.assertEqual(remaining_order.quantity, 6)
        self.assertEqual(remaining_order.traderId, "T1")

    def test_multiple_levels(self):
        o1 = order(1, "B1", "AAPL", 102, 5, "BUY", 1)
        o2 = order(2, "B2", "AAPL", 101, 5, "BUY", 2)
        o3 = order(3, "S1", "AAPL", 100, 10, "SELL", 3)
        self.engine.receiveOrder(o1)
        self.engine.receiveOrder(o2)
        trades = self.engine.receiveOrder(o3)

        self.assertEqual(len(trades), 2)
        self.assertEqual(trades[0]['buyer'], "B1")
        self.assertEqual(trades[1]['buyer'], "B2")


def test_cancel_order(self):
    o1 = order(1, "T1", "AAPL", 100, 5, "BUY", 1)
    self.engine.receiveOrder(o1)

    book = self.engine.orderBooks["AAPL"]
    self.assertIn(100, book.bidLevels)
    self.assertEqual(len(book.bidLevels[100].orders), 1)

    success = self.engine.cancelOrder("AAPL", 1, "T1", "BUY")
    self.assertTrue(success)

    self.assertNotIn(100, book.bidLevels)

if __name__ == "__main__":
    unittest.main()




























































































    #sdgh#













































    #








































