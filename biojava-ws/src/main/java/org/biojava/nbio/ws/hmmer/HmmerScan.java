/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */
package org.biojava.nbio.ws.hmmer;

import org.biojava.nbio.core.sequence.ProteinSequence;

import java.io.IOException;
import java.util.SortedSet;

/** Interface for performing Hmmscans on sequences.
 *
 * @author Andreas Prlic
 * @since 3.0.3
 */
public interface HmmerScan {

	public  SortedSet<HmmerResult> scan(ProteinSequence sequence) throws IOException;

}
