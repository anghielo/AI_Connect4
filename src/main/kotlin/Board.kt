// Dany Flores, Umer Amin, Chanrady Ho, Dante Martinez, Thongsavik Sirivong
// Kotlin Language
// Capstone: Real-World Project Scenario
// 16 November, 2021

class Board internal constructor(time: Long) {
    private val ANSI_BLUE = "\u001B[34m"
    private val ANSI_GREEN = "\u001B[32m"
    private val ANSI_YELLOW = "\u001B[33m"
    private val ANSI_RESET = "\u001B[0m"
    private val SIZE = 16
    private val MAXDEPTH = 10
    private val ai_connect4: Array<IntArray>
    private val win_number = 4
    private val time: Long
    private var startTime: Long = 0
    private var alpha = 0
    private var beta = 0
    private val occupied_tiles = HashMap<Int, String>()
    private var is_computer_first = false
    private var first_move = 0
    private var history = ""
    private fun checkLegalMove(i: Int, j: Int): Int {
        return if (i < SIZE && i >= 0 && j < SIZE && j >= 0) {
            if (ai_connect4[i][j] == 0) 0 else 1
        } else -1
    }

    fun getAMove(i: Int, j: Int): Boolean {
        return when (checkLegalMove(i, j)) {
            0 -> {
                ai_connect4[i][j] = 2
                occupied_tiles[i * SIZE + j] = "" + i + j
                val convert = 65 + i
                val c = convert.toChar()
                history = history + c + (j + 1)
                //System.out.println("\nThis is the history: " + history);
                printBoard()
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
        var explored_tiles: HashMap<Int, String> = HashMap<Int, String>()
        var eval_value = 0
        for ((key, value) in occupied_tiles) {
            if (explored_tiles.containsKey(key)) {
                continue
            }
            val i = value[0].toInt() - 48
            val j = value[1].toInt() - 48
            var sum = 0
            var left = true
            var right = true
            var k = 1
            var move_count = 1
            var empty_count = 0
            while (left == true || right == true) {
                if (j - k < 0) left = false
                if (j + k == SIZE) right = false
                if (left == true) {
                    if (ai_connect4[i][j - k] == ai_connect4[i][j]) {
                        move_count++
                        if (!explored_tiles.containsKey(i * SIZE + (j - k))) {
                            explored_tiles[i * SIZE + (j - k)] = "" + i + (j - k)
                        }
                    } else {
                        if (ai_connect4[i][j - k] == 0) {
                            empty_count++
                        }
                        left = false
                    }
                }
                if (right == true) {
                    if (ai_connect4[i][j + k] == ai_connect4[i][j]) {
                        move_count++
                        if (!explored_tiles.containsKey(i * SIZE + (j + k))) {
                            explored_tiles[i * SIZE + (j + k)] = "" + i + (j + k)
                        }
                    } else {
                        if (ai_connect4[i][j + k] == 0) {
                            empty_count++
                        }
                        right = false
                    }
                }
                k++
            }
            sum += if (move_count > 1) {
                if (empty_count >= 1 && move_count >= 3) 500 * empty_count * move_count
                else if (empty_count == 2 && move_count == 2) 150 * empty_count * move_count
                    else 50 * empty_count * move_count
            } else {
                empty_count
            }
            var up = true
            var down = true
            k = 1
            move_count = 1
            empty_count = 0
            while (up == true || down == true) {
                if (i - k < 0) down = false
                if (i + k == SIZE) up = false
                if (down == true) {
                    if (ai_connect4[i - k][j] == ai_connect4[i][j]) {
                        move_count++
                        if (!explored_tiles.containsKey((i - k) * SIZE + j)) {
                            explored_tiles[(i - k) * SIZE + j] = "" + (i - k) + j
                        }
                    } else {
                        if (ai_connect4[i - k][j] == 0) {
                            empty_count++
                        }
                        down = false
                    }
                }
                if (up == true) {
                    if (ai_connect4[i + k][j] == ai_connect4[i][j]) {
                        move_count++
                        if (!explored_tiles.containsKey((i + k) * SIZE + j)) {
                            explored_tiles[(i + k) * SIZE + j] = "" + (i + k) + j
                        }
                    } else {
                        if (ai_connect4[i + k][j] == 0) {
                            empty_count++
                        }
                        up = false
                    }
                }
                k++
            }
            sum += if (move_count > 1) {
                if (empty_count >= 1 && move_count >= 3) 400 * empty_count * move_count
                else if (empty_count == 2 && move_count == 2) 150 * empty_count * move_count
                    else 50 * empty_count * move_count
            }
            else {empty_count}
            if (ai_connect4[i][j] == 2) {
                sum = sum * -2
            }
            eval_value += sum
            explored_tiles[i * SIZE + j] = "" + i + j
        }
        return if (is_min == true) {
            eval_value
        }
        else {
            eval_value * -1
        }
    }

    fun makeMove() {
        var best = -200000
        val depth = MAXDEPTH
        var score: Int
        var mi = 0
        var mj = 0
        startTimer()
        for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                if (ai_connect4[i][j] == 0) {
                    val eval_value = 0
                    alpha = -200000
                    beta = 200000
                    ai_connect4[i][j] = 1 // make move on board
                    occupied_tiles[i * SIZE + j] = "" + i + j
                    score = min(depth - 1, i, j, eval_value)
                    if (score > best) {
                        mi = i
                        mj = j
                        best = score
                    }
                    ai_connect4[i][j] = 0 // undo move
                    occupied_tiles.remove(i * SIZE + j)
                }
            }
        }
        ai_connect4[mi][mj] = 1
        occupied_tiles[mi * SIZE + mj] = "" + mi + mj
        val convert = 65 + mi
        val c = convert.toChar()
        history = history + c + (mj + 1)
        //System.out.println("\nThis is the history: " + history);
        printBoard()
        //System.out.println("\nTime taken: " + (System.currentTimeMillis() - startTime)/1000 + " secs\n");
        gameOver(mi, mj, 1)
    }

