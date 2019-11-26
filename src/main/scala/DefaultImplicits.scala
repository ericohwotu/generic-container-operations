import scala.annotation.implicitNotFound
import scala.concurrent.{ExecutionContext, Future}

object DefaultImplicits {
  type MyEither[A] = Either[Exception, A]

  implicit val eitherImplicits: Monad[MyEither] with Foldable[MyEither] = new Monad[MyEither] with Foldable[MyEither] {
    def map[A, B](actual: MyEither[A])(f: A => B): Either[Exception, B] = actual.map(f)

    def flatMap[A, B](actual: MyEither[A])(f: A => MyEither[B]): MyEither[B] = actual.flatMap(f)

    def fold[A, B, C](actual: MyEither[A])(undefined: MyEither[B] => C, isDefined: A => C): C =
      actual.fold(exception => undefined(Left(exception)), isDefined)
  }

  /**
   * TODO: Figure out how to enable List Foldable
   * */
  implicit val listImplicits: Monad[List] with Functor[List] = new Monad[List] with Functor[List] {
    def map[A, B](actual: List[A])(f: A => B): List[B] = actual.map(f)

    def flatMap[A, B](actual: List[A])(f: A => List[B]): List[B] = actual.flatMap(f)

    def pure[A](item: A): List[A] = List(item)
  }

  implicit val optionImplicits: Monad[Option] with Functor[Option] with Foldable[Option] = new Monad[Option] with Functor[Option] with Foldable[Option] {
    def map[A, B](actual: Option[A])(f: A => B): Option[B] = actual.map(f)

    def flatMap[A, B](actual: Option[A])(f: A => Option[B]): Option[B] = actual.flatMap(f)

    def pure[A](item: A): Option[A] = Some(item)

    def fold[A, B, C](actual: Option[A])(undefined: Option[B] => C, defined: A => C): C =
      actual.fold(undefined(None))(defined)
  }

  implicit def futureImplicits(implicit ec: ExecutionContext): Monad[Future] with Functor[Future] = new Monad[Future] with Functor[Future] {

    def map[A, B](actual: Future[A])(f: A => B): Future[B] = actual.map(f)

    def flatMap[A, B](actual: Future[A])(f: A => Future[B]): Future[B] = actual.flatMap(f)

    def pure[A](item: A): Future[A] = Future.successful(item)
  }

}
