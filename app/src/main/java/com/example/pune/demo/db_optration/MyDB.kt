package com.example.pune.demo.db_optration

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.pune.demo.model.DataModel


class MyDB(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null,1){


    companion object {
        private val DATABASE_NAME="/storage/sdcard0/storage/sdcard0/MyEmployee/emp.db"
        private val TABLE_NAME="Employee"
        private val COL_Id="id"
        private val COL_Name="name"
        private var COL_AvatarPath="avatarPath"
    }



    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE $TABLE_NAME ($COL_Id INTEGER PRIMARY KEY AUTOINCREMENT,$COL_Name TEXT,$COL_AvatarPath TEXT)")
        Log.d("TAG_SQLite"," onCreate ")
     }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
          db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
          onCreate(db)
        Log.d("TAG_SQLite ","onUpgrade")
    }



    fun AddData(model:DataModel):Boolean{
        val db=this.writableDatabase
        val vals=ContentValues()

     //   vals.put(COL_Id,model.id)
        vals.put(COL_Name,model.name)
        vals.put(COL_AvatarPath,model.avatarPath)

        val i=db.insert(TABLE_NAME,null,vals)
        db.close()
        if(i>0){
            Log.d("TAG_SQLite","SAVE DATA")
            return true
        }else
            Log.d("TAG_SQLite","Not SAVE DATA")
            return false

    }


    fun getAllData():List<DataModel>{
        val list=ArrayList<DataModel>()
        val db=this.readableDatabase
        val cursor=db.rawQuery("SELECT * FROM  $TABLE_NAME",null)

        if(cursor.moveToFirst()){
            do {
                val v=DataModel()
                v.id=cursor.getInt(0)
                v.name=cursor.getString(1)
                v.avatarPath=cursor.getString(2)
                list.add(v)
            }while (cursor.moveToNext())

        }
        cursor.close()
        return list
    }


}