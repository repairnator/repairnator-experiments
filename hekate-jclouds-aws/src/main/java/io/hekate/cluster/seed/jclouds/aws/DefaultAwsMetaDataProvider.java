/*
 * Copyright 2018 The Hekate Project
 *
 * The Hekate Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.hekate.cluster.seed.jclouds.aws;

import com.amazonaws.util.EC2MetadataUtils;
import io.hekate.util.format.ToString;

/**
 * Default implementation of {@link AwsMetaDataProvider}.
 *
 * <p>
 * This class uses {@link EC2MetadataUtils} to obtain meta-data of a running instance.
 * </p>
 */
public class DefaultAwsMetaDataProvider implements AwsMetaDataProvider {
    @Override
    public String getInstanceId() {
        return EC2MetadataUtils.getInstanceId();
    }

    @Override
    public String getAmiId() {
        return EC2MetadataUtils.getAmiId();
    }

    @Override
    public String getInstanceType() {
        return EC2MetadataUtils.getInstanceType();
    }

    @Override
    public String getInstanceRegion() {
        return EC2MetadataUtils.getEC2InstanceRegion();
    }

    @Override
    public String toString() {
        return ToString.format(this);
    }
}
