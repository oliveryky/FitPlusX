package com.app.fitplusx.project;

import android.widget.DatePicker;

import com.app.fitplusx.project.ViewActivity.ViewActivityWelcome;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
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
public class TestSetup {
    @Rule
    public ActivityTestRule<ViewActivityWelcome> mActivityRule = new ActivityTestRule(ViewActivityWelcome.class);

    @Test
    public void testSignup() {
        signUp();
        setupProfile();
        setupRegiment();
    }

    private void signUp() {
        onView(withId(R.id.welcomeSignUpButton)).perform(click());
        onView(withId(R.id.username)).perform(typeText(TestUser.userName), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(TestUser.pw), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
    }

    private void setupProfile() {
        onView(withParent(TestUser.withIndex(withId(R.id.editProfileName), 1))).perform(typeText(TestUser.name));
        Espresso.pressBack();
        onView(withParent(TestUser.withIndex(withId(R.id.editProfileBirthday), 1))).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(1996, 6, 6));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withParent(TestUser.withIndex(withId(R.id.editProfileHeight), 1))).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(TestUser.height))).perform(click());

        onView(withParent(TestUser.withIndex(withId(R.id.editProfileWeight), 1))).perform(click());
        onData(allOf(is(instanceOf(Double.class)), is(TestUser.weight))).perform(click());

        onView(withParent(TestUser.withIndex(withId(R.id.editProfileSex), 1))).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(TestUser.gender))).perform(click());

        onView(withParent(TestUser.withIndex(withId(R.id.editProfileButton), 0))).perform(click());
    }

    private void setupRegiment() {
        onView(allOf(withId(R.id.loseToggle),
                isDescendantOfA(allOf(withId(R.id.editRegimenLayout),
                        isDescendantOfA(withId(R.id.editRegimenScrollView)))))).perform(click());
        onView(allOf(withId(R.id.weightGoal),
                isDescendantOfA(allOf(withId(R.id.weeklyGoalLayout),
                        isDescendantOfA(allOf(withId(R.id.editRegimenLayout),
                                isDescendantOfA(withId(R.id.editRegimenScrollView)))))))).perform(replaceText(TestUser.goal));

        onView(allOf(withId(R.id.createRegimeButton),
                isDescendantOfA(allOf(withId(R.id.editRegimenLayout),
                        isDescendantOfA(withId(R.id.editRegimenScrollView)))))).perform(click());
    }
}
