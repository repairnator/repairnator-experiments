/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.testsuite.client.resources;

import org.keycloak.testsuite.domainextension.CompanyRepresentation;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
@Path("/realms/{realmName}/example/companies")
@Consumes(MediaType.APPLICATION_JSON)
public interface TestExampleCompanyResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<CompanyRepresentation> getCompanies(@PathParam("realmName") String realmName);

    @GET
    @Path("/{companyId}")
    @Produces(MediaType.APPLICATION_JSON)
    CompanyRepresentation getCompany(@PathParam("realmName") String realmName, @PathParam("companyId") String companyId);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response createCompany(@PathParam("realmName") String realmName, CompanyRepresentation rep);

    @DELETE
    void deleteAllCompanies(@PathParam("realmName") String realmName);
}