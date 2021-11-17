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
    var isComputerFirst: Int
    var isInGame = true
    while (isInGame) {
        print("\nWho will be the first player? ('X' means computer, 'O' means human)\n==> ")
        firstPlayer = keyboard_input.nextLine()
        if (firstPlayer == "X" || firstPlayer == "x") {
            isComputerFirst = 1
            b.setFirst(isComputerFirst)
            println("AI Calculating...")
            b.makeMove()
        }
        else if (firstPlayer == "O" || firstPlayer == "o") {
            isComputerFirst = 2
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

// In PvAI Game
private fun gameLoop(b: Board) {
    var gameOver = false
    loop@ while (true) {
        while (true) {
            displayGame(b)
            print("\nEnter your move. (Ex: d5)\n==> ")
            val move: String = keyboard_input.nextLine()
            if (move == "quit")
                break@loop
            var x: Int
            var y: Int
            try {
                x = if (move[0] < 'a') (move[0] - 'A') else (move[0] - 'a')
                y = move[1] - '0'
            } catch (e: Exception) {
                System.err.println("Please run application again. Format: letter must be between A-H and number between 1-8 (Ex: d5)")
                exitProcess(1)
            }
            if (b.getAMove(x, y - 1, 2)) {
                displayGame(b)
                if(b.isGameOver(x, y - 1, 2)) {
                    gameOver = true
                    println()
                    println("******************")
                    println("*   \u001B[32mHuman Wins\u001B[33m!\u001B[0m  *")
                    println("******************")
                    break
                }
                break
            }
        }
        if(!gameOver) println("\nAI Calculating...\n") else break
        if (b.makeMove()) {
            displayGame(b)
            println()
            println("******************")
            println("* \u001B[32mComputer Wins\u001B[33m!\u001B[0m *")
            println("******************")
            break
        }
    }
}

// Player 1 vs. Player 2
private fun option2() {
    val timeLimit: Long = 5
    var b = Board(timeLimit)
    var isInGame = true
    while (isInGame) {

        gameLoop2(b)

        isInGame = postGameOption()
        if (isInGame)
            b = Board(timeLimit)
    }
}

//In PvP Game
private fun gameLoop2(b:Board) {
    var gameOver = false
    while (true) {
        while (true) {
            displayGame(b)
            print("\nEnter your move. (Ex: d5)\n==> ")
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
            if (b.getAMove(x, y - 1, 2)) {
                displayGame(b)
                if(b.isGameOver(x, y - 1, 2)) {
                    gameOver = true
                    println()
                    println("******************")
                    println("* \u001B[32mPlayer 1 Wins\u001B[33m!\u001B[0m *")
                    println("******************")
                    break
                }
                break
            }
        }
        if (gameOver) {
            break
        }
        while (true) {
            println()
            print("\nPlayer 2 enter your move. (Ex: d5)\n==> ")
            val move2: String = keyboard_input.nextLine()
            var x2: Int
            var y2: Int
            try {
                x2 = if (move2[0] < 'a') (move2[0] - 'A') else (move2[0] - 'a')
                y2 = move2[1] - '0'
            } catch (e: Exception) {
                System.err.println("Please run application again. Format: letter must be between A-H and number between 1-8 (Ex: d5)")
                exitProcess(1)
            }
            if (b.getAMove(x2, y2 - 1, 1)) {
                displayGame(b)
                if(b.isGameOver(x2, y2 - 1, 2)) {
                    gameOver = true
                    println()
                    println("******************")
                    println("* \u001B[32mPlayer 2 Wins\u001B[33m!\u001B[0m *")
                    println("******************")
                    break
                }
                break
            }
        }
        if (gameOver){
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

fun displayGame(b :Board){
    b.printBoard()
    b.displayHistory()
    println()
}
