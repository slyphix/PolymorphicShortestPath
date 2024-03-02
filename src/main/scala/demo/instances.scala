package demo

class AdditiveMonoid[I](using n: Numeric[I]) extends CommutativeMonoid[I]:
  override def unit: I = n.zero
  override def add(x: I, y: I): I = n.plus(x, y)

class MultiplicativeMonoid[I](using n: Numeric[I]) extends CommutativeMonoid[I]:
  override def unit: I = n.one
  override def add(x: I, y: I): I = n.times(x, y)

given additionIsDefault[I](using Numeric[I]): CommutativeMonoid[I] = AdditiveMonoid[I]()


val floatInf: OrderingWithInfinity[Float] = new OrderingWithInfinity[Float]:
  override def infinity: Float = Float.PositiveInfinity
  override def ordering: Ordering[Float] = Ordering.Float.TotalOrdering

val doubleInf: OrderingWithInfinity[Double] = new OrderingWithInfinity[Double]:
  override def infinity: Double = Double.PositiveInfinity
  override def ordering: Ordering[Double] = Ordering.Double.TotalOrdering

val floatInfRev: OrderingWithInfinity[Float] = new OrderingWithInfinity[Float]:
  override def infinity: Float = Float.NegativeInfinity
  override def ordering: Ordering[Float] = Ordering.Float.TotalOrdering.reverse

val doubleInfRev: OrderingWithInfinity[Double] = new OrderingWithInfinity[Double]:
  override def infinity: Double = Double.NegativeInfinity
  override def ordering: Ordering[Double] = Ordering.Double.TotalOrdering.reverse

given[A](using wi: OrderingWithInfinity[A]): Ordering[A] = wi.ordering
given OrderingWithInfinity[Double] = doubleInf


case class PrimitiveGraph[N, E](nodes: Iterable[N], edges: Iterable[E])

given primitiveGraphLike[N, E]: GraphLike[PrimitiveGraph[N, E], N, E] with
  def nodes(graph: PrimitiveGraph[N, E]): Iterable[N] = graph.nodes
  def edges(graph: PrimitiveGraph[N, E]): Iterable[E] = graph.edges

case class PrimitiveEdge[N, W](from: N, to: N, weight: W)

given primitiveEdgeLike[N, W]: EdgeLike[PrimitiveEdge[N, W], N] with
  def from(edge: PrimitiveEdge[N, W]): N = edge.from
  def to(edge: PrimitiveEdge[N, W]): N = edge.to

given primitiveEdgeWeights[N, W]: AssignedWeights[PrimitiveEdge[N, W], W] with
  def weightOf(elem: PrimitiveEdge[N, W]): W = elem.weight
