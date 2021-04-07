package com.example.project


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
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
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class QuickAddTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun quickAddTest() {
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
                withId(R.id.addFoodToBreakfastTextView), withText("+ Add Food"),
                childAtPosition(
                    allOf(
                        withId(R.id.breakfastLayout),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            1
                        )
                    ),
                    2
                )
            )
        )
        appCompatTextView.perform(scrollTo(), click())

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.actionQuickAdd), withContentDescription("Quick Add"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.addFoodToolbar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.enterCalories),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.custom),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("500"), closeSoftKeyboard())

        val appCompatButton2 = onView(
            allOf(
                withId(android.R.id.button1), withText("ADD"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton2.perform(scrollTo(), click())

        val textView = onView(
            allOf(
                withId(R.id.caloriesAmount), withText("500"),
                withParent(withParent(withId(R.id.breakfastRecyclerView))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("500")))

        val recyclerView = onView(
            allOf(
                withId(R.id.breakfastRecyclerView),
                childAtPosition(
                    withId(R.id.breakfastLayout),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val editText = onView(
            allOf(
                withId(R.id.quickAddEditText), withText("500"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        editText.check(matches(withText("500")))

        val actionMenuItemView2 = onView(
            allOf(
                withId(R.id.actionQuickAddDelete), withContentDescription("Calories delete"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.quickAddInfoToolbar),
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
