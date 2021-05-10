package eu.livesport.rules

import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import kotlin.test.fail
import org.junit.Test

class RobolectricAnnotationDetectorTest  {

    @Test
    fun correctAnnotationsJava() {

        val lintResult = lint()
            .allowCompilationErrors()
            .allowMissingSdk()
            .files(TestFiles.java(
                """
            package foo;

            @RunWith(RobolectricTestRunner.class)
            @Category(RobolectricTest.class)
            public class MyTest {
            }"""
            ).indented())
            .issues(ISSUE_MISSING_ROBOLECTRIC_CATEGORY)
            .run()
            .expectClean()

    }

    @Test
    fun noAnnotations() {

        val lintResult = lint()
            .allowCompilationErrors()
            .allowMissingSdk()
            .files(TestFiles.java(
                """
            package foo;

            public class MyTest {
            }"""
            ).indented())
            .issues(ISSUE_MISSING_ROBOLECTRIC_CATEGORY)
            .run()
            .expectClean()

    }

    @Test
    fun missingAnnotation() {

        val lintResult = lint()
            .allowCompilationErrors()
            .allowMissingSdk()
            .files(TestFiles.java(
                """
            package foo;

            @RunWith(RobolectricTestRunner.class)
            public class MyTest {
            }"""
            ).indented())
            .issues(ISSUE_MISSING_ROBOLECTRIC_CATEGORY)
            .run()
            .expectErrorCount(1)
    }
}