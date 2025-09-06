package nl.w8mr.parsek.text

import kotlin.test.Test
import kotlin.test.assertEquals
import nl.w8mr.parsek.*
import nl.w8mr.parsek.text.AocTests.CellType.*
import nl.w8mr.parsek.text.AocTests.Direction.*
import kotlin.js.JsName

class AocTests {
    @Test
    @JsName("Aoc202507")
    fun `aoc2025-07`() {
        val example = """190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20
"""
        val parser = some(number and ": " and (number sepBy ' ') and '\n')
        val parsed = parser(CharSequenceContext(example))
        assertEquals(
            listOf(
                190 to listOf(10, 19),
                3267 to listOf(81, 40, 27),
                83 to listOf(17, 5),
                156 to listOf(15, 6),
                7290 to listOf(6, 8, 6, 15),
                161011 to listOf(16, 10, 13),
                192 to listOf(17, 8, 14),
                21037 to listOf(9, 7, 18, 13),
                292 to listOf(11, 6, 16, 20)
            ), parsed
        )
    }

    @Test
    @JsName("Aoc202513")
    fun `aoc2025-13`() {
        val example = """Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279
"""

        val buttonA = "Button A: X+" and number and ", Y+" and number and '\n'
        val buttonB = "Button B: X+" and number and ", Y+" and number and '\n'
        val prize = "Prize: X=" and number and ", Y=" and number and '\n'
        val behavior = buttonA and buttonB and prize
        val parser = behavior sepBy '\n'
        val parsed = parser(CharSequenceContext(example))
        assertEquals(
            listOf(
                ((94 to 34) to (22 to 67) to (8400 to 5400)),
                ((26 to 66) to (67 to 21) to (12748 to 12176)),
                ((17 to 86) to (84 to 37) to (7870 to 6450)),
                ((69 to 23) to (27 to 71) to (18641 to 10279)),
            ), parsed
        )
    }

    @Test
    @JsName("Aoc202514")
    fun `aoc2025-14`() {
        val example = """p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3
"""

        val vec2 = signedNumber and ',' and signedNumber
        val parser = "p=" and vec2 and " v=" and vec2 sepBy '\n'
        val parsed = parser(CharSequenceContext(example))
        assertEquals(
            listOf(
                (0 to 4) to (3 to -3),
                (6 to 3) to (-1 to -3),
                (10 to 3) to (-1 to 2),
                (2 to 0) to (2 to -1),
                (0 to 0) to (1 to 3),
                (3 to 0) to (-2 to -2),
                (7 to 6) to (-1 to -3),
                (3 to 0) to (-1 to -2),
                (9 to 3) to (2 to 3),
                (7 to 3) to (-1 to 2),
                (2 to 4) to (2 to -3),
                (9 to 5) to (-3 to -3),
            ), parsed
        )

    }

    enum class CellType {
        Wall, Empty, Robot, Box
    }

    enum class Direction {
        North, East, South, West
    }

    @Test
    @JsName("Aoc202515")
    fun `aoc2025-15`() {
        val smallExample = """|########
                              |#..O.O.#
                              |##@.O..#
                              |#...O..#
                              |#.#.O..#
                              |#...O..#
                              |#......#
                              |########
                              |
                              |<^^>>>vv<v>>v<<
                              |""".trimMargin()

        val cell = oneOf(
            '#' value Wall,
            '.' value Empty,
            '@' value Robot,
            'O' value Box,
        )
        val instruction = oneOf(
            '^' value North,
            '>' value East,
            'v' value South,
            '<' value West,
        )
        val grid = some(some(cell) and '\n') and '\n'
        val instructions = some(instruction) sepBy '\n' map { it.flatten() }
        val parser = grid and instructions

        val parsed = parser(CharSequenceContext(smallExample))
        assertEquals(listOf(
            listOf(Wall, Wall, Wall, Wall, Wall, Wall, Wall, Wall),
            listOf(Wall, Empty, Empty, Box, Empty, Box, Empty, Wall),
            listOf(Wall, Wall, Robot, Empty, Box, Empty, Empty, Wall),
            listOf(Wall, Empty, Empty, Empty, Box, Empty, Empty, Wall),
            listOf(Wall, Empty, Wall, Empty, Box, Empty, Empty, Wall),
            listOf(Wall, Empty, Empty, Empty, Box, Empty, Empty, Wall),
            listOf(Wall, Empty, Empty, Empty, Empty, Empty, Empty, Wall),
            listOf(Wall, Wall, Wall, Wall, Wall, Wall, Wall, Wall),
        ), parsed.first)
        assertEquals(listOf(West, North, North, East, East, East, South, South, West, South, East, East, South, West, West), parsed.second)
        println(parsed)
    }

