// code by ob and jph
package ch.ethz.idsc.owl.subdiv.curve;

import ch.ethz.idsc.owl.math.GeodesicInterface;
import ch.ethz.idsc.owl.math.group.LieGroupGeodesic;
import ch.ethz.idsc.owl.math.group.RnExponential;
import ch.ethz.idsc.owl.math.group.RnGroup;
import ch.ethz.idsc.owl.math.group.Se2CoveringExponential;
import ch.ethz.idsc.owl.math.group.Se2Group;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GeodesicCausal1FilterTest extends TestCase {
  public void testSimple() {
    GeodesicInterface geodesicInterface = //
        new LieGroupGeodesic(Se2Group.INSTANCE::element, Se2CoveringExponential.INSTANCE);
    Scalar alpha = RationalScalar.HALF;
    GeodesicCausal1Filter geodesicCausal1Filter = new GeodesicCausal1Filter(geodesicInterface, alpha);
    Tensor vector0 = Tensors.vector(1, 2, 0.25);
    Tensor res0 = geodesicCausal1Filter.apply(vector0);
    assertEquals(res0, vector0);
    Tensor vector1 = Tensors.vector(4, 5, 0.5);
    Tensor res1 = geodesicCausal1Filter.apply(vector1);
    // System.out.println(res1);
    assertTrue(Chop._10.close(res1, Tensors.vector(2.593872261349412, 3.406127738650588, 0.375)));
    // Tensor extrapolate = geodesicCausal1Filter.extrapolate();
    // Tensor expected = Tensors.vector(6.164525387368366, 8.648949142895502, 0.75);
    // assertTrue(Chop._10.close(extrapolate, expected));
    // Tensor filtered = geodesicCausal1Filter.apply(expected);
    // assertTrue(Chop._10.close(filtered, expected));
  }

  public void testLinear() {
    GeodesicInterface geodesicInterface = //
        new LieGroupGeodesic(RnGroup.INSTANCE::element, RnExponential.INSTANCE);
    Scalar alpha = RationalScalar.HALF;
    GeodesicCausal1Filter geodesicCausal1Filter = new GeodesicCausal1Filter(geodesicInterface, alpha);
    {
      Tensor tensor = geodesicCausal1Filter.apply(RealScalar.of(10));
      assertEquals(tensor, RealScalar.of(10));
    }
    {
      Tensor tensor = geodesicCausal1Filter.apply(RealScalar.of(10));
      assertEquals(tensor, RealScalar.of(10));
    }
    {
      Tensor tensor = geodesicCausal1Filter.apply(RealScalar.of(20));
      System.out.println(tensor);
      assertEquals(tensor, RealScalar.of(15));
    }
    {
      Tensor tensor = geodesicCausal1Filter.apply(RealScalar.of(20));
      System.out.println(tensor);
      assertEquals(tensor, RealScalar.of(20));
    }
    {
      Tensor tensor = geodesicCausal1Filter.apply(RealScalar.of(20.));
      System.out.println(tensor);
      // assertEquals(tensor, RealScalar.of(20));
    }
    {
      Tensor tensor = geodesicCausal1Filter.apply(RealScalar.of(20.));
      System.out.println(tensor);
      // assertEquals(tensor, RealScalar.of(20));
    }
    {
      Tensor tensor = geodesicCausal1Filter.apply(RealScalar.of(20.));
      System.out.println(tensor);
      // assertEquals(tensor, RealScalar.of(20));
    }
    {
      Tensor tensor = geodesicCausal1Filter.apply(RealScalar.of(20.));
      System.out.println(tensor);
      // assertEquals(tensor, RealScalar.of(20));
    }
    {
      Tensor tensor = geodesicCausal1Filter.apply(RealScalar.of(20.));
      System.out.println(tensor);
      // assertEquals(tensor, RealScalar.of(20));
    }
  }
}
