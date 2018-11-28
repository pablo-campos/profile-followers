package com.bypassmobile.octo;


import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ToolBarTitleTest {



	private String toolbarTitle;



	@Rule
	public ActivityTestRule<SearchActivity> mActivityTestRule = new ActivityTestRule<>(SearchActivity.class);



	@Before
	public void initValidString() {
		toolbarTitle = "Repo-Stalker";
	}



	@Test
	public void dummyTest () {

		onView(allOf(instanceOf(TextView.class),
					 withParent(withResourceName("action_bar"))))
				.check(matches(withText(toolbarTitle)));
	}

}
