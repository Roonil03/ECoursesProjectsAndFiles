package nicestring

fun String.isNice(): Boolean {
    val noBadSubstrings = listOf("bu", "ba", "be").none {this.contains(it) }
    val hasThreeVowels = this.count { it in "aeiou" } >= 3
    val hasDoubleLetter = this.zipWithNext().any { it.first == it.second }
    return listOf(noBadSubstrings, hasThreeVowels, hasDoubleLetter).count { it } >= 2
}