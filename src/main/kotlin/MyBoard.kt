// Dany Flores, Umer Amin, Chanrady Ho, Dante Martinez, Thongsavik Sirivong
// Kotlin Language
// Capstone: Real-World Project Scenario
// 16 November, 2021

open class MyBoard () {
    // Colors for the texts
    private val _ansiBlue = "\u001B[34m"
    private val _ansiYellow = "\u001B[33m"
    private val _ansiReset = "\u001B[0m"
    //private val _ansiGreen = "\u001B[32m"

    private val _size = 8                   // the size of the board
    private val _winNumber = 4              // variable to check if there are the same 4 input next to each other
    private var _isComputerFirst = 0    // to check if the AI makes the first move

    protected val board: Array<IntArray> = Array(_size) { IntArray(_size) }    // board: 2D integer array
    protected val occupiedTiles = HashMap<Int, String>() //private val _occupiedTiles = HashMap<Int, String>()
    protected var history = ""          // stores history of the game

    // initialize array of the board
    init{
        for (i in 0 until _size-1) {
            for (j in 0 until _size-1) {
                board[i][j] = 0
            }
        }
    }

    // check if position is taken or not
    protected fun checkLegalMove(i: Int, j: Int): Int {
        return if ((i in 0 until _size) && (j in 0 until _size)) {
            if (board[i][j] == 0) 0 else 1
        } else -1
    }

    /*
    Make a move on board for player
        Arguments:
        * i = row index on the board
        * j = column index on the board
        * moveType = integers of 1 or 2 corresponds to piece X or O
                        If game mode is PvAI, by default moveType is 2.
     */
    fun getAMove(i: Int, j: Int, moveType: Int): Boolean {
        return when (checkLegalMove(i, j)) {
            0 -> {
                board[i][j] = moveType
                occupiedTiles[i * _size + j] = "" + i + j
                val c = (65 + i).toChar()
                history = history + c + (j + 1)
                true
            }
            1 -> {
                println("Move already taken!")
                false
            }
            else -> {
                println("Invalid move!")
                false
            }
        }
    }

    // Check if there's a winner or not
    fun check4Winner(row: Int, col: Int, player: Int): Int {
        return if (checkRow(row, col) == _winNumber ||
            checkCol(row, col) == _winNumber ||
            checkLeftDiag(row, col) == _winNumber ||
            checkRightDiag(row, col) == _winNumber) {
            if (player == 1) Int.MAX_VALUE else Int.MIN_VALUE
        }
        else if (occupiedTiles.size == _size * _size) 1
        else 0
    }

    // Row checking
    private fun checkRow(i: Int, j: Int): Int {
        var count = 1
        var left = true
        var right = true
        var k = 1
        while (left || right) {
            if (j - k < 0) left = false
            if (j + k == _size) right = false
            if (left) {
                if (board[i][j - k] == board[i][j]) count++ else left = false
            }
            if (right) {
                if (board[i][j + k] == board[i][j]) count++ else right = false
            }
            k++
        }
        return count
    }

    // Column checking
    private fun checkCol(i: Int, j: Int): Int {
        var count = 1
        var up = true
        var down = true
        var k = 1
        while (up || down) {
            if (i - k < 0) down = false
            if (i + k == _size) up = false
            if (down) {
                if (board[i - k][j] == board[i][j]) count++ else down = false
            }
            if (up) {
                if (board[i + k][j] == board[i][j]) count++ else up = false
            }
            k++
        }
        return count
    }

    // Check left diagonal
    private fun checkLeftDiag(i: Int, j: Int): Int {
        var count = 1
        var left = true
        var right = true
        var k = 1
        while (left || right) {
            if (i + k >= _size || j - k < 0) left = false
            if (i - k < 0 || j + k >= _size) right = false
            if (left) {
                if (board[i + k][j - k] == board[i][j]) count++ else left = false
            }
            if (right) {
                if (board[i - k][j + k] == board[i][j]) count++ else right = false
            }
            k++
        }
        return count
    }

    // Check right diagonal
    private fun checkRightDiag(i: Int, j: Int): Int {
        var count = 1
        var left = true
        var right = true
        var k = 1
        while (left || right) {
            if (i - k < 0 || j - k < 0) left = false
            if (i + k >= _size || j + k >= _size) right = false
            if (left) {
                if (board[i - k][j - k] == board[i][j]) count++ else left = false
            }
            if (right) {
                if (board[i + k][j + k] == board[i][j]) count++ else right = false
            }
            k++
        }
        return count
    }

    // Check if the game is over or not
    fun isGameOver(i: Int, j: Int, player: Int): Boolean {
        if (check4Winner(i, j, player) == Int.MAX_VALUE) {
//            println("Computer wins!")
            return true
        }
        if (check4Winner(i, j, player) == Int.MIN_VALUE) {
//            println("Human wins!\n")
            return true
        }
        if (check4Winner(i, j, player) == 1) {
            println("Draw!")
            return true
        }
        return false
    }

    fun setFirst(is_computer_first: Int) {
        this._isComputerFirst = is_computer_first
    }

    // Displaying the history of a connect 4 game
    fun displayHistory() {
        println("\nThis is the history:")
        if(_isComputerFirst == 1)
            println("Computer's moves \t\tPlayer's moves")
        else if(_isComputerFirst == 2)
            println("Player's moves \t\t\tComputer's moves")
        else
            println("Player 1 (O) moves \t\tPlayer 2 (X) moves")
        var j = 0
        for (i in history.indices step 2){
            if(j % 2 == 0)
                print(history[i] + "" + history[i+1])
            else {
                print("\t\t\t\t\t\t" + history[i] + "" + history[i + 1])
                println()
            }
            j++
        }
    }

    // Display the game board
    fun printBoard() {
        print(" ")
        for (i in 1.._size)  // print number of column
            print("$_ansiBlue $i $_ansiReset")
        println()
        for (i in 0 until _size) {
            print(_ansiBlue + (i+65).toChar() + _ansiReset)         // Print row: A,B,C,...
            for (j in 0 until _size) {
                when (board[i][j]) {
                    0 ->
                        if (j < 10)
                            print(" - ")
                        else
                            print("  - ")
                    1 ->
                        if (j < 10)
                            print("$_ansiYellow X $_ansiReset")
                        else
                            print("$_ansiYellow  X $_ansiReset")
                    else ->
                        if (j < 10)
                            print("$_ansiYellow O $_ansiReset")
                        else
                            print("$_ansiYellow  O $_ansiReset")
                }
            }
            println()
        }
    }

    operator fun get(x: Int, y: Int) : Int {
        return board[x][y]
    }

    operator fun set(x: Int, y: Int, z: Int) {
        board[x][y] = z
    }
}