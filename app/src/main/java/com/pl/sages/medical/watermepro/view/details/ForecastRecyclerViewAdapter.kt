package com.pl.sages.medical.watermepro.view.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pl.sages.medical.watermepro.R
import com.pl.sages.medical.watermepro.databinding.ItemForecastBinding
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind


class ForecastRecyclerViewAdapter(private var forecast: List<ForecastViewData> = emptyList()): RecyclerView.Adapter<ForecastRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemForecastBinding.bind(view)

        // (6) Aktualizacja widoku pojedynczego widoku (kafelka) danymi z obiektu ForecastViewData
        fun update(forecast: ForecastViewData) {

            // ustawiam teksty w TextView
            binding.itemTemperatureTv.text = "${forecast.temperature} C"
            binding.itemPressureTv.text = "${forecast.pressure} hPa"

            // ustawiam ikonę w zależności od enuma WeatherKind
            when(forecast.weatherKind) {
                WeatherKind.SUNNY -> binding.weatherIcon.setImageResource(R.drawable.ic_sunny)
                WeatherKind.RAINY -> binding.weatherIcon.setImageResource(R.drawable.ic_thunder)
                WeatherKind.CLOUDY -> binding.weatherIcon.setImageResource(R.drawable.ic_cloudy)
                WeatherKind.SNOWY -> binding.weatherIcon.setImageResource(R.drawable.ic_snowy)
                WeatherKind.NONE -> binding.weatherIcon.setImageResource(R.drawable.ic_question_mark)
            }
        }
    }

    // (4) Wczytujemy layout XML, który będzie reprezentował pojedynczy obiekt z listy i będzie wyświetlany w RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context) // context - dostęp do zasobów aplikacji i usług
        val view = inflater.inflate(R.layout.item_forecast, parent, false) // Wczytanie konkretnego XMLa z zasobów
        return ViewHolder(view) // Zwrócenie ViewHoldera
    }

    // zapis tradycyjny
    //    override fun getItemCount(): Int {
    //        return forecast.size
    //    }
    // zapis skrócony (inline)

    // (3) Ta funkcja określa ile rzeczy będziemy wyświetlać - jaka jest długość tej listy przewijanej (na podstawie ilości obiektów w liście forecast)
    override fun getItemCount() = forecast.size


    // (5) Aktualizacja widoku pojedynczego widoku (kafelka) danymi z obiektu ForecastViewData.
    // Metoda uruchamiana dla każdego elementu listy forecast
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(forecast[position])
    }


}