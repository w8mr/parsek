package jafun
//
//
//
//interface Seq<T,X>  {
//    sealed interface Result<T> {
//        data class Next<T>(val value: T) : Result<T>
//        object EoS : Result<Unit>
//    }
//    fun next() : Result<T>
//}
//
//interface IterSeq<T> : Seq<T, Iterator<T>>
//fun <R> seq(iter: Iterator<R>) = object : IterSeq<R> {
//    override fun next(): Seq.Result<R> =
//        when {
//            iter.hasNext() -> Seq.Result.Next(iter.next())
//            else -> Seq.Result.EoS as Seq.Result<R>
//        }
//
//}
//
//fun <R,T,X> Seq<R,Iterator<R>>.map(f :(R) -> T) = object : IterSeq() {
//    override fun next(): Sequence.Result<T> =
//        when (val next = this@map.next()) {
//            is Sequence.Result.Next -> Sequence.Result.Next(f(next.value))
//            is Sequence.Result.EoS -> Sequence.Result.EoS as Sequence.Result<T>
//        }
//}
//
//fun <R,X> Seq<R,X>.filter(f :(R) -> Boolean) = object : Sequence<R,X> {
//    override fun next(): Sequence.Result<R> {
//        do {
//            val next = this@filter.next()
//            when {
//                next is Sequence.Result.Next -> {
//                    if (f(next.value)) return Sequence.Result.Next(next.value)
//                }
//            }
//        } while (next !is Sequence.Result.EoS)
//        return Sequence.Result.EoS as Sequence.Result<R>
//    }
//}
//
//fun <R,X> Seq<R,X>.terminate(): X {
//    val result = mutableListOf<R>()
//    var next = this.next()
//    while (next != Sequence.Result.EoS) {
//        result.add((next as Sequence.Result.Next<R>).value)
//        next = this.next()
//    }
//    return result.toList()
//}
//
//fun <R,T> Iterable<R>.map(f: (R) -> T): Sequence<T> = seq(this.iterator()).map(f)
//fun <R> Iterable<R>.filter(f: (R) -> Boolean): Sequence<R> = seq(this.iterator()).filter(f)