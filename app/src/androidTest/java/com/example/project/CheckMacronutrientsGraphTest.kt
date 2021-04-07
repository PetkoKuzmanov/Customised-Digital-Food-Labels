package com.example.project


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CheckMacronutrientsGraphTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun checkMacronutrientsGraphTest() {
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
                withId(R.id.addFoodToLunchTextView), withText("+ Add Food"),
                childAtPosition(
                    allOf(
                        withId(R.id.lunchLayout),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            2
                        )
                    ),
                    2
                )
            )
        )
        appCompatTextView.perform(scrollTo(), click())

        val recyclerView = onView(
            allOf(
                withId(R.id.historyRecyclerView),
                childAtPosition(
                    withClassName(`is`("android.widget.LinearLayout")),
                    2
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(3, click()))

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.actionFoodInfoNext), withContentDescription("Food info"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.foodInfoToolbar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val actionMenuItemView2 = onView(
            allOf(
                withId(R.id.action_macronutrients), withContentDescription("Macros"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.diaryToolbar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView2.perform(click())

        val viewGroup = onView(
            allOf(
                withId(R.id.macronutrientsChart),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

        pressBack()

        val recyclerView2 = onView(
            allOf(
                withId(R.id.lunchRecyclerView),
                childAtPosition(
                    withId(R.id.lunchLayout),
                    1
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val actionMenuItemView3 = onView(
            allOf(
                withId(R.id.actionFoodDelete), withContentDescription("Food delete"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.foodInfoToolbar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView3.perform(click())
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
