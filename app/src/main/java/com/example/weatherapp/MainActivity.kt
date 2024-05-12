package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherapp.api.fetchWeatherData
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.permission.PermissionHandler
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var permission : PermissionHandler
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        permission = PermissionHandler()
        var city = "Mumbai"
        if(!permission.isLocationOk(this)) permission.requestLocationPermission(this)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!isLocationEnabled()) showLocationSettingsDialog()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    // Convert latitude and longitude into city name
                    val geocoder = Geocoder(this, Locale.getDefault())
                    geocoder.getFromLocation(
                        it.latitude, it.longitude, 1
                    ) { addresses ->
                        if (addresses.isNotEmpty()) {
                            city = addresses[0].locality
                            fetchWeatherData(city, binding)
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "City not found",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        fetchWeatherData(city,binding)
        searchCity()
    }
    private fun showLocationSettingsDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Location Services Required")
        dialogBuilder.setMessage("Please enable location services to use this app.")
        dialogBuilder.setPositiveButton("Enable") { dialog, which ->
            startActivityForResult(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0,null)
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }
    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    private fun searchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query, binding)
                }
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }
}
