package com.recep.hunt.inviteFriend

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.hunt.R
import org.jetbrains.anko.toast
import androidx.room.util.CursorUtil.getColumnIndex
import com.recep.hunt.inviteFriend.InviteFragment.ShowYourInviteFrag
import com.recep.hunt.inviteFriend.model.InvitesAFriendModle
import com.recep.hunt.inviteFriend.model.InvitesModel
import com.recep.hunt.utilis.SimpleDividerItemDecoration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.my_friend_invited_adapter.view.*
import kotlinx.android.synthetic.main.my_invited_adapter_item.view.*
import org.jetbrains.anko.find


class InviteFriendContactActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = GroupAdapter<ViewHolder>()

    companion object {
        private val PERMISSIONS_REQUEST_READ_CONTACTS = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_friend_contact)
        init()
    }


    private fun init() {
        recyclerView = find(R.id.rvInviteFriendId)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(SimpleDividerItemDecoration(this))
        for (model in getInviteData()) {
            adapter.add(InviteFriendModleItems(model))
        }

    }


    private fun getInviteData(): ArrayList<InvitesAFriendModle> {
        val data = ArrayList<InvitesAFriendModle>()
        if (data.size == 0) {
            data.add(InvitesAFriendModle("Sara Adam", userNumber = "+4556456786"))
            data.add(InvitesAFriendModle("Tim Hieght", userNumber = "+4556409786"))
            data.add(InvitesAFriendModle("Jaane Mabry", userNumber = "+4556409723"))
            data.add(InvitesAFriendModle("Joh Doe", userNumber = "+4551109786"))
            data.add(InvitesAFriendModle("King Doe", userNumber = "+4551109786"))
            data.add(InvitesAFriendModle("Smith Doe", userNumber = "+4551109786"))
            data.add(InvitesAFriendModle("James Gosling", userNumber = "+4551894386"))
            data.add(InvitesAFriendModle("Mr Sidhizi", userNumber = "+4551109123"))
            data.add(InvitesAFriendModle("Tone Marrow", userNumber = "+4551009786"))
        }
        return data
    }

    class InviteFriendModleItems(private val model: InvitesAFriendModle) : Item<ViewHolder>() {
        override fun getLayout() = R.layout.my_friend_invited_adapter
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.tvFriendInvitUserNameId.text = model.userName
            viewHolder.itemView.tvFriendInvitUserNumberId.text = model.userNumber

        }
    }


    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //callback onRequestPermissionsResult
        } else {

        }
    }

    fun getNameEmailDetails(): ArrayList<String> {
        val names = ArrayList<String>()
        val cr = contentResolver
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cur!!.count > 0) {
            while (cur.moveToNext()) {
                val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val cur1 = cr.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    arrayOf(id), null
                )
                while (cur1!!.moveToNext()) {
                    //to get the contact names
                    val name =
                        cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    Log.e("Name :", name)
                    val email =
                        cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    Log.e("Email", email)
                    if (email != null) {
                        names.add(name)
                    }
                }
                cur1.close()
            }
        }
        return names
    }

    private fun getContacts(): StringBuilder {
        val builder = StringBuilder()
        val resolver: ContentResolver = contentResolver;
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null
        )

        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = (cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                )).toInt()

                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null
                    )

                    if (cursorPhone.count > 0) {
                        while (cursorPhone.moveToNext()) {
                            val phoneNumValue = cursorPhone.getString(
                                cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            )
                            builder.append("Contact: ").append(name).append(", Phone Number: ").append(
                                phoneNumValue
                            ).append("\n\n")
                            Log.e("Name ===>", phoneNumValue);
                        }
                    }
                    cursorPhone.close()
                }
            }
        } else {
            toast("No contacts available!")
        }
        cursor.close()
        return builder
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                loadContacts()
            } else {
                //  toast("Permission must be granted in order to display contacts information")
            }
        }
    }

}
