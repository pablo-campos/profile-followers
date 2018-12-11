package com.bypassmobile.octo;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.bypassmobile.octo.activity.SearchActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EasyWorkFlowTest {



	private String firstUserName;



	@Rule
	public ActivityTestRule<SearchActivity> mActivityTestRule = new ActivityTestRule<>(SearchActivity.class);



	@Before
	public void initValidString() {
		firstUserName = "Pedlar";
	}



	@Test
	public void easyWorkFlowTest () {

		// Wait during 15 seconds for a view
		onView(isRoot()).perform(waitId(R.id.user_name, TimeUnit.SECONDS.toMillis(15)));

		ViewInteraction textView = onView(
				allOf(withId(R.id.user_name), withText(firstUserName),
					  childAtPosition(
							  childAtPosition(
									  withId(R.id.user_list),
									  0),
							  1),
					  isDisplayed()));
		textView.check(matches(withText(firstUserName)));
		ViewInteraction recyclerView = onView(
				allOf(
						withId(R.id.user_list),
						childAtPosition(
								withClassName(is("android.widget.LinearLayout")),
								1)));
		recyclerView.perform(actionOnItemAtPosition(0, click()));

		// Wait during 15 seconds for a view
		onView(isRoot()).perform(waitId(R.id.user_name, TimeUnit.SECONDS.toMillis(15)));

		ViewInteraction recyclerView2 = onView(
				allOf(
						withId(R.id.user_list),
						childAtPosition(
								withClassName(is("android.widget.LinearLayout")),
								1)));
		recyclerView2.perform(actionOnItemAtPosition(4, click()));

		ViewInteraction recyclerView3 = onView(
				allOf(
						withId(R.id.user_list),
						childAtPosition(
								withClassName(is("android.widget.LinearLayout")),
								1)));
		recyclerView3.perform(actionOnItemAtPosition(2, click()));

		pressBack();

		pressBack();

		pressBack();

		ViewInteraction textView2 = onView(
				allOf(withId(R.id.user_name), withText(firstUserName),
					  childAtPosition(
							  childAtPosition(
									  withId(R.id.user_list),
									  0),
							  1),
					  isDisplayed()));
		textView2.check(matches(withText(firstUserName)));
	}



	private static Matcher<View> childAtPosition (
			final Matcher<View> parentMatcher, final int position)
	{

		return new TypeSafeMatcher<View>() {
			@Override
			public void describeTo (Description description) {
				description.appendText("Child at position " + position + " in parent ");
				parentMatcher.describeTo(description);
			}



			@Override
			public boolean matchesSafely (View view) {
				ViewParent parent = view.getParent();
				return parent instanceof ViewGroup && parentMatcher.matches(parent)
						&& view.equals(((ViewGroup) parent).getChildAt(position));
			}
		};
	}



	/** Perform action of waiting for a specific view id. */
	public static ViewAction waitId(final int viewId, final long millis) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return isRoot();
			}

			@Override
			public String getDescription() {
				return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
			}

			@Override
			public void perform(final UiController uiController, final View view) {
				uiController.loopMainThreadUntilIdle();
				final long startTime = System.currentTimeMillis();
				final long endTime = startTime + millis;
				final Matcher<View> viewMatcher = withId(viewId);

				do {
					for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
						// found view with required ID
						if (viewMatcher.matches(child)) {
							return;
						}
					}

					uiController.loopMainThreadForAtLeast(50);
				}
				while (System.currentTimeMillis() < endTime);

				// timeout happens
				throw new PerformException.Builder()
						.withActionDescription(this.getDescription())
						.withViewDescription(HumanReadables.describe(view))
						.withCause(new TimeoutException())
						.build();
			}
		};
	}
}
