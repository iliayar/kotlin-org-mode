/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package orgmode

import java.io.File

var timer: Long = System.nanoTime()

fun tic() {
    timer = System.nanoTime()
}

fun toc() {
    println("Operation done in ${(System.nanoTime() - timer)/1000000} ms")
}


fun main(args: Array<String>) {

    var org: Org

    tic()

    if(args.size > 0) {
	org = RegexOrgParser(FileSource(args[0])).parse()
    } else {

	org = RegexOrgParser(StringSource("""
* Test
Text 1
** Section
Text 2
* Parser +markup+
Text 3
""")).parse()
    }

    println(org.toString())
    println(org.toJson())

    toc()

    File("/tmp/kt.html").writeText(org.toHtml())
}
