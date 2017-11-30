package org.knowm.xchange.lakebtc.dto.trade;

import org.knowm.xchange.lakebtc.dto.LakeBTCResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cristian.lucaci on 12/19/2014.
 */
public class LakeBTCOrdersResponse extends LakeBTCResponse<LakeBTCOrder> {

  /**
   * Constructor
   *
   * @param id
   * @param result
   */
  public LakeBTCOrdersResponse(@JsonProperty("id") String id, @JsonProperty("result") LakeBTCOrder result) {
    super(id, result);
  }
}
