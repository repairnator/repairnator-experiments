/*
 * Copyright (C) 2016 Max Planck Institute for Psycholinguistics
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package nl.mpi.tg.eg.experimentdesigner.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import nl.mpi.tg.eg.experimentdesigner.model.Experiment;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @since May 3, 2016 11:27:16 AM (creation date)
 * @author Peter Withers <peter.withers@mpi.nl>
 */
public class Sara01Test {

    /**
     * Test of getExperiment method, of class Sara01.
     *
     * @throws javax.xml.bind.JAXBException
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException
     */
    @Test
    public void testGetExperiment() throws JAXBException, IOException, URISyntaxException {
        System.out.println("getExperiment");
        Sara01 instance = new Sara01();
        URI testXmlUri = this.getClass().getResource("/frinex-rest-output/antwoordraden.xml").toURI();
        String expResult = new String(Files.readAllBytes(Paths.get(testXmlUri)), StandardCharsets.UTF_8);
        Experiment result = instance.getExperiment();
        JAXBContext jaxbContext = JAXBContext.newInstance(Experiment.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter stringWriter = new StringWriter();
//        jaxbMarshaller.marshal(result, System.out);
        jaxbMarshaller.marshal(result, new File(new File(testXmlUri).getParentFile(),"antwoordraden-testoutput.xml"));
        jaxbMarshaller.marshal(result, stringWriter);
        assertEquals(expResult, stringWriter.toString());
    }
}
