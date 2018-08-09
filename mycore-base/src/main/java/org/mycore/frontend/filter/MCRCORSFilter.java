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

package org.mycore.frontend.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mycore.common.config.MCRConfiguration;
import org.mycore.common.config.MCRConfigurationException;

/**
 * This filter can be used to add a specific Access-Control-Allow-Origin header to a response.
 * Access-Control-Allow-Origin is processed by the browser if a ajax request was made.
 * If the origin from where the request was made is not contained in the Access-Control-Allow-Origin field, then the Request will be rejected.
 * <p>
 * Parameter:
 * corsFilterSuffix - MCR.CORSFilter.%corsFilterSuffix% will be resolved from the mycore.properties and used as Access-Control-Allow-Origin header field
 * </p>
 * @author Sebastian Hofmann
 */
public class MCRCORSFilter implements Filter {

    private static final String CORS_FILTER_NAME = "corsFilterSuffix";

    private static final Logger LOGGER;

    private static final String CONFIGURATION_PREFIX = "MCR.CORSFilter";

    private String allowOriginValue;

    static {
        LOGGER = LogManager.getLogger(MCRCORSFilter.class);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String filterName = filterConfig.getInitParameter(CORS_FILTER_NAME);
        if (filterName != null) {
            LOGGER.info("initializing {}", MCRCORSFilter.class.getSimpleName());
            LOGGER.info(String.format(Locale.ROOT, "%s is %s", CORS_FILTER_NAME, filterName));
            String propertyName = String.format(Locale.ROOT, "%s.%s", CONFIGURATION_PREFIX, filterName);

            this.allowOriginValue = MCRConfiguration.instance().getString(propertyName);
        } else {
            throw new MCRConfigurationException(String.format(Locale.ROOT, "No %s is set!", CORS_FILTER_NAME));
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        // check if the request is a http request
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            resp.setHeader("Access-Control-Allow-Origin", this.allowOriginValue);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        LOGGER.info("destroying {}", MCRCORSFilter.class.getSimpleName());
    }

}
