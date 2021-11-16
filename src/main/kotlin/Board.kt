// Dany Flores, Umer Amin, Chanrady Ho, Dante Martinez, Thongsavik Sirivong
// Kotlin Language
// Capstone: Real-World Project Scenario
// 16 November, 2021

class Board internal constructor(time: Long) : MyBoard() {
//    private val _ansiBlue = "\u001B[34m"
//    private val _ansiGreen = "\u001B[32m"
//    private val _ansiYellow = "\u001B[33m"
//    private val _ansiReset = "\u001B[0m"
    private val _size = 8
    private val _maxDepth = 10
//    private val _board: Array<IntArray>
//    private var _board = MyBoard()
//    private val _winNumber = 4
    private val _time: Long
    private var _startTime: Long = 0
    private var _alpha = 0
    private var _beta = 0
    private var _occupiedTiles = HashMap<Int, String>()
//    private var _isComputerFirst = false
//    private var _firstMove = 0
    private var _history = ""

//    private fun checkLegalMove(i: Int, j: Int): Int {
//        return if ((i in 0 until _size) && (j in 0 until _size)) {
////            if (_board[i][j] == 0) 0 else 1
//            if (_board[i, j] == 0) 0 else 1
//        } else -1
//    }

//    fun getAMove(i: Int, j: Int): Boolean {
//        return when (checkLegalMove(i, j)) {
//            0 -> {
////                _board[i][j] = 2
//                _board[i, j] = 2
//                _occupiedTiles[i * _size + j] = "" + i + j
//                val convert = 65 + i
//                val c = convert.toChar()
//                _history = _history + c + (j + 1)
//                //System.out.println("\nThis is the history: " + history);
//                printBoard()
//                true
//            }
//            1 -> {
//                println("Move already taken!")
//                false
//            }
//            else -> {
//                println("Invalid move!")
//                false
//            }
//        }
//    }

