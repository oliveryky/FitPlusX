package com.app.fitplusx.project;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.app.fitplusx.project.ViewActivity.ViewActivityWelcome;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Instrumented test, which will execute on an Android device.
 * RUN ORDER: TestSetup -> TestLogin -> TestUpdate
 * DO NOT RUN UNIT TESTS ON TABLET, LIBRARY CAN'T SEEM TO FIND IDs WHEN RUNNING ON A TABLET
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestUpdate {
    private final String name = "Gothalion",
            height = "5'10",
            gender = "Male",
            goal = "1",
            goalType = "Gain";
    private double weight = 201.0;
    @Rule
    public ActivityTestRule<ViewActivityWelcome> mActivityRule = new ActivityTestRule(ViewActivityWelcome.class);

    @Test
    public void testUpdate() {
        login();
        updateRegimen();
        checkDashboardData();
        updateProfile();
        checkProfileData();
    }

    private void login() {
        onView(withId(R.id.welcomeLoginButton)).perform(click());
        onView(withId(R.id.username)).perform(typeText(TestUser.userName), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(TestUser.pw), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
    }

    private void updateRegimen() {
        onView(allOf(withId(R.id.changeGoalButton),
                isDescendantOfA(allOf(withId(R.id.dashboardLayout),
                        isDescendantOfA(withId(R.id.dashboardScrollView)))))).perform(click());


        onView(allOf(withId(R.id.gainToggle),
                isDescendantOfA(allOf(withId(R.id.editRegimenLayout),
                        isDescendantOfA(withId(R.id.editRegimenScrollView)))))).perform(click());
        onView(allOf(withId(R.id.weightGoal),
                isDescendantOfA(allOf(withId(R.id.weeklyGoalLayout),
                        isDescendantOfA(allOf(withId(R.id.editRegimenLayout),
                                isDescendantOfA(withId(R.id.editRegimenScrollView)))))))).perform(replaceText(goal));

        onView(allOf(withId(R.id.createRegimeButton),
                isDescendantOfA(allOf(withId(R.id.editRegimenLayout),
                        isDescendantOfA(withId(R.id.editRegimenScrollView)))))).perform(click());
    }

    private void checkDashboardData() {
        onView(allOf(withId(R.id.dashboardGoalText),
                isDescendantOfA(allOf(withId(R.id.dashboardLayout),
                        isDescendantOfA(withId(R.id.dashboardScrollView)))))).check(matches(withText(containsString(goalType))));
        onView(allOf(withId(R.id.dashboardGoalText),
                isDescendantOfA(allOf(withId(R.id.dashboardLayout),
                        isDescendantOfA(withId(R.id.dashboardScrollView)))))).check(matches(withText(containsString(goal))));
    }

    private void updateProfile() {
        onView(withId(R.id.toolbarAvatar)).perform(click());
        onView(allOf(withId(R.id.userProfileEditButton),
                isDescendantOfA(allOf(withId(R.id.userProfileLayout),
                        isDescendantOfA(withId(R.id.userProfileScrollView)))))).perform(click());


        onView(withParent(TestUser.withIndex(withId(R.id.editProfileName), 1))).perform(replaceText(name));


        onView(withParent(TestUser.withIndex(withId(R.id.editProfileHeight), 1))).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(height))).perform(click());

        onView(withParent(TestUser.withIndex(withId(R.id.editProfileWeight), 1))).perform(click());
        onData(allOf(is(instanceOf(Double.class)), is(weight))).perform(click());

        onView(withParent(TestUser.withIndex(withId(R.id.editProfileSex), 1))).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(gender))).perform(click());

        onView(withParent(TestUser.withIndex(withId(R.id.editProfileButton), 0))).perform(click());

    }

    private void checkProfileData() {
        onView(allOf(withId(R.id.userProfileName),
                isDescendantOfA(allOf(withId(R.id.userProfileLayout),
                        isDescendantOfA(withId(R.id.userProfileScrollView)))))).check(matches(withText(name)));

        onView(allOf(withId(R.id.userProfileHeightVar),
                isDescendantOfA(allOf(withId(R.id.userProfileHeight),
                        isDescendantOfA(allOf(withId(R.id.userProfileLayout),
                                isDescendantOfA(withId(R.id.userProfileScrollView)))))))).check(matches(withText(height)));

        onView(allOf(withId(R.id.userProfileWeightVar),
                isDescendantOfA(allOf(withId(R.id.userProfileWeight),
                        isDescendantOfA(allOf(withId(R.id.userProfileLayout),
                                isDescendantOfA(withId(R.id.userProfileScrollView)))))))).check(matches(withText(containsString(weight + ""))));

        onView(allOf(withId(R.id.userProfileSexVar),
                isDescendantOfA(allOf(withId(R.id.userProfileSex),
                        isDescendantOfA(allOf(withId(R.id.userProfileLayout),
                                isDescendantOfA(withId(R.id.userProfileScrollView)))))))).check(matches(withText(gender)));
    }
}