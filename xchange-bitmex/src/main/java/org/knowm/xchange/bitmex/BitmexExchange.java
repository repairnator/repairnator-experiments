package org.knowm.xchange.bitmex;

import java.io.IOException;
import java.util.List;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bitmex.dto.account.BitmexTicker;
import org.knowm.xchange.bitmex.service.BitmexAccountService;
import org.knowm.xchange.bitmex.service.BitmexMarketDataService;
import org.knowm.xchange.bitmex.service.BitmexMarketDataServiceRaw;
import org.knowm.xchange.bitmex.service.BitmexTradeService;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.utils.nonce.AtomicLongIncrementalTime2013NonceFactory;

import si.mazi.rescu.SynchronizedValueFactory;

public class BitmexExchange extends BaseExchange implements Exchange {

  private SynchronizedValueFactory<Long> nonceFactory = new AtomicLongIncrementalTime2013NonceFactory();

  @Override
  protected void initServices() {
    this.marketDataService = new BitmexMarketDataService(this);
    this.accountService = new BitmexAccountService(this);
    this.tradeService = new BitmexTradeService(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass().getCanonicalName());
    exchangeSpecification.setSslUri("https://www.bitmex.com/");
    exchangeSpecification.setHost("bitmex.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("Bitmex");
    exchangeSpecification.setExchangeDescription("Bitmex is a bitcoin exchange.");

    return exchangeSpecification;
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {

    return nonceFactory;
  }
  @Override
  public void remoteInit() throws IOException, ExchangeException {
    BitmexMarketDataServiceRaw dataService = (BitmexMarketDataServiceRaw) this.marketDataService;
    List<BitmexTicker> tickers = dataService.getActiveTickers();
    exchangeMetaData = BitmexAdapters.adaptMetaData(tickers);
  }
}
