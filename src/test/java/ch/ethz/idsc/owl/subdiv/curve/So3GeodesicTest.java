// code by jph
package ch.ethz.idsc.owl.subdiv.curve;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.lie.Rodriguez;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3GeodesicTest extends TestCase {
  public void testSimple() {
    Tensor p = Rodriguez.exp(Tensors.vector(1, 2, 3));
    Tensor q = Rodriguez.exp(Tensors.vector(2, -1, 2));
    Tensor split = So3Geodesic.INSTANCE.split(p, q, RationalScalar.HALF);
    assertTrue(OrthogonalMatrixQ.of(split));
  }

  public void testEndPoints() {
    Distribution distribution = NormalDistribution.of(0, .3);
    for (int index = 0; index < 10; ++index) {
      Tensor p = Rodriguez.exp(RandomVariate.of(distribution, 3));
      Tensor q = Rodriguez.exp(RandomVariate.of(distribution, 3));
      assertTrue(Chop._14.close(p, So3Geodesic.INSTANCE.split(p, q, RealScalar.ZERO)));
      assertTrue(Chop._11.close(q, So3Geodesic.INSTANCE.split(p, q, RealScalar.ONE)));
    }
  }
}
