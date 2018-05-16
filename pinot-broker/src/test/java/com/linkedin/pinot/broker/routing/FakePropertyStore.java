/**
 * Copyright (C) 2014-2016 LinkedIn Corp. (pinot-core@linkedin.com)
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
package com.linkedin.pinot.broker.routing;

import java.util.HashMap;
import java.util.Map;
import org.I0Itec.zkclient.IZkDataListener;
import org.apache.helix.ZNRecord;
import org.apache.helix.manager.zk.ZkBaseDataAccessor;
import org.apache.helix.store.zk.ZkHelixPropertyStore;
import org.apache.zookeeper.data.Stat;

public class FakePropertyStore extends ZkHelixPropertyStore<ZNRecord> {
  private Map<String, ZNRecord> _contents = new HashMap<>();
  private IZkDataListener _listener = null;

  public FakePropertyStore() {
    super((ZkBaseDataAccessor<ZNRecord>) null, null, null);
  }

  @Override
  public ZNRecord get(String path, Stat stat, int options) {
    return _contents.get(path);
  }

  @Override
  public void subscribeDataChanges(String path, IZkDataListener listener) {
    _listener = listener;
  }

  @Override
  public boolean set(String path, ZNRecord stat, int options) {
    try {
      setContents(path, stat);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public void setContents(String path, ZNRecord contents) throws Exception {
    _contents.put(path, contents);
    if (_listener != null) {
      _listener.handleDataChange(path, contents);
    }
  }

  @Override
  public void start() {
    // Don't try to connect to zk
  }
}
