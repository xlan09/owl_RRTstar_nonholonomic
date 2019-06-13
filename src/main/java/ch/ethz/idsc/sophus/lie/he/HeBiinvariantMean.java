// code by ob
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.math.win.AffineQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;

/** @param sequence of (x,y,z) points in He(n) of shape ((x1, ... , xm),(y1, ... , ym),z)
 * @param normalized non negative weights
 * @return associated biinvariant mean which is the solution to the barycentric equation
 * 
 * Reference 1:
 * "Bi-invariant Means in Lie Groups. Application toLeft-invariant Polyaffine Transformations."
 * Vincent Arsigny, Xavier Pennec, Nicholas Ayache, p. 32
 * 
 * Reference 2:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.29, Section 4.2 */
public enum HeBiinvariantMean implements BiinvariantMean {
  INSTANCE;
  // ---
  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor ws = AffineQ.require(weights).dot(sequence);
    Tensor xMean = ws.get(0);
    Tensor yMean = ws.get(1);
    Tensor xyMean = weights.dot(Tensor.of(sequence.stream().map(xyz -> xyz.get(0).dot(xyz.get(1)))));
    return Tensors.of( //
        xMean, //
        yMean, //
        ws.Get(2).add(xMean.dot(yMean).subtract(xyMean).multiply(RationalScalar.HALF)));
  }
}
