package com.example.pune.demo.view


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.pune.demo.R
import com.example.pune.demo.db_optration.MyDB
import com.example.pune.demo.model.DataModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(){




    private lateinit var list: ArrayList<DataModel>
    private lateinit var mAdapter:mAdapters
    private lateinit var db: MyDB
    private lateinit var mRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = MyDB(this)
        list= ArrayList()





        mRecyclerView=findViewById(R.id.recy_categroies)
        val layoutManager = GridLayoutManager(applicationContext,4)
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.layoutManager = layoutManager



       /* list.add(0,DataModel(3,"New","1"))
        mAdapter = mAdapters(list)
        mRecyclerView.adapter = mAdapter
     */


        fab.setOnClickListener {
            startActivity(Intent(this@MainActivity,Pic::class.java))
        }


    }


    override fun onResume() {
        super.onResume()


        try {
            val l=db.getAllData()
            list.clear()
            for(c in l) {
                val m=DataModel(c.id!!, c.name!!, c.avatarPath!!)
                list.add(m)
            }
            mAdapter = mAdapters(list)
            mRecyclerView.adapter = mAdapter

        }catch (e:Exception){}

    }

    fun addData(){
        startActivity(Intent(this@MainActivity,Pic::class.java))
    }





   /* private fun initJson() {

        apiServices!!.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Log.d(TAG, "Response JSON = ${Gson().toJson(result.result)}")

                            list = ArrayList(result.result)
                            mAdapter = mAdapters(list)
                            recyclerView.adapter = mAdapter

                        },
                        { error ->
                            Log.d(TAG, "ERROR ${error.message}")
                        }
                )
    }*/


}
