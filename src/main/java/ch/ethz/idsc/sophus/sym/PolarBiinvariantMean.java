// code by jph
package ch.ethz.idsc.sophus.sym;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.red.ScalarSummaryStatistics;

public enum PolarBiinvariantMean implements BiinvariantMean {
  INSTANCE;
  // ---
  private static final int MAX_ITERATIONS = 5;

  @Override
  public PolarScalar mean(Tensor sequence, Tensor weights) {
    AffineQ.require(weights); // TODO remove check at some point
    PolarScalar r = (PolarScalar) sequence.dot(weights);
    ScalarSummaryStatistics scalarSummaryStatistics = sequence.stream() //
        .map(PolarScalar.class::cast) //
        .map(PolarScalar::arg) //
        .collect(ScalarSummaryStatistics.collector());
    Scalar max = scalarSummaryStatistics.getMax(); // .add(EPS);
    Scalar min = scalarSummaryStatistics.getMin(); // .subtract(EPS);
    int add_count = 0;
    while (Scalars.lessThan(r.arg(), min) //
        && Scalars.lessEquals(r.arg().add(Pi.TWO), max) //
        && ++add_count < MAX_ITERATIONS)
      r = PolarScalar.of(r.abs(), r.arg().add(Pi.TWO));
    int sub_count = 0;
    while (Scalars.lessThan(max, r.arg()) //
        && Scalars.lessEquals(min, r.arg().subtract(Pi.TWO)) //
        && ++sub_count < MAX_ITERATIONS)
      r = PolarScalar.of(r.abs(), r.arg().subtract(Pi.TWO));
    if (0 < add_count && 0 < sub_count)
      throw TensorRuntimeException.of(sequence);
    return r;
  }
}