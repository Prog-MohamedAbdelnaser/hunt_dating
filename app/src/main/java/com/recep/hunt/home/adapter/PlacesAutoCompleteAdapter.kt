package com.recep.hunt.home.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.recep.hunt.R
import com.recep.hunt.utilis.Utils
import kotlinx.coroutines.coroutineScope
import org.jetbrains.anko.find
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class PlacesAutoCompleteAdapter(val context: Context) :
    RecyclerView.Adapter<PlacesAutoCompleteAdapter.PredictionHolder>(), Filterable {

    private val placesClient: PlacesClient = Places.createClient(context)
    private val STYLE_NORMAL = StyleSpan(Typeface.NORMAL)
    private val STYLE_BOLD = StyleSpan(Typeface.BOLD)
    private lateinit var clickListener: ClickListener
    private var mResultList: ArrayList<PlaceAutoComplete> = ArrayList()

    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun click(place: Place)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                if (constraint != null) {
                    mResultList = getPredictions(constraint)
                    if (mResultList != null) {
                        results.values = mResultList
                        results.count = mResultList.size
                    }
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
//                if (results != null && results.count > 0) {
//
//                } else {
//
//                }
            }

        }
    }

    fun getPredictions(constraint: CharSequence): ArrayList<PlaceAutoComplete> {
        val resultList = ArrayList<PlaceAutoComplete>()
        val token = AutocompleteSessionToken.newInstance()

        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(constraint.toString())
            .build()
        val autoCompletePredictions = placesClient.findAutocompletePredictions(request)
        try {
            Tasks.await(autoCompletePredictions, 60, TimeUnit.SECONDS)
        } catch (e: ExecutionException) {
            Thread {
                var message= e.localizedMessage.split(":")[2]
                Utils.placesApiError.postValue(message)
            }.start()

            e.printStackTrace()
        }

        if (autoCompletePredictions.isSuccessful) {
            val findAutoCompletePredictionsResponse = autoCompletePredictions.result
            if (findAutoCompletePredictionsResponse != null) {
                val iterator =
                    findAutoCompletePredictionsResponse.autocompletePredictions.iterator()
                iterator.forEach {
                    resultList.add(
                        PlaceAutoComplete(
                            it.placeId,
                            it.getPrimaryText(STYLE_NORMAL).toString(),
                            it.getFullText(STYLE_BOLD).toString()
                        )
                    )
                }
                return resultList
            } else {
                return resultList
            }
        }
        return resultList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionHolder {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = layoutInflater.inflate(R.layout.place_list_item_layout, parent, false)
        return PredictionHolder(convertView)
    }

    override fun getItemCount(): Int {
        return mResultList.size
    }

    override fun onBindViewHolder(holder: PredictionHolder, position: Int) {
        holder.address.text = mResultList[position].address.toString()
    }

    fun getItem(position: Int): PlaceAutoComplete {
        return mResultList.get(position)
    }

    inner class PredictionHolder(view: View) : RecyclerView.ViewHolder(view) {
        var address: TextView = view.find(R.id.place_detail_textView)
        var mRow: ConstraintLayout = view.find(R.id.place_item_constraintLayout)

        init {
            view.setOnClickListener {
                val item = mResultList[adapterPosition]
                val placeId = item.placeId.toString()

                val placeFields =
                    Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.ADDRESS)
                val request = FetchPlaceRequest.builder(placeId, placeFields).build()
                placesClient.fetchPlace(request)
                    .addOnSuccessListener { p0 ->
                        val place = p0!!.place
                        clickListener.click(place)
                    }.addOnFailureListener { p0 ->
                        if (p0 is ApiException) {
                            Log.d("Home Activity ->", p0.message)
//                            Toast.makeText(context, p0.message, Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
//        override fun onClick(v: View?) {

//            if (v?.id == R.id.place_item_constraintLayout) {
//
//            }
//        }

    }

    class PlaceAutoComplete(
        val placeId: CharSequence,
        val area: CharSequence,
        val address: CharSequence
    ) {

        override fun toString(): String {
            return area.toString()
        }
    }

    private fun showErrorAlertToUser(msg: String) {

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { dialog, _ ->
                dialog.cancel()
            }
        val alert = alertDialogBuilder.create()
        alert.show()
    }
}