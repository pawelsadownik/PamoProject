package com.pamo.iparish;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.pamo.iparish.home.HomeActivity;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.pamo.iparish.TestMiscs.TEST_USER_LOGIN;
import static com.pamo.iparish.TestMiscs.TEST_USER_PASSWORD;
import static com.pamo.iparish.TestMiscs.childAtPosition;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NavigationTests {

    private ViewInteraction tvWelcome,
            buttonQuickOffering,
            buttonPrayer,
            buttonCustomOffering,
            overflowMenuButton,
            menuSettings;

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_COARSE_LOCATION");

    @BeforeClass
    public static void auth() {
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().getContext());
        FirebaseAuth.getInstance().signInWithEmailAndPassword(TEST_USER_LOGIN, TEST_USER_PASSWORD);
    }

    @Before
    public void setUp() {

        tvWelcome = onView(
                allOf(withId(R.id.textview_first),
                        isDisplayed()));
        buttonQuickOffering = onView(
                allOf(withId(R.id.button_quick_offering),
                        isDisplayed()));
        buttonPrayer = onView(
                allOf(withId(R.id.button_prayer),
                        isDisplayed()));
        buttonCustomOffering = onView(
                allOf(withId(R.id.button_custom_offering),
                        isDisplayed()));
        menuSettings = onView(
                allOf(withId(R.id.title), withText("Settings"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        overflowMenuButton = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                1),
                        isDisplayed()));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void amILoggedInTest() {
        tvWelcome.check(matches(isDisplayed()));
    }

    @Test
    public void settingsTest() {
        overflowMenuButton.perform(click());

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        menuSettings.perform(click());

        ViewInteraction button = onView(allOf(
                withId(R.id.button_user_form),
                isDisplayed()));
        button.check(matches(isDisplayed()));
    }

    @Test
    public void prayersTest() {
        buttonPrayer.perform(click());

        ViewInteraction editTextPrayer = onView(allOf(
                withId(R.id.prayer),
                isDisplayed()));
        editTextPrayer.check(matches(isDisplayed()));
    }

    @Test
    public void quickOfferingTest() {
        buttonQuickOffering.perform(click());

        ViewInteraction buttonPay = onView(allOf(
                withId(R.id.pay),
                isDisplayed()));
        buttonPay.check(matches(isDisplayed()));

        buttonPay.perform(click());

        ViewInteraction tvAmount = onView(allOf(
                withId(R.id.payment_amount),
                isDisplayed()));
        tvAmount.check(matches(isDisplayed()));
    }

    @Test
    public void customOfferingsTest() {
        buttonCustomOffering.perform(click());

        ViewInteraction buttonConfession = onView(allOf(
                withId(R.id.confession),
                isDisplayed()));
        buttonConfession.check(matches(isDisplayed()));

        buttonConfession.perform(click());

        ViewInteraction tvAmount = onView(allOf(
                withId(R.id.payment_amount),
                isDisplayed()));
        tvAmount.check(matches(isDisplayed()));
    }

    @Test
    public void isSatanInsideYouTest() {
        overflowMenuButton.perform(click());

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        menuSettings.perform(click());

        ViewInteraction button = onView(allOf(
                withId(R.id.dark_mode),
                isDisplayed()));
        button.perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction satan = onView(allOf(
                withId(R.id.gifInit),
                isDisplayed()));
        satan.check(matches(isDisplayed()));
    }
}
