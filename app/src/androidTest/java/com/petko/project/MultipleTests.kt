package com.petko.project


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.petko.project.diary.DiaryActivity
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
class MultipleTests {

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
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

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
        recyclerView2.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

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
        recyclerView3.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

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
        recyclerView4.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

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
        recyclerView5.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

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
        recyclerView6.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

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
        recyclerView7.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

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
        recyclerView8.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

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

    @Test
    fun addWeightTest() {

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.action_weight), withContentDescription("Weight"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.diaryToolbar),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val floatingActionButton = onView(
            allOf(
                withId(R.id.fab),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.enterWeight),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(click())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.enterWeight),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("66.6"), closeSoftKeyboard())

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.datePickerButton), withText("Change date"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())

        val appCompatButton3 = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton3.perform(scrollTo(), click())

        val appCompatButton4 = onView(
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
        appCompatButton4.perform(scrollTo(), click())

    }

    @Test
    fun checkMacronutrientsGraphTest() {

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
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                3,
                click()
            )
        )

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

        Espresso.pressBack()

        val recyclerView2 = onView(
            allOf(
                withId(R.id.lunchRecyclerView),
                childAtPosition(
                    withId(R.id.lunchLayout),
                    1
                )
            )
        )
        recyclerView2.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

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

    @Test
    fun diaryYesterdayAndTomorrowTest() {

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.previousDayButton), withContentDescription("Previous day"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.dateTextView), withText("Yesterday"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Yesterday")))

        val appCompatButton3 = onView(
            allOf(
                withId(R.id.nextDayButton), withContentDescription("Next day"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton3.perform(click())

        val textView2 = onView(
            allOf(
                withId(R.id.dateTextView), withText("Today"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Today")))

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.nextDayButton), withContentDescription("Next day"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton4.perform(click())

        val textView3 = onView(
            allOf(
                withId(R.id.dateTextView), withText("Tomorrow"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Tomorrow")))

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.previousDayButton), withContentDescription("Previous day"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton5.perform(click())

        val textView4 = onView(
            allOf(
                withId(R.id.dateTextView), withText("Today"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Today")))
    }

    @Test
    fun quickAddTest() {
        // Added a sleep statement to match the app's execution delay.
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
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

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

    @Test
    fun searchInHistoryAndDatabaseTest() {

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
