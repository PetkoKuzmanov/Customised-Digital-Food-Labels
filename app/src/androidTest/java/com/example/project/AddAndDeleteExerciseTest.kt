package com.example.project


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddAndDeleteExerciseTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest2() {
        val appCompatEditText = onView(
            allOf(
                withId(R.id.email),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(click())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.email),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("asd@asd.com"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.password),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("12345678"), closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(
                withId(R.id.buttonLogIn), withText("Log in"),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatTextView = onView(
            allOf(
                withId(R.id.addExerciseTextView), withText("+ Add Exercise"),
                childAtPosition(
                    allOf(
                        withId(R.id.exerciseLayout),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            5
                        )
                    ),
                    2
                )
            )
        )
        appCompatTextView.perform(scrollTo(), click())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.exerciseEditText),
                childAtPosition(
                    allOf(
                        withId(R.id.addExerciseConstrainLayout),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("test exercise"), closeSoftKeyboard())

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.actionAddExercise), withContentDescription("+ Add Exercise"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.exerciseToolbar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val recyclerView = onView(
            allOf(
                withId(R.id.exerciseRecyclerView),
                childAtPosition(
                    withId(R.id.exerciseLayout),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val actionMenuItemView2 = onView(
            allOf(
                withId(R.id.actionExerciseDelete), withContentDescription("Exercise delete"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.exerciseToolbar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView2.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
