// Dany Flores, Umer Amin, Chanrady Ho, Dante Martinez, Thongsavik Sirivong
// Kotlin Language
// Capstone: Real-World Project Scenario
// 16 November, 2021

class Board constructor(time: Long) : MyBoard() {
    // AI Scores:
    private val _cross4 = 1000000           // xxxx
    private val _cross3Empty2 = 100000      // xxx
    private val _cross3Empty1 = 50000       // xxxo
    private val _cross2Empty2 = 20000       // xx
    private val _cross2Empty1 = 5000        // xxo
    private val _cross1Empty2 = 1000        // x
    private val _cross1Empty1 = 100         // xo

    private val _size = 8
    private val _maxDepth = 10
    private val _time: Long
    private var _startTime: Long = 0
    private var _alpha = 0
    private var _beta = 0

    // Assign scores based on the number of connect moves and number of empty ends of the cross
    private fun score (move: Int, empty: Int): Int {
        return if (empty > 0) {
            when (move) {
                4 -> _cross4
                3 -> if (empty == 2) _cross3Empty2 else _cross3Empty1
                2 -> if (empty == 2) _cross2Empty2 else _cross2Empty1
                1 -> if (empty == 2) _cross1Empty2 else _cross1Empty1
                else -> 0
            }
        }
        else if (empty == 0 && move == 4) _cross4
        else 0
    }

    // Evaluate the score of the current entire board
    private fun evaluate(): Int {
        val exploredTiles: HashMap<Int, String> = HashMap()
        var row: Int
        var col: Int
        var evalValue = 0
        for ((key, value) in occupiedTiles) {
            if (exploredTiles.containsKey(key)) continue
            val i = value[0] - '0'
            val j = value[1] - '0'
            var sum = 0
            /*
            *   Scoring the row of tile(i, j)
            * */
            var left = true
            var right = true
            var k = 1
            var moveCount = 1
            var emptyCount = 0
            while (left || right) {
                row = i
                col = j - k
                if (col < 0) left = false
                if (left) {
                    if (board[row][col] == board[i][j]) {
                        moveCount++
                        if (!exploredTiles.containsKey(row * _size + col)) {
                            exploredTiles[row * _size + col] = "" + row + col
                        }
                    }
                    else {
                        if (board[row][col] == 0) emptyCount++
                        left = false
                    }
                }
                row = i
                col = j + k
                if (col == _size) right = false
                if (right) {
                    if (board[row][col] == board[i][j]) {
                        moveCount++
                        if (!exploredTiles.containsKey(row * _size + col)) {
                            exploredTiles[row * _size + col] = "" + row + col
                        }
                    }
                    else {
                        if (board[row][col] == 0) emptyCount++
                        right = false
                    }
                }
                k++
            }
            sum += score(moveCount, emptyCount)
            /*
            *   Scoring the column of tile(i, j)
            * */
            var down = true
            var up = true
            k = 1
            moveCount = 1
            emptyCount = 0
            while (down || up) {
                row = i - k
                col = j
                if (row < 0) up = false
                if (up) {
                    if (board[row][col] == board[i][j]) {
                            moveCount++
                        if (!exploredTiles.containsKey(row * _size + col)) {
                            exploredTiles[row * _size + col] = "" + row + col
                        }
                    } else {
                        if (board[row][col] == 0) emptyCount++
                        up = false
                    }
                }
                row = i + k
                col = j
                if (row == _size) down = false
                if (down) {
                    if (board[row][col] == board[i][j]) {
                            moveCount++
                        if (!exploredTiles.containsKey(row * _size + col)) {
                            exploredTiles[row * _size + col] = "" + row + col
                        }
                    } else {
                        if (board[row][col] == 0) emptyCount++
                        down = false
                    }
                }
                k++
            }
            sum += score(moveCount, emptyCount)
            /*
            *   Scoring the lean right diagonal of tile(i, j)
            * */
            var downLeft = true
            var upRight = true
            k = 1
            moveCount = 1
            emptyCount = 0
            while (downLeft || upRight) {
                row = i - k
                col = j + k
                if (row < 0 || col == _size) upRight = false
                if (upRight) {
                    if (board[row][col] == board[i][j]) {
                        moveCount++
                        if (!exploredTiles.containsKey(row * _size + col)) {
                            exploredTiles[row * _size + col] = "" + row + col
                        }
                    } else {
                        if (board[row][col] == 0) emptyCount++
                        upRight = false
                    }
                }
                row = i + k
                col = j - k
                if (row == _size || col < 0) downLeft = false
                if (downLeft) {
                    if (board[row][col] == board[i][j]) {
                        moveCount++
                        if (!exploredTiles.containsKey(row * _size + col)) {
                            exploredTiles[row * _size + col] = "" + row + col
                        }
                    } else {
                        if (board[row][col] == 0) emptyCount++
                        downLeft = false
                    }
                }
                k++
            }
            sum += score(moveCount, emptyCount)
            /*
            *   Scoring the lean left diagonal of tile(i, j)
            * */
            var upLeft = true
            var downRight = true
            k = 1
            moveCount = 1
            emptyCount = 0
            while (upLeft || downRight) {
                row = i - k
                col = j - k
                if (row < 0 || col < 0) downRight = false
                if (downRight) {
                    if (board[row][col] == board[i][j]) {
                        moveCount++
                        if (!exploredTiles.containsKey(row * _size + col)) {
                            exploredTiles[row * _size + col] = "" + row + col
                        }
                    } else {
                        if (board[row][col] == 0) emptyCount++
                        downRight = false
                    }
                }
                row = i + k
                col = j + k
                if (row == _size || col == _size) upLeft = false
                if (upLeft) {
                    if (board[row][col] == board[i][j]) {
                        moveCount++
                        if (!exploredTiles.containsKey(row * _size + col)) {
                            exploredTiles[row * _size + col] = "" + row + col
                        }
                    } else {
                        if (board[row][col] == 0) emptyCount++
                        upLeft = false
                    }
                }
                k++
            }
            sum += score(moveCount, emptyCount)

            if (board[i][j] == 2) sum *= -2
            evalValue += sum
            exploredTiles[i * _size + j] = "" + i + j
        }
        return evalValue
    }

