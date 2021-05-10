package eu.livesport.rules

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Detector.UastScanner
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Scope.Companion.JAVA_FILE_SCOPE
import com.android.tools.lint.detector.api.Severity.ERROR
import com.android.tools.lint.detector.api.Severity.WARNING
import java.util.EnumSet
import org.jetbrains.uast.UAnnotated
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement

val ISSUE_MISSING_ROBOLECTRIC_CATEGORY = Issue.create(
    "MissingRobolectricCategory",
    "Checks that Classes annotated with @RunWith(RobolectricTestRunner.class) are also annotated with @Category(RobolectricTest)",
    "This categorization is important for CI setup, where slow robolectric tests are run as separate task.",
    CORRECTNESS, 10, ERROR,
    Implementation(
        MissingRobolectricAnnotationDetector::class.java,
        EnumSet.of(Scope.JAVA_FILE, Scope.TEST_SOURCES),
    )
)

class MissingRobolectricAnnotationDetector : Detector(), UastScanner {

    override fun getApplicableUastTypes() = listOf<Class<out UElement>>(UClass::class.java)

    override fun createUastHandler(context: JavaContext) = MissingAnnotationVisitor(context)

    class MissingAnnotationVisitor(private val context: JavaContext) : UElementHandler() {

        private val robolectricAnnotations = listOf(
            "Category",
            "RunWith",
        )

        override fun visitClass(node: UClass) {
            processAnnotations(node, node)
        }

        private fun processAnnotations(element: UElement, modifierListOwner: UAnnotated) {

            val ann = context.evaluator.getAllAnnotations(modifierListOwner, false)
                .filter { it.qualifiedName?.split(".")?.lastOrNull() in robolectricAnnotations }
                .map { Pair(it.qualifiedName, it.attributeValues[0].expression.toString()) }
                .distinct()

//            println("$ann")

            var correct = true
            if (ann.contains(Pair("RunWith", "RobolectricTestRunner")) && !ann.contains(Pair("Category", "RobolectricTest"))) {
                correct = false
            }

//            println("Annotations are $correct!")

            if (!correct) {

                // TODO: Quickfix
//                val fix = LintFix.create()
//                    .replace()
//                    .text(annotations.joinToString(separator = " ") { "@$it" })
//                    .with(correctOrder)
//                    .range(context.getLocation(element))
//                    .name("Robolectric Test missing category")
//                    .autoFix()
//                    .build()
//
                context.report(ISSUE_MISSING_ROBOLECTRIC_CATEGORY, element, context.getNameLocation(element), "Missing @Category(RobolectricTest.class)")
            }
        }
    }
}