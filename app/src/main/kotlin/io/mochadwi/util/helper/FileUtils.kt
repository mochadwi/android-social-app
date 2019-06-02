package io.mochadwi.util.helper

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_DOCUMENTS
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import io.mochadwi.R
import io.mochadwi.util.ext.coroutineLaunch
import io.mochadwi.util.ext.showLogError
import io.mochadwi.util.helper.AppHelper.Const.BASE_IMAGE_URL_MOVIE_DB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.File.separator
import java.io.FileFilter
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.DecimalFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.properties.Delegates

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 07/05/19
 * dedicated to build android-native
 *
 */

object FileUtils {

    /**
     * TAG for log messages.
     */
    internal val TAG = "FileUtils"
    private val DEBUG = false // Set to true to enable logging

    val MIME_TYPE_AUDIO = "audio/*"
    val MIME_TYPE_TEXT = "text/*"
    val MIME_TYPE_IMAGE = "image/*"
    val MIME_TYPE_VIDEO = "video/*"
    val MIME_TYPE_APP = "application/*"
    val DIRECTORY_OUTPUTS = "outputs"

    val HIDDEN_PREFIX = "."

    /**
     * File and folder comparator. TODO Expose sorting option method
     *
     * @author paulburke
     */
    var sComparator: Comparator<File> = Comparator<File> { f1, f2 ->
        // Sort alphabetically by lower case, which is much cleaner
        f1.name.toLowerCase().compareTo(
                f2.name.toLowerCase())
    }

    /**
     * File (not directories) filter.
     *
     * @author paulburke
     */
    var sFileFilter: FileFilter = FileFilter { file ->
        val fileName = file.name
        // Return files only (not directories) and skip hidden files
        file.isFile && !fileName.startsWith(HIDDEN_PREFIX)
    }

    /**
     * Folder (directories) filter.
     *
     * @author paulburke
     */
    var sDirFilter: FileFilter = FileFilter { file ->
        val fileName = file.name
        // Return directories only and skip hidden directories
        file.isDirectory && !fileName.startsWith(HIDDEN_PREFIX)
    }

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @param uri
     * @return Extension including the dot("."); "" if there is no extension;
     * null if uri was null.
     */
    fun getExtension(uri: String?): String? = when {
        uri == null -> null
        else -> {
            val dot = uri.lastIndexOf(".")
            if (dot >= 0) {
                uri.substring(dot)
            } else {
                // No extension.
                ""
            }
        }
    }

    /**
     * @return Whether the URI is a local one.
     */
    fun isLocal(url: String?): Boolean {
        return url != null && !url.startsWith("http://") && !url.startsWith("https://")
    }

    /**
     * @return True if Uri is a MediaStore Uri.
     * @author paulburke
     */
    fun isMediaUri(uri: Uri): Boolean {
        return "media".equals(uri.authority!!, ignoreCase = true)
    }

    /**
     * Convert File into Uri.
     *
     * @param file
     * @return uri
     */
    fun getUri(file: File?): Uri? {
        return if (file != null) {
            Uri.fromFile(file)
        } else null
    }

