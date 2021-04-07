package com.example.project


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
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
class SearchInHistoryAndDatabaseTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun searchInHistoryAndDatabaseTest() {
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
        appCompatEditText2.perform(replaceText("qwe@qwe.com"), closeSoftKeyboard())

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

        val appCompatImageView = onView(
            allOf(
                withId(R.id.search_button), withContentDescription("Search"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_bar),
                        childAtPosition(
                            withId(R.id.foodSearchView),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val searchAutoComplete = onView(
            allOf(
                withId(R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete.perform(replaceText("pasta"), closeSoftKeyboard())

        val textView = onView(
            allOf(
                withId(R.id.foodName), withText("Pasta"),
                withParent(withParent(withId(R.id.historyRecyclerView))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Pasta")))

        val searchAutoComplete2 = onView(
            allOf(
                withId(R.id.search_src_text), withText("pasta"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete2.perform(click())

        val searchAutoComplete3 = onView(
            allOf(
                withId(R.id.search_src_text), withText("pasta"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete3.perform(replaceText("oats"))

        val searchAutoComplete4 = onView(
            allOf(
                withId(R.id.search_src_text), withText("oats"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete4.perform(closeSoftKeyboard())

        val searchAutoComplete5 = onView(
            allOf(
                withId(R.id.search_src_text), withText("oats"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete5.perform(pressImeActionButton())

        val textView2 = onView(
            allOf(
                withId(R.id.foodName), withText("Oats"),
                withParent(withParent(withId(R.id.historyRecyclerView))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Oats")))

        val appCompatImageView2 = onView(
            allOf(
                withId(R.id.search_close_btn), withContentDescription("Clear query"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView2.perform(click())
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
