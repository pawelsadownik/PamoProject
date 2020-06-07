package com.pamo.iparish;

import android.content.Context;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.pamo.iparish.TestMiscs.TEST_USER_LOGIN;
import static com.pamo.iparish.TestMiscs.TEST_USER_PASSWORD;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginTests {

    private static final String TEST_NEW_USER = "@test.com";

    private ViewInteraction loginText, passwordText, buttonLogin, tvRegister, tvWelcome;
    private Random rand = new Random();

    private Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void auth() {
        FirebaseAuth.getInstance().signOut();
    }

    @Before
    public void setUp() {

        loginText = onView(
                allOf(withId(R.id.edittext_login),
                        isDisplayed()));
        passwordText = onView(
                allOf(withId(R.id.editext_password),
                        isDisplayed()));
        buttonLogin = onView(
                allOf(withId(R.id.button_login),
                        isDisplayed()));
        tvRegister = onView(
                allOf(withId(R.id.textview_register),
                        isDisplayed()));
    }

    @After
    public void tearDown() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void loginCorrectExistingUserTest() {
        loginText.perform(
                click(),
                replaceText(TEST_USER_LOGIN),
                closeSoftKeyboard());

        passwordText.perform(
                replaceText(TEST_USER_PASSWORD),
                closeSoftKeyboard());

        buttonLogin.perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tvWelcome = onView(allOf(
                withId(R.id.textview_first),
                withText(mActivityTestRule.getActivity().getString(R.string.praise_god)),
                isDisplayed()));
        tvWelcome.check(matches(isDisplayed()));
    }

    @Test
    public void registerCorrectUserTest() {

        int n = rand.nextInt(1000);

        loginText.perform(
                click(),
                replaceText(n + TEST_NEW_USER),
                closeSoftKeyboard());

        passwordText.perform(
                replaceText(TEST_USER_PASSWORD),
                closeSoftKeyboard());

        tvRegister.perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tvWelcome = onView(allOf(
                withId(R.id.textview_first),
                withText(appContext.getString(R.string.praise_god)),
                isDisplayed()));
        tvWelcome.check(matches(isDisplayed()));
    }
}
