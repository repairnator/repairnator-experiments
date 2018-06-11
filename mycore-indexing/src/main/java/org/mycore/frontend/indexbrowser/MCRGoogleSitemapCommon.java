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

package org.mycore.frontend.indexbrowser;

import java.io.File;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.mycore.common.config.MCRConfiguration;
import org.mycore.datamodel.common.MCRObjectIDDate;
import org.mycore.datamodel.ifs2.MCRObjectIDDateImpl;
import org.mycore.solr.MCRSolrClientFactory;

/**
 * This class implements all common methods to create the sitemap data.
 * <br>
 * used properties:
 * <br>
 * <ul>
 * <li>MCR.baseurl - the application base URL</li>
 * <li>MCR.WebApplication.basedir - the directory where the web application is stored</li>
 * <li>MCR.GoogleSitemap.Directory - the directory where the sitemap should be stored relative to
 *      MCR.WebApplication.basedir (it could be empty)</li>
 * <li>MCR.GoogleSitemap.Types - a list of MCRObject types, they should be included</li>
 * <li>MCR.GoogleSitemap.Freq - the frequency of harvesting, 'monthly' is default<li>
 * <li>MCR.GoogleSitemap.Style - a style extension for the URL in form of ?XSL.Style={style}, default is empty</li>
 * <li>MCR.GoogleSitemap.ObjectPath - the path to get the MCRObject in the sitemap URL, 'receive/' is default</li>
 * <li>MCR.GoogleSitemap.NumberOfURLs - the number of URLs in one sitemap file, 10000 is default</li>
 * </ul>
 *
 * see http://www.sitemaps.org/de/protocol.html
 *
 * @author Frank Lützenkirchen
 * @author Jens Kupferschmidt
 * @author Thomas Scheffler (yagee)
 * @version $Revision$ $Date$
 *
 */
public final class MCRGoogleSitemapCommon {

    /** Zone information **/
    private static final Locale SITEMAP_LOCALE = Locale.ROOT;

    private static final TimeZone SITEMAP_TIMEZONE = TimeZone.getTimeZone("UTC");

    /** The namespaces */
    private static final Namespace NS = Namespace.getNamespace("http://www.sitemaps.org/schemas/sitemap/0.9");

    private static final String XSI_URL = "http://www.w3.org/2001/XMLSchema-instance";

    private static final Namespace XSI_NAMESPACE = Namespace.getNamespace("xsi", XSI_URL);

    private static final String SITEINDEX_SCHEMA = "http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/siteindex.xsd";

    private static final String SITEMAP_SCHEMA = "http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd";

    /** The directory path to store sitemaps relative to MCR.WebApplication.basedir */
    private static final String CDIR = MCRConfiguration.instance().getString("MCR.GoogleSitemap.Directory", "");

    /** The frequence of crawle by Google */
    private static final String FREQ = MCRConfiguration.instance().getString("MCR.GoogleSitemap.Freq", "monthly");

    /** The style for by Google link */
    private static final String STYLE = MCRConfiguration.instance().getString("MCR.GoogleSitemap.Style", "");

    /** The url path for retrieving object metadata */
    private static final String OBJECT_PATH = MCRConfiguration.instance().getString("MCR.GoogleSitemap.ObjectPath",
        "receive/");

    /** The filter query for selecting objects to present in google sitemap */
    private static final String SOLR_QUERY = MCRConfiguration.instance().getString("MCR.GoogleSitemap.SolrQuery");

    /** The logger */
    private static Logger LOGGER = LogManager.getLogger(MCRGoogleSitemapCommon.class.getName());

    /** Number of URLs in one sitemap */
    private int numberOfURLs = MCRConfiguration.instance().getInt("MCR.GoogleSitemap.NumberOfURLs", 10000);

    /** number format for parts */
    private static NumberFormat number_format = getNumberFormat();

    /** date formatter */
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", SITEMAP_LOCALE);

