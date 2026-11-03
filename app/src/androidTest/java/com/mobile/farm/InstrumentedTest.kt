package com.mobile.farm

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule



import com.mobile.farm.tree.TreesDatabase
import com.mobile.farm.visit.VisitsDatabase

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun resetDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val treeDao = TreesDatabase.getInstance(context).treeDao()
        val visitDao = VisitsDatabase.getInstance(context).visitDao()

        runBlocking {
            treeDao.deleteAll()
            visitDao.deleteAll()
        }
    }

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.mobile.farm", appContext.packageName)
    }

    @Test
    fun mainScreenTest() {
        composeTestRule.onNodeWithText("Farm").assertIsDisplayed()
        composeTestRule.onNodeWithText("Visits").assertIsDisplayed()
        composeTestRule.onNodeWithText("Trees").assertIsDisplayed()
    }
}