    data class State(val a: Long, val b: Long, val c: Long)
    @Test
    @JsName("Aoc202518")
    fun `aoc2025-18`() {
        val example="""|Register A: 729
                       |Register B: 0
                       |Register C: 0
                       |
                       |Program: 0,1,5,4,3,0
                       |""".trimMargin()

        fun register(which: Char) = "Register $which: " and longNumber and "\n"
        val registers = register('A') and register('B') and register('C') map (::State)
        val program = "Program: " and ((number and ',' and number) sepBy ",")
        val parser = registers and '\n' and program
        val parsed = parser(CharSequenceContext(example))
        assertEquals(State(729, 0, 0), parsed.first)
        assertEquals(listOf(0 to 1, 5 to 4, 3 to 0), parsed.second)
    }

    enum class Operation(val op: (a:Int, b: Int) -> Int) {
        XOR(Int::xor),
        AND(Int::and),
        OR(Int::or)
    }
    data class Gate(val wire1: String, val op: Operation, val wire2: String, val outWire: String)

    @Test
    @JsName("Aoc202524")
    fun `aoc2025-24`() {
        val example="""|x00: 1
                       |x01: 0
                       |x02: 1
                       |x03: 1
                       |x04: 0
                       |y00: 1
                       |y01: 1
                       |y02: 1
                       |y03: 1
                       |y04: 1
                       |
                       |ntg XOR fgs -> mjb
                       |y02 OR x01 -> tnw
                       |kwq OR kpj -> z05
                       |x00 OR x03 -> fst
                       |tgd XOR rvg -> z01
                       |vdt OR tnw -> bfw
                       |bfw AND frj -> z10
                       |ffh OR nrd -> bqk
                       |y00 AND y03 -> djm
                       |y03 OR y00 -> psh
                       |bqk OR frj -> z08
                       |tnw OR fst -> frj
                       |gnj AND tgd -> z11
                       |bfw XOR mjb -> z00
                       |x03 OR x00 -> vdt
                       |gnj AND wpb -> z02
                       |x04 AND y00 -> kjc
                       |djm OR pbm -> qhw
                       |nrd AND vdt -> hwm
                       |kjc AND fst -> rvg
                       |y04 OR y02 -> fgs
                       |y01 AND x02 -> pbm
                       |ntg OR kjc -> kwq
                       |psh XOR fgs -> tgd
                       |qhw XOR tgd -> z09
                       |pbm OR djm -> kpj
                       |x03 XOR y03 -> ffh
                       |x00 XOR y04 -> ntg
                       |bfw OR bqk -> z06
                       |nrd XOR fgs -> wpb
                       |frj XOR qhw -> z04
                       |bqk OR frj -> z07
                       |y03 OR x01 -> nrd
                       |hwm AND bqk -> z03
                       |tgd XOR rvg -> z12
                       |tnw OR pbm -> gnj
                       |""".trimMargin()


        val wire = anyChar * 3
        val inp = wire and ": " and number and '\n'
        val inputs = some(inp)
        val operation = oneOf("XOR" value Operation.XOR, "AND" value Operation.AND, "OR" value Operation.OR )
        val gate = wire and " " and operation and " " and wire and " -> " and wire map (::Gate)
        val gatesParser = (gate sepBy "\n").map { it.map { it.outWire to it}.toMap() }
        val parser = inputs and "\n" and gatesParser
        println(parser(CharSequenceContext(example)))
    }
}
