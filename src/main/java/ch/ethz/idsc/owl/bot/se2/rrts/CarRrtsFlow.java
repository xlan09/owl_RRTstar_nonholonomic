// code by gjoel
package ch.ethz.idsc.owl.bot.se2.rrts;

import ch.ethz.idsc.owl.bot.se2.Se2Wrap;
import ch.ethz.idsc.owl.math.state.StateTime;
import ch.ethz.idsc.sophus.math.Extract2D;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.red.Norm;

public enum CarRrtsFlow {
  ;
  /** @param orig
   * @param dest
   * @return */
  public static Tensor uBetween(StateTime orig, StateTime dest) {
    Tensor log = Se2Wrap.INSTANCE.difference(orig.state(), dest.state());
    Scalar delta = dest.time().subtract(orig.time());
    // TODO GJOEL/JPH write test
    // TODO GJOEL side speed should not result in forward motion! rather project
    // ... the sign of vx is not always correct when using norm!
    Scalar vx = Norm._2.ofVector(Extract2D.FUNCTION.apply(log));
    return Tensors.of(vx, vx.zero(), log.Get(2)).divide(delta);
  }
}
