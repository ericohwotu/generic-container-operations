import scala.concurrent.Future

object app extends App {

  import scala.concurrent.ExecutionContext.Implicits.global
  import DefaultImplicits._

  type MyEither[T] = Either[Exception, T]

  def passedOption(value: Option[String]): Future[Option[String]] = Future {
    value
  }

  def passed(value: List[String]): Option[List[String]] = if (value.isEmpty) None else Some(value)

  (for {
    one <- NestedValueOps(passedOption(Some("hlo")))
    two <- NestedValueOps(passedOption(Some("hiena")))
    three <- NestedValueOps(passedOption(Some(s"$one free")))
  } yield three)

//  (for {
//    one <- FutureTOps(passed(List("one")))
//    two <- FutureTOps(passed(List("two", "five") :+ one))
//    three <- FutureTOps(passed(List(s"three") :+ two))
//  } yield three).item.foreach(println)

  println("hello")
}
