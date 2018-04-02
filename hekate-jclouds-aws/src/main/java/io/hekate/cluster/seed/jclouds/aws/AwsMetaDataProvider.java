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

/**
 * AWS EC2 instance meta-data provider.
 *
 * <p>
 * This interface provides an abstraction layer for {@link EC2MetadataUtils}.
 * </p>
 */
public interface AwsMetaDataProvider {
    /**
     * See {@link EC2MetadataUtils#getInstanceId()}.
     *
     * @return ID of this instance.
     */
    String getInstanceId();

    /**
     * See {@link EC2MetadataUtils#getAmiId()}.
     *
     * @return AMI ID used to launch the instance.
     */
    String getAmiId();

    /**
     * See {@link EC2MetadataUtils#getInstanceType()}.
     *
     * @return Type of the instance.
     */
    String getInstanceType();

    /**
     * See {@link EC2MetadataUtils#getEC2InstanceRegion()}.
     *
     * @return current region of this running EC2 instance; or null if it is unable to do so.
     */
    String getInstanceRegion();
}
