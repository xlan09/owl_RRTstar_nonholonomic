// code by jph
package ch.ethz.idsc.owl.math.order;

import java.util.Arrays;
import java.util.List;

import ch.ethz.idsc.owl.demo.order.EqualityOrder;
import ch.ethz.idsc.owl.demo.order.IntegerTotalOrder;
import ch.ethz.idsc.owl.demo.order.ScalarTotalOrder;
import ch.ethz.idsc.owl.demo.order.SetPartialOrder;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class LexicographicComparatorTest extends TestCase {
  public void testEmpty() {
    LexicographicComparator genericLexicographicOrder = new LexicographicComparator(Arrays.asList());
    List<Scalar> list = Arrays.asList();
    assertEquals(genericLexicographicOrder.compare(list, list), OrderComparison.INDIFFERENT);
  }

  public void testSimple() {
    List<OrderComparator<? extends Object>> comparators = Arrays.asList( //
        ScalarTotalOrder.INSTANCE, //
        ScalarTotalOrder.INSTANCE); //
    LexicographicComparator genericLexicographicOrder = new LexicographicComparator(comparators);
    List<Scalar> list = Arrays.asList(RealScalar.ONE, RealScalar.of(3));
    OrderComparison orderComparison = genericLexicographicOrder.compare(list, list);
    assertEquals(orderComparison, OrderComparison.INDIFFERENT);
  }

  public void testMixed2() {
    List<OrderComparator<? extends Object>> comparators = Arrays.asList( //
        IntegerTotalOrder.INSTANCE, //
        SetPartialOrder.INSTANCE); //
    LexicographicComparator genericLexicographicOrder = new LexicographicComparator(comparators);
    List<Object> listX = Arrays.asList(123, Arrays.asList(2, 3, 4));
    List<Object> listY = Arrays.asList(123, Arrays.asList(3, 4));
    assertEquals(genericLexicographicOrder.compare(listX, listY), OrderComparison.STRICTLY_SUCCEEDS);
    assertEquals(genericLexicographicOrder.compare(listY, listX), OrderComparison.STRICTLY_PRECEDES);
    assertEquals(genericLexicographicOrder.compare(listX, listX), OrderComparison.INDIFFERENT);
    assertEquals(genericLexicographicOrder.compare(listY, listY), OrderComparison.INDIFFERENT);
  }

  public void testTensorAsIterable() {
    BinaryRelation<Tensor> relation1 = (x, y) -> x.length() <= y.length();
    List<OrderComparator<? extends Object>> comparators = Arrays.asList( //
        new Order<>(relation1), //
        ScalarTotalOrder.INSTANCE); //
    LexicographicComparator genericLexicographicOrder = new LexicographicComparator(comparators);
    Tensor tensorX = Tensors.fromString("{{1, 2, 3}, 2}");
    Tensor tensorY = Tensors.fromString("{{2, 3, 4, 5}, -2}");
    OrderComparison orderComparison = genericLexicographicOrder.compare(tensorX, tensorY);
    assertEquals(orderComparison, OrderComparison.STRICTLY_PRECEDES);
  }

  public void testEquality() {
    List<OrderComparator<? extends Object>> comparators = Arrays.asList( //
        IntegerTotalOrder.INSTANCE, //
        ScalarTotalOrder.INSTANCE, //
        EqualityOrder.INSTANCE); //
    LexicographicComparator genericLexicographicOrder = new LexicographicComparator(comparators);
    List<Object> listX = Arrays.asList(2, RealScalar.of(3), "abc");
    List<Object> listY = Arrays.asList(2, RealScalar.of(3), "different");
    assertEquals(genericLexicographicOrder.compare(listX, listY), OrderComparison.INCOMPARABLE);
    assertEquals(genericLexicographicOrder.compare(listY, listX), OrderComparison.INCOMPARABLE);
    assertEquals(genericLexicographicOrder.compare(listX, listX), OrderComparison.INDIFFERENT);
    assertEquals(genericLexicographicOrder.compare(listY, listY), OrderComparison.INDIFFERENT);
  }
}
