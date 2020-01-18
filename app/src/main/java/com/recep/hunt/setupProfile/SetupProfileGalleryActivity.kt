package com.recep.hunt.setupProfile

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.setupProfile.adapters.SetupProfileGalleryAdapter
import com.recep.hunt.setupProfile.model.GalleryImageDetailsModel
import com.recep.hunt.utilis.BaseActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import org.jetbrains.anko.find
import java.util.HashMap

class SetupProfileGalleryActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView

    private val adapter = GroupAdapter<ViewHolder>()
    internal var albumList = java.util.ArrayList<HashMap<String, String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile_gallery)
        setScreenTitle("Gallery")
        init()
    }

    private fun init() {

        recyclerView = find(R.id.photo_recyclerView)
        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        val gridLayout = GridLayoutManager(this@SetupProfileGalleryActivity, 2)
        recyclerView.layoutManager = gridLayout
        recyclerView.adapter = adapter
        val imageData = dummyImagedata()

        for(data in imageData){

            adapter.add(
                SetupProfileGalleryAdapter(
                    this@SetupProfileGalleryActivity,
                    data
                )
            )
        }
    }

    //Dummy Chat QuestionData
    private fun dummyImagedata():ArrayList<GalleryImageDetailsModel>{
        val data = ArrayList<GalleryImageDetailsModel>()
        if(data.size == 0){
            data.add(
                GalleryImageDetailsModel(
                    "Personal images",
                    "uri",
                    123
                )
            )
            data.add(
                GalleryImageDetailsModel(
                    "Adventures",
                    "uri",
                    13
                )
            )
            data.add(
                GalleryImageDetailsModel(
                    "Cute girl",
                    "uri",
                    13
                )
            )
            data.add(
                GalleryImageDetailsModel(
                    "Personal images",
                    "uri",
                    123
                )
            )
            data.add(
                GalleryImageDetailsModel(
                    "Adventures",
                    "uri",
                    13
                )
            )
            data.add(
                GalleryImageDetailsModel(
                    "Cute girl",
                    "uri",
                    13
                )
            )
            data.add(
                GalleryImageDetailsModel(
                    "Personal images",
                    "uri",
                    123
                )
            )
            data.add(
                GalleryImageDetailsModel(
                    "Adventures",
                    "uri",
                    13
                )
            )
            data.add(
                GalleryImageDetailsModel(
                    "Cute girl",
                    "uri",
                    13
                )
            )

                   }
        return data
    }




}
