/**
 * Copyright (C) 2014-2018 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.common.query.context;

import com.linkedin.pinot.common.metrics.ServerMetrics;
import com.linkedin.pinot.common.metrics.ServerQueryPhase;
import com.linkedin.pinot.common.request.BrokerRequest;
import java.util.concurrent.TimeUnit;


public class TimerContext {
  private final BrokerRequest _brokerRequest;
  private final ServerMetrics _serverMetrics;
  private final long _queryArrivalTimeMs;
  private final Timer[] _phaseTimers = new Timer[ServerQueryPhase.values().length];

  public class Timer {
    private final ServerQueryPhase _queryPhase;
    private final long _startTimeMs;

    private long _durationMs;
    private boolean _ended;

    private Timer(ServerQueryPhase queryPhase, long startTimeMs) {
      _queryPhase = queryPhase;
      _startTimeMs = startTimeMs;
    }

    public void stopAndRecord() {
      if (!_ended) {
        _durationMs = System.currentTimeMillis() - _startTimeMs;
        _serverMetrics.addPhaseTiming(_brokerRequest, _queryPhase, _durationMs, TimeUnit.MILLISECONDS);
        _ended = true;
      }
    }

    public long getDurationMs() {
      return _ended ? _durationMs : -1;
    }
  }

  public TimerContext(BrokerRequest brokerRequest, ServerMetrics serverMetrics, long queryArrivalTimeMs) {
    _brokerRequest = brokerRequest;
    _serverMetrics = serverMetrics;
    _queryArrivalTimeMs = queryArrivalTimeMs;
  }

  public long getQueryArrivalTimeMs() {
    return _queryArrivalTimeMs;
  }

  /**
   * Creates a new timer for query phase with the given start time in millis.
   * <p>Calling this again for same phase will overwrite existing timing information.
   *
   * @param queryPhase Query phase to be timed
   * @param startTimeMs Timer start time in millis
   * @return Timer for the query phase
   */
  public Timer startNewPhaseTimer(ServerQueryPhase queryPhase, long startTimeMs) {
    Timer phaseTimer = new Timer(queryPhase, startTimeMs);
    _phaseTimers[queryPhase.ordinal()] = phaseTimer;
    return phaseTimer;
  }

  /**
   * Creates a new timer for query phase with {@link System#currentTimeMillis()} as the start time.
   * <p>Calling this again for same phase will overwrite existing timing information.
   *
   * @param queryPhase Query phase to be timed
   * @return Timer for the query phase
   */
  public Timer startNewPhaseTimer(ServerQueryPhase queryPhase) {
    return startNewPhaseTimer(queryPhase, System.currentTimeMillis());
  }

  public Timer getPhaseTimer(ServerQueryPhase queryPhase) {
    return _phaseTimers[queryPhase.ordinal()];
  }

  public long getPhaseDurationMs(ServerQueryPhase queryPhase) {
    Timer phaseTimer = _phaseTimers[queryPhase.ordinal()];
    return phaseTimer != null ? phaseTimer.getDurationMs() : -1;
  }
}
