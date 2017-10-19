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
 * Created on Feb 3, 2010
 * Author: Andreas Prlic
 *
 */

package demo;

import org.biojava.nbio.structure.align.ce.CeMain;

public class DemoCommandLineStartup
{

	public static void main(String[] arg) throws Exception {

		// demo how to use with command line parameters
		String commandLine = "-file1 /tmp/cd/pdb1cdg.ent.gz -file2 file:///tmp/ti/pdb1tim.ent.gz -printCE";
		String[] args = commandLine.split(" ");

		CeMain.main(args);

	}
}
