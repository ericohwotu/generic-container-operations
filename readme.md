# NestedValueOps

Given a container with the type G\[F\[A]] this repo aims to provide a common interface to allow for a to be easily accessed.

example usage: 
```scala
import scala.concurrent.Future

def function: Future[Either[Exception, Int]] = ???

NestedValueOps[Future, Either, String](function).map[String]{ innerInt => innerInt.toString }.value
```