    /** The webapps directory path from configuration */
    private final File webappBaseDir;

    /** The base URL */
    private String baseurl = MCRConfiguration.instance().getString("MCR.baseurl", "");

    /** local data */
    private List<MCRObjectIDDate> objidlist = null;

    /** The constructor 
     * @throws NotDirectoryException */
    public MCRGoogleSitemapCommon(File baseDir) throws NotDirectoryException {
        if (!Objects.requireNonNull(baseDir, "baseDir may not be null.").isDirectory()) {
            throw new NotDirectoryException(baseDir.getAbsolutePath());
        }
        this.webappBaseDir = baseDir;
        LOGGER.info("Using webappbaseDir: {}", baseDir.getAbsolutePath());
        objidlist = new ArrayList<>();
        if ((numberOfURLs < 1) || (numberOfURLs > 50000)) {
            numberOfURLs = 50000;
        }
        if (CDIR.length() != 0) {
            File sitemapDirectory = new File(webappBaseDir, CDIR);
            if (!sitemapDirectory.exists()) {
                sitemapDirectory.mkdirs();
            }
        }
    }

    public MCRGoogleSitemapCommon(String baseURL, File baseDir) throws NotDirectoryException {
        this(baseDir);
        this.baseurl = baseURL;
    }

    private static NumberFormat getNumberFormat() {
        NumberFormat nf = NumberFormat.getIntegerInstance(SITEMAP_LOCALE);
        nf.setMinimumFractionDigits(5);
        return nf;
    }

    /**
     * The method computes the number of sitemap files. If we have less than
     * <em>numberOfURLs</em> URLs and only one MyCoRe type the sitemap_google.xml
     * contained all URLs. Otherwise it split the sitemap in an sitemap_google.xml
     * index file and a lot of sitemap_google_xxxx.xml URL files.
     *
     * @return the number of files, one for a single sitemap_google.xml file, more than
     *         one for the index and all parts.
     *
     */
    protected int checkSitemapFile() throws IOException {
        int number = 0;
        QueryResponse response;
        SolrQuery query = new SolrQuery();
        query.setQuery(SOLR_QUERY);
        query.setRows(Integer.MAX_VALUE);
        query.setParam("fl", "id,modified");

        try {
            response = MCRSolrClientFactory.getSolrDefaultClient().query(query);
            objidlist = response.getResults().stream().map((document) -> {
                String id = (String) document.getFieldValue("id");
                Date modified = (Date) document.getFieldValue("modified");

                return new MCRObjectIDDateImpl(modified, id);
            }).collect(Collectors.toList());

        } catch (SolrServerException e) {
            LOGGER.error(e);
        }
        number = objidlist.size() / numberOfURLs;
        if (objidlist.size() % numberOfURLs != 0) {
            number++;
        }
        return number;
    }

    /**
     * The method return the path to the sitemap_google.xml file.
     *
     * @param number
     *            number of this file - '1' = sitemap_google.xml - '&gt; 1' sitemap_google_xxx.xml
     * @param withpath
     *            true for the full path, false for the file name
     * @return a path to sitemap_google.xml
     */
    protected String getFileName(int number, boolean withpath) {
        String fn = "sitemap_google.xml";
        if (number > 1) {
            fn = "sitemap_google_" + number_format.format(number - 1) + ".xml";
        }
        String localPath = fn;
        if (CDIR.length() != 0) {
            localPath = CDIR + File.separator + fn;
        }
        if (withpath) {
            return webappBaseDir + File.separator + localPath;
        }
        return localPath;
    }

    /**
     * The method build the sitemap_google.xml JDOM document over all items.
     *
     * @return The sitemap_google.xml as JDOM document
     */
    protected Document buildSingleSitemap() throws Exception {
        LOGGER.debug("Build Google URL sitemap_google.xml for whole items.");
        // build document frame
        Element urlset = new Element("urlset", NS);
        urlset.addNamespaceDeclaration(XSI_NAMESPACE);
        urlset.setAttribute("noNamespaceSchemaLocation", SITEMAP_SCHEMA, XSI_NAMESPACE);
        Document jdom = new Document(urlset);
        // build over all types
        for (MCRObjectIDDate objectIDDate : objidlist) {
            urlset.addContent(buildURLElement(objectIDDate));
        }
        return jdom;
    }

