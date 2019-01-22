package com.hexadecimal.snapchatclone

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class SnapsActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    var snapsListView: ListView? = null
    var emails: ArrayList<String> = ArrayList()
    var snaps: ArrayList<DataSnapshot> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)

        snapsListView = findViewById(R.id.snapsListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,emails)
        snapsListView?.adapter = adapter

        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid!!).child("snaps").addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                // kulllanicinin tum snaplarini alip listview de gosteriyoruz
                emails.add(p0.child("from").value as String)
                snaps.add(p0)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildRemoved(p0: DataSnapshot) {}
        })

        snapsListView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val snapshot = snaps.get(i)

            val intent = Intent (this, ViewSnapActivity::class.java)
            intent.putExtra("imageName", snapshot.child("imageName").value as String)
            intent.putExtra("imageURL", snapshot.child("imageURL").value as String)
            intent.putExtra("message", snapshot.child("message").value as String)
            intent.putExtra("snapKey", snapshot.key)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // menu'nun olusturuldugu kisim
        val inflater = menuInflater
        inflater.inflate(R.menu.snaps, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // eger kullanici item'lardan birini secerse
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // kullanicinin sectigi iteme gore ne yapilmasi gerektigini belirledigimiz kısım
        if (item?.itemId == R.id.createSnap) {

            val intent = Intent (this, CreateSnapActivity::class.java)
            startActivity(intent)

        }else if (item?.itemId == R.id.logout) {
            // firebase'den kullanici cikisi yaptik
            mAuth.signOut()
            // log in ekranina geri donmesi icin bu metodu kullandi
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
    // kullanici geri tusuna basarsa cikis yapacak
    override fun onBackPressed() {
        super.onBackPressed()
        mAuth.signOut()
    }

}
