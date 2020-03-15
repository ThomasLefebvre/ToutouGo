package fr.thomas.lefebvre.toutougo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import fr.thomas.lefebvre.toutougo.ui.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {


    @Rule @JvmField
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    private lateinit var mActivity:MainActivity

    @Before
    fun setUpActivity(){
        mActivity=activityTestRule.activity
    }

    @Test
    fun clickOnSettings(){
        onView(withId(R.id.imageButtonToolsUser))
            .perform(click())

        Thread.sleep(1500)

        onView(withId(R.id.button_log_out)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnPlace(){
        onView(withId(R.id.placeFragment))
            .perform(click())

        Thread.sleep(1500)

        onView(withId(R.id.button_add_place)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnEvent(){
        onView(withId(R.id.evenementFragment))
            .perform(click())

        Thread.sleep(1500)

        onView(withId(R.id.button_create_event)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnMap(){
        onView(withId(R.id.mapFragment))
            .perform(click())

        Thread.sleep(1500)

        onView(withId(R.id.button_infos)).check(matches(isDisplayed()))
    }




}
