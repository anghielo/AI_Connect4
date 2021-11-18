// Dany Flores, Umer Amin, Chanrady Ho, Dante Martinez, Thongsavik Sirivong
// Kotlin Language
// Capstone: Real-World Project Scenario
// 16 November, 2021

class Board constructor(time: Long) : MyBoard() {
    private val _size = 8
    private val _maxDepth = 10
    private val _time: Long
    private var _startTime: Long = 0
    private var _alpha = 0
    private var _beta = 0
    private var _occupiedTiles = HashMap<Int, String>()

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

            sum += if (emptyCount > 0) {
                when (moveCount) {
                    4 -> 1000000
                    3 -> if (emptyCount == 2) 100000 else 50000
                    2 -> if (emptyCount == 2) 30000 else 5000
                    1 -> if (emptyCount == 2) 100 else 10
                    else -> 0
                }
            }
            else 0

            /*
            sum += if (moveCount > 1) {
                if (emptyCount >= 1 && moveCount >= 3) 10000 * emptyCount * moveCount
                else if (emptyCount == 2 && moveCount == 2) 5000 * emptyCount * moveCount
                    else 500 * emptyCount * moveCount
            } else emptyCount
            */
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

            sum += if (emptyCount > 0) {
                when (moveCount) {
                    4 -> 1000000
                    3 -> if (emptyCount == 2) 100000 else 50000
                    2 -> if (emptyCount == 2) 30000 else 5000
                    1 -> if (emptyCount == 2) 100 else 10
                    else -> 0
                }
            }
            else 0

            /*
            sum += if (moveCount > 1) {
                if (emptyCount >= 1 && moveCount >= 3) 10000 * emptyCount * moveCount
                else if (emptyCount == 2 && moveCount == 2) 5000 * emptyCount * moveCount
                else 500 * emptyCount * moveCount
            }
            else emptyCount
            */
            if (board[i][j] == 2) sum *= -2
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
        var best = Int.MIN_VALUE
        val depth = _maxDepth
        var score: Int
        var mi = 0
        var mj = 0
        startTimer()
        for (i in 0 until _size) {
            for (j in 0 until _size) {
                if (board[i][j] == 0) {
                    val evalValue = 0
                    _alpha = Int.MIN_VALUE
                    _beta = Int.MAX_VALUE
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
        history = history + c + (mj + 1)
        return isGameOver(mi, mj, 1)
    }

    private fun min(depth: Int, row: Int, col: Int, e_value: Int): Int {
        var best = Int.MAX_VALUE
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
        var best = Int.MIN_VALUE
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

    private fun startTimer() {
        _startTime = System.currentTimeMillis()
    }

    init {
        this._time = time * 1000 - 500
    }
}