    private fun min(depth: Int, row: Int, col: Int, e_value: Int): Int {
        var best = 200000
        var score: Int
        val temp = check4Winner(row, col, 1)
        if (temp != 0) return temp
        if (System.currentTimeMillis() - startTime > time) return evaluate(true)
        if (depth == 0) return evaluate(true)
        for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                if (ai_connect4[i][j] == 0) {
                    ai_connect4[i][j] = 2
                    score = max(depth - 1, i, j, e_value)
                    occupied_tiles[i * SIZE + j] = "" + i + j
                    if (score < best) best = score
                    ai_connect4[i][j] = 0 // undo move
                    occupied_tiles.remove(i * SIZE + j)
                    if (best <= alpha) return best else {
                        if (best < beta) beta = best
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
        if (System.currentTimeMillis() - startTime > time) return evaluate(false)
        if (depth == 0) return evaluate(false)
        for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                if (ai_connect4[i][j] == 0) {
                    ai_connect4[i][j] = 1
                    occupied_tiles[i * SIZE + j] = "" + i + j
                    score = min(depth - 1, i, j, e_value)
                    if (score > best) best = score
                    ai_connect4[i][j] = 0 // undo move
                    occupied_tiles.remove(i * SIZE + j)
                    if (best >= beta) return best else {
                        if (best > alpha) alpha = best
                    }
                }
            }
        }
        return best
    }

