/*
 * The MIT License
 *
 * Copyright 2015-2016 Coinmate.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.knowm.xchange.coinmate;

import java.io.IOException;
import java.math.BigDecimal;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.knowm.xchange.coinmate.dto.account.CoinmateBalance;
import org.knowm.xchange.coinmate.dto.account.CoinmateDepositAddresses;
import org.knowm.xchange.coinmate.dto.trade.CoinmateCancelOrderResponse;
import org.knowm.xchange.coinmate.dto.trade.CoinmateCancelOrderWithInfoResponse;
import org.knowm.xchange.coinmate.dto.trade.CoinmateOpenOrders;
import org.knowm.xchange.coinmate.dto.trade.CoinmateOrderHistory;
import org.knowm.xchange.coinmate.dto.trade.CoinmateTradeResponse;
import org.knowm.xchange.coinmate.dto.trade.CoinmateTransactionHistory;

import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

/**
 * @author Martin Stachon
 */
@Path("api")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public interface CoinmateAuthenticated extends Coinmate {

  // acount info
  @POST
  @Path("balances")
  CoinmateBalance getBalances(@FormParam("publicKey") String publicKey, @FormParam("clientId") String clientId,
      @FormParam("signature") ParamsDigest signer, @FormParam("nonce") SynchronizedValueFactory<Long> nonce) throws IOException;

  // trade
  @POST
  @Path("transactionHistory")
  CoinmateTransactionHistory getTransactionHistory(@FormParam("publicKey") String publicKey, @FormParam("clientId") String clientId,
      @FormParam("signature") ParamsDigest signer, @FormParam("nonce") SynchronizedValueFactory<Long> nonce, @FormParam("offset") int offset,
      @FormParam("limit") int limit, @FormParam("sort") String sort) throws IOException;

  @POST
  @Path("openOrders")
  CoinmateOpenOrders getOpenOrders(@FormParam("publicKey") String publicKey, @FormParam("clientId") String clientId,
      @FormParam("signature") ParamsDigest signer, @FormParam("nonce") SynchronizedValueFactory<Long> nonce,
      @FormParam("currencyPair") String currencyPair) throws IOException;

  @POST
  @Path("cancelOrder")
  CoinmateCancelOrderResponse cancelOder(@FormParam("publicKey") String publicKey, @FormParam("clientId") String clientId,
      @FormParam("signature") ParamsDigest signer, @FormParam("nonce") SynchronizedValueFactory<Long> nonce,
      @FormParam("orderId") String orderId) throws IOException;

  // new in version 1.3
  @POST
  @Path("cancelOrderWithInfo")
  CoinmateCancelOrderWithInfoResponse cancelOderWithInfo(@FormParam("publicKey") String publicKey, @FormParam("clientId") String clientId,
      @FormParam("signature") ParamsDigest signer, @FormParam("nonce") SynchronizedValueFactory<Long> nonce,
      @FormParam("orderId") String orderId) throws IOException;

  @POST
  @Path("buyLimit")
  CoinmateTradeResponse buyLimit(@FormParam("publicKey") String publicKey, @FormParam("clientId") String clientId,
      @FormParam("signature") ParamsDigest signer, @FormParam("nonce") SynchronizedValueFactory<Long> nonce, @FormParam("amount") BigDecimal amount,
      @FormParam("price") BigDecimal price, @FormParam("currencyPair") String currencyPair) throws IOException;

  @POST
  @Path("sellLimit")
  CoinmateTradeResponse sellLimit(@FormParam("publicKey") String publicKey, @FormParam("clientId") String clientId,
      @FormParam("signature") ParamsDigest signer, @FormParam("nonce") SynchronizedValueFactory<Long> nonce, @FormParam("amount") BigDecimal amount,
      @FormParam("price") BigDecimal price, @FormParam("currencyPair") String currencyPair) throws IOException;

  @POST
  @Path("buyInstant")
  CoinmateTradeResponse buyInstant(@FormParam("publicKey") String publicKey, @FormParam("clientId") String clientId,
      @FormParam("signature") ParamsDigest signer, @FormParam("nonce") SynchronizedValueFactory<Long> nonce, @FormParam("total") BigDecimal total,
      @FormParam("currencyPair") String currencyPair) throws IOException;

  @POST
  @Path("sellInstant")
  CoinmateTradeResponse sellInstant(@FormParam("publicKey") String publicKey, @FormParam("clientId") String clientId,
      @FormParam("signature") ParamsDigest signer, @FormParam("nonce") SynchronizedValueFactory<Long> nonce, @FormParam("amount") BigDecimal amount,
      @FormParam("currencyPair") String currencyPair) throws IOException;

  // withdrawal and deposits
  @POST
  @Path("bitcoinWithdrawal")
  CoinmateTradeResponse bitcoinWithdrawal(@FormParam("publicKey") String publicKey, @FormParam("clientId") String clientId,
      @FormParam("signature") ParamsDigest signer, @FormParam("nonce") SynchronizedValueFactory<Long> nonce, @FormParam("amount") BigDecimal amount,
      @FormParam("address") String address) throws IOException;

  @POST
  @Path("bitcoinDepositAddresses")
  CoinmateDepositAddresses bitcoinDepositAddresses(@FormParam("publicKey") String publicKey, @FormParam("clientId") String clientId,
      @FormParam("signature") ParamsDigest signer, @FormParam("nonce") SynchronizedValueFactory<Long> nonce) throws IOException;

  // new in version 1.5
  // trade
  @POST
  @Path("orderHistory")
  CoinmateOrderHistory getOrderHistory(@FormParam("publicKey") String publicKey,
      @FormParam("clientId") String clientId,
      @FormParam("signature") ParamsDigest signer,
      @FormParam("nonce") SynchronizedValueFactory<Long> nonce,
      @FormParam("currencyPair") String currencyPair,
      @FormParam("limit") int limit) throws IOException;
}
