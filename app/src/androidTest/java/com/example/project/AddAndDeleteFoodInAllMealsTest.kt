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
import com.example.project.diary.DiaryActivity
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
class AddAndDeleteFoodInAllMealsTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(DiaryActivity::class.java)

    @Test
    fun addAndDeleteFoodInAllMealsTest() {
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

        val recyclerView = onView(
            allOf(
                withId(R.id.historyRecyclerView),
                childAtPosition(
                    withClassName(`is`("android.widget.LinearLayout")),
                    2
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

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

        val viewGroup = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.breakfastRecyclerView),
                        withParent(withId(R.id.breakfastLayout))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

        val appCompatTextView2 = onView(
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
        appCompatTextView2.perform(scrollTo(), click())

        val recyclerView2 = onView(
            allOf(
                withId(R.id.historyRecyclerView),
                childAtPosition(
                    withClassName(`is`("android.widget.LinearLayout")),
                    2
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val actionMenuItemView2 = onView(
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
        actionMenuItemView2.perform(click())

        val viewGroup2 = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.lunchRecyclerView),
                        withParent(withId(R.id.lunchLayout))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup2.check(matches(isDisplayed()))

        val appCompatTextView3 = onView(
            allOf(
                withId(R.id.addFoodToDinnerTextView), withText("+ Add Food"),
                childAtPosition(
                    allOf(
                        withId(R.id.dinnerLayout),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            3
                        )
                    ),
                    2
                )
            )
        )
        appCompatTextView3.perform(scrollTo(), click())

        val recyclerView3 = onView(
            allOf(
                withId(R.id.historyRecyclerView),
                childAtPosition(
                    withClassName(`is`("android.widget.LinearLayout")),
                    2
                )
            )
        )
        recyclerView3.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val actionMenuItemView3 = onView(
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
        actionMenuItemView3.perform(click())

        val viewGroup3 = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.dinnerRecyclerView),
                        withParent(withId(R.id.dinnerLayout))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup3.check(matches(isDisplayed()))

        val appCompatTextView4 = onView(
            allOf(
                withId(R.id.addFoodToSnacksTextView), withText("+ Add Food"),
                childAtPosition(
                    allOf(
                        withId(R.id.snacksLayout),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            4
                        )
                    ),
                    2
                )
            )
        )
        appCompatTextView4.perform(scrollTo(), click())

        val recyclerView4 = onView(
            allOf(
                withId(R.id.historyRecyclerView),
                childAtPosition(
                    withClassName(`is`("android.widget.LinearLayout")),
                    2
                )
            )
        )
        recyclerView4.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val actionMenuItemView4 = onView(
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
        actionMenuItemView4.perform(click())

        val viewGroup4 = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.snacksRecyclerView),
                        withParent(withId(R.id.snacksLayout))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup4.check(matches(isDisplayed()))

        val recyclerView5 = onView(
            allOf(
                withId(R.id.snacksRecyclerView),
                childAtPosition(
                    withId(R.id.snacksLayout),
                    1
                )
            )
        )
        recyclerView5.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val actionMenuItemView5 = onView(
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
        actionMenuItemView5.perform(click())

        val recyclerView6 = onView(
            allOf(
                withId(R.id.dinnerRecyclerView),
                childAtPosition(
                    withId(R.id.dinnerLayout),
                    1
                )
            )
        )
        recyclerView6.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val actionMenuItemView6 = onView(
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
        actionMenuItemView6.perform(click())

        val recyclerView7 = onView(
            allOf(
                withId(R.id.lunchRecyclerView),
                childAtPosition(
                    withId(R.id.lunchLayout),
                    1
                )
            )
        )
        recyclerView7.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val actionMenuItemView7 = onView(
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
        actionMenuItemView7.perform(click())

        val recyclerView8 = onView(
            allOf(
                withId(R.id.breakfastRecyclerView),
                childAtPosition(
                    withId(R.id.breakfastLayout),
                    1
                )
            )
        )
        recyclerView8.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val actionMenuItemView8 = onView(
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
        actionMenuItemView8.perform(click())
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
