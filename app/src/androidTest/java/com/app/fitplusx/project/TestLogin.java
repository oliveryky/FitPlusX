package com.app.fitplusx.project;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.app.fitplusx.project.ViewActivity.ViewActivityWelcome;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Instrumented test, which will execute on an Android device.
 * RUN ORDER: TestSetup -> TestLogin -> TestUpdate
 * DO NOT RUN UNIT TESTS ON TABLET, LIBRARY CAN'T SEEM TO FIND IDs WHEN RUNNING ON A TABLET
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestLogin {
    @Rule
    public ActivityTestRule<ViewActivityWelcome> mActivityRule = new ActivityTestRule(ViewActivityWelcome.class);

    @Test
    public void testLogin() {
        login();
        checkDashboardData();
        checkProfileData();
    }

    private void login() {
        onView(withId(R.id.welcomeLoginButton)).perform(click());
        onView(withId(R.id.username)).perform(typeText(TestUser.userName), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(TestUser.pw), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
    }

    private void checkDashboardData() {
        onView(allOf(withId(R.id.dashboardGoalText),
                isDescendantOfA(allOf(withId(R.id.dashboardLayout),
                        isDescendantOfA(withId(R.id.dashboardScrollView)))))).check(matches(withText(containsString(TestUser.goalType))));
        onView(allOf(withId(R.id.dashboardGoalText),
                isDescendantOfA(allOf(withId(R.id.dashboardLayout),
                        isDescendantOfA(withId(R.id.dashboardScrollView)))))).check(matches(withText(containsString(TestUser.goal))));
    }

    private void checkProfileData() {
        onView(withId(R.id.toolbarAvatar)).perform(click());
        onView(allOf(withId(R.id.userProfileName),
                isDescendantOfA(allOf(withId(R.id.userProfileLayout),
                        isDescendantOfA(withId(R.id.userProfileScrollView)))))).check(matches(withText(TestUser.name)));

        onView(allOf(withId(R.id.userProfileHeightVar),
                isDescendantOfA(allOf(withId(R.id.userProfileHeight),
                        isDescendantOfA(allOf(withId(R.id.userProfileLayout),
                                isDescendantOfA(withId(R.id.userProfileScrollView)))))))).check(matches(withText(TestUser.height)));

        onView(allOf(withId(R.id.userProfileWeightVar),
                isDescendantOfA(allOf(withId(R.id.userProfileWeight),
                        isDescendantOfA(allOf(withId(R.id.userProfileLayout),
                                isDescendantOfA(withId(R.id.userProfileScrollView)))))))).check(matches(withText(containsString(TestUser.weight + ""))));

        onView(allOf(withId(R.id.userProfileSexVar),
                isDescendantOfA(allOf(withId(R.id.userProfileSex),
                        isDescendantOfA(allOf(withId(R.id.userProfileLayout),
                                isDescendantOfA(withId(R.id.userProfileScrollView)))))))).check(matches(withText(TestUser.gender)));
    }
}