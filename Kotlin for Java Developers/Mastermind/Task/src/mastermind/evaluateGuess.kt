package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
//    val rightPosition = secret.zip(guess).count { (secretChar, guessChar) ->
//        secretChar == guessChar
//    }
//    val totalCommonLetters = "ABCDEF".sumOf { ch ->
//        val countInSecret = secret.count { it == ch }
//        val countInGuess = guess.count { it == ch }
//        Math.min(countInSecret, countInGuess)
//    }
//    val wrongPosition = totalCommonLetters - rightPosition
//    return Evaluation(rightPosition, wrongPosition)
//    var rightPosition = 0
//    var wrongPosition = 0
//    val secretVisited = BooleanArray(secret.length)
//    val guessVisited = BooleanArray(guess.length)
//    for (i in secret.indices) {
//        if (secret[i] == guess[i]) {
//            rightPosition++
//            secretVisited[i] = true
//            guessVisited[i] = true
//        }
//    }
//    for (i in guess.indices) {
//        if (!guessVisited[i]) {
//            for (j in secret.indices) {
//                if (!secretVisited[j] && guess[i] == secret[j]) {
//                    wrongPosition++
//                    secretVisited[j] = true
//                    break
//                }
//            }
//        }
//    }
//    return Evaluation(rightPosition, wrongPosition)
    val rightPositions = secret.zip(guess).count { it.first == it.second }

    val commonLetters = "ABCDEF".sumOf { ch ->
        Math.min(secret.count { it == ch }, guess.count { it == ch })
    }

    return Evaluation(rightPositions, commonLetters - rightPositions)

}
