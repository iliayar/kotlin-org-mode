/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package orgmode

import java.io.File

fun main(args: Array<String>) {

    var org: Org

    if (args.size > 0) {
        org = RegexOrgParser(FileSource(args[0])).parse()
    } else {

        org = RegexOrgParser(
            StringSource(
                """
* Test blocks
#+BEGIN_SRC
test
code
#+END_SRC
"""
            )
        ).parse()
    }

    println(org.toString())
    println(org.toJson())
    File("/tmp/kt.html").writeText(org.toHtml())
}