    /**
     * Returns the path only (without file name).
     *
     * @param file
     * @return
     */
    fun getPathWithoutFilename(file: File?): File? {
        file?.let { }
        if (file != null) {
            if (file!!.isDirectory) {
                // no file to be split off. Return everything
                return file
            } else {
                val filename = file!!.name
                val filepath = file!!.absolutePath

                // Construct path without file name.
                var pathwithoutname = filepath.substring(0,
                        filepath.length - filename.length)
                if (pathwithoutname.endsWith("/")) {
                    pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length - 1)
                }
                return File(pathwithoutname)
            }
        }
        return null
    }

    /**
     * @return The MIME type for the given file.
     */
    fun getMimeType(file: File): String? {

        val extension = getExtension(file.name)

        return if (extension!!.isNotEmpty()) MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension!!.substring(1)) else "application/octet-stream"

    }

    /**
     * @return The MIME type for the give Uri.
     */
    fun getMimeType(context: Context, uri: Uri?): String? {
        val file = File(getPath(context, uri)!!)
        return getMimeType(file)
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is [LocalStorageProvider].
     * @author paulburke
     */
    //    public static boolean isLocalStorageDocument(Uri uri) {
    //        return LocalStorageProvider.AUTHORITY.equals(uri.getAuthority());
    //    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                      selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor!!.moveToFirst()) {
                if (DEBUG)
                    DatabaseUtils.dumpCursor(cursor)

                val column_index = cursor!!.getColumnIndexOrThrow(column)
                return cursor!!.getString(column_index)
            }
        } finally {
            if (cursor != null)
                cursor!!.close()
        }
        return null
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br></br>
     * <br></br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     * @see .isLocal
     * @see .getFile
     */
    fun getPath(context: Context, uri: Uri?): String? {

        uri?.let {
            if (DEBUG)
                Log.d("$TAG File -",
                        "Authority: " + uri.authority +
                                ", Fragment: " + uri.fragment +
                                ", Port: " + uri.port +
                                ", Query: " + uri.query +
                                ", Scheme: " + uri.scheme +
                                ", Host: " + uri.host +
                                ", Segments: " + uri.pathSegments.toString()
                )

            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return (Environment.getExternalStorageDirectory()).toString() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
            } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)

            } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
                return uri.path
            }// File
            // MediaStore (and general)
        }

        return null
    }

    /**
     * Convert Uri into File, if possible.
     *
     * @return file A local file that the Uri was pointing to, or null if the
     * Uri is unsupported or pointed to a remote resource.
     * @author paulburke
     * @see .getPath
     */
    fun getFile(context: Context, uri: Uri?): File? {
        if (uri != null) {
            val path = getPath(context, uri)
            if (path != null && isLocal(path)) {
                return File(path!!)
            }
        }
        return null
    }

    /**
     * Get the file size in a human-readable string.
     *
     * @param size
     * @return
     * @author paulburke
     */
    fun getReadableFileSize(size: Int): String {
        val BYTES_IN_KILOBYTES = 1024
        val dec = DecimalFormat("###.#")
        val KILOBYTES = " KB"
        val MEGABYTES = " MB"
        val GIGABYTES = " GB"
        var fileSize = 0f
        var suffix = KILOBYTES

        if (size > BYTES_IN_KILOBYTES) {
            fileSize = (size / BYTES_IN_KILOBYTES).toFloat()
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES
                    suffix = GIGABYTES
                } else {
                    suffix = MEGABYTES
                }
            }
        }
        return (dec.format(fileSize.toDouble()) + suffix).toString()
    }

    /**
     * Attempt to retrieve the thumbnail of given File from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param file
     * @return
     * @author paulburke
     */
    fun getThumbnail(context: Context, file: File): Bitmap? {
        return getThumbnail(context, getUri(file), getMimeType(file))
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param uri
     * @param mimeType
     * @return
     * @author paulburke
     */
    @JvmOverloads
    fun getThumbnail(context: Context, uri: Uri?, mimeType: String? = getMimeType(context, uri)): Bitmap? {
        if (DEBUG)
            Log.d(TAG, "Attempting to get thumbnail")

        if (!isMediaUri(uri!!)) {
            Log.e(TAG, "You can only retrieve thumbnails for images and videos.")
            return null
        }

        var bm: Bitmap? = null
        if (uri != null) {
            val resolver = context.contentResolver
            var cursor: Cursor? = null
            try {
                cursor = resolver.query(uri!!, null, null, null, null)
                if (cursor!!.moveToFirst()) {
                    val id = cursor!!.getInt(0)
                    if (DEBUG)
                        Log.d(TAG, "Got thumb ID: $id")

                    if (mimeType!!.contains("video")) {
                        bm = MediaStore.Video.Thumbnails.getThumbnail(
                                resolver,
                                id.toLong(),
                                MediaStore.Video.Thumbnails.MINI_KIND, null)
                    } else if (mimeType!!.contains(MIME_TYPE_IMAGE)) {
                        bm = MediaStore.Images.Thumbnails.getThumbnail(
                                resolver,
                                id.toLong(),
                                MediaStore.Images.Thumbnails.MINI_KIND, null)
                    }
                }
            } catch (e: Exception) {
                if (DEBUG)
                    Log.e(TAG, "getThumbnail", e)
            } finally {
                if (cursor != null)
                    cursor!!.close()
            }
        }
        return bm
    }

    /**
     * Get the Intent for selecting content to be used in an Intent Chooser.
     *
     * @return The intent for opening a file with Intent.createChooser()
     * @author paulburke
     */
    fun createGetContentIntent(): Intent {
        // Implicitly allow the user to select a particular kind of data
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        // The MIME data type filter
        intent.type = "*/*"
        // Only return URIs that can be opened with ContentResolver
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        return intent
    }

    private fun openConnection(url: String): HttpURLConnection =
            try {
                with(URL(url)) {
                    val conn = (openConnection() as HttpURLConnection).apply {
                        // change this params
                        requestMethod = "GET"
                        setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ")
                        setRequestProperty("Accept", "*/*")
                        readTimeout = 10 * 1000
                        connectTimeout = 10 * 1000
                        connect()
                    }

                    // TODO: Gracefully handle error here, does this means should be catched on MalformedURL?
                    if (conn.contentLength < 1) {
                        // hotfix
                        (URL(BASE_IMAGE_URL_MOVIE_DB).openConnection() as HttpURLConnection).apply {
                            // change this params
                            requestMethod = "GET"
                            setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ")
                            setRequestProperty("Accept", "*/*")
                            readTimeout = 10 * 1000
                            connectTimeout = 10 * 1000
                            connect()
                        }
                    } else conn
                }
            } catch (e: MalformedURLException) {
                // hotfix
                // TODO: Gracefully handle error here
                (URL(BASE_IMAGE_URL_MOVIE_DB).openConnection() as HttpURLConnection).apply {
                    // change this params
                    requestMethod = "GET"
                    setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ")
                    setRequestProperty("Accept", "*/*")
                    readTimeout = 10 * 1000
                    connectTimeout = 10 * 1000
                    connect()
                }
            } catch (e: IOException) {
                (URL(BASE_IMAGE_URL_MOVIE_DB).openConnection() as HttpURLConnection).apply {
                    // change this params
                    requestMethod = "GET"
                    setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ")
                    setRequestProperty("Accept", "*/*")
                    readTimeout = 10 * 1000
                    connectTimeout = 10 * 1000
                    connect()
                }
            }


    private suspend fun openConnectionSuspend(url: String): HttpURLConnection = suspendCancellableCoroutine { cont ->
        with(URL(url)) {
            try {
                (openConnection() as HttpURLConnection).apply {
                    // change this params
                    requestMethod = "GET"
                    setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ")
                    setRequestProperty("Accept", "*/*")
                    readTimeout = 10 * 1000
                    connectTimeout = 10 * 1000
                    connect()

                    if (contentLength > 0 && !cont.isActive && cont.isCompleted)
                        cont.resume(this)
                    else {
                        // hotfix
                        // TODO: Gracefully handle error here
                        (URL(BASE_IMAGE_URL_MOVIE_DB).openConnection() as HttpURLConnection).apply {
                            // change this params
                            requestMethod = "GET"
                            setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ")
                            setRequestProperty("Accept", "*/*")
                            readTimeout = 10 * 1000
                            connectTimeout = 10 * 1000
                            connect()

                            cont.resume(this)
                        }
                    }
                }
            } catch (e: IOException) {
                cont.resumeWithException(e)
            }
        }
    }

    /**
     * Use to fetch network request using Retrofit
     * to obtain actual data from fileUrl
     */
    suspend fun getFileFromUrlChannel(context: Context?, fileUrl: String): Channel<Uri?> {
        var channel = Channel<Uri?>()

        coroutineLaunch(Dispatchers.IO) {
            var count: Int
            val dirTemp by lazy { createFile(getExternalStorageDirectory("/${context?.getString(R.string.app_folder)}/"), "temp") }
            var dirOutput: File by Delegates.observable(dirTemp) { prop, old, new -> }

            try {
                val conn = openConnection(fileUrl.replace("com:", "com"))
                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                val lenghtOfFile = conn.contentLength

                val raw = conn.getHeaderField("Content-Disposition")
                val responseCode = conn.responseCode
                val isError = responseCode >= 400

                when (responseCode) {
                    200 -> {
                        // TODO: Handle getting filename from direct link without Content-Disposition header
                        // Append timestamp to file name?
                        var fileName: String = raw
                                ?.let {
                                    with(raw) {
                                        // raw = "attachment; filename=abc.jpg"
                                        val chars: List<String>? = split("=")
                                        if (indexOf("=") != -1) {
                                            chars?.get(1)?.replace("\"", "") ?: ""
                                        } else {
                                            // TODO: fall back to random generated file name?
                                            // check for last type
                                            this
                                        }

                                    }
                                }
                                ?: "${context?.getString(R.string.app_name)}.jpg"
                        // External directory path to save file
                        dirOutput = createFile(getExternalStorageDirectory("/${context?.getString(R.string.app_folder)}/"), fileName)

                        // Output stream to write file
                        val outStream = FileOutputStream(dirOutput)

                        // input stream to read file
                        val inStream = if (isError) conn.errorStream else conn.inputStream

                        val data = ByteArray(1024 * 1024)
                        var total: Long = 0
                        while (true) {
                            count = inStream.read(data)
                            if (count < 0) break

                            total += count
                            outStream.write(data, 0, count)
                        }

                        // flushing output
                        outStream.flush()
                        // closing streams
                        outStream.close()
                        inStream.close()
                        conn.disconnect()
                    }

                    else -> channel.close(RuntimeException("error http"))
                }
            } catch (e: Exception) {
                channel.close(cause = e)
            }

            // Using FileProvider to prevent error FileNotFound, due to android security for URI
            channel.send(Uri.fromFile(dirOutput))
            channel.close()
        }
        return channel
    }

    /**
     * Use to fetch network request using Retrofit
     * to obtain actual data from url
     */
    suspend fun getFileFromUrlSuspend(context: Context, url: String): Uri? = suspendCancellableCoroutine { cont ->

        var count: Int
        val dirTemp by lazy { createFile(getExternalStorageDirectory(context.getString(R.string.app_name)), "temp") }
        var dirOutput: File by Delegates.observable(dirTemp) { prop, old, new ->

        }

        try {
            val url = URL(url)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ")
            conn.setRequestProperty("Accept", "*/*")
            conn.readTimeout = 10 * 1000
            conn.connectTimeout = 10 * 1000
            conn.connect()

            val raw = conn.getHeaderField("Content-Disposition")
            val responseCode = conn.responseCode
            val isError = responseCode >= 400
            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            val lenghtOfFile = conn.contentLength

            when (responseCode) {
                200 -> {
                    // TODO: Handle getting filename from direct link without Content-Disposition header
                    // Append timestamp to file name?
                    var fileName: String = with(raw) {
                        // raw = "attachment; filename=abc.jpg"
                        var chars: List<String> = split("=")
                        if (this != null && indexOf("=") != -1) {
                            chars[1].replace("\"", "")
                        } else {
                            // TODO: fall back to random generated file name?
                            // check for last type
                            "app_image.jpg"
                        }
                    }

                    // External directory path to save file
                    dirOutput = createFile(getExternalStorageDirectory(context.getString(R.string.app_name)), fileName)

                    // Output stream to write file
                    val outStream = FileOutputStream(dirOutput)

                    // input stream to read file
                    val inStream = if (isError) conn.errorStream else conn.inputStream

                    val data = ByteArray(1024 * 1024)
                    var total: Long = 0
                    while (true) {
                        count = inStream.read(data)
                        if (count < 0) break

                        total += count
                        outStream.write(data, 0, count)
                    }

                    // flushing output
                    outStream.flush()
                    // closing streams
                    outStream.close()
                    inStream.close()
                    conn.disconnect()
                }

                else -> cont.resumeWithException(RuntimeException("error http"))
            }
        } catch (e: Exception) {
            showLogError<FileUtils>(e.message ?: "error file utils")
            cont.resumeWithException(e)
        }

        // Using FileProvider to prevent error FileNotFound, due to android security for URI
        if (cont.isCompleted && !cont.isActive) cont.resume(Uri.fromFile(dirOutput))
    }

    private fun getOutputDirectory(applicationContext: Context): File {
        return File(applicationContext.filesDir, DIRECTORY_OUTPUTS).apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    private fun getExternalFilesDirectory(applicationContext: Context, fileName: String = "filename"): File {
        return File("${applicationContext.getExternalFilesDir(DIRECTORY_DOCUMENTS)}$separator").apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    private fun getExternalStorageDirectory(dirName: String = "directory"): File {
        return File("${Environment.getExternalStorageDirectory()}$dirName").apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    private fun createFile(file: File, fileName: String): File {
        return File(file, fileName).apply {
            if (exists())
                delete()
        }
    }
}//private constructor to enforce Singleton pattern