package com.example.final_mobile

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.final_mobile.adapter.RvAdapter
import com.example.final_mobile.data.forecastModels.ForecastData
import com.example.final_mobile.databinding.ActivityMainBinding
import com.example.final_mobile.databinding.BottomSheetLayoutBinding
import com.example.final_mobile.utils.RetrofitInstance
import com.squareup.picasso.Picasso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var sheetLayoutBinding: BottomSheetLayoutBinding

    private lateinit var dialog: BottomSheetDialog

    lateinit var pollutionFragment: PollutionFragment

    private var city: String = "Gowa"

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sheetLayoutBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
        dialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
        dialog.setContentView(sheetLayoutBinding.root)
        setContentView(binding.root)

        pollutionFragment = PollutionFragment()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query!= null){
                    city = query
                }
                getCurrentWeather(city)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        //fetchLocation()
        getCurrentWeather(city)

        binding.tvForecast.setOnClickListener {
            openDialog()
        }

        binding.tvLocation.setOnClickListener {
            fetchLocation()
        }
    }
}

private fun fetchLocation() {
    val task: Task<Location> = fusedLocationProviderClient.lastLocation


    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),101
        )
        return
    }

    task.addOnSuccessListener {
        val geocoder=Geocoder(this,Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            geocoder.getFromLocation(it.latitude,it.longitude,1, object: Geocoder.GeocodeListener{
                override fun onGeocode(addresses: MutableList<Address>) {
                    city = addresses[0].locality
                }

            })
        }else{
            val address = geocoder.getFromLocation(it.latitude,it.longitude,1) as List<Address>

            city = address[0].locality
        }
        getCurrentWeather(city)
    }
}