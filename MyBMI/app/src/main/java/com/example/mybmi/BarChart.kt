package com.example.mybmi

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import java.util.*

class BarChart(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint()
    private val paintText = Paint()
    private val gson = Gson()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paintText.textSize = 50f

        val sharedPref = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val bmiJson = sharedPref.getString("BMI list", null)

        var listOfBMI = mutableListOf<Float>()
        if (bmiJson == null) {
            listOfBMI.clear()
        } else {
            listOfBMI = gson.fromJson(bmiJson, Array<Float>::class.java).toMutableList()
        }
        val dateJson = sharedPref.getString("DateList", null)
        var listOfDate = mutableListOf<String>()
        if (dateJson == null) {
            listOfDate.clear()
        } else {
            listOfDate = gson.fromJson(dateJson, Array<String>::class.java).toMutableList()
        }


        var positionTop = 100F
        var indexDate = 0

        if (listOfBMI.isNullOrEmpty()) {
            val toast = Toast.makeText(context, resources.getString(R.string.eiTietoja), Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0, 400)
            toast.show()
        }
        for (x in listOfBMI.reversed()) {

            paintText.color = Color.WHITE
            paintText.typeface = Typeface.DEFAULT_BOLD
            paintText.textSize = 50f

            when {
                x < 18.5 -> {
                    paint.color = Color.rgb(65, 122, 186)
                }
                x in 18.5..25.0 -> {
                    paint.color = Color.rgb(0, 153, 0)
                }
                x in 25.1..30.0 -> {
                    paint.color = Color.rgb(208, 156, 78)
                }
                else -> {
                    paint.color = Color.rgb(178, 55, 50)
                }
            }
            var positionX = x*20
            if (positionX < 150f) {
                positionX = 160f
            }
            canvas?.drawRect(0f, positionTop, positionX, positionTop+100f, paint)
            canvas?.drawText(String.format("%.2f", x), 50f, positionTop+70f, paintText)

            val nightModeFlags = context.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
            when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> paintText.color = Color.WHITE
                Configuration.UI_MODE_NIGHT_NO -> paintText.color = Color.BLACK
                Configuration.UI_MODE_NIGHT_UNDEFINED -> paintText.color = Color.BLACK
            }

            paintText.textSize = 40F
            var xPosition = x*20+50f

            if (positionX+260f >= width) {
                xPosition = positionX - 250f
                paintText.color = Color.WHITE
            } else if (x*20 < 150f) {
                xPosition = x*20 + 150f
            }
            if (listOfDate.size == listOfBMI.size) {
                canvas?.drawText(listOfDate.reversed().elementAt(indexDate), xPosition, positionTop + 70f, paintText)
            }
            positionTop += 150f
            indexDate += 1

        }
    }
}