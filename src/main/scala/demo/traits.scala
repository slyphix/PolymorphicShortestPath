package demo

import scala.annotation.targetName

trait CommutativeMonoid[I]:
  def unit: I
  def add(x: I, y: I): I

trait OrderingWithInfinity[I]:
  def infinity: I
  def ordering: Ordering[I]


trait GraphLike[G, N, E]:
  def nodes(graph: G): Iterable[N]
  def edges(graph: G): Iterable[E]

trait EdgeLike[E, N]:
  def from(edge: E): N
  def to(edge: E): N

trait AssignedWeights[A, W]:
  def weightOf(elem: A): W


object extensions:
  extension[I](x: I)(using monoid: CommutativeMonoid[I])
    @targetName("add")
    def +(y: I): I = monoid.add(x, y)

  def zero[I](using m: CommutativeMonoid[I]): I = m.unit
  def unit[I](using m: CommutativeMonoid[I]): I = m.unit
  def infinity[I](using inf: OrderingWithInfinity[I]): I = inf.infinity

  extension[G, N, E](graph: G)(using gl: GraphLike[G, N, E])
    def nodes: Iterable[N] = gl.nodes(graph)
    def edges: Iterable[E] = gl.edges(graph)

  extension[E, V](edge: E)(using el: EdgeLike[E, V])
    def from: V = el.from(edge)
    def to: V = el.to(edge)

  extension[A, W](elem: A)(using aw: AssignedWeights[A, W])
    def weight: W = aw.weightOf(elem)
