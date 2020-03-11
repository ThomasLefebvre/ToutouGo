package fr.thomas.lefebvre.toutougo

import fr.thomas.lefebvre.toutougo.utils.computeDistance
import fr.thomas.lefebvre.toutougo.utils.hourToMillis
import fr.thomas.lefebvre.toutougo.utils.minuteToMillis
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilsCalculTest {
    @Test
    fun testComputeDistance() {
        val latUser:Double=0.0
        val lngUser:Double=0.0
        val latPlace:Double=0.0
        val lngPlace:Double=0.0
        assertEquals(computeDistance(latUser,lngUser,latPlace,lngPlace), 0)
    }

    @Test
    fun testHourToMillis(){
        val hours:Int=10
        assertEquals(hourToMillis(hours),36000000)
    }

   @Test
   fun testMinuteToMillis(){
       val minute:Int=15
       assertEquals(minuteToMillis(minute),900000)
   }

}
