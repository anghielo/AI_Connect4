class MyBoard () {
    private val ANSI_BLUE = "\u001B[34m"
    private val ANSI_GREEN = "\u001B[32m"
    private val ANSI_YELLOW = "\u001B[33m"
    private val ANSI_RESET = "\u001B[0m"
    private val SIZE = 12                   // the size of the board
    private val MAXDEPTH = 10
    private val ai_connect4: Array<IntArray>    // board: 2D integer array
    private val win_number = 4              // variable to check if there are the same 4 input next to each other
    //private lateinit val time: Long
    private var startTime: Long = 0
    private var alpha = 0
    private var beta = 0
    private val occupied_tiles = HashMap<Int, String>()
    private var is_computer_first = false
    private var first_move = 0
    private var history = ""

    // initialize array of the board
    init{
        ai_connect4 = Array(SIZE) { IntArray(SIZE) }
        for (i in 0 until SIZE-1) {
            for (j in 0 until SIZE-1) {
                ai_connect4[i][j] = 0
            }
        }
    }

    // check if position is taken or not
    private fun checkLegalMove(i: Int, j: Int): Int {
        return if ((i in 0 until SIZE) && (j in 0 until SIZE)) {
            if (ai_connect4[i][j] == 0) 0 else 1
        } else -1
    }

    fun getAMove(i: Int, j: Int): Boolean {
        return when (checkLegalMove(i, j)) {
            0 -> {
                ai_connect4[i][j] = 2
                occupied_tiles[i * SIZE + j] = "" + i + j
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

    private fun check4Winner(row: Int, col: Int, player: Int): Int {
        if (checkRow(row, col) == win_number || checkCol(row, col) == win_number || checkLeftDiag(row, col) == win_number || checkRightDiag(row, col) == win_number)
            return if (player == 1) 1 else 2
        for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                if (ai_connect4[i][j] == 0) return 0
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
            if (j + k == SIZE) right = false
            if (left) {
                if (ai_connect4[i][j - k] == ai_connect4[i][j])
                    count++
                else
                    left = false
            }
            if (right) {
                if (ai_connect4[i][j + k] == ai_connect4[i][j])
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
            if (i + k == SIZE) up = false
            if (down) {
                if (ai_connect4[i - k][j] == ai_connect4[i][j]) count++ else down = false
            }
            if (up) {
                if (ai_connect4[i + k][j] == ai_connect4[i][j]) count++ else up = false
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
            if (i + k >= SIZE || j - k < 0) left = false
            if (i - k < 0 || j + k >= SIZE) right = false
            if (left) {
                if (ai_connect4[i + k][j - k] == ai_connect4[i][j]) count++ else left = false
            }
            if (right) {
                if (ai_connect4[i - k][j + k] == ai_connect4[i][j]) count++ else right = false
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
            if (i + k >= SIZE || j + k >= SIZE) right = false
            if (left) {
                if (ai_connect4[i - k][j - k] == ai_connect4[i][j]) count++ else left = false
            }
            if (right) {
                if (ai_connect4[i + k][j + k] == ai_connect4[i][j]) count++ else right = false
            }
            k++
        }
        return count
    }

    fun isGameOver(i: Int, j: Int, player: Int): Int {
        if (check4Winner(i, j, player) == 1) {
            return 1
        }
        if (check4Winner(i, j, player) == 2) {
            return 2
        }
        if (check4Winner(i, j, player) == 1) {
            return 0
        }
        return -1
    }

    fun displayHistory() {
        println("\nThis is the history:")
        var j = 0
        for (i in history.indices step 2){
            if(j % 2 == 0)
                print(history[i] + "" + history[i+1])
            else {
                print("\t\t" + history[i] + "" + history[i + 1])
                println()
            }
            j++
        }
        println()
    }

    fun printBoard() {
        print(" ")
        for (i in 1..SIZE)  // print number of column
            print("$ANSI_BLUE $i $ANSI_RESET")
        println()
        for (i in 0 until SIZE) {
            print(ANSI_BLUE + (i+65).toChar() + ANSI_RESET)         // Print row: A,B,C,...
            for (j in 0 until SIZE) {
                when (ai_connect4[i][j]) {
                    0 ->
                        if (j < 10)
                            print(" - ")
                        else
                            print("  - ")
                    1 ->
                        if (j < 10)
                            print("$ANSI_YELLOW X $ANSI_RESET")
                        else
                            print("$ANSI_YELLOW  X $ANSI_RESET")
                    else ->
                        if (j < 10)
                            print("$ANSI_YELLOW O $ANSI_RESET")
                        else
                            print("$ANSI_YELLOW  O $ANSI_RESET")
                }
            }
            println()
        }
    }
}