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

package org.mycore.media;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * Get informations about an PostScript file and implements thumbnail support. 
 * 
 * @author Ren\u00E9 Adler (Eagle)
 *
 */
@SuppressWarnings("deprecation")
public class MCRMediaPDFParser extends MCRMediaParser {
    private static final String[] supportedFileExts = { "pdf", "ps" };

    private static MCRMediaPDFParser instance = new MCRMediaPDFParser();

    private static final Logger LOGGER = LogManager.getLogger(MCRMediaPDFParser.class);

    public static MCRMediaPDFParser getInstance() {
        return instance;
    }

    private MCRMediaPDFParser() {
    }

    /**
     * Checks if PDF parser is valid.
     * 
     * @return boolean if is vaild
     */
    public boolean isValid() {
        return true;
    }

    /**
     * Checks if given file is supported.
     * 
     * @return boolean if true
     */
    public boolean isFileSupported(String fileName) {
        if (fileName.endsWith(".")) {
            return false;
        }

        int pos = fileName.lastIndexOf(".");
        if (pos != -1) {
            String ext = fileName.substring(pos + 1);

            for (String sExt : supportedFileExts) {
                if (!sExt.equals(ext.toLowerCase()))
                    continue;
                return isValid();
            }
        }

        return false;
    }

    /**
     * Checks if given file is supported.
     * 
     * @return boolean if true
     */
    public boolean isFileSupported(File file) {
        return isFileSupported(file.getPath());
    }

    /**
     * Checks if given file is supported.
     * 
     * @return boolean if true
     */
    public boolean isFileSupported(org.mycore.datamodel.ifs.MCRFile file) {
        return isFileSupported(toFile(file));
    }

    /**
     * Parse file and store metadata in related Object.
     * 
     * @return MCRMediaObject
     *              can be held any MCRMediaObject
     * @see MCRMediaObject#clone()
     */
    @SuppressWarnings("unchecked")
    public synchronized MCRMediaObject parse(File file) throws Exception {
        if (!file.exists())
            throw new IOException("File \"" + file.getName() + "\" doesn't exists!");

        MCRPDFObject media = new MCRPDFObject();

        LOGGER.info("parse {}...", file.getName());

        try (PDDocument pdf = PDDocument.load(file)) {
            media.fileName = file.getName();
            media.fileSize = file.length();
            media.folderName = (file.getAbsolutePath()).replace(file.getName(), "");

            PDPageTree pages = pdf.getDocumentCatalog().getPages();

            media.numPages = pdf.getNumberOfPages();

            PDPage page = pages.get(0);
            PDRectangle rect = page.getMediaBox();

            media.width = Math.round(rect.getWidth());
            media.height = Math.round(rect.getHeight());

            PDDocumentInformation info = pdf.getDocumentInformation();
            if (info != null) {
                media.tags = new MCRMediaTagObject();
                media.tags.author = info.getAuthor();
                media.tags.creator = info.getCreator();
                media.tags.producer = info.getProducer();
                media.tags.title = info.getTitle();
                media.tags.subject = info.getSubject();
                media.tags.keywords = info.getKeywords();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new Exception(e.getMessage());
        }

        return media;
    }

    public synchronized MCRMediaObject parse(org.mycore.datamodel.ifs.MCRFile file) throws Exception {
        return parse(toFile(file));
    }
}
