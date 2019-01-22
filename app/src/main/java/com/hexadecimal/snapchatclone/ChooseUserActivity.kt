package com.hexadecimal.snapchatclone

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ChooseUserActivity : AppCompatActivity() {

    var chooseUserListView : ListView? = null
    var emails: ArrayList<String> = ArrayList()

    var keys: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_user)

        chooseUserListView = findViewById(R.id.chooseUserListView)
        // adapter ile listemizi dolduruyoruz
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,emails)
        chooseUserListView?.adapter = adapter

        // firebase'de veritabanina ulastik ve users child'i üzerinden dinlemeye basladik
        // eger users icinde bir degisiklik olursa burada yakalayacagiz
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                // kullanicinin email adresini string olarak aldik
                val email = p0.child("email").value as String
                // email adresini listemize ekledik
                emails.add(email)
                // kullanicilarin UUID'lerini de listemize ekledik
                keys.add(p0.key!!)
                // listemizi guncelledik
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildRemoved(p0: DataSnapshot) {}
        })
        // emaillerin listelendigi listview'de secilen email adresine gore islem yapacagiz
        chooseUserListView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            // Map<String, Any> olarak da kullanilabilirdi fakat tum istediklerimiz string oldugu icin ikisini de string olarak belirttik
            val snapMap: Map<String, String> = mapOf("from" to FirebaseAuth.getInstance().currentUser!!.email!!, "imageName" to intent.getStringExtra("imageName"), "imageURL" to intent.getStringExtra("imageURL"), "message" to intent.getStringExtra("message"))

            // database icinde users'a geldik, sonraki adımda kullandigimiz key, snap'i gonderecegimiz kisinin UUID'si
            // push() metodu, random isimli yeni bir child ekler
            FirebaseDatabase.getInstance().getReference().child("users").child(keys.get(i)).child("snaps").push().setValue(snapMap)

            val intent = Intent (this, SnapsActivity::class.java)
            // geri tusundaki herseyi siler ve direk SnapsActivity'e gider
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }
}
