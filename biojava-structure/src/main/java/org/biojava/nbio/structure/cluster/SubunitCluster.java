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
package org.biojava.nbio.structure.cluster;

import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.alignment.Alignments.PairwiseSequenceAlignerType;
import org.biojava.nbio.alignment.SimpleGapPenalty;
import org.biojava.nbio.alignment.template.GapPenalty;
import org.biojava.nbio.alignment.template.PairwiseSequenceAligner;
import org.biojava.nbio.core.alignment.matrices.SubstitutionMatrixHelper;
import org.biojava.nbio.core.alignment.template.SubstitutionMatrix;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.align.StructureAlignment;
import org.biojava.nbio.structure.align.StructureAlignmentFactory;
import org.biojava.nbio.structure.align.ce.CeMain;
import org.biojava.nbio.structure.align.model.AFPChain;
import org.biojava.nbio.structure.align.multiple.Block;
import org.biojava.nbio.structure.align.multiple.BlockImpl;
import org.biojava.nbio.structure.align.multiple.BlockSet;
import org.biojava.nbio.structure.align.multiple.BlockSetImpl;
import org.biojava.nbio.structure.align.multiple.MultipleAlignment;
import org.biojava.nbio.structure.align.multiple.MultipleAlignmentEnsembleImpl;
import org.biojava.nbio.structure.align.multiple.MultipleAlignmentImpl;
import org.biojava.nbio.structure.align.multiple.util.MultipleAlignmentScorer;
import org.biojava.nbio.structure.align.multiple.util.ReferenceSuperimposer;
import org.biojava.nbio.structure.symmetry.core.QuatSymmetrySubunits;
import org.biojava.nbio.structure.symmetry.internal.CESymmParameters;
import org.biojava.nbio.structure.symmetry.internal.CeSymm;
import org.biojava.nbio.structure.symmetry.internal.CeSymmResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A SubunitCluster contains a set of equivalent {@link QuatSymmetrySubunits},
 * the set of equivalent residues (EQR) between {@link Subunit} and a
 * {@link Subunit} representative. It also stores the method used for
 * clustering.
 * <p>
 * This class allows the comparison and merging of SubunitClusters.
 * 
 * @author Aleix Lafita
 * @since 5.0.0
 * 
 */
public class SubunitCluster {

	private static final Logger logger = LoggerFactory
			.getLogger(SubunitCluster.class);

	private List<Subunit> subunits = new ArrayList<Subunit>();
	private List<List<Integer>> subunitEQR = new ArrayList<List<Integer>>();
	private int representative = -1;
	private SubunitClustererMethod method = SubunitClustererMethod.IDENTITY;

	/**
	 * A SubunitCluster is always initialized with a single Subunit. To obtain a
	 * SubunitCluster with multiple Subunits, initialize different
	 * SubunitClusters and merge them.
	 * 
	 * @param subunit
	 *            initial Subunit
	 */
	public SubunitCluster(Subunit subunit) {

		subunits.add(subunit);

		List<Integer> identity = new ArrayList<Integer>();
		for (int i = 0; i < subunit.size(); i++)
			identity.add(i);
		subunitEQR.add(identity);

		representative = 0;
	}

	/**
	 * Subunits contained in the SubunitCluster.
	 * 
	 * @return an unmodifiable view of the original List
	 */
	public List<Subunit> getSubunits() {
		return Collections.unmodifiableList(subunits);
	}

	/**
	 * Tells whether the other SubunitCluster contains exactly the same Subunit.
	 * This is checked by String equality of their residue one-letter sequences.
	 * 
	 * @param other
	 *            SubunitCluster
	 * @return true if the SubunitClusters are identical, false otherwise
	 */
	public boolean isIdenticalTo(SubunitCluster other) {
		String thisSequence = this.subunits.get(this.representative)
				.getProteinSequenceString();
		String otherSequence = other.subunits.get(other.representative)
				.getProteinSequenceString();
		return thisSequence.equals(otherSequence);
	}

