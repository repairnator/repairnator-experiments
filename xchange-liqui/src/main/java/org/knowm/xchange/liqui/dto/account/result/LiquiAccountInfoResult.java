package org.knowm.xchange.liqui.dto.account.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.liqui.dto.LiquiStat;
import org.knowm.xchange.liqui.dto.account.LiquiAccountInfo;
import org.knowm.xchange.liqui.dto.marketdata.LiquiResult;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LiquiAccountInfoResult extends LiquiResult<LiquiAccountInfo> {

    private final boolean success;
    private final LiquiStat stat;

    public LiquiAccountInfoResult(@JsonProperty("success") final boolean success,
                                  @JsonProperty("return") final LiquiAccountInfo result,
                                  @JsonProperty("stat") final LiquiStat stat,
                                  @JsonProperty("error") final String error) {
        super(result, error);
        this.success = success;
        this.stat = stat;
    }

    public boolean isSuccess() {
        return success;
    }

    public LiquiStat getStat() {
        return stat;
    }

    @Override
    public String toString() {
        return "LiquiAccountInfoResult{" +
                "success=" + success +
                ", stat=" + stat +
                '}';
    }
}
