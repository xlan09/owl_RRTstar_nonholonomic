// code by jph
package ch.ethz.idsc.sophus.filter;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import ch.ethz.idsc.sophus.app.api.GeodesicDisplay;
import ch.ethz.idsc.sophus.app.api.Se2GeodesicDisplay;
import ch.ethz.idsc.sophus.app.data.GokartPoseData;
import ch.ethz.idsc.sophus.group.LieExponential;
import ch.ethz.idsc.sophus.group.LieGroup;
import ch.ethz.idsc.sophus.math.BiinvariantMean;
import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.sophus.math.SmoothingKernel;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Mod;
import junit.framework.TestCase;

public class LieGroupFiltersTest extends TestCase {
  private static final Mod MOD = Mod.function(Pi.TWO, Pi.VALUE.negate());

  public void testSimple() {
    List<String> lines = GokartPoseData.INSTANCE.list();
    Tensor control = GokartPoseData.getPose(lines.get(0), 250);
    GeodesicDisplay geodesicDisplay = Se2GeodesicDisplay.INSTANCE;
    GeodesicInterface geodesicInterface = geodesicDisplay.geodesicInterface();
    SmoothingKernel smoothingKernel = SmoothingKernel.GAUSSIAN;
    LieGroup lieGroup = geodesicDisplay.lieGroup();
    LieExponential lieExponential = geodesicDisplay.lieExponential();
    BiinvariantMean biinvariantMean = geodesicDisplay.biinvariantMean();
    int radius = 7;
    Map<LieGroupFilters, Tensor> map = new EnumMap<>(LieGroupFilters.class);
    for (LieGroupFilters lieGroupFilters : LieGroupFilters.values()) {
      TensorUnaryOperator tensorUnaryOperator = //
          lieGroupFilters.supply(geodesicInterface, smoothingKernel, lieGroup, lieExponential, biinvariantMean);
      Tensor filtered = GeodesicCenterFilter.of(tensorUnaryOperator, radius).apply(control);
      map.put(lieGroupFilters, filtered);
    }
    for (LieGroupFilters lieGroupFilters : LieGroupFilters.values()) {
      Tensor diff = map.get(lieGroupFilters).subtract(map.get(LieGroupFilters.BIINVARIANT_MEAN));
      diff.set(MOD, Tensor.ALL, 2);
      Scalar norm = Norm.INFINITY.ofMatrix(diff);
      assertTrue(Chop._02.allZero(norm));
    }
  }
}