    // Determine the best move for the AI by Alpha-Beta Pruning algorithm
    fun makeMove(): Boolean {
        var best = Int.MIN_VALUE
        val depth = _maxDepth
        var score: Int
        var mi = 0
        var mj = 0

        // If the AI is first, the first move by the AI is randomized to any non-edges tiles.
        if (occupiedTiles.isEmpty()) {
            mi = (1..6).random()
            mj = (1..6).random()
        }
        else {
            startTimer()
            for (i in 0 until _size) {
                for (j in 0 until _size) {
                    if (board[i][j] == 0) {
                        val evalValue = 0
                        _alpha = Int.MIN_VALUE
                        _beta = Int.MAX_VALUE
                        board[i][j] = 1 // make move on board
                        occupiedTiles[i * _size + j] = "" + i + j
                        score = min(depth - 1, i, j, evalValue)
                        if (score > best) {
                            mi = i
                            mj = j
                            best = score
                        }
                        board[i][j] = 0 // undo move
                        occupiedTiles.remove(i * _size + j)
                    }
                }
            }
        }
        board[mi][mj] = 1
        occupiedTiles[mi * _size + mj] = "" + mi + mj
        val convert = 65 + mi
        val c = convert.toChar()
        history = history + c + (mj + 1)
        return isGameOver(mi, mj, 1)
    }

    // Minimize the player score
    private fun min(depth: Int, row: Int, col: Int, e_value: Int): Int {
        var best = Int.MAX_VALUE
        var score: Int
        val temp = check4Winner(row, col, 1)
        if (temp != 0) return temp
        if (System.currentTimeMillis() - _startTime > _time) return evaluate()
        if (depth == 0) return evaluate()
        for (i in 0 until _size) {
            for (j in 0 until _size) {
                if (board[i][j] == 0) {
                    board[i][j] = 2
                    score = max(depth - 1, i, j, e_value)
                    occupiedTiles[i * _size + j] = "" + i + j
                    if (score < best) best = score
                    board[i][j] = 0 // undo move
                    occupiedTiles.remove(i * _size + j)
                    if (best <= _alpha) return best else {
                        if (best < _beta) _beta = best
                    }
                }
            }
        }
        return best
    }

    // Maximize the AI score
    private fun max(depth: Int, row: Int, col: Int, e_value: Int): Int {
        var best = Int.MIN_VALUE
        var score: Int
        val temp = check4Winner(row, col, 2)
        if (temp != 0) return temp
        if (System.currentTimeMillis() - _startTime > _time) return evaluate()
        if (depth == 0) return evaluate()
        for (i in 0 until _size) {
            for (j in 0 until _size) {
                if (board[i][j] == 0) {
                    board[i][j] = 1
                    occupiedTiles[i * _size + j] = "" + i + j
                    score = min(depth - 1, i, j, e_value)
                    if (score > best) best = score
                    board[i][j] = 0 // undo move
                    occupiedTiles.remove(i * _size + j)
                    if (best >= _beta) return best else {
                        if (best > _alpha) _alpha = best
                    }
                }
            }
        }
        return best
    }

    // Time the AI decision
    private fun startTimer() {
        _startTime = System.currentTimeMillis()
    }

    init {
        this._time = time * 1000 - 500
    }
}
