package com.recep.hunt.inviteFriend

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import com.kaopiz.kprogresshud.KProgressHUD
import com.recep.hunt.inviteFriend.inviteAdapter.ContactListAdapter
import com.recep.hunt.inviteFriend.model.ChipsInvitesModel
import com.recep.hunt.inviteFriend.model.ContactHistoryModel
import com.recep.hunt.utilis.SimpleDividerItemDecoration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_invite_friend_contact.*
import kotlinx.android.synthetic.main.chips_friend_invited_adapter.view.*

import org.jetbrains.anko.find
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class InviteFriendContactActivity : AppCompatActivity(), ContactListAdapter.OnItemClickListener {
    override fun onItemClick(item: ContactHistoryModel, position: Int) {
        // chipContactList.add(item)
        tvSearchContactId.visibility = View.GONE
        rvInviteFriendCheckedId.visibility = View.VISIBLE
        var userName = item.userName.split(" ");
        var sShort: String = ""
        var fshort: String = ""
        var shorName: String = ""

        if (userName.get(0) == null) {
            fshort = userName[0]
            if (userName[1] == null) {
                sShort = userName.get(1)
            }
            shorName = "$fshort $sShort"
        }

        var chipsInvitesModel = ChipsInvitesModel(userName[0], shorName)
        chipContactList.add(chipsInvitesModel)
        rvInviteFriendCheckedId.adapter = adapter
        rvInviteFriendCheckedId.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rvInviteFriendCheckedId.addItemDecoration(SimpleDividerItemDecoration(this))
        for (modle in chipContactList) {
            adapter.add(ChipsInviteItems(modle))
        }
    }

    val chipContactList = LinkedList<ChipsInvitesModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvInviteFriendCheckedId: RecyclerView
    private lateinit var tvSearchContactId: TextView
    private lateinit var dialog: KProgressHUD
    private var bitmap: Bitmap? = null

    private val adapter = GroupAdapter<ViewHolder>()

    companion object {
        private val PERMISSIONS_REQUEST_READ_CONTACTS = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_friend_contact)
        init()
        checkPermission();
    }

    private fun init() {
        tvSearchContactId = find(R.id.tvSearchContactId)
        recyclerView = find(R.id.rvInviteFriendId)
        rvInviteFriendCheckedId = find(R.id.rvInviteFriendCheckedId)
    }

    class ChipsInviteItems(private val model: ChipsInvitesModel) : Item<ViewHolder>() {
        override fun getLayout() = R.layout.chips_friend_invited_adapter
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.tvChipShortNameId.text = model.userShortName
            viewHolder.itemView.tvChipNameId.text = model.userName
            viewHolder.itemView.ivChipsCloseId.setOnClickListener()
            {

            }

        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )

        } else {
            adapterSetup();
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // checkPermission()
                } else {
                    adapterSetup();
                }

            } else {
                //  toast("Permission must be granted in order to display contacts information")
            }
        }
    }

    private fun adapterSetup() {
        var contactList = getContacts(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(SimpleDividerItemDecoration(this))
        var contactListAdapter = ContactListAdapter(contactList, this)
        recyclerView.adapter = contactListAdapter
        contactListAdapter.notifyDataSetChanged()
        /* var contactList = getContacts(this)
         for (model in contactList) {
             adapter.add(InviteFriendModleItems(model))
         }*/
    }

    fun getContacts(ctx: Context): List<ContactHistoryModel> {
        val contactList = ArrayList<ContactHistoryModel>()
        val numberFlag = ArrayList<String>()
        contactList.clear()
        numberFlag.clear()
        val contentResolver = ctx.contentResolver
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val cursorInfo = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id), null
                    )
                    val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(
                        ctx.contentResolver,
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id.toLong())
                    )
                    val person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id.toLong())
                    var pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
                    var photo: Bitmap? = null
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream)
                    }
                    while (cursorInfo!!.moveToNext()) {
                        var name =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                        var number =
                            cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        var id = id;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, pURI)

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        val info = ContactHistoryModel(id, name, number, bitmap);

                        if (!numberFlag.contains(cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))) {
                            numberFlag.add(cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
                            contactList.add(info)
                        }
                    }


                    cursorInfo.close()
                }
            }
            cursor.close()
        }
        return contactList
    }

}

