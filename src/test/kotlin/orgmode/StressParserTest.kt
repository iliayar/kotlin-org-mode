
package orgmode

import kotlin.random.Random
import kotlin.test.Test
import orgmode.parser.*

class StressParserTest {
    var timer: Long = System.nanoTime()

    fun tic() {
        timer = System.nanoTime()
    }

    fun toc(msg: String) {
        println("$msg in ${(System.nanoTime() - timer) / 1000000} ms")
    }

    fun stress(src: Source, srcRegex: Source) {
        tic()
        var regexParser = RegexOrgParser(srcRegex)
        toc("Regex Parser created")

        tic()
        var parser = OrgParser(src)
        toc("Parser created      ")

        tic()
        val regexRes = regexParser.parse()
        toc("Regex parser done   ")

        tic()
        val res = parser.parse()
        toc("Parser done         ")

        // assertEquals(regexRes, res)
    }

    @Test fun rawText() {

        var res: StringBuilder = StringBuilder("")

        for (i in 1..1000) {
            for (j in 1..Random.nextInt(10, 100)) {
                res.append(Random.nextInt(0x61, 0x7a).toChar())
            }
            if (Random.nextBoolean()) {
                if (Random.nextBoolean()) {
                    res.append("\n")
                } else {
                    res.append(" \\\\\n")
                }
            }
            res.append(" ")
        }

        stress(StringSource(res.toString()), StringSource(res.toString()))
    }

    @Test fun markupText() {

        var res: StringBuilder = StringBuilder("")

        for (i in 1..10000) {
            { markupType: String, space: Boolean ->
                if (space) res.append(" ")
                res.append(markupType)
                for (j in 1..Random.nextInt(20, 100)) {
                    res.append(Random.nextInt(0x61, 0x7a).toChar())
                }
                res.append(markupType)
                if (space) res.append(" ")
            }(
                when (Random.nextInt(1, 10)) {
                    1 -> "*"
                    2 -> "+"
                    3 -> "_"
                    4 -> "="
                    5 -> "/"
                    else -> ""
                },
                Random.nextBoolean()
            )
            if (Random.nextBoolean()) {
                if (Random.nextBoolean()) {
                    res.append("\n")
                } else {
                    res.append(" \\\\\n")
                }
            }
            res.append(" ")
        }

        stress(StringSource(res.toString()), StringSource(res.toString()))
    }
    @Test fun linksText() {

        var res: StringBuilder = StringBuilder("")

        for (i in 1..10000) {
            { link: Boolean, named: Boolean ->

                if (link) res.append("[[")
                for (j in 1..Random.nextInt(20, 100)) {
                    res.append(Random.nextInt(0x61, 0x7a).toChar())
                }
                if (link) res.append("]")
                if (link && named) {
                    res.append("[")
                    for (j in 1..Random.nextInt(20, 100)) {
                        res.append(Random.nextInt(0x61, 0x7a).toChar())
                    }
                    res.append("]")
                }
                if (link) res.append("]")
            }(Random.nextBoolean(), Random.nextBoolean())
            if (Random.nextBoolean()) {
                if (Random.nextBoolean()) {
                    res.append("\n")
                } else {
                    res.append(" \\\\\n")
                }
            }
            res.append(" ")
        }

        stress(StringSource(res.toString()), StringSource(res.toString()))
    }
    @Test fun headersTest() {

        var res: StringBuilder = StringBuilder("")

        for (q in 1..1000) {
            res.append("\n" + "*".repeat(Random.nextInt(1, 15)) + " ")
            for (j in 1..Random.nextInt(10, 50)) {
                res.append(Random.nextInt(0x61, 0x7a).toChar())
            }
            res.append("\n")
            for (i in 1..5) {
                for (j in 1..Random.nextInt(10, 20)) {
                    res.append(Random.nextInt(0x61, 0x7a).toChar())
                }
                if (Random.nextBoolean()) {
                    if (Random.nextBoolean()) {
                        res.append("\n")
                    } else {
                        res.append(" \\\\\n")
                    }
                }
                res.append(" ")
            }
        }

        stress(StringSource(res.toString()), StringSource(res.toString()))
    }
    @Test fun listsTest() {

        var res: StringBuilder = StringBuilder("")

        for (q in 1..100) {
            var indent = Random.nextInt(1, 10)
            for (i in 1..10) {
                res.append(" ".repeat(indent - 1))
                res.append("+ ")
                for (j in 1..Random.nextInt(10, 50)) {
                    res.append(Random.nextInt(0x61, 0x7a).toChar())
                }
                res.append("\n")
                for (j in 1..5) {
                    for (k in 1..Random.nextInt(10, 20)) {
                        res.append(Random.nextInt(0x61, 0x7a).toChar())
                    }
                    if (Random.nextBoolean()) {
                        if (Random.nextBoolean()) {
                            res.append("\n" + " ".repeat(indent))
                        } else {
                            res.append(" \\\\\n" + " ".repeat(indent))
                        }
                    } else {
                        res.append(" ")
                    }
                }
            }
        }

        stress(StringSource(res.toString()), StringSource(res.toString()))
    }
}
