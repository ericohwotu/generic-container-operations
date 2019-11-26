# NestedValueOps

#### Limitations
This only works with inner containers of type F\[A] and so an either is if you need and Either it can be defined as: 
```scala
type MyEither[R] = Either[GenericSuperClass, R]
```
Currently sequences cannot be the F because reasons but the can be G

#### Example usage: 
```scala
import scala.concurrent.Future

type MyEither[R] = Either[GenericSuperClass, R]

def function: Future[MyEither[Int]] = ???

// individual
NestedValueOps[Future, MyEither, Int](function).map[String]{ innerInt => innerInt.toString }.value

// chained
(for {
 w <- NestedValueOps(function)
 x <- NestedValueOps(function)
 y <- NestedValueOps(function)
 z <- NestedValueOps(function)
} yield z).value
```
```scala
import scala.concurrent.Future

def function: Future[Option[Int]] = ???

// individual
NestedValueOps[Future, Option, Int](function).map[String]{ innerInt => innerInt.toString }.value

// chained
(for {
 w <- NestedValueOps(function)
 x <- NestedValueOps(function)
 y <- NestedValueOps(function)
 z <- NestedValueOps(function)
} yield z).value
```
```scala
type MyEither[R] = Either[GenericSuperClass, R]

def function: Seq[MyEither[Int]] = ???

// individual
NestedValueOps[Seq, MyEither, Int](function).map[String]{ innerInt => innerInt.toString }.value

// chained
(for {
 w <- NestedValueOps(function)
 x <- NestedValueOps(function)
 y <- NestedValueOps(function)
 z <- NestedValueOps(function)
} yield z).value
```
