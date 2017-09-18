/* (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.script.rest;

import static java.lang.String.format;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.io.IOUtils;
import org.geoserver.platform.resource.Resources;
import org.geoserver.rest.RestletException;
import org.geoserver.script.ScriptManager;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Resource;

/**
 * Resource for the contents of a script.
 * 
 * @author Justin Deoliveira, OpenGeo
 */
public class ScriptResource extends Resource {

    ScriptManager scriptMgr;
    String path;

    public ScriptResource(ScriptManager scriptMgr, String path, Request request, Response response) {
        super(null, request, response);
        this.scriptMgr = scriptMgr;
        this.path = path;
    }

    @Override
    public void handleGet() {
        org.geoserver.platform.resource.Resource script;
        try {
            if (path.contains(":")) {
                path = path.replace(":","/");
            }
            script = scriptMgr.script(path);
        } catch (IllegalStateException e) {
            throw new RestletException(format("Error looking up script %s", path),
                Status.SERVER_ERROR_INTERNAL, e);
        }
        if (!Resources.exists(script)) {
            throw new RestletException(format("Could not find script %s", path), 
                Status.CLIENT_ERROR_NOT_FOUND);
        }

        //TODO: set different content type?
        //TODO: not sure about this time to live parameter...  
        getResponse().setEntity(new FileRepresentation(script.file(), MediaType.TEXT_PLAIN, 10));
    }

    @Override
    public boolean allowPut() {
        return true;
    }

    @Override
    public void handlePut() {
        org.geoserver.platform.resource.Resource script;
        try {
            if (path.contains(":")) {
                path = path.replace(":","/");
            }
            script = scriptMgr.script(path);
        }
        catch(IllegalStateException e) {
            throw new RestletException(format("Error creating script file %s", path),
                Status.SERVER_ERROR_INTERNAL, e);
        }

        // copy over the contents
        try {
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(script.out()));

            try {
                IOUtils.copy(getRequest().getEntity().getStream(), w);
                w.flush();

                //TODO: set Location header
                getResponse().setStatus(Status.SUCCESS_CREATED);
            }
            finally {
                IOUtils.closeQuietly(w);
            }
        }
        catch(IOException e) {
            throw new RestletException(format("Error writing script file %s", path),
                Status.SERVER_ERROR_INTERNAL, e);
        }
    }

    @Override
    public boolean allowDelete() {
        return true;
    }

    @Override
    public void handleDelete() {
        org.geoserver.platform.resource.Resource script;
        try {
            if (path.contains(":")) {
                path = path.replace(":","/");
            }
            script = scriptMgr.script(path);
            if (!Resources.exists(script)) {
                throw new IOException(format("Unable to find script file %s", path));
            }
        } catch (IOException e) {
            throw new RestletException(format("Error finding script file %s", path),
                    Status.SERVER_ERROR_INTERNAL, e);
        }

        boolean success = false;
        if (script != null && Resources.exists(script)) {
            success = script.delete();
            if (path.startsWith("apps")) {
                success = script.parent().delete();
            }
        }

        if (!success) {
            throw new RestletException(format("Error deleting script file %s", path),
                    Status.SERVER_ERROR_INTERNAL);
        }
    }
}
