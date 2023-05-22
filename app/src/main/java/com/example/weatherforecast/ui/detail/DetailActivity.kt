package com.example.weatherforecast.ui.detail

import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.ui.main.WeatherDataNew
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var rcView1: RecyclerView
    private lateinit var rcView2: RecyclerView

    private var weatherData: WeatherDataNew? = null
    private lateinit var changedTemp: String
    private lateinit var changedTemp1: String
    private lateinit var changedTemp2: String
    private lateinit var changedTemp3: String

    private lateinit var updatedHumidity: String
    private lateinit var updatedAirPressure: String
    private lateinit var updatedWindVelocity: String
    private lateinit var updatedVisibility: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Data for detail section
        weatherData = intent.getParcelableExtra("weatherData")
        val temperature: Int? = weatherData?.list?.get(0)?.main?.temp?.toInt()
        changedTemp = ((temperature?.minus(272)).toString() + "℃")
        findViewById<TextView>(R.id.detail4).text = changedTemp

        val description: String? = weatherData?.list?.get(0)?.weather?.get(0)?.description
        findViewById<TextView>(R.id.detail5).text = description

        // Data for today weather recycler view
        val temperature1: Int? = weatherData?.list?.get(1)?.main?.temp?.toInt()
        changedTemp1 = ((temperature1?.minus(272)).toString() + "℃")
        val temperature2: Int? = weatherData?.list?.get(2)?.main?.temp?.toInt()
        changedTemp2 = ((temperature2?.minus(272)).toString() + "℃")
        val temperature3: Int? = weatherData?.list?.get(3)?.main?.temp?.toInt()
        changedTemp3 = ((temperature3?.minus(272)).toString() + "℃")

        // Data for detail info recycler view
        val humidity: Int? = weatherData?.list?.get(0)?.main?.humidity
        updatedHumidity = (humidity.toString() + "%")
        val windVelocity: Double? = weatherData?.list?.get(0)?.wind?.speed
        updatedWindVelocity = (windVelocity.toString() + " km/h")
        val visibility: Int? = weatherData?.list?.get(0)?.visibility
        updatedVisibility = (visibility.toString() + " m")
        val airPressure: Int? = weatherData?.list?.get(0)?.main?.pressure
        updatedAirPressure = (airPressure.toString() + " hPa")

        // RecyclerView for Hour
        rcView1 = findViewById(R.id.recyclerViewText02)
        rcView1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val data1 = initHourData()
        rcView1.adapter = HourWeatherRVAdapter(data1)

        // RecyclerView for Daily Detail
        rcView2 = findViewById(R.id.recyclerViewText03)
        rcView2.layoutManager = GridLayoutManager(this, 2)
        val data2 = initDailyData()
        rcView2.adapter = ConditionWeatherRVAdapter(data2)

        findViewById<ImageView>(R.id.back_button).setOnClickListener{
            homeScreen()
        }
        findViewById<ImageView>(R.id.theme_menu).setOnClickListener{
            showMenu()
        }

        val txtViewForDateTime: TextView = findViewById(R.id.detail2)
        val currentTime = Calendar.getInstance().time
        val sdf1 = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = sdf1.format(currentTime)

        val currentDate = Calendar.getInstance()
        val sdf2 = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = sdf2.format(currentDate.time)
        txtViewForDateTime.text = formattedDate + " - " + formattedTime

    }

    private fun homeScreen () {
        finish()
    }

    private fun showMenu() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false)
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val anchor = findViewById<ImageView>(R.id.theme_menu)
        val popupMenu = PopupMenu(this@DetailActivity, anchor)
        popupMenu.menuInflater.inflate(R.menu.theme_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.light && isDarkModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putString("appTheme", "lightTheme")
                editor.apply()
                Toast.makeText(this@DetailActivity, "Light Mode Enabled", Toast.LENGTH_LONG).show()
            }
            if (item.itemId == R.id.dark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putString("appTheme", "darkTheme")
                editor.apply()
                Toast.makeText(this@DetailActivity, "Dark Mode Enabled", Toast.LENGTH_LONG).show()
            }
            false
        }
        popupMenu.show()
    }

    private fun initHourData(): List<HourWeather> {
        val data = mutableListOf<HourWeather>()
        data.add(HourWeather(R.drawable.ic_night_storm, changedTemp, "12:00 AM"))
        data.add(HourWeather(R.drawable.ic_rain03, changedTemp1, "03:00 PM"))
        data.add(HourWeather(R.drawable.ic_night_storm, changedTemp2, "06:00 PM"))
        data.add(HourWeather(R.drawable.ic_rain03, changedTemp3, "09:00 PM"))
        return data
    }

    private fun initDailyData(): List<ConditionWeather> {
        val data = mutableListOf<ConditionWeather>()
        data.add(ConditionWeather(R.drawable.ic_humidity, updatedHumidity, "Humidity"))
        data.add(ConditionWeather(R.drawable.ic_air_pressure, updatedAirPressure, "Air Pressure"))
        data.add(ConditionWeather(R.drawable.ic_wind, updatedWindVelocity, "Wind Velocity"))
        data.add(ConditionWeather(R.drawable.ic_fog, updatedVisibility, "Visibility"))
        return data
    }
}