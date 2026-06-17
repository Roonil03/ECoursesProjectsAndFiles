package rationals

import java.math.BigInteger

class Rational(n: BigInteger, d: BigInteger) : Comparable<Rational> {
    val numerator: BigInteger
    val denominator: BigInteger

    init {
        require(d != BigInteger.ZERO) { "Denominator cannot be zero" }
        val gcd = n.gcd(d)
        val simplifiedNum = n / gcd
        val simplifiedDen = d / gcd
        if (simplifiedDen < BigInteger.ZERO) {
            numerator = -simplifiedNum
            denominator = -simplifiedDen
        } else {
            numerator = simplifiedNum
            denominator = simplifiedDen
        }
    }

    operator fun plus(other: Rational): Rational =
        Rational(
            numerator * other.denominator + other.numerator * denominator,
            denominator * other.denominator
        )

    operator fun minus(other: Rational): Rational =
        Rational(
            numerator * other.denominator - other.numerator * denominator,
            denominator * other.denominator
        )

    operator fun times(other: Rational): Rational =
        Rational(numerator * other.numerator, denominator * other.denominator)

    operator fun div(other: Rational): Rational =
        Rational(numerator * other.denominator, denominator * other.numerator)

    operator fun unaryMinus(): Rational =
        Rational(-numerator, denominator)

    override fun compareTo(other: Rational): Int =
        (numerator * other.denominator).compareTo(other.numerator * denominator)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Rational) return false
        return numerator == other.numerator && denominator == other.denominator
    }

    override fun hashCode(): Int =
        31 * numerator.hashCode() + denominator.hashCode()

    override fun toString(): String {
        return if (denominator == BigInteger.ONE) "$numerator" else "$numerator/$denominator"
    }
}

infix fun Int.divBy(denominator: Int): Rational =
    Rational(this.toBigInteger(), denominator.toBigInteger())

infix fun Long.divBy(denominator: Long): Rational =
    Rational(this.toBigInteger(), denominator.toBigInteger())

infix fun BigInteger.divBy(denominator: BigInteger): Rational =
    Rational(this, denominator)

fun String.toRational(): Rational {
    val parts = this.split("/")
    return if (parts.size == 2) {
        Rational(parts[0].toBigInteger(), parts[1].toBigInteger())
    } else {
        Rational(this.toBigInteger(), BigInteger.ONE)
    }
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println(
        "912016490186296920119201192141970416029".toBigInteger() divBy
                "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2
    )
}