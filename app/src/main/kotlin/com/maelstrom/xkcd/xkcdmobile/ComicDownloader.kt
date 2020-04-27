package com.maelstrom.xkcd.xkcdmobile

import android.app.Activity
import android.app.DownloadManager
import android.app.Fragment
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import java.io.File

class ComicDownloader {

    private var connectivityManager: ConnectivityManager? = null
    private var manager: DownloadManager? = null

    var deviceConnectedState = false
        private set

    var fileStatus = false

    var fileExtension = ".txt"

    var fileName = "downloaded_file"

    var dirName = "/file_downloader/"

    var canFileBeDownloaded = true

    fun setDeviceConnected(deviceConnected: Boolean) {
        this.deviceConnectedState = deviceConnected
    }

    fun isReadyForDownload(Parent: Any): Boolean {

        if (Parent is Fragment)
            connectivityManager = Parent.activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Parent is Activity)
            connectivityManager = Parent.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectivityManager!!.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected)
            setDeviceConnected(true)

        return deviceConnectedState
    }


    fun isFilePresent(dirName: String, pathRelativeToRoot: String): Boolean {
        val totalPath = Environment.getExternalStorageDirectory().toString() + dirName + pathRelativeToRoot
        return File(totalPath).exists()
    }

    fun isFilePresent(file: File): Boolean {
        return file.exists()
    }

    fun getFilePath(pathRelativeToRoot: String): String {
        return Environment.getExternalStorageDirectory().toString() + pathRelativeToRoot
    }

    fun DownloadFile(context: Context, url: String, dirName: String, fileName: String, fileExtension: String): Long {

        this.dirName = dirName
        this.fileName = fileName
        this.fileExtension = fileExtension

        val request = DownloadManager.Request(Uri.parse(url))
        request.setDescription("Downloading the comic...")
        request.setTitle(fileName + fileExtension)

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(dirName, fileName + fileExtension)

        manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        return manager!!.enqueue(request)
    }

    fun isValidDownload(downloadID: Long): Boolean {

        if (downloadID.equals(0)) return false

        try {
            val c = manager!!.query(DownloadManager.Query().setFilterById(downloadID))
            canFileBeDownloaded = c.moveToFirst() && c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_FAILED || c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_RUNNING

            c.close()
        } catch (e: Exception) {
            Log.d("COMIC", e.message)
            canFileBeDownloaded = false
        }

        return canFileBeDownloaded
    }
}