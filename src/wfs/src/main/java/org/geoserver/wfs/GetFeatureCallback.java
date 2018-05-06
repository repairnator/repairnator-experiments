/* (c) 2017 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wfs;

import org.geoserver.platform.ExtensionPriority;
import org.geoserver.platform.ServiceException;

import java.io.IOException;

/**
 * <p><Callback that implementors can call onto in order to manipulate GetFeature data gather before it happens, by
 * replacing the data source and the query being run, or just perform checks on the request.
 * </p>
 * <p>A GetFeature can perform multiple queries, the callback will be invoked for each one of them separately</p>
 */
public interface GetFeatureCallback extends ExtensionPriority {

    /**
     * Called before the actual data query happens
     * @param context
     */
    void beforeQuerying(GetFeatureContext context) throws IOException, ServiceException;

    @Override
    default int getPriority() {
        return ExtensionPriority.LOWEST;
    }
}