	/**
	 * Merges the other SubunitCluster into this one if it contains exactly the
	 * same Subunit. This is checked by {@link #isIdenticalTo(SubunitCluster)}.
	 * 
	 * @param other
	 *            SubunitCluster
	 * @return true if the SubunitClusters were merged, false otherwise
	 */
	public boolean mergeIdentical(SubunitCluster other) {

		if (!isIdenticalTo(other))
			return false;

		logger.info("SubunitClusters are identical");

		this.subunits.addAll(other.subunits);
		this.subunitEQR.addAll(other.subunitEQR);

		return true;
	}

	/**
	 * Merges the other SubunitCluster into this one if their representatives
	 * sequences are similar (higher sequence identity and coverage than the
	 * thresholds).
	 * <p>
	 * The sequence alignment is performed using Smith Waterman, default linear
	 * {@link SimpleGapPenalty} and BLOSUM62 as scoring matrix.
	 * 
	 * @param other
	 *            SubunitCluster
	 * @param minSeqid
	 *            sequence identity threshold. Value in [0,1]. Values lower than
	 *            0.7 are not recommended. Use {@link #mergeStructure} for lower
	 *            values.
	 * @param minCoverage
	 *            coverage (alignment fraction) threshold. Value in [0,1].
	 * @return true if the SubunitClusters were merged, false otherwise
	 * @throws CompoundNotFoundException
	 */
	public boolean mergeSequence(SubunitCluster other, double minSeqid,
			double minCoverage) throws CompoundNotFoundException {
		return mergeSequence(other, minSeqid, minCoverage,
				PairwiseSequenceAlignerType.LOCAL, new SimpleGapPenalty(),
				SubstitutionMatrixHelper.getBlosum62());
	}