    private fun check4Winner(row: Int, col: Int, player: Int): Int {
        if (checkRow(row, col) == win_number) return if (player == 1) 50000 else -50000
        if (checkCol(row, col) == win_number) return if (player == 1) 50000 else -50000
        /*
        if (checkLeftDiag(row, col) == win_number)
            return (player == 1)? 5000 : -5000;
        if (checkRightDiag(row, col) == win_number)
            return (player == 1)? 5000 : -5000;
        */for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                if (ai_connect4[i][j] == 0) return 0
            }
        }
        return -1
    }

    private fun checkRow(i: Int, j: Int): Int {
        var count = 1
        var left = true
        var right = true
        var k = 1
        while (left == true || right == true) {
            if (j - k < 0) left = false
            if (j + k == SIZE) right = false
            if (left == true) {
                if (ai_connect4[i][j - k] == ai_connect4[i][j]) count++ else left = false
            }
            if (right == true) {
                if (ai_connect4[i][j + k] == ai_connect4[i][j]) count++ else right = false
            }
            k++
        }
        return count
    }

    private fun checkCol(i: Int, j: Int): Int {
        var count = 1
        var up = true
        var down = true
        var k = 1
        while (up == true || down == true) {
            if (i - k < 0) down = false
            if (i + k == SIZE) up = false
            if (down == true) {
                if (ai_connect4[i - k][j] == ai_connect4[i][j]) count++ else down = false
            }
            if (up == true) {
                if (ai_connect4[i + k][j] == ai_connect4[i][j]) count++ else up = false
            }
            k++
        }
        return count
    }

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
    fun gameOver(i: Int, j: Int, player: Int) {
        if (check4Winner(i, j, player) == 50000) {
            println("Computer wins!\n")
            System.exit(0)
        }
        if (check4Winner(i, j, player) == -50000) {
            println("Human wins!\n")
            System.exit(0)
        }
        if (check4Winner(i, j, player) == 1) {
            println("Draw!\n")
            System.exit(0)
        }
    }

    fun setFirst(is_computer_first: Boolean) {
        this.is_computer_first = is_computer_first
    }

    private fun startTimer() {
        startTime = System.currentTimeMillis()
    }

    private fun printBoard() {
        val letters = "ABCDEFGH"
        val letterArray = letters.toCharArray()
        println()
        print(" ")
        for (i in 1..SIZE) print(ANSI_BLUE + " " + i + ANSI_RESET)
        if (first_move == 0 && is_computer_first == false) {
            print("\t\t Move List")
            first_move++
        }
        else if (first_move != 0 && is_computer_first) print("\t\t Computer vs. Opponent")
        else if (first_move != 0 && is_computer_first == false) print("\t\t Opponent vs. Computer")
        var two = true
        var four = true
        var six = true
        var eight = true
        var ten = true
        var twelve = true
        var fourteen = true
        var sixteen = true
        var eighteen = true
        var twenty = true
        var twenty_two = true
        var twenty_four = true
        var twenty_six = true
        var twenty_eight = true
        var thirty = true
        var thirty_two = true
        var thirty_four = true
        var thirty_six = true
        var thirty_eight = true
        var forty = true
        var forty_two = true
        var forty_four = true
        var forty_six = true
        var forty_eight = true
        var round_counter = 1
        print("\n")
        for (j in 0..0) {
            print(ANSI_BLUE + letterArray[j] + ANSI_RESET)
            for (k in 0 until SIZE) {
                when (ai_connect4[j][k]) {
                    0 -> print(" -")
                    1 -> print(ANSI_YELLOW + " X" + ANSI_RESET)
                    else -> print(ANSI_GREEN + " O" + ANSI_RESET)
                }
            }
            if (history.length >= 2 && two == true) {
                print("\t\t$round_counter. ")
                print("" + history[0] + history[1])
                two = false
                round_counter++
            }
            if (history.length >= 4 && four == true) {
                print("\t\t" + history[2] + history[3])
                four = false
            }
        }
        println()
        for (j in 1..1) {
            print(ANSI_BLUE + letterArray[j] + ANSI_RESET)
            for (k in 0 until SIZE) {
                when (ai_connect4[j][k]) {
                    0 -> print(" -")
                    1 -> print(ANSI_YELLOW + " X" + ANSI_RESET)
                    else -> print(ANSI_GREEN + " O" + ANSI_RESET)
                }
            }
            if (history.length >= 6 && six == true) {
                print("\t\t$round_counter. ")
                print("" + history[4] + history[5])
                six = false
                round_counter++
            }
            if (history.length >= 8 && eight == true) {
                print("\t\t" + history[6] + history[7])
                eight = false
            }
        }
        println()
        for (j in 2..2) {
            print(ANSI_BLUE + letterArray[j] + ANSI_RESET)
            for (k in 0 until SIZE) {
                when (ai_connect4[j][k]) {
                    0 -> print(" -")
                    1 -> print(ANSI_YELLOW + " X" + ANSI_RESET)
                    else -> print(ANSI_GREEN + " O" + ANSI_RESET)
                }
            }
            if (history.length >= 10 && ten == true) {
                print("\t\t$round_counter. ")
                print("" + history[8] + history[9])
                ten = false
                round_counter++
            }
            if (history.length >= 12 && twelve == true) {
                print("\t\t" + history[10] + history[11])
                twelve = false
            }
        }
        println()
        for (j in 3..3) {
            print(ANSI_BLUE + letterArray[j] + ANSI_RESET)
            for (k in 0 until SIZE) {
                when (ai_connect4[j][k]) {
                    0 -> print(" -")
                    1 -> print(ANSI_YELLOW + " X" + ANSI_RESET)
                    else -> print(ANSI_GREEN + " O" + ANSI_RESET)
                }
            }
            if (history.length >= 14 && fourteen == true) {
                print("\t\t$round_counter. ")
                print("" + history[12] + history[13])
                fourteen = false
                round_counter++
            }
            if (history.length >= 16 && sixteen == true) {
                print("\t\t" + history[14] + history[15])
                sixteen = false
            }
        }
        println()
        for (j in 4..4) {
            print(ANSI_BLUE + letterArray[j] + ANSI_RESET)
            for (k in 0 until SIZE) {
                when (ai_connect4[j][k]) {
                    0 -> print(" -")
                    1 -> print(ANSI_YELLOW + " X" + ANSI_RESET)
                    else -> print(ANSI_GREEN + " O" + ANSI_RESET)
                }
            }
            if (history.length >= 18 && eighteen == true) {
                print("\t\t$round_counter. ")
                print("" + history[16] + history[17])
                eighteen = false
                round_counter++
            }
            if (history.length >= 20 && twenty == true) {
                print("\t\t" + history[18] + history[19])
                twenty = false
            }
        }
        println()
        for (j in 5..5) {
            print(ANSI_BLUE + letterArray[j] + ANSI_RESET)
            for (k in 0 until SIZE) {
                when (ai_connect4[j][k]) {
                    0 -> print(" -")
                    1 -> print(ANSI_YELLOW + " X" + ANSI_RESET)
                    else -> print(ANSI_GREEN + " O" + ANSI_RESET)
                }
            }
            if (history.length >= 22 && twenty_two == true) {
                print("\t\t$round_counter. ")
                print("" + history[20] + history[21])
                twenty_two = false
                round_counter++
            }
            if (history.length >= 24 && twenty_four == true) {
                print("\t\t" + history[22] + history[23])
                twenty_four = false
            }
        }
        println()
        for (j in 6..6) {
            print(ANSI_BLUE + letterArray[j] + ANSI_RESET)
            for (k in 0 until SIZE) {
                when (ai_connect4[j][k]) {
                    0 -> print(" -")
                    1 -> print(ANSI_YELLOW + " X" + ANSI_RESET)
                    else -> print(ANSI_GREEN + " O" + ANSI_RESET)
                }
            }
            if (history.length >= 26 && twenty_six == true) {
                print("\t\t$round_counter. ")
                print("" + history[24] + history[25])
                twenty_six = false
                round_counter++
            }
            if (history.length >= 28 && twenty_eight == true) {
                print("\t\t" + history[26] + history[27])
                twenty_eight = false
            }
        }
        println()
        for (j in 7..7) {
            print(ANSI_BLUE + letterArray[j] + ANSI_RESET)
            for (k in 0 until SIZE) {
                when (ai_connect4[j][k]) {
                    0 -> print(" -")
                    1 -> print(ANSI_YELLOW + " X" + ANSI_RESET)
                    else -> print(ANSI_GREEN + " O" + ANSI_RESET)
                }
            }
            if (history.length >= 30 && thirty == true) {
                print("\t\t$round_counter. ")
                print("" + history[28] + history[29])
                thirty = false
                round_counter++
            }
            if (history.length >= 32 && thirty_two == true) {
                print("\t\t" + history[30] + history[31])
                thirty_two = false
            }
        }
        println()
        if (history.length >= 34 && thirty_four == true) {
            print("\t\t\t\t$round_counter. ")
            print("" + history[32] + history[33])
            thirty_four = false
            round_counter++
        }
        if (history.length >= 36 && thirty_six == true) {
            print("\t\t" + history[34] + history[35])
            thirty_six = false
        }
        println()
        if (history.length >= 38 && thirty_eight == true) {
            print("\t\t\t\t$round_counter. ")
            print("" + history[36] + history[37])
            thirty_eight = false
            round_counter++
        }
        if (history.length >= 40 && forty == true) {
            print("\t\t" + history[38] + history[39])
            forty = false
        }
        println()
        if (history.length >= 42 && forty_two == true) {
            print("\t\t\t\t$round_counter. ")
            print("" + history[40] + history[41])
            forty_two = false
            round_counter++
        }
        if (history.length >= 44 && forty_four == true) {
            print("\t\t" + history[42] + history[43])
            forty_four = false
        }
        println()
        if (history.length >= 46 && forty_six == true) {
            print("\t\t\t\t$round_counter. ")
            print("" + history[44] + history[45])
            forty_six = false
            round_counter++
        }
        if (history.length >= 48 && forty_eight == true) {
            print("\t\t" + history[46] + history[47])
            forty_eight = false
        }
        println()
    }

    init {
        this.time = time * 1000 - 500
        ai_connect4 = Array(SIZE) { IntArray(SIZE) }
        for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                ai_connect4[i][j] = 0
            }
        }
        printBoard()
    }
}