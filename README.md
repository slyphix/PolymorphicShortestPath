
## Proof-Of-Concept: _Polymorphic Algorithms_ 

Whenever I attended lectures about algorithms, I always found it fascinating to observe how people in theoretical CS approached problems differently from people in practical CS.
One example that stood out in particular was an application of the _[Bellman-Ford algorithm](https://en.wikipedia.org/wiki/Bellman%E2%80%93Ford_algorithm)_ (BFA).

Traditionally, the goal of the BFA is to find a path from a pre-determined starting node to every other (reachable) node in a graph while _minimizing the sum of edge weights_ along each path individually.
This is usually referred to as the _single-source shortest path problem_ in reference to road networks.

However, the BFA can also be utilized to solve a related problem:
Imagine nodes being currencies and edge weights describing the _exchange rate_ between pairs of currencies.
Given a starting currency, we now want to discover a sequence of exchanges to each other currency that loses us the least amount of money,
i.e., the sequence has to _maximize the product of the individual exchange rates_ along the path.

My theoretical CS professor solved the problem by logarithmizing and then negating the exchange rates in the original graph.
This way, adding the logarithmized edge weights would effectively amount to a multiplication of the original weights, and the weight negation would turn the maximization problem into a minimization problem.
Consequently, the same implementation could be used for both variants of the problem.

However, this requires duplicating and transforming the input data.
Following basic principles of software engineering, one could also introduce a level of abstraction to the algorithm that would allow the caller to freely choose
the edge weight aggregation function as well as
the path length comparison function,
so that the algorithm accepts the input data as-is.
This comes at the cost of readability, especially for people unfamiliar with the algorithm.

In Scala, one can use _[contextual abstractions](https://docs.scala-lang.org/scala3/book/ca-contextual-abstractions-intro.html)_ to get the best of both worlds:
The [core Bellman-Ford implementation](src/main/scala/demo/bellmanFord.scala) almost looks like the textbook version.
Meanwhile, the meanings of `+`, `<`, `unit` (zero distance), and `inf` (unreachable node) [can be overridden at runtime](src/main/scala/demo/traits.scala) to fit the desired application.

Note that compilation errors become significantly harder to understand when using contextual abstractions.
I still think the concept is cool enough to show off.
Feel free to check out the side-by-side comparison between having to pre-process the input and using runtime polymorphism in [main.scala](src/main/scala/demo/main.scala).