	/**
	 * Merges the other SubunitCluster into this one if their representatives
	 * sequences are similar (higher sequence identity and coverage than the
	 * thresholds).
	 * <p>
	 * The sequence alignment is performed using Smith Waterman, default linear
	 * {@link SimpleGapPenalty} and BLOSUM62 as scoring matrix.
	 * 
	 * @param other
	 *            SubunitCluster
	 * @param minSeqid
	 *            sequence identity threshold. Value in [0,1]. Values lower than
	 *            0.7 are not recommended. Use {@link #mergeStructure} for lower
	 *            values.
	 * @param minCoverage
	 *            coverage (alignment fraction) threshold. Value in [0,1].
	 * @param alignerType
	 *            parameter for the sequence alignment algorithm
	 * @param gapPenalty
	 *            parameter for the sequence alignment algorithm
	 * @param subsMatrix
	 *            parameter for the sequence alignment algorithm
	 * @return true if the SubunitClusters were merged, false otherwise
	 * @throws CompoundNotFoundException
	 */
	public boolean mergeSequence(SubunitCluster other, double minSeqid,
			double minCoverage, PairwiseSequenceAlignerType alignerType,
			GapPenalty gapPenalty,
			SubstitutionMatrix<AminoAcidCompound> subsMatrix)
			throws CompoundNotFoundException {

		// Extract the protein sequences as BioJava alignment objects
		ProteinSequence thisSequence = this.subunits.get(this.representative)
				.getProteinSequence();
		ProteinSequence otherSequence = other.subunits
				.get(other.representative).getProteinSequence();

		// Perform the alignment with provided parameters
		PairwiseSequenceAligner<ProteinSequence, AminoAcidCompound> aligner = Alignments
				.getPairwiseAligner(thisSequence, otherSequence, alignerType,
						gapPenalty, subsMatrix);

		// Calculate real coverage (subtract gaps in both sequences)
		double gaps1 = aligner.getPair().getAlignedSequence(1)
				.getNumGapPositions();
		double gaps2 = aligner.getPair().getAlignedSequence(2)
				.getNumGapPositions();
		double lengthAlignment = aligner.getPair().getLength();
		double lengthThis = aligner.getQuery().getLength();
		double lengthOther = aligner.getTarget().getLength();
		double coverage = (lengthAlignment - gaps1 - gaps2)
				/ Math.max(lengthThis, lengthOther);

		if (coverage < minCoverage)
			return false;

		double seqid = aligner.getPair().getPercentageOfIdentity();

		if (seqid < minSeqid)
			return false;

		logger.info(String.format("SubunitClusters are similar in sequence "
				+ "with %.2f sequence identity and %.2f coverage", seqid,
				coverage));

		// If coverage and sequence identity sufficient, merge other and this
		List<Integer> thisAligned = new ArrayList<Integer>();
		List<Integer> otherAligned = new ArrayList<Integer>();

		// Extract the aligned residues of both Subunit
		for (int p = 1; p < aligner.getPair().getLength() + 1; p++) {

			// Skip gaps in any of the two sequences
			if (aligner.getPair().getAlignedSequence(1).isGap(p))
				continue;
			if (aligner.getPair().getAlignedSequence(2).isGap(p))
				continue;

			int thisIndex = aligner.getPair().getIndexInQueryAt(p) - 1;
			int otherIndex = aligner.getPair().getIndexInTargetAt(p) - 1;

			// Only consider residues that are part of the SubunitCluster
			if (this.subunitEQR.get(this.representative).contains(thisIndex)
					&& other.subunitEQR.get(other.representative).contains(
							otherIndex)) {
				thisAligned.add(thisIndex);
				otherAligned.add(otherIndex);
			}
		}

		// Do a List intersection to find out which EQR columns to remove
		List<Integer> thisRemove = new ArrayList<Integer>();
		List<Integer> otherRemove = new ArrayList<Integer>();

		for (int t = 0; t < this.subunitEQR.get(this.representative).size(); t++) {
			// If the index is aligned do nothing, otherwise mark as removing
			if (!thisAligned.contains(this.subunitEQR.get(this.representative)
					.get(t)))
				thisRemove.add(t);
		}

		for (int t = 0; t < other.subunitEQR.get(other.representative).size(); t++) {
			// If the index is aligned do nothing, otherwise mark as removing
			if (!otherAligned.contains(other.subunitEQR.get(
					other.representative).get(t)))
				otherRemove.add(t);
		}

		// Now remove unaligned columns, from end to start
		Collections.sort(thisRemove);
		Collections.reverse(thisRemove);
		Collections.sort(otherRemove);
		Collections.reverse(otherRemove);

		for (int t = 0; t < thisRemove.size(); t++) {
			for (List<Integer> eqr : this.subunitEQR) {
				int column = thisRemove.get(t);
				eqr.remove(column);
			}
		}

		for (int t = 0; t < otherRemove.size(); t++) {
			for (List<Integer> eqr : other.subunitEQR) {
				int column = otherRemove.get(t);
				eqr.remove(column);
			}
		}

		// The representative is the longest sequence
		if (this.subunits.get(this.representative).size() < other.subunits.get(
				other.representative).size())
			this.representative = other.representative + subunits.size();

		this.subunits.addAll(other.subunits);
		this.subunitEQR.addAll(other.subunitEQR);

		this.method = SubunitClustererMethod.SEQUENCE;

		return true;
	}

	/**
	 * Merges the other SubunitCluster into this one if their representative
	 * Atoms are structurally similar (lower RMSD and higher coverage than the
	 * thresholds).
	 * <p>
	 * The structure alignment is performed using FatCatRigid, with default
	 * parameters.
	 * 
	 * @param other
	 *            SubunitCluster
	 * @param maxRmsd
	 *            RMSD threshold.
	 * @param minCoverage
	 *            coverage (alignment fraction) threshold. Value in [0,1].
	 * @return true if the SubunitClusters were merged, false otherwise
	 * @throws StructureException
	 */
	public boolean mergeStructure(SubunitCluster other, double maxRmsd,
			double minCoverage) throws StructureException {
		// Use a CE alignment with default parameters
		StructureAlignment algorithm = StructureAlignmentFactory
				.getAlgorithm(CeMain.algorithmName);
		return mergeStructure(other, maxRmsd, minCoverage, algorithm);
	}

