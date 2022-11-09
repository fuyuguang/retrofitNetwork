package com.fyg.networklib

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.pm.ApplicationInfo
import android.database.Cursor
import android.net.Uri
import com.fyg.networklib.log.CostTimeAspectJ

val appContext: Application by lazy { Ktx.app }

class Ktx : ContentProvider() {

    companion object {
        @JvmStatic
         var DEBUG: Boolean = false
        lateinit var app: Application

        fun setDebugMode(debug : Boolean){
            DEBUG = debug;
        }
    }

    override fun onCreate(): Boolean {
        val application = context!!.applicationContext as Application
        install(application)
        return true
    }

    private fun install(application: Application) {
        app = application
        CostTimeAspectJ.setEnabled(false &&(app.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE)
        //ApiServiceAdd.initServiceAdd()
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = null


    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun getType(uri: Uri): String? = null
}