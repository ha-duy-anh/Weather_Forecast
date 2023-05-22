package com.example.weatherforecast.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.weatherforecast.ui.detail.DetailActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.ui.note.NoteActivity
import com.example.weatherforecast.ui.note.NoteDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var rcView: RecyclerView
    private lateinit var weatherCollectionRef : CollectionReference
    private val roomDb by lazy {
        Room.databaseBuilder(applicationContext,
            NoteDatabase::class.java,
            "notes.db"
        ).build()
    }

    private var day01DateTime: String = "01"
    private var day01temp: String = "01"
    private var day01desc: String = "01"
    private var day02DateTime: String = "01"
    private var day02temp: String = "02"
    private var day02desc: String = "02"
    private var day03DateTime: String = "01"
    private var day03temp: String = "03"
    private var day03desc: String = "03"
    private var day04DateTime: String = "01"
    private var day04temp: String = "04"
    private var day04desc: String = "04"
    private var day05DateTime: String = "01"
    private var day05temp: String = "05"
    private var day05desc: String = "05"

    private var description: String = ""
    private var temperature: String = ""
    private var weatherRetrieved: WeatherDataNew? = WeatherDataNew()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrieveNotes()

        //Dropdown menu for location input
        val spinnerLocation = findViewById<Spinner>(R.id.location_dropdown_menu)
        val adapter = ArrayAdapter.createFromResource(this, R.array.location, R.layout.custom_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerLocation.adapter = adapter
        rcView = findViewById(R.id.recyclerViewText)
        rcView.layoutManager = LinearLayoutManager(this)
        spinnerLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> {weatherCollectionRef = Firebase.firestore.collection("weatherHanoi")}
                    1 -> {weatherCollectionRef = Firebase.firestore.collection("weatherDanang")}
                    2 -> {weatherCollectionRef = Firebase.firestore.collection("weatherHochiminh")}
                    3 -> {weatherCollectionRef = Firebase.firestore.collection("weatherHue")}
                }
                uploadWeatherData()
                retrieveWeatherData {
                    val data = initWeekData()
                    rcView.adapter = WeekWeatherRVAdapter(data) {showNote()}
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        spinnerLocation.setSelection(0)


        findViewById<ConstraintLayout>(R.id.card_detail).setOnClickListener{
            showDetail()
        }
        findViewById<ConstraintLayout>(R.id.note).setOnClickListener{
            showNote()
        }

        // Date and Time
        val txtViewForTime: TextView = findViewById(R.id.currentTime)
        val currentTime = Calendar.getInstance().time
        val sdf1 = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = sdf1.format(currentTime)
        txtViewForTime.text = formattedTime
        val txtViewForDate: TextView = findViewById(R.id.currentDate)
        val currentDate = Calendar.getInstance()
        val sdf2 = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = sdf2.format(currentDate.time)
        txtViewForDate.text = formattedDate
    }

    private fun initWeekData(): List<WeekWeather> {
        val data = mutableListOf<WeekWeather>()
        data.add(WeekWeather(R.drawable.ic_rain02, day01DateTime, day01desc, day01temp))
        data.add(WeekWeather(R.drawable.ic_rain02, day02DateTime, day02desc, day02temp))
        data.add(WeekWeather(R.drawable.ic_rain02, day03DateTime, day03desc, day03temp))
        data.add(WeekWeather(R.drawable.ic_rain02, day04DateTime, day04desc, day04temp))
        data.add(WeekWeather(R.drawable.ic_rain02, day05DateTime, day05desc, day05temp))
        return data
    }

    private fun retrieveNotes() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val noteDao = roomDb.dao
            val allNotes = noteDao.getAllNotes()
            for (note in allNotes) {
                Log.d("Note check", "Note: ${note.title} - ${note.content}")
                val noteTitle = findViewById<TextView>(R.id.noteTitleMain)
                val noteContent = findViewById<TextView>(R.id.noteContentMain)
                noteTitle.text = note.title
                noteContent.text = note.content
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "Successfully retrieve notes", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun uploadWeatherData() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val apiKey = "ef7d0f574f4f130be5273b3c5e07988d"
            val interceptor = Interceptor {chain ->
                val url = chain.request().url.newBuilder()
                    .addQueryParameter("APPID", apiKey)
                    .build()
                val request = chain.request().newBuilder()
                    .url(url)
                    .build()
                chain.proceed(request)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            val openWeatherAPI = retrofit.create(OpenWeatherAPI::class.java)

            val db = Firebase.firestore

            val weatherHanoi = openWeatherAPI.getWeatherData("21.028511", "105.804817", apiKey)
            val weatherRefHanoi = db.collection("weatherHanoi")
            weatherRefHanoi.add(weatherHanoi).await()

            val weatherDanang = openWeatherAPI.getWeatherData("16.047079", "108.206230", apiKey)
            val weatherRefDanang = db.collection("weatherDanang")
            weatherRefDanang.add(weatherDanang).await()

            val weatherHochiminh = openWeatherAPI.getWeatherData("10.762622", "106.660172", apiKey)
            val weatherRefHochiminh = db.collection("weatherHochiminh")
            weatherRefHochiminh.add(weatherHochiminh).await()

            val weatherHue = openWeatherAPI.getWeatherData("16.4666648", "107.5999976", apiKey)
            val weatherRefHue = db.collection("weatherHue")
            weatherRefHue.add(weatherHue).await()

            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "Successfully saved data.", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                Log.d("new api call", e.message.toString())
            }
        }
    }

    private fun retrieveWeatherData(callback: () -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = weatherCollectionRef.get().await()
            var weatherDataList : List<WeatherData>? = null
            for(document in querySnapshot.documents) {
                weatherRetrieved = document.toObject<WeatherDataNew>()
                weatherDataList = weatherRetrieved?.list
            }
            withContext(Dispatchers.Main) {
                // Current day weather
                val currentWeatherInfo = weatherDataList?.get(0)
                // Text for temperature in Detail card
                val desc: TextView = findViewById(R.id.shortDesc)
                val weatherInfo = currentWeatherInfo?.weather
                description = weatherInfo?.get(0)?.description.toString()
                desc.text = description
                // Text for short description in Detail card
                val temp: TextView = findViewById(R.id.celsiusDegree)
                val weatherMainInfo = currentWeatherInfo?.main
                val unconvertedTemperature = weatherMainInfo?.temp
                temperature = tempConverter(unconvertedTemperature)
                temp.text = (temperature)

                // Next days weather data
                val firstWeatherInfo = weatherDataList?.get(4)
                day01desc = firstWeatherInfo?.weather?.get(0)?.description.toString()
                val temp01: Double? = firstWeatherInfo?.main?.temp
                day01temp = tempConverter(temp01)
                day01DateTime = firstWeatherInfo?.dt_txt.toString()

                val secondWeatherInfo = weatherDataList?.get(12)
                day02desc = secondWeatherInfo?.weather?.get(0)?.description.toString()
                val temp02: Double? = secondWeatherInfo?.main?.temp
                day02temp = tempConverter(temp02)
                day02DateTime = secondWeatherInfo?.dt_txt.toString()

                val thirdWeatherInfo = weatherDataList?.get(20)
                day03desc = thirdWeatherInfo?.weather?.get(0)?.description.toString()
                val temp03: Double? = thirdWeatherInfo?.main?.temp
                day03temp = tempConverter(temp03)
                day03DateTime = thirdWeatherInfo?.dt_txt.toString()

                val fourthWeatherInfo = weatherDataList?.get(28)
                day04desc = fourthWeatherInfo?.weather?.get(0)?.description.toString()
                val temp04: Double? = fourthWeatherInfo?.main?.temp
                day04temp = tempConverter(temp04)
                day04DateTime = fourthWeatherInfo?.dt_txt.toString()

                val fifthWeatherInfo = weatherDataList?.get(28)
                day05desc = fifthWeatherInfo?.weather?.get(0)?.description.toString()
                val temp05: Double? = fifthWeatherInfo?.main?.temp
                day05temp = tempConverter(temp05)
                day05DateTime = fifthWeatherInfo?.dt_txt.toString()
            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
        callback()
    }

    private fun tempConverter(temp: Double?): String {
        val temperatureInCelsius = if (temp != null)
            (temp.toInt() - 272) else null
        return (temperatureInCelsius.toString() + "℃")
    }

    private fun showDetail() {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("weatherData", weatherRetrieved)
        startActivity(intent)
    }

    private fun showNote() {
        val intent = Intent(applicationContext, NoteActivity::class.java)
        startActivity(intent)
    }

}
