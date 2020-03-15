package fr.thomas.lefebvre.toutougo

import android.view.View
import fr.thomas.lefebvre.toutougo.ui.MainActivity
import fr.thomas.lefebvre.toutougo.utils.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilsFormatTest {
    @Test
    fun testDateToString() {

        val date:Long=1584763200000
        assertEquals(setDateToString(date), "21/03/2020")
    }

    @Test
    fun testTimeToString(){
        val hour=10
        val minute=35
        assertEquals(setTimeToString(hour,minute),"10H35")
    }

    @Test
    fun testDistanceToString(){
        val  distance=2000.00
        assertEquals(setDistanceToString(distance),"2000m")
    }

    @Test
    fun testVisibility(){
        val boolean=true
        val visibility:Int=View.VISIBLE
        assertEquals(setVisibilityView(boolean),visibility)
    }



}
