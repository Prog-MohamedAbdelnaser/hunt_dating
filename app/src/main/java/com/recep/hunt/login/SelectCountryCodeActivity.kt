package com.recep.hunt.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.api.GetCountryCodes
import com.recep.hunt.models.CountryCodeModel
import com.recep.hunt.utilis.SimpleDividerItemDecoration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import org.jetbrains.anko.find
interface CountryCodeSelectedListener {
    fun countryCodeSelected(model:CountryCodeModel)
}
class SelectCountryCodeActivity : AppCompatActivity(),CountryCodeSelectedListener {

    override fun countryCodeSelected(model: CountryCodeModel) {
        val returnIntent = Intent()
        returnIntent.putExtra(countryCodeKey,model.dialCode)
        returnIntent.putExtra(countryNameKey,model.name)
        setResult(Activity.RESULT_OK,returnIntent)
        finish()
    }

    companion object{
        const val countryCodeKey = "country_Code_Key"
        const val countryNameKey = "country_Name_Key"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var searchView: SearchView
    private lateinit var mAdapter :SearchCountryCodeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_country_code)
        init()
    }
    private fun init(){
        recyclerView = find(R.id.search_recycler_view)
        toolbar = find(R.id.search_toolbar)
        setSupportActionBar(toolbar)
        setupRecyclerView()

    }
    private fun setupRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this@SelectCountryCodeActivity)
        recyclerView.addItemDecoration(SimpleDividerItemDecoration(this@SelectCountryCodeActivity))
        val countryCodes = GetCountryCodes.getCountryCodes(this@SelectCountryCodeActivity) as ArrayList<CountryCodeModel>
        mAdapter = SearchCountryCodeAdapter(this@SelectCountryCodeActivity, countryCodes,this)
        recyclerView.adapter = mAdapter
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu,menu)
        val searchItem = menu.findItem(R.id.quiz_action_search)
        searchView = searchItem.actionView as SearchView
        val iconClose = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        iconClose.setColorFilter(resources.getColor(R.color.white))
        searchView.isIconified = true
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.e("onQueryTextSubmit", "query :$query")
                if (!searchView.isIconified) {
                    searchView.isIconified = true
                    mAdapter.filter.filter(query)
                }
                searchItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                Log.e("onQueryTextChange", "query :$s")
                mAdapter.filter.filter(s)
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null){
            val id = item.itemId
            when (id) {
                android.R.id.home -> {
                    finish()
                    return true
                }
            }
        }

        return super.onOptionsItemSelected(item)

    }
}
class SearchCountryCodeAdapter(val context: Context,val model:ArrayList<CountryCodeModel>,val listener:CountryCodeSelectedListener) :RecyclerView.Adapter<SearchCountryCodeAdapter.SearchCodeViewHolder>(),Filterable{

    private var codeList = ArrayList<CountryCodeModel>()
    init {
        this.codeList  = model
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchCodeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.select_country_code_adapter_item,parent,false))
    override fun getItemCount() = codeList.size
    override fun onBindViewHolder(holder: SearchCodeViewHolder, position: Int) {
        holder.setupView(codeList[position])
        holder.itemView.setOnClickListener {
            listener.countryCodeSelected(codeList[position])
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                codeList = if (charString.isEmpty()) {
                    model
                } else {
                    val filteredList = ArrayList<CountryCodeModel>()
                    for (row in model) {
                        if (row.name!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = codeList
                return filterResults
            }
            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                codeList = filterResults.values as ArrayList<CountryCodeModel>
                notifyDataSetChanged()
            }
        }
    }

    inner class SearchCodeViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private var name_tv: TextView = view.findViewById(R.id.country_code_item_text)
        fun setupView(model:CountryCodeModel){
            name_tv.text = model.dialCode + " " +model.name
        }
    }
}