	/**
	 * Merges the other SubunitCluster into this one if their representative
	 * Atoms are structurally similar (lower RMSD and higher coverage than the
	 * thresholds).
	 * <p>
	 * The structure alignment is performed using FatCatRigid, with default
	 * parameters.
	 * 
	 * @param other
	 *            SubunitCluster
	 * @param maxRmsd
	 *            RMSD threshold.
	 * @param minCoverage
	 *            coverage (alignment fraction) threshold. Value in [0,1].
	 * @param aligner
	 *            StructureAlignment algorithm
	 * @return true if the SubunitClusters were merged, false otherwise
	 * @throws StructureException
	 */
	public boolean mergeStructure(SubunitCluster other, double maxRmsd,
			double minCoverage, StructureAlignment aligner)
			throws StructureException {

		AFPChain afp = aligner.align(this.subunits.get(this.representative)
				.getRepresentativeAtoms(),
				other.subunits.get(other.representative)
						.getRepresentativeAtoms());

		// Convert AFPChain to MultipleAlignment for convinience
		MultipleAlignment msa = new MultipleAlignmentEnsembleImpl(
				afp,
				this.subunits.get(this.representative).getRepresentativeAtoms(),
				other.subunits.get(other.representative)
						.getRepresentativeAtoms(), false)
				.getMultipleAlignment(0);

		double coverage = Math.min(msa.getCoverages().get(0), msa
				.getCoverages().get(1));
		if (coverage < minCoverage)
			return false;

		double rmsd = afp.getTotalRmsdOpt();
		if (rmsd > maxRmsd)
			return false;

		String.format("SubunitClusters are structurally similar with "
				+ "%.2f RMSD %.2f coverage", rmsd, coverage);

		// If RMSD is low and coverage sufficient merge clusters
		List<List<Integer>> alignedRes = msa.getBlock(0).getAlignRes();
		List<Integer> thisAligned = new ArrayList<Integer>();
		List<Integer> otherAligned = new ArrayList<Integer>();

		// Extract the aligned residues of both Subunit
		for (int p = 0; p < msa.length(); p++) {

			// Skip gaps in any of the two sequences
			if (alignedRes.get(0).get(p) == null)
				continue;
			if (alignedRes.get(1).get(p) == null)
				continue;

			int thisIndex = alignedRes.get(0).get(p);
			int otherIndex = alignedRes.get(1).get(p);

			// Only consider residues that are part of the SubunitCluster
			if (this.subunitEQR.get(this.representative).contains(thisIndex)
					&& other.subunitEQR.get(other.representative).contains(
							otherIndex)) {
				thisAligned.add(thisIndex);
				otherAligned.add(otherIndex);
			}
		}

		// Do a List intersection to find out which EQR columns to remove
		List<Integer> thisRemove = new ArrayList<Integer>();
		List<Integer> otherRemove = new ArrayList<Integer>();

		for (int t = 0; t < this.subunitEQR.get(this.representative).size(); t++) {
			// If the index is aligned do nothing, otherwise mark as removing
			if (!thisAligned.contains(this.subunitEQR.get(this.representative)
					.get(t)))
				thisRemove.add(t);
		}

		for (int t = 0; t < other.subunitEQR.get(other.representative).size(); t++) {
			// If the index is aligned do nothing, otherwise mark as removing
			if (!otherAligned.contains(other.subunitEQR.get(
					other.representative).get(t)))
				otherRemove.add(t);
		}

		// Now remove unaligned columns, from end to start
		Collections.sort(thisRemove);
		Collections.reverse(thisRemove);
		Collections.sort(otherRemove);
		Collections.reverse(otherRemove);

		for (int t = 0; t < thisRemove.size(); t++) {
			for (List<Integer> eqr : this.subunitEQR) {
				int column = thisRemove.get(t);
				eqr.remove(column);
			}
		}

		for (int t = 0; t < otherRemove.size(); t++) {
			for (List<Integer> eqr : other.subunitEQR) {
				int column = otherRemove.get(t);
				eqr.remove(column);
			}
		}

		// The representative is the longest sequence
		if (this.subunits.get(this.representative).size() < other.subunits.get(
				other.representative).size())
			this.representative = other.representative + subunits.size();

		this.subunits.addAll(other.subunits);
		this.subunitEQR.addAll(other.subunitEQR);

		this.method = SubunitClustererMethod.STRUCTURE;

		return true;
	}

