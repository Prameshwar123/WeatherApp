package com.example.weatherapp

import com.example.weatherapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun changeImagesAccordingToWeatherCondition(conditions: String, binding: ActivityMainBinding) {
    when(conditions){
        "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy", "Haze"->{
            binding.root.setBackgroundResource(R.drawable.cloud_background)
            binding.lottieAnimationView.setAnimation(R.raw.cloud)
        }
        "Clear Sky", "Sunny", "Clear" -> {
            binding.root.setBackgroundResource(R.drawable.sunny_background)
            binding.lottieAnimationView.setAnimation(R.raw.sun)
        }
        "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
            binding.root.setBackgroundResource(R.drawable.rain_background)
            binding.lottieAnimationView.setAnimation(R.raw.rain)
        }
        "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
            binding.root.setBackgroundResource(R.drawable.snow_background)
            binding.lottieAnimationView.setAnimation(R.raw.snow)
        }
        else ->{
            binding.root.setBackgroundResource(R.drawable.sunny_background)
            binding.lottieAnimationView.setAnimation(R.raw.sun)
        }
    }
    binding.lottieAnimationView.playAnimation()
}

fun dayName(timeStamp: Long): String{
    val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
    return sdf.format((Date()))
}
fun time(timeStamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format((Date(timeStamp*1000)))
}

fun date(): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.format((Date()))
}