// Dany Flores, Umer Amin, Chanrady Ho, Dante Martinez, Thongsavik Sirivong
// Kotlin Language
// Capstone: Real-World Project Scenario
// 16 November, 2021

/*  Things to add:
        1. Print board according to the size
        2. Player vs Player
        3. Update the board instead of adding it to command prompt
        4. GUI (optional)
    Things to fix:
        AI algorithm:
            First AI move should be random
            Diagonal check
        Input verification
        History on the bottom instead
 */

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
import kotlin.system.exitProcess

var keyboard_input = Scanner(System.`in`)
fun main() {
    println("*********************************************************")
    println("\t\t\tAI-\u001B[32mConnect\u001B[0m-\u001B[33m4\u001B[0m")
    println("*********************************************************")
    var choice: Int = -1
    while (true) {
        try {
            print("\n*** Main Menu ***\n\t1. Player vs AI\n\t2. Player 1 vs Player 2\n\t3. Exit\n==> ")
            choice = Integer.valueOf(readLine())
            if (choice !in 1..3) {
                println("Please try again. Enter number 1, 2 or 3.\n")
                continue
            }
        } catch (e: NumberFormatException) {
            println("Please try again. Only the numbers 1, 2 or 3 are allowed.\n")
        }
        when (choice) {
            1 -> option1()
            2 -> option2()
            3 -> {
                println("\nTHANKS FOR PLAYING!\n")
                exitProcess(0)
            }
        }
    }
}

// Player vs. AI
private fun option1() {
    val timeLimit: Long = 5
    var b = Board(timeLimit)
    var firstPlayer: String?
    var isComputerFirst: Boolean
    var isInGame = true
    while (isInGame) {
        print("\nWho will be the first player? ('X' means computer, 'O' means human)\n==> ")
        firstPlayer = keyboard_input.nextLine()
        if (firstPlayer.lowercase(Locale.getDefault()) == "x") {
            isComputerFirst = true
            b.setFirst(isComputerFirst)
            println("Calculating...")
            b.makeMove()
        }
        else if (firstPlayer.lowercase(Locale.getDefault()) == "o") {
            isComputerFirst = false
            b.setFirst(isComputerFirst)
        }
        else {
            println("Please try again. Only 'X' or 'O' are allowed.")
            continue
        }

        gameLoop(b)

        isInGame = postGameOption()
        if (isInGame)
            b = Board(timeLimit)
    }
}

// Player 1 vs. Player 2
private fun option2() {
    //TO-DO:
}

// In Game
private fun gameLoop(b: Board) {
    while (true) {
        while (true) {
            print("Enter your move. (Ex: d5)\n==> ")
            val move: String = keyboard_input.nextLine()
            var x: Int
            var y: Int
            try {
                x = if (move[0] < 'a') (move[0] - 'A') else (move[0] - 'a')
                y = move[1] - '0'
            } catch (e: Exception) {
                System.err.println("Please run application again. Format: letter must be between A-H and number between 1-8 (Ex: d5)")
                exitProcess(1)
            }
            if (b.getAMove(x, y - 1)) {
                b.isGameOver(x, y - 1, 2)
                break
            }
        }
        println("Calculating...")
        if (b.makeMove()) {
            break
        }
    }
}

// Post-Game Menu
private fun postGameOption(): Boolean {
    var choice: Int
    while (true) {
        try {
            print("\n*** Post-Game Menu ***\n\t1. Play Again\n\t2. Return to Main Menu\n\t3. Exit\n==> ")

            choice = Integer.valueOf(readLine())
            if (choice in 1..3) break else println("Please try again. Enter number 1, 2 or 3.\n")
        } catch (e: NumberFormatException) {
            println("Please try again. Only the numbers 1, 2 or 3 are allowed.\n")
        }
    }
    when (choice) {
        1 -> return true
        2 -> return false
        3 -> {
            println("\nTHANKS FOR PLAYING!\n")
            exitProcess(0)
        }
    }
    return false
}