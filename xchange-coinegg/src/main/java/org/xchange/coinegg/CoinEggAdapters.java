package org.xchange.coinegg;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.utils.DateUtils;
import org.xchange.coinegg.dto.accounts.CoinEggBalance;
import org.xchange.coinegg.dto.marketdata.CoinEggOrder;
import org.xchange.coinegg.dto.marketdata.CoinEggTicker;
import org.xchange.coinegg.dto.marketdata.CoinEggTrades;
import org.xchange.coinegg.dto.marketdata.CoinEggOrder.Type;
import org.xchange.coinegg.dto.marketdata.CoinEggTrades.CoinEggTrade;
import org.xchange.coinegg.dto.trade.CoinEggTradeAdd;
import org.xchange.coinegg.dto.trade.CoinEggTradeCancel;
import org.xchange.coinegg.dto.trade.CoinEggTradeView;

public class CoinEggAdapters {

  public static Ticker adaptTicker(CoinEggTicker coinEggTicker, CurrencyPair currencyPair) {
    BigDecimal last = coinEggTicker.getLast();
    BigDecimal bid = coinEggTicker.getSell();
    BigDecimal ask = coinEggTicker.getBuy();
    BigDecimal high = coinEggTicker.getHigh();
    BigDecimal low = coinEggTicker.getLow();
    BigDecimal volume = coinEggTicker.getVolume();
      
    Ticker ticker = new Ticker.Builder()
                .currencyPair(currencyPair)
                .last(last)
                .bid(bid)
                .ask(ask)
                .high(high)
                .low(low)
                .volume(volume)
                .build();
      
    return ticker;
  }

  public static LimitOrder adaptOrder(CoinEggOrder coinEggOrder, CurrencyPair currencyPair) {
    
    OrderType type = coinEggOrder.getType() == Type.BUY ? OrderType.ASK : OrderType.BID;
    Date timestamp = DateUtils.fromMillisUtc(coinEggOrder.getTimestamp());
    String id = String.valueOf(coinEggOrder.getTimestamp());
    BigDecimal amount = coinEggOrder.getAmount();
    BigDecimal price = coinEggOrder.getPrice();
   
    LimitOrder order = new LimitOrder.Builder(type, currencyPair)
                             .id(id)
                             .timestamp(timestamp)
                             .originalAmount(amount)
                             .limitPrice(price)
                             .build();
    
    return order;
  }
  
  public static OrderBook adaptOrders(CoinEggOrder[] coinEggOrders, CurrencyPair currencyPair) {
    List<LimitOrder> asks = Stream.of(coinEggOrders)
                                  .filter(o -> o.getType() == Type.BUY)
                                  .map(o -> adaptOrder(o, currencyPair))
                                  .collect(Collectors.toList());
              
    List<LimitOrder> bids = Stream.of(coinEggOrders)
                                  .filter(o -> o.getType() == Type.SELL)
                                  .map(o -> adaptOrder(o, currencyPair))
                                  .collect(Collectors.toList());

    return new OrderBook(null, asks, bids);
  }

  public static Trade adaptTrade(CoinEggTrade coinEggTrade, OrderType type, CurrencyPair currencyPair) {
    return new Trade.Builder()
              .currencyPair(currencyPair)
              .id(String.valueOf(coinEggTrade.hashCode()))
              .type(type)
              .price(coinEggTrade.getPrice())
              .originalAmount(coinEggTrade.getQuantity())
              .build();
  }
  
  public static Trades adaptTrades(CoinEggTrades coinEggTrades, CurrencyPair currencyPair) {
    List<Trade> trades = new ArrayList<Trade>();
    
    trades.addAll(Stream.of(coinEggTrades.getAsks())
        .map(t -> adaptTrade(t, OrderType.ASK, currencyPair))
        .collect(Collectors.toList()));
    
    trades.addAll(Stream.of(coinEggTrades.getBids())
        .map(t -> adaptTrade(t, OrderType.BID, currencyPair))
        .collect(Collectors.toList()));
    
    return new Trades(trades);
  }

  //TODO: Implement XAS Currency
  public static AccountInfo adaptAccountInfo(CoinEggBalance coinEggBalance, Exchange exchange) {
    
    String userName = exchange.getExchangeSpecification().getUserName();
    Wallet btcWallet = new Wallet(new Balance(Currency.BTC, coinEggBalance.getBTCBalance()));
    Wallet ethWallet = new Wallet(new Balance(Currency.ETH, coinEggBalance.getETHBalance()));
    //Wallet xasWallet = new Wallet(new Balance(Currency.XAS, coinEggBalance.getXASBalance()));
    
    Set<Wallet> wallets = new HashSet<Wallet>();
    wallets.add(btcWallet);
    wallets.add(ethWallet);
    //wallets.add(xasWallet);
    
    return new AccountInfo(userName, null, wallets);
  }
  
  // TODO: Cleanup Code
  // TODO: Make Use Of Adapt Trade
  public static UserTrades adaptTradeHistory(CoinEggTradeView coinEggTradeView) {
    List<UserTrade> trades = new ArrayList<UserTrade>();
    Trade trade = new Trade.Builder()
                          //.currencyPair(null)
                          .id(String.valueOf(coinEggTradeView.getID()))
                          .type(coinEggTradeView.getType() == Type.BUY ? OrderType.ASK : OrderType.BID)
                          .price(coinEggTradeView.getPrice())
                          .originalAmount(coinEggTradeView.getAmountOriginal())
                          .timestamp(coinEggTradeView.getDateTime())
                          .build();
    
    trades.add((UserTrade) UserTrade.Builder.from(trade).build());
    
    return new UserTrades(trades, null);
  }

  
  public static String adaptTradeAdd(CoinEggTradeAdd coinEggTradeAdd) {
    return String.valueOf(coinEggTradeAdd.getID());
  }

  public static boolean adaptTradeCancel(CoinEggTradeCancel coinEggTradeCancel) {
    return coinEggTradeCancel.getResult();
  }
   
}