	/**
	 * Analyze the internal symmetry of the SubunitCluster and divide its
	 * {@link Subunit} into the internal repeats (domains) if they are
	 * internally symmetric.
	 * 
	 * @param coverageThreshold
	 *            the minimum coverage of all repeats in the Subunit
	 * @param rmsdThreshold
	 *            the maximum allowed RMSD between the repeats
	 * @param minSequenceLength
	 *            the minimum length of the repeating units
	 * @return true if the cluster was internally symmetric, false otherwise
	 * @throws StructureException
	 */
	public boolean divideInternally(double coverageThreshold,
			double rmsdThreshold, int minSequenceLength)
			throws StructureException {

		CESymmParameters params = new CESymmParameters();
		params.setMinCoreLength(minSequenceLength);
		params.setGaps(false); // We want no gaps between the repeats

		// Analyze the internal symmetry of the representative subunit
		CeSymmResult result = CeSymm.analyze(subunits.get(representative)
				.getRepresentativeAtoms(), params);

		if (!result.isSignificant())
			return false;

		double rmsd = result.getMultipleAlignment().getScore(
				MultipleAlignmentScorer.RMSD);
		if (rmsd > rmsdThreshold)
			return false;

		double coverage = result.getMultipleAlignment().getCoverages().get(0)
				* result.getNumRepeats();
		if (coverage < coverageThreshold)
			return false;

		logger.info("SubunitCluster is internally symmetric with {} repeats, "
				+ "{} RMSD and {} coverage", result.getNumRepeats(), rmsd,
				coverage);

		// Divide if symmety was significant with RMSD and coverage sufficient
		List<List<Integer>> alignedRes = result.getMultipleAlignment()
				.getBlock(0).getAlignRes();

		List<List<Integer>> columns = new ArrayList<List<Integer>>();
		for (int s = 0; s < alignedRes.size(); s++)
			columns.add(new ArrayList<Integer>(alignedRes.get(s).size()));

		// Extract the aligned columns of each repeat in the Subunit
		for (int col = 0; col < alignedRes.get(0).size(); col++) {

			// Check that all aligned residues are part of the Cluster
			boolean missing = false;
			for (int s = 0; s < alignedRes.size(); s++) {
				if (!subunitEQR.get(representative).contains(
						alignedRes.get(s).get(col))) {
					missing = true;
					break;
				}
			}

			// Skip the column if any residue was not part of the cluster
			if (missing)
				continue;

			for (int s = 0; s < alignedRes.size(); s++) {
				columns.get(s).add(
						subunitEQR.get(representative).indexOf(
								alignedRes.get(s).get(col)));
			}
		}

		// Divide the Subunits in their repeats
		List<Subunit> newSubunits = new ArrayList<Subunit>(subunits.size()
				* columns.size());
		List<List<Integer>> newSubunitEQR = new ArrayList<List<Integer>>(
				subunits.size() * columns.size());

		for (int s = 0; s < subunits.size(); s++) {
			for (int r = 0; r < columns.size(); r++) {

				// Calculate start and end residues of the new Subunit
				int start = subunitEQR.get(s).get(columns.get(r).get(0));
				int end = subunitEQR.get(s).get(
						columns.get(r).get(columns.get(r).size() - 1));

				Atom[] reprAtoms = Arrays.copyOfRange(subunits.get(s)
						.getRepresentativeAtoms(), start, end + 1);

				newSubunits.add(new Subunit(reprAtoms, subunits.get(s)
						.getName(), subunits.get(s).getIdentifier(), subunits
						.get(s).getStructure()));

				// Recalculate equivalent residues
				List<Integer> eqr = new ArrayList<Integer>();
				for (int p = 0; p < columns.get(r).size(); p++) {
					eqr.add(subunitEQR.get(s).get(columns.get(r).get(p))
							- start);
				}
				newSubunitEQR.add(eqr);
			}
		}

		subunits = newSubunits;
		subunitEQR = newSubunitEQR;

		// Update representative
		for (int s = 0; s < subunits.size(); s++) {
			if (subunits.get(s).size() > subunits.get(representative).size())
				representative = s;
		}

		method = SubunitClustererMethod.STRUCTURE;

		return true;
	}

