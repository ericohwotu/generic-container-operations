import org.scalatest.{FlatSpec, MustMatchers}
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import DefaultImplicits._
import org.mockito.Mockito
import org.mockito.Mockito._
import org.mockito.Matchers.any
import org.scalatest.concurrent.ScalaFutures

class FutureFunctorSpec extends FlatSpec with MustMatchers with MockitoSugar with ScalaFutures {

  object FutureFunctorTestFunctions {
    def successFullOption[T](data: T): NestedValueOps[Future, Option, T] = NestedValueOps(Future(Some(data)))

    def failedOption[T](data: T): NestedValueOps[Future, Option, T] = NestedValueOps(Future(Option.empty[T]))
  }

  behavior of "NestedValueOps[Future, Option, T]"

  it must "perform all operation" in {
    val spiedOnObject = Mockito.spy(FutureFunctorTestFunctions)
    import spiedOnObject._

    val testOperation: NestedValueOps[Future, Option, Seq[String]] = for {
      firstCall <- successFullOption(1)
      secondCall <- successFullOption("hello " + firstCall)
      thirdCall <- successFullOption(secondCall +: Seq("one", "two"))
    } yield thirdCall

    whenReady(testOperation.value){
      resultOfTestOperation =>
        resultOfTestOperation mustBe Some(Seq("hello 1", "one", "two"))
        verify(spiedOnObject, times(3)).successFullOption(any())
    }
  }
  it must "stop at the first call if that returns an Option.empty[T]" in {
    val spiedOnObject = Mockito.spy(FutureFunctorTestFunctions)
    import spiedOnObject._

    val testOperation: NestedValueOps[Future, Option, Seq[String]] = for {
      firstCall <- failedOption(1)
      secondCall <- successFullOption("hello " + firstCall)
      thirdCall <- successFullOption(secondCall +: Seq("one", "two"))
    } yield thirdCall

    whenReady(testOperation.value){
      resultOfTestOperation =>
        resultOfTestOperation mustBe Option.empty
        verify(spiedOnObject, times(1)).failedOption(1)
        verify(spiedOnObject, times(0)).successFullOption(any())
    }
  }
  it must "stop at the second call if that returns an Option.empty[T]" in {
    val spiedOnObject = Mockito.spy(FutureFunctorTestFunctions)
    import spiedOnObject._

    val testOperation: NestedValueOps[Future, Option, Seq[String]] = for {
      firstCall <- successFullOption(1)
      secondCall <- failedOption("hello " + firstCall)
      thirdCall <- successFullOption(secondCall +: Seq("one", "two"))
    } yield thirdCall

    whenReady(testOperation.value){
      resultOfTestOperation =>
        resultOfTestOperation mustBe Option.empty
        verify(spiedOnObject, times(1)).failedOption("hello 1")
        verify(spiedOnObject, times(1)).successFullOption(1)
    }
  }
}
