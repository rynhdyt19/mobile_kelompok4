package com.example.final_mobile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.final_mobile.data.forecastModels.ForecastData
import com.example.final_mobile.databinding.RvItemLayoutBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class RvAdapter( private val forecastArray: ArrayList<ForecastData>) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    class ViewHolder(val binding : RvItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(RvItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = forecastArray[position]
        holder.binding.apply {
            val imageIcon = currentItem.weather[0].icon
            val imageUrl = "https://openweathermap.org/img/w/$imageIcon.png"

            Picasso.get().load(imageUrl).into(imgItem)

            tvItemTemp.text = "${currentItem.main.temp.toInt()} °C"
            tvItemStatus.text = "${currentItem.weather[0].description}"
            tvItemTime.text = displayTime(currentItem.dt_txt)
        }
    }

    private fun displayTime(dtTxt: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())

        val date = inputFormat.parse(dtTxt)
        return if (date != null) outputFormat.format(date) else ""
    }

    override fun getItemCount(): Int {
        return forecastArray.size
    }
}