	/**
	 * @return the number of Subunits in the cluster
	 */
	public int size() {
		return subunits.size();
	}

	/**
	 * @return the number of aligned residues between Subunits of the cluster
	 */
	public int length() {
		return subunitEQR.get(representative).size();
	}

	/**
	 * @return the {@link SubunitClustererMethod} used for clustering the
	 *         Subunits
	 */
	public SubunitClustererMethod getClustererMethod() {
		return method;
	}

	/**
	 * @return A List of size {@link #size()} of Atom arrays of length
	 *         {@link #length()} with the aligned Atoms for each Subunit in the
	 *         cluster
	 */
	public List<Atom[]> getAlignedAtomsSubunits() {

		List<Atom[]> alignedAtoms = Collections.emptyList();

		// Loop through all subunits and add the aligned positions
		for (int s = 0; s < subunits.size(); s++)
			alignedAtoms.add(getAlignedAtomsSubunit(s));

		return alignedAtoms;
	}

	/**
	 * @param index
	 *            Subunit index in the Cluster
	 * @return An Atom array of length {@link #length()} with the aligned Atoms
	 *         from the selected Subunit in the Cluster
	 */
	public Atom[] getAlignedAtomsSubunit(int index) {

		Atom[] aligned = new Atom[subunitEQR.get(index).size()];

		// Add only the aligned positions of the Subunit in the Cluster
		for (int p = 0; p < subunitEQR.get(index).size(); p++) {
			aligned[p] = subunits.get(index).getRepresentativeAtoms()[subunitEQR
					.get(index).get(p)];
		}

		return aligned;
	}

	/**
	 * The multiple alignment is calculated from the equivalent residues in the
	 * SubunitCluster. The alignment is recalculated every time the method is
	 * called (no caching).
	 * 
	 * @return MultipleAlignment representation of the aligned residues in this
	 *         Subunit Cluster
	 * @throws StructureException
	 */
	public MultipleAlignment getMultipleAlignment() throws StructureException {

		// Create a multiple alignment with the atom arrays of the Subunits
		MultipleAlignment msa = new MultipleAlignmentImpl();
		msa.setEnsemble(new MultipleAlignmentEnsembleImpl());
		msa.getEnsemble().setAtomArrays(
				subunits.stream().map(s -> s.getRepresentativeAtoms())
						.collect(Collectors.toList()));

		// Fill in the alignment information
		BlockSet bs = new BlockSetImpl(msa);
		Block b = new BlockImpl(bs);
		b.setAlignRes(subunitEQR);

		// Fill in the transformation matrices
		new ReferenceSuperimposer(representative).superimpose(msa);

		// Calculate some scores
		MultipleAlignmentScorer.calculateScores(msa);

		return msa;

	}

	@Override
	public String toString() {
		return "SubunitCluster [Size=" + size() + ", Length=" + length()
				+ ", Representative=" + representative + ", Method=" + method
				+ "]";
	}

}
