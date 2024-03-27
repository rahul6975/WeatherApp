package com.rahul.weatherapp.features.views

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.rahul.weatherapp.R
import com.rahul.weatherapp.databinding.ActivityMainBinding
import com.rahul.weatherapp.features.model.SearchList
import com.rahul.weatherapp.features.model.WeatherData
import com.rahul.weatherapp.features.model.WeatherModelImp
import com.rahul.weatherapp.features.viewModel.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var model: WeatherModelImp
    private lateinit var viewModel: WeatherViewModel

    private lateinit var googleMap: GoogleMap
    private var currentLocation: Location? = null
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    lateinit var binding: ActivityMainBinding
    private var fusedLocationClient: FusedLocationProviderClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = WeatherModelImp(applicationContext)
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        hideAllLayout()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermission()

        setLiveDataListeners()

        onClickListeners()

    }


    private fun onClickListeners() {
        binding.searchButton.setOnClickListener {
            if (binding.autoCompleteTv.text.isNotEmpty()) {
                viewModel.getSearchResult(binding.autoCompleteTv.text.toString(), model)
            }
        }
    }

    private fun setLiveDataListeners() {
        viewModel.weatherLiveData.observe(this, Observer { weatherData ->
            setWeatherInfo(weatherData)
        })

        viewModel.searchLiveData.observe(this, Observer {
            if (it.isNotEmpty()) {
                viewModel.getWeatherInfo(it.first().lat, it.first().lon, model)
            } else {
                Toast.makeText(this, "No result found", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.progressBarLiveData.observe(this, Observer { loading ->
            if (loading) {
                hideAllLayout()
                binding.progressBar.visibility = View.VISIBLE
            } else {
                showAllLayout()
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun setWeatherInfo(weatherData: WeatherData) {

        binding.tvErrorMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        showAllLayout()

        binding.layoutWeatherBasic.tvDateTime.text = weatherData.dateTime
        binding.layoutWeatherBasic.tvTemperature.text = weatherData.temperature
        binding.layoutWeatherBasic.tvCityCountry.text = weatherData.cityAndCountry
        Glide.with(binding.layoutWeatherBasic.ivWeatherCondition)
            .load(weatherData.weatherConditionIconUrl)
            .into(binding.layoutWeatherBasic.ivWeatherCondition)
        binding.layoutWeatherBasic.tvWeatherCondition.text =
            weatherData.weatherConditionIconDescription

        binding.layoutWeatherAdditional.tvHumidityValue.text = weatherData.humidity
        binding.layoutWeatherAdditional.tvPressureValue.text = weatherData.pressure
        binding.layoutWeatherAdditional.tvVisibilityValue.text = weatherData.visibility
        binding.layoutWeatherAdditional.tvDescValue.text =
            weatherData.weatherConditionIconDescription

        binding.layoutSunsetSunrise.tvSunriseTime.text = weatherData.sunrise
        binding.layoutSunsetSunrise.tvSunsetTime.text = weatherData.sunset
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            requestLocationPermission()
        } else {
            // Permission is already granted
            initializeMap()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                initializeMap()
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }


    private fun initializeMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                currentLocation = task.result
                if (currentLocation?.latitude != null && currentLocation?.longitude != null) {
                    viewModel.getWeatherInfo(
                        currentLocation!!.latitude,
                        currentLocation!!.longitude, model
                    )
                }
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    this,
                    "Enable to fetch current location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun hideAllLayout() {
        binding.layoutSunsetSunrise.root.visibility = View.GONE
        binding.layoutWeatherBasic.root.visibility = View.GONE
        binding.layoutWeatherAdditional.root.visibility = View.GONE
    }

    private fun showAllLayout() {
        binding.layoutSunsetSunrise.root.visibility = View.VISIBLE
        binding.layoutWeatherBasic.root.visibility = View.VISIBLE
        binding.layoutWeatherAdditional.root.visibility = View.VISIBLE

    }

}
