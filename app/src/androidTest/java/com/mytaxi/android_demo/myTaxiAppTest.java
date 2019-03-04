package com.mytaxi.android_demo;

import com.mytaxi.android_demo.activities.MainActivity;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class myTaxiAppTest {

    //Test data
    private static final String USERNAME = "crazydog335";
    private static final String PASSWORD = "venture";
    private static final String SEARCH_KEYWORD = "sa";
    private static final String DIVER = "Sarah Scott";

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = activityActivityTestRule.getActivity();

        //--Tap allow button on location access dialog, if displayed--
        // Initialize UiDevice instance
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Search for correct button in the dialog.
        UiObject button = uiDevice.findObject(new UiSelector().text("ALLOW"));
        if (button.exists() && button.isEnabled()) {
            button.click();
        }
    }

    @Test
    //case1 : when user doesn't input username
    public void case1_empty_username() {
        //Do
        onView(withId(R.id.edt_username)).perform(clearText());
        onView(withId(R.id.btn_login)).perform(click());
        //Assertion : error message is displayed
        onView(withText(R.string.message_username_empty)).check(matches(isDisplayed()));
    }

    @Test
    //case2 : when user doesn't input password
    public void case2_empty_password() {
        //Do
        onView(withId(R.id.edt_username)).perform(clearText());
        onView(withId(R.id.edt_username)).perform(typeText(USERNAME), closeSoftKeyboard());
        onView(withId(R.id.edt_password)).perform(clearText());
        onView(withId(R.id.btn_login)).perform(click());
        //Assertion : error message is displayed
        onView(withText(R.string.message_password_empty)).check(matches(isDisplayed()));
    }

    @Test
    //case3 : when user inputs wrong username or password
    public void case3_invalid_username_password() {
        //Do
        onView(withId(R.id.edt_username)).perform(clearText());
        onView(withId(R.id.edt_username)).perform(typeText(USERNAME));
        onView(withId(R.id.edt_password)).perform(typeText("1234"), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        //Assertion : error message is displayed
        onView(withText(R.string.message_username_password_invalid)).check(matches(isDisplayed()));
    }

    @Test
    //case4 : when user input correct username and password
    public void case4_login_success() {
        //Do
        onView(withId(R.id.edt_username)).perform(clearText());
        onView(withId(R.id.edt_password)).perform(clearText());
        onView(withId(R.id.edt_username)).perform(typeText(USERNAME));
        onView(withId(R.id.edt_password)).perform(typeText(PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        //Assertion : Login succeeds
        //App name is displayed at toolbar
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(ViewMatchers.withText(R.string.app_name)).check(matches(isDisplayed()));
        //Logged in username is displayed on navigator screen
        onView(ViewMatchers.withContentDescription("Open navigation drawer")).perform(click());
        onView(withId(R.id.nav_username)).check(matches(withText(USERNAME)));
        pressBack();
    }

    @Test
    //Search a driver and make a call
    public void case5_search_driver() {
        //Do
        onView(withId(R.id.textSearch)).perform(typeText(SEARCH_KEYWORD));
        onView(withText(DIVER))
                .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                .perform(click());
        //Assertion : Searched driver name is displayed on driver screen
        onView(withText(R.string.title_activity_driver_profile)).check(matches(isDisplayed()));
        onView(withId(R.id.textViewDriverName)).check(matches(withText(DIVER)));
        //click the call button
        onView(withId(R.id.fab)).perform(click());
    }

    @After
    public void tearDown() {
        activity.finish();
    }

}
