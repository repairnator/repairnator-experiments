/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.jersey.media.htmljson;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.TestProperties;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import net.java.html.json.Model;
import net.java.html.json.Property;

/**
 * Reading and writing class generated by {@link Model}.
 *
 * @author Jaroslav Tulach
 */
public class ModelEntityTest extends AbstractTypeTester {

    @Model(className = "MyBean", properties = {
            @Property(name = "value", type = String.class)
    })
    static class MB {
    }

    @Path("empty")
    public static class TestResource {
        @POST
        @Path("mybean")
        public String myBean(MyBean bean) {
            return (bean.getValue().equals("Hello")) ? "PASSED" : "ERROR";
        }

        @GET
        @Path("getbean")
        public Response getBean(@Context HttpHeaders headers) {
            MyBean teb = new MyBean();
            teb.setValue("hello");
            return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(teb).build();
        }
    }

    public ModelEntityTest() {
        enable(TestProperties.LOG_TRAFFIC);
    }

    @Test
    public void myBeanAndPut() {
        WebTarget target = target("empty/mybean");

        MyBean mb = new MyBean();
        mb.setValue("Hello");

        final Response response = target.request().post(Entity.entity(mb, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200, response.getStatus());
        assertEquals("PASSED", response.readEntity(String.class));
    }

    @Test
    public void myBeanAndGet() {
        WebTarget target = target("empty/getbean");
        final Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());
        final MyBean teb = response.readEntity(MyBean.class);

        assertEquals("value", "hello", teb.getValue());
    }
}
