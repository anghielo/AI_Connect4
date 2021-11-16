open class MyBoard () {
    private val _ansiBlue = "\u001B[34m"
    private val _ansiGreen = "\u001B[32m"
    private val _ansiYellow = "\u001B[33m"
    private val _ansiReset = "\u001B[0m"
    private val _size = 8                   // the size of the board
//    private val MAXDEPTH = 10
    protected val board: Array<IntArray> = Array(_size) { IntArray(_size) }    // board: 2D integer array
    private val _winNumber = 4              // variable to check if there are the same 4 input next to each other
//    private lateinit val time: Long
    private var startTime: Long = 0
    private var alpha = 0
    private var beta = 0
    private val _occupiedTiles = HashMap<Int, String>()
    private var _isComputerFirst = 0
//    private var first_move = 0
    protected var history = ""

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

    open fun getAMove(i: Int, j: Int): Boolean {
        return when (checkLegalMove(i, j)) {
            0 -> {
                board[i][j] = 2
                _occupiedTiles[i * _size + j] = "" + i + j
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
        if (checkRow(row, col) == _winNumber || checkCol(row, col) == _winNumber || checkLeftDiag(row, col) == _winNumber || checkRightDiag(row, col) == _winNumber)
            return if (player == 1) 50000 else -50000
        for (i in 0 until _size) {
            for (j in 0 until _size) {
                if (board[i][j] == 0) return 0
            }
        }
        return -1
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
                if (board[i][j - k] == board[i][j])
                    count++
                else
                    left = false
            }
            if (right) {
                if (board[i][j + k] == board[i][j])
                    count++
                else
                    right = false
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

    fun isGameOver(i: Int, j: Int, player: Int): Boolean {
        if (check4Winner(i, j, player) == 50000) {
//            println("Computer wins!")
            return true
        }
        if (check4Winner(i, j, player) == -50000) {
//            println("Human wins!\n")
            return true
        }
        if (check4Winner(i, j, player) == 1) {
//            println("Draw!")
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
            println("Player 1's moves \t\t\tPlayer 2's moves")
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