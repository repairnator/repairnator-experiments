// code by jph
package ch.ethz.idsc.owl.math.region;

import java.io.Serializable;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Ramp;
import ch.ethz.idsc.tensor.sca.Sign;

/** the spherical region is a special case of an {@link EllipsoidRegion}.
 * 
 * <p>{@link SphericalRegion} is implemented separately, because the implementation
 * 1) requires less operations than if treated as an elliptic case
 * 2) is numerically more stable in corner cases
 * 
 * <p>the function {@link #apply(Tensor)} returns the minimal Euclidean distance
 * that is separating the input coordinate from the spherical region, and negative
 * values when inside the spherical region.
 * 
 * <p>for radius == 0, the region evaluates
 * <ul>
 * <li>zero in a single point: the center, and
 * <li>negative nowhere
 * </ul> */
public class SphericalRegion extends ImplicitFunctionRegion<Tensor> implements //
    RegionWithDistance<Tensor>, Serializable {
  private final Tensor center;
  private final Scalar radius;

  /** @param center vector with length() == n
   * @param radius non-negative */
  public SphericalRegion(Tensor center, Scalar radius) {
    this.center = VectorQ.require(center).copy();
    this.radius = Sign.requirePositiveOrZero(radius);
  }

  @Override // from SignedDistanceFunction<Tensor>
  public Scalar signedDistance(Tensor x) {
    // ||x - center|| - radius
    return Norm._2.between(x, center).subtract(radius); // result may be negative
  }

  @Override // from DistanceFunction<Tensor>
  public Scalar distance(Tensor element) {
    // max(0, ||x - center|| - radius)
    return Ramp.FUNCTION.apply(signedDistance(element));
  }

  public Tensor center() {
    return center.unmodifiable();
  }

  public Scalar radius() {
    return radius;
  }
}