    /**
     * The method call the database and build the sitemap_google.xml JDOM document.
     *
     * @param number
     *            number of this file - '1' = sitemap_google.xml - '&gt; 1' sitemap_google_xxx.xml
     * @return The sitemap.xml as JDOM document
     */
    protected Document buildPartSitemap(int number) throws Exception {
        LOGGER.debug("Build Google URL sitemap list number {}", Integer.toString(number));
        // build document frame
        Element urlset = new Element("urlset", NS);
        urlset.addNamespaceDeclaration(XSI_NAMESPACE);
        urlset.setAttribute("schemaLocation", SITEMAP_SCHEMA, XSI_NAMESPACE);
        Document jdom = new Document(urlset);

        // build over all types
        int start = numberOfURLs * (number);
        int stop = numberOfURLs * (number + 1);
        if (stop > objidlist.size()) {
            stop = objidlist.size();
        }
        LOGGER.debug("Build Google URL in range from {} to {}.", Integer.toString(start), Integer.toString(stop - 1));
        for (int i = start; i < stop; i++) {
            MCRObjectIDDate objectIDDate = objidlist.get(i);
            urlset.addContent(buildURLElement(objectIDDate));

        }
        return jdom;
    }

    private Element buildURLElement(MCRObjectIDDate objectIDDate) {
        String mcrID = objectIDDate.getId();
        StringBuilder sb = new StringBuilder(1024);
        sb.append(baseurl).append(OBJECT_PATH).append(mcrID);
        if ((STYLE != null) && (STYLE.trim().length() > 0)) {
            sb.append("?XSL.Style=").append(STYLE);
        }
        // build entry
        Element url = new Element("url", NS);
        url.addContent(new Element("loc", NS).addContent(sb.toString()));
        String datestr = formatter.format(objectIDDate.getLastModified());
        url.addContent(new Element("lastmod", NS).addContent(datestr));
        url.addContent(new Element("changefreq", NS).addContent(FREQ));
        return url;
    }

    /**
     * The method build the index sitemap_google.xml JDOM document.
     *
     * @param number
     *            number of indexed files (must greater than 1
     * @return The index sitemap_google.xml as JDOM document
     */
    protected Document buildSitemapIndex(int number) {
        LOGGER.debug("Build Google sitemap number {}", Integer.toString(number));
        // build document frame
        Element index = new Element("sitemapindex", NS);
        index.addNamespaceDeclaration(XSI_NAMESPACE);
        index.setAttribute("schemaLocation", SITEINDEX_SCHEMA, XSI_NAMESPACE);
        Document jdom = new Document(index);
        // build over all files
        for (int i = 0; i < number; i++) {
            Element sitemap = new Element("sitemap", NS);
            index.addContent(sitemap);
            sitemap.addContent(new Element("loc", NS).addContent((baseurl + getFileName(i + 2, false)).trim()));
            String datestr = formatter.format((new GregorianCalendar(SITEMAP_TIMEZONE, SITEMAP_LOCALE)).getTime());
            sitemap.addContent(new Element("lastmod", NS).addContent(datestr.trim()));
        }
        return jdom;
    }

    /**
     * This method remove all sitemap files from the webapps directory.
     */
    protected void removeSitemapFiles() {
        File dir = new File(webappBaseDir, CDIR);
        File[] li = dir.listFiles();
        if (li != null) {
            for (File fi : li) {
                if (fi.getName().startsWith("sitemap_google")) {
                    LOGGER.debug("Remove file {}", fi.getName());
                    fi.delete();
                }
            }
        }
    }
}