    override fun getAMove(i: Int, j: Int): Boolean {
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

    private fun evaluate(is_min: Boolean): Int {
        val exploredTiles: HashMap<Int, String> = HashMap()
        var evalValue = 0
        for ((key, value) in _occupiedTiles) {
            if (exploredTiles.containsKey(key)) {
                continue
            }
            val i = value[0] - '0'
            val j = value[1] - '0'
            var sum = 0
            var left = true
            var right = true
            var k = 1
            var moveCount = 1
            var emptyCount = 0
            while (left || right) {
                if (j - k < 0) left = false
                if (j + k == _size) right = false
                if (left) {
                    if (board[i][j - k] == board[i][j]) {
                        moveCount++
                        if (!exploredTiles.containsKey(i * _size + (j - k))) {
                            exploredTiles[i * _size + (j - k)] = "" + i + (j - k)
                        }
                    } else {
                        if (board[i][j - k] == 0) {
                            emptyCount++
                        }
                        left = false
                    }
                }
                if (right) {
                    if (board[i][j + k] == board[i][j]) {
                        moveCount++
                        if (!exploredTiles.containsKey(i * _size + (j + k))) {
                            exploredTiles[i * _size + (j + k)] = "" + i + (j + k)
                        }
                    } else {
                        if (board[i][j + k] == 0) {
                            emptyCount++
                        }
                        right = false
                    }
                }
                k++
            }
            sum += if (moveCount > 1) {
                if (emptyCount >= 1 && moveCount >= 3) 500 * emptyCount * moveCount
                else if (emptyCount == 2 && moveCount == 2) 150 * emptyCount * moveCount
                    else 50 * emptyCount * moveCount
            } else {
                emptyCount
            }
            var up = true
            var down = true
            k = 1
            moveCount = 1
            emptyCount = 0
            while (up || down) {
                if (i - k < 0) down = false
                if (i + k == _size) up = false
                if (down) {
                    if (board[i - k][j] == board[i][j]) {
                            moveCount++
                        if (!exploredTiles.containsKey((i - k) * _size + j)) {
                            exploredTiles[(i - k) * _size + j] = "" + (i - k) + j
                        }
                    } else {
                        if (board[i - k][j] == 0) {
                            emptyCount++
                        }
                        down = false
                    }
                }
                if (up) {
                    if (board[i + k][j] == board[i][j]) {
                            moveCount++
                        if (!exploredTiles.containsKey((i + k) * _size + j)) {
                            exploredTiles[(i + k) * _size + j] = "" + (i + k) + j
                        }
                    } else {
                        if (board[i + k][j] == 0) {
                            emptyCount++
                        }
                        up = false
                    }
                }
                k++
            }
            sum += if (moveCount > 1) {
                if (emptyCount >= 1 && moveCount >= 3) 400 * emptyCount * moveCount
                else if (emptyCount == 2 && moveCount == 2) 150 * emptyCount * moveCount
                    else 50 * emptyCount * moveCount
            }
            else {emptyCount}
            if (board[i][j] == 2) {
                sum *= -2
            }
            evalValue += sum
            exploredTiles[i * _size + j] = "" + i + j
        }
        return if (is_min) {
            evalValue
        }
        else {
            evalValue * -1
        }
    }

    fun makeMove(): Boolean {
        var best = -200000
        val depth = _maxDepth
        var score: Int
        var mi = 0
        var mj = 0
        startTimer()
        for (i in 0 until _size) {
            for (j in 0 until _size) {
                if (board[i][j] == 0) {
                    val evalValue = 0
                    _alpha = -200000
                    _beta = 200000
                    board[i][j] = 1 // make move on board
                    _occupiedTiles[i * _size + j] = "" + i + j
                    score = min(depth - 1, i, j, evalValue)
                    if (score > best) {
                        mi = i
                        mj = j
                        best = score
                    }
                    board[i][j] = 0 // undo move
                    _occupiedTiles.remove(i * _size + j)
                }
            }
        }
        board[mi][mj] = 1
        _occupiedTiles[mi * _size + mj] = "" + mi + mj
        val convert = 65 + mi
        val c = convert.toChar()
//        _history = _history + c + (mj + 1)
        history = history + c + (mj + 1)
        //System.out.println("\nThis is the history: " + history);
//        printBoard()
        //System.out.println("\nTime taken: " + (System.currentTimeMillis() - startTime)/1000 + " secs\n");
        return isGameOver(mi, mj, 1)
    }

    private fun min(depth: Int, row: Int, col: Int, e_value: Int): Int {
        var best = 200000
        var score: Int
        val temp = check4Winner(row, col, 1)
        if (temp != 0) return temp
        if (System.currentTimeMillis() - _startTime > _time) return evaluate(true)
        if (depth == 0) return evaluate(true)
        for (i in 0 until _size) {
            for (j in 0 until _size) {
                if (board[i][j] == 0) {
                    board[i][j] = 2
                    score = max(depth - 1, i, j, e_value)
                    _occupiedTiles[i * _size + j] = "" + i + j
                    if (score < best) best = score
                    board[i][j] = 0 // undo move
                    _occupiedTiles.remove(i * _size + j)
                    if (best <= _alpha) return best else {
                        if (best < _beta) _beta = best
                    }
                }
            }
        }
        return best
    }

    private fun max(depth: Int, row: Int, col: Int, e_value: Int): Int {
        var best = -200000
        var score: Int
        val temp = check4Winner(row, col, 2)
        if (temp != 0) return temp
        if (System.currentTimeMillis() - _startTime > _time) return evaluate(false)
        if (depth == 0) return evaluate(false)
        for (i in 0 until _size) {
            for (j in 0 until _size) {
                if (board[i][j] == 0) {
                    board[i][j] = 1
                    _occupiedTiles[i * _size + j] = "" + i + j
                    score = min(depth - 1, i, j, e_value)
                    if (score > best) best = score
                    board[i][j] = 0 // undo move
                    _occupiedTiles.remove(i * _size + j)
                    if (best >= _beta) return best else {
                        if (best > _alpha) _alpha = best
                    }
                }
            }
        }
        return best
    }

//    private fun check4Winner(row: Int, col: Int, player: Int): Int {
//        if (checkRow(row, col) == _winNumber) return if (player == 1) 50000 else -50000
//        if (checkCol(row, col) == _winNumber) return if (player == 1) 50000 else -50000
//        /*
//        if (checkLeftDiag(row, col) == win_number)
//            return (player == 1)? 5000 : -5000;
//        if (checkRightDiag(row, col) == win_number)
//            return (player == 1)? 5000 : -5000;
//        */for (i in 0 until _size) {
//            for (j in 0 until _size) {
//                if (_board[i][j] == 0) return 0
//            }
//        }
//        return -1
//    }

//    private fun checkRow(i: Int, j: Int): Int {
//        var count = 1
//        var left = true
//        var right = true
//        var k = 1
//        while (left || right) {
//            if (j - k < 0) left = false
//            if (j + k == _size) right = false
//            if (left) {
//                if (_board[i][j - k] == _board[i][j]) count++ else left = false
//            }
//            if (right) {
//                if (_board[i][j + k] == _board[i][j]) count++ else right = false
//            }
//            k++
//        }
//        return count
//    }

//    private fun checkCol(i: Int, j: Int): Int {
//        var count = 1
//        var up = true
//        var down = true
//        var k = 1
//        while (up || down) {
//            if (i - k < 0) down = false
//            if (i + k == _size) up = false
//            if (down) {
//                if (_board[i - k][j] == _board[i][j]) count++ else down = false
//            }
//            if (up) {
//                if (_board[i + k][j] == _board[i][j]) count++ else up = false
//            }
//            k++
//        }
//        return count
//    }

    /*
    *   These two functions allow the game to check for 4-in-a-line in the diagonal line
    *   - If implemented, must check with the check4Winner() and evaluate() functions
    */
    /*
    private int checkLeftDiag(int i, int j) {
        int count = 1;
        boolean left = true, right = true;
        int k = 1;
        while(left == true || right == true) {
            if (i-k < 0 || j-k < 0) left = false;
            if (i+k == SIZE || j+k == SIZE) right = false;

            if (left == true) {
                if (ai_connect4[i-k][j-k] == ai_connect4[i][j])
                    count++;
                else
                    left = false;
            }
            if (right == true) {
                if (ai_connect4[i+k][j+k] == ai_connect4[i][j])
                    count++;
                else
                    right = false;
            }
            k++;
        }

        return count;
    }

    private int checkRightDiag(int i, int j) {
        int count = 1;
        boolean left = true, right = true;
        int k = 1;
        while(left == true || right == true) {
            if (i+k == SIZE || j-k < 0) left = false;
            if (i-k < 0 || j+k == SIZE) right = false;

            if (left == true) {
                if (ai_connect4[i+k][j-k] == ai_connect4[i][j])
                    count++;
                else
                    left = false;
            }
            if (right == true) {
                if (ai_connect4[i-k][j+k] == ai_connect4[i][j])
                    count++;
                else
                    right = false;
            }
            k++;
        }

        return count;
    }
*/
//    fun isGameOver(i: Int, j: Int, player: Int): Boolean {
//        if (check4Winner(i, j, player) == 50000) {
//            println("Computer wins!\n")
//            //exitProcess(0)
//            return true
//        }
//        if (check4Winner(i, j, player) == -50000) {
//            println("Human wins!\n")
//            //exitProcess(0)
//            return true
//        }
//        if (check4Winner(i, j, player) == 1) {
//            println("Draw!\n")
//            //exitProcess(0)
//            return true
//        }
//        return false
//    }

//    fun setFirst(is_computer_first: Boolean) {
//        this._isComputerFirst = is_computer_first
//    }

    private fun startTimer() {
        _startTime = System.currentTimeMillis()
    }

//    private fun printBoard() {
//        val letters = "ABCDEFGH"
//        val letterArray = letters.toCharArray()
//        println()
//        print(" ")
//        for (i in 1.._size) print(_ansiBlue + " " + i + _ansiReset)
//        if (_firstMove == 0 && _isComputerFirst == false) {
//            print("\t\t Move List")
//            _firstMove++
//        }
//        else if (_firstMove != 0 && _isComputerFirst) print("\t\t Computer vs. Opponent")
//        else if (_firstMove != 0 && _isComputerFirst == false) print("\t\t Opponent vs. Computer")
//        var two = true
//        var four = true
//        var six = true
//        var eight = true
//        var ten = true
//        var twelve = true
//        var fourteen = true
//        var sixteen = true
//        var eighteen = true
//        var twenty = true
//        var twenty_two = true
//        var twenty_four = true
//        var twenty_six = true
//        var twenty_eight = true
//        var thirty = true
//        var thirty_two = true
//        var thirty_four = true
//        var thirty_six = true
//        var thirty_eight = true
//        var forty = true
//        var forty_two = true
//        var forty_four = true
//        var forty_six = true
//        var forty_eight = true
//        var round_counter = 1
//        print("\n")
//        for (j in 0..0) {
//            print(_ansiBlue + letterArray[j] + _ansiReset)
//            for (k in 0 until _size) {
//                when (_board[j][k]) {
//                    0 -> print(" -")
//                    1 -> print(_ansiYellow + " X" + _ansiReset)
//                    else -> print(_ansiGreen + " O" + _ansiReset)
//                }
//            }
//            if (_history.length >= 2 && two == true) {
//                print("\t\t$round_counter. ")
//                print("" + _history[0] + _history[1])
//                two = false
//                round_counter++
//            }
//            if (_history.length >= 4 && four == true) {
//                print("\t\t" + _history[2] + _history[3])
//                four = false
//            }
//        }
//        println()
//        for (j in 1..1) {
//            print(_ansiBlue + letterArray[j] + _ansiReset)
//            for (k in 0 until _size) {
//                when (_board[j][k]) {
//                    0 -> print(" -")
//                    1 -> print(_ansiYellow + " X" + _ansiReset)
//                    else -> print(_ansiGreen + " O" + _ansiReset)
//                }
//            }
//            if (_history.length >= 6 && six == true) {
//                print("\t\t$round_counter. ")
//                print("" + _history[4] + _history[5])
//                six = false
//                round_counter++
//            }
//            if (_history.length >= 8 && eight == true) {
//                print("\t\t" + _history[6] + _history[7])
//                eight = false
//            }
//        }
//        println()
//        for (j in 2..2) {
//            print(_ansiBlue + letterArray[j] + _ansiReset)
//            for (k in 0 until _size) {
//                when (_board[j][k]) {
//                    0 -> print(" -")
//                    1 -> print(_ansiYellow + " X" + _ansiReset)
//                    else -> print(_ansiGreen + " O" + _ansiReset)
//                }
//            }
//            if (_history.length >= 10 && ten == true) {
//                print("\t\t$round_counter. ")
//                print("" + _history[8] + _history[9])
//                ten = false
//                round_counter++
//            }
//            if (_history.length >= 12 && twelve == true) {
//                print("\t\t" + _history[10] + _history[11])
//                twelve = false
//            }
//        }
//        println()
//        for (j in 3..3) {
//            print(_ansiBlue + letterArray[j] + _ansiReset)
//            for (k in 0 until _size) {
//                when (_board[j][k]) {
//                    0 -> print(" -")
//                    1 -> print(_ansiYellow + " X" + _ansiReset)
//                    else -> print(_ansiGreen + " O" + _ansiReset)
//                }
//            }
//            if (_history.length >= 14 && fourteen == true) {
//                print("\t\t$round_counter. ")
//                print("" + _history[12] + _history[13])
//                fourteen = false
//                round_counter++
//            }
//            if (_history.length >= 16 && sixteen == true) {
//                print("\t\t" + _history[14] + _history[15])
//                sixteen = false
//            }
//        }
//        println()
//        for (j in 4..4) {
//            print(_ansiBlue + letterArray[j] + _ansiReset)
//            for (k in 0 until _size) {
//                when (_board[j][k]) {
//                    0 -> print(" -")
//                    1 -> print(_ansiYellow + " X" + _ansiReset)
//                    else -> print(_ansiGreen + " O" + _ansiReset)
//                }
//            }
//            if (_history.length >= 18 && eighteen == true) {
//                print("\t\t$round_counter. ")
//                print("" + _history[16] + _history[17])
//                eighteen = false
//                round_counter++
//            }
//            if (_history.length >= 20 && twenty == true) {
//                print("\t\t" + _history[18] + _history[19])
//                twenty = false
//            }
//        }
//        println()
//        for (j in 5..5) {
//            print(_ansiBlue + letterArray[j] + _ansiReset)
//            for (k in 0 until _size) {
//                when (_board[j][k]) {
//                    0 -> print(" -")
//                    1 -> print(_ansiYellow + " X" + _ansiReset)
//                    else -> print(_ansiGreen + " O" + _ansiReset)
//                }
//            }
//            if (_history.length >= 22 && twenty_two == true) {
//                print("\t\t$round_counter. ")
//                print("" + _history[20] + _history[21])
//                twenty_two = false
//                round_counter++
//            }
//            if (_history.length >= 24 && twenty_four == true) {
//                print("\t\t" + _history[22] + _history[23])
//                twenty_four = false
//            }
//        }
//        println()
//        for (j in 6..6) {
//            print(_ansiBlue + letterArray[j] + _ansiReset)
//            for (k in 0 until _size) {
//                when (_board[j][k]) {
//                    0 -> print(" -")
//                    1 -> print(_ansiYellow + " X" + _ansiReset)
//                    else -> print(_ansiGreen + " O" + _ansiReset)
//                }
//            }
//            if (_history.length >= 26 && twenty_six == true) {
//                print("\t\t$round_counter. ")
//                print("" + _history[24] + _history[25])
//                twenty_six = false
//                round_counter++
//            }
//            if (_history.length >= 28 && twenty_eight == true) {
//                print("\t\t" + _history[26] + _history[27])
//                twenty_eight = false
//            }
//        }
//        println()
//        for (j in 7..7) {
//            print(_ansiBlue + letterArray[j] + _ansiReset)
//            for (k in 0 until _size) {
//                when (_board[j][k]) {
//                    0 -> print(" -")
//                    1 -> print(_ansiYellow + " X" + _ansiReset)
//                    else -> print(_ansiGreen + " O" + _ansiReset)
//                }
//            }
//            if (_history.length >= 30 && thirty == true) {
//                print("\t\t$round_counter. ")
//                print("" + _history[28] + _history[29])
//                thirty = false
//                round_counter++
//            }
//            if (_history.length >= 32 && thirty_two == true) {
//                print("\t\t" + _history[30] + _history[31])
//                thirty_two = false
//            }
//        }
//        println()
//        if (_history.length >= 34 && thirty_four == true) {
//            print("\t\t\t\t$round_counter. ")
//            print("" + _history[32] + _history[33])
//            thirty_four = false
//            round_counter++
//        }
//        if (_history.length >= 36 && thirty_six == true) {
//            print("\t\t" + _history[34] + _history[35])
//            thirty_six = false
//        }
//        println()
//        if (_history.length >= 38 && thirty_eight == true) {
//            print("\t\t\t\t$round_counter. ")
//            print("" + _history[36] + _history[37])
//            thirty_eight = false
//            round_counter++
//        }
//        if (_history.length >= 40 && forty == true) {
//            print("\t\t" + _history[38] + _history[39])
//            forty = false
//        }
//        println()
//        if (_history.length >= 42 && forty_two == true) {
//            print("\t\t\t\t$round_counter. ")
//            print("" + _history[40] + _history[41])
//            forty_two = false
//            round_counter++
//        }
//        if (_history.length >= 44 && forty_four == true) {
//            print("\t\t" + _history[42] + _history[43])
//            forty_four = false
//        }
//        println()
//        if (_history.length >= 46 && forty_six == true) {
//            print("\t\t\t\t$round_counter. ")
//            print("" + _history[44] + _history[45])
//            forty_six = false
//            round_counter++
//        }
//        if (_history.length >= 48 && forty_eight == true) {
//            print("\t\t" + _history[46] + _history[47])
//            forty_eight = false
//        }
//        println()
//    }

    init {
        this._time = time * 1000 - 500
    }
}