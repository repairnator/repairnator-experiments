/*
 * This file is part of ***  M y C o R e  ***
 * See http://www.mycore.de/ for details.
 *
 * MyCoRe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyCoRe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyCoRe.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mycore.restapi.v1;

import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.frontend.MCRFrontendUtil;
import org.mycore.frontend.jersey.MCRStaticContent;
import org.mycore.restapi.v1.errors.MCRRestAPIError;
import org.mycore.restapi.v1.errors.MCRRestAPIException;
import org.mycore.restapi.v1.utils.MCRJSONWebTokenUtil;
import org.mycore.user2.MCRUserManager;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;

/**
 * Rest Controller that handles authentication.
 * 
 * @author Robert Stephan
 *
 */
@Path("/v1/auth")
@MCRStaticContent
public class MCRRestAPIAuthentication {
    private static final Logger LOGGER = LogManager.getLogger(MCRRestAPIAuthentication.class);

    private static final String HEADER_NAME_AUTHORIZATION = "Authorization";

    private static final String HEADER_PREFIX_BEARER = "Bearer ";

    /**
     * @return the server public key as Java Web Token
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    public Response initAuthorization() {
        SignedJWT jwt = MCRJSONWebTokenUtil.createEmptyJWTwithPublicKey("http:/localhost:8080");
        String msg = "{" + "\n    \"access_token\": \"" + jwt + "\"," + "\n}";

        return Response.ok(msg).type("application/json; charset=UTF-8")
            .header(HEADER_NAME_AUTHORIZATION, HEADER_PREFIX_BEARER + jwt.serialize()).build();
    }

    /**
     * Validation: https://jwt.io/ Public Key: http://localhost:8080/api/v1/auth/public_key.txt
     *
     * Unauthenticated requests should return a response whose header contains a HTTP 401 Unauthorized status and a
     * WWW-Authenticate field.
     * 
     * 200 OK Content-Type: application/json;charset=UTF-8
     * 
     * { "access_token": "NgCXRK...MzYjw", "token_type": "Bearer", "expires_at": 1372700873, "refresh_token":
     * "NgAagA...Um_SHo" }
     * 
     * Returning the JWT (Java Web Token to the client is not properly specified). We use the "Authorization" Header in
     * the response, which is unusual but not strictly forbidden.
     * 
     * @param authorization - content HTTP Header Authorization
     * @return response message as JSON
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    @Path("/login")
    public Response authorize(@DefaultValue("") @HeaderParam("Authorization") String authorization) {
        String username = null;
        String password = null;
        JWK clientPubKey = null;
        String userPwd = null;
        if (authorization.startsWith("Basic ")) {
            byte[] encodedAuth = authorization.substring(6).trim().getBytes(StandardCharsets.ISO_8859_1);
            userPwd = new String(Base64.getDecoder().decode(encodedAuth), StandardCharsets.ISO_8859_1);

        }
        if (authorization.startsWith(HEADER_PREFIX_BEARER)) {
            userPwd = MCRJSONWebTokenUtil.retrieveUsernamePasswordFromLoginToken(authorization.substring(7).trim());
            clientPubKey = MCRJSONWebTokenUtil.retrievePublicKeyFromLoginToken(authorization.substring(7).trim());
        }

        if (userPwd != null && userPwd.contains(":")) {
            int splitPos = userPwd.indexOf(":");
            username = userPwd.substring(0, splitPos);
            password = userPwd.substring(splitPos + 1);
        }
        //validate username and password
        if (username != null && password != null && MCRUserManager.checkPassword(username, password) != null) {
            SignedJWT jwt = MCRJSONWebTokenUtil.createJWT(username, Collections.singletonList("restapi"),
                MCRFrontendUtil.getBaseURL(), clientPubKey);
            if (jwt != null) {
                String msg = "{" + "\n    \"login_successful\":true," + "\n    \"access_token\": \"" + jwt.serialize()
                    + "\","
                    + "\n    \"token_type\": \"Bearer\"" + "\n}";

                return Response.ok(msg).type("application/json; charset=UTF-8")
                    .header(HEADER_NAME_AUTHORIZATION, HEADER_PREFIX_BEARER + jwt.serialize()).build();
            }
        }

        String msg = "{" + "\n    \"login_successful\":false," + "\n    \"error\": \"login_failed\""
            + "\n    \"error_description\": "
            + "\"Login failed. Please provider proper user name and password via HTTP Basic Authentication.\"" + "\n}";

        return Response.status(Status.FORBIDDEN).header("WWW-Authenticate", "Basic realm=\"MyCoRe REST API\"")
            .entity(msg).type("application/json; charset=UTF-8").build();
    }

    @GET
    @Path("/public_key.jwk")
    public Response sendPublicKey() {
        JWK jwk = new RSAKey.Builder((RSAPublicKey) MCRJSONWebTokenUtil.RSA_KEYS.getPublic()).build();
        return Response.ok(jwk.toJSONString()).type("application/json; charset=UTF-8").build();
    }

    @GET
    @Path("/api/v1/auth/public_key.txt")
    public Response sendPublicKeyasText() {
        String txt = "-----BEGIN PUBLIC KEY-----\n"
            + Base64.getEncoder().encodeToString(MCRJSONWebTokenUtil.RSA_KEYS.getPublic().getEncoded())
            + "\n-----END PUBLIC KEY-----";
        return Response.ok(txt).type("text/plain; charset=UTF-8").build();
    }

    @POST
    @Path("/renew")
    public Response renew(@DefaultValue("") String data, @Context HttpServletRequest request)
        throws MCRRestAPIException {
        try {
            String authHeader = MCRJSONWebTokenUtil
                .createJWTAuthorizationHeader(MCRJSONWebTokenUtil.retrieveAuthenticationToken(request));
            if (authHeader != null) {
                String msg = "{" + "\n    \"executed\":true," + "\n    \"access_token\": \"" + authHeader.replace(
                    HEADER_PREFIX_BEARER, "") + "\"," + "\n    \"token_type\": \"Bearer\"," + "\n    \"data\": \""
                    + data + "\"," + "\n}";

                return Response.ok(msg).type("application/json; charset=UTF-8")
                    .header(HEADER_NAME_AUTHORIZATION, authHeader).build();
            }
        } catch (MCRRestAPIException rae) {
            throw rae;
        } catch (Exception e) {
            LOGGER.error(e);
            throw new MCRRestAPIException(Status.INTERNAL_SERVER_ERROR,
                new MCRRestAPIError(MCRRestAPIError.CODE_INTERNAL_ERROR, "Session cannot be renewed!", e.getMessage()));
        }
        throw new MCRRestAPIException(Status.FORBIDDEN, new MCRRestAPIError(MCRRestAPIError.CODE_INVALID_AUTHENCATION,
            "Permission denied", "Please provide a valid JWT Token for the session."));
    }
}
