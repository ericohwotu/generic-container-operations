import scala.annotation.implicitNotFound

@implicitNotFound("Missing Foldable implicit for ${F}")
trait Foldable[F[_]] {
  def fold[A, B, C](actual: F[A])(undefined: F[B] => C, defined: A => C): C
}

@implicitNotFound("Missing Monad implicit for ${F}")
trait Monad[F[_]] {
  def map[A, B](actual: F[A])(f: A => B): F[B]
}

@implicitNotFound("Missing Functor implicit for ${F}")
trait Functor[F[_]] {
  def flatMap[A, B](actual: F[A])(f: A => F[B]): F[B]

  def pure[A](item: A): F[A]
}

case class NestedValueOps[G[_], F[_], T](value: G[F[T]])
                                        (implicit monadF: Monad[F], foldableF: Foldable[F], monadG: Monad[G], functorG: Functor[G]) {
  def map[B](f: T => B): NestedValueOps[G, F, B] = NestedValueOps(monadG.map(value)(x => monadF.map(x)(f)))

  def flatMap[B](f: T => NestedValueOps[G, F, B]): NestedValueOps[G, F, B] = NestedValueOps(functorG.flatMap(value)(
    x => foldableF.fold[T, B, G[F[B]]](x)(undefined => functorG.pure(undefined), defined => f(defined).value)
  ))
}
