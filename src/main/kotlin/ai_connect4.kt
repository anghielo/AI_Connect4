// Dany Flores, Umer Amin, Chanrady Ho, Dante Martinez, Thongsavik Sirivong
// Kotlin Language
// Capstone: Real-World Project Scenario
// 16 November, 2021

/*
Connect-4 is an 8x8 board where players take turns placing a piece on any
grid to get either 4 in a row or 4 in a column. Time t is designated as 5 seconds
by default to allow the AI opponent to decide on a next move. The program will also request
from the user as to who will start the game first. 'X' is designated as the computer while
'O' is designated as the human player.

Alpha-Beta pruning will be the algorithm used to find the best move along the game tree using
adversarial search minimax algorithm to efficiently prune moves that do not need to be
evaluated any further down the depth of the tree branch. The algorithm will recursively evaluate
the next move for the player using time = 5 as its constraint.
 */

import java.util.*

var keyboard_input = Scanner(System.`in`)
fun main(args: Array<String>) {
    println("*********************************************************")
    println("\t\t\tAI-\u001B[32mConnect\u001B[0m-\u001B[33m4\u001B[0m")
    println("*********************************************************")
    val time_limit: Long = 5
    var choice = -1
    while (true) {
        try {
            print("Select:\n\t1. Play AI-Connect-4\n\t2. Exit\n==> ")

            choice = Integer.valueOf(readLine())
            if (choice > 0 && choice <= 2) break else println("Please try again. Enter number 1 or 2.\n")
        } catch (e: NumberFormatException) {
            println("Please try again. Only the numbers 1 or 2 are allowed.\n")
        }
    }
    when (choice) {
        1 -> choice = -1
        2 -> {
            println("\nTHANKS FOR PLAYING!\n")
            System.exit(0)
        }
        else -> {
        }
    }
    val b = Board(time_limit)
    var firstPlayer: String? = null
    val is_computer_first: Boolean
    while (true) {
        print("\nWho will be the first player? ('X' means computer, 'O' means human)\n==> ")
        firstPlayer = keyboard_input.nextLine()
        if (firstPlayer.lowercase(Locale.getDefault()) == "x") {
            is_computer_first = true
            b.setFirst(is_computer_first)
            println("Calculating...")
            b.makeMove()
            gameLoop(b)
            break
        } else if (firstPlayer.lowercase(Locale.getDefault()) == "o") {
            is_computer_first = false
            b.setFirst(is_computer_first)
            gameLoop(b)
            break
        } else println("Please try again. Only 'X' or 'O' are allowed.")
    }
}

private fun gameLoop(b: Board) {
    while (true) {
        while (true) {
            print("Enter your move. (Ex: d5)\n==> ")
            val move = keyboard_input.nextLine() // ====================Needs to be rewritten
            val split = move.split("".toRegex()).toTypedArray()
            var x = 1
            var y = 1
            try {
                x = charPosition(split[0]) //A-H
                y = split[1].toInt() //1-8
            } catch (e: Exception) {
                System.err.println("Please run application again. Format: letter must be between A-H and number between 1-8 (Ex: d5)")
                System.exit(1)
            }
            if (b.getAMove(x, y - 1)) {
                b.gameOver(x, y - 1, 2)
                break
            }
        }
        println("Calculating...")
        b.makeMove()
    }
}

// returns the position based on the character
private fun charPosition(s: String): Int {
    val str = "ABCDEFGH"
    val c = str.split("".toRegex()).toTypedArray()
    for (i in c.indices) {
        if (s.toUpperCase() == c[i]) return i
    }
    return -1
}