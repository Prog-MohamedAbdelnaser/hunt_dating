package com.recep.hunt.setupProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.recep.hunt.utilis.BaseActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import org.jetbrains.anko.find
import java.util.stream.BaseStream

class SetupProfileGalleryActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = GroupAdapter<ViewHolder>()
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
        val gridLayout = GridLayoutManager(this@SetupProfileGalleryActivity,2)
        recyclerView.layoutManager=gridLayout
    }
}
