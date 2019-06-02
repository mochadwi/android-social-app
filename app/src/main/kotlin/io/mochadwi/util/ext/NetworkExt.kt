package io.mochadwi.util.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationServices
import io.mochadwi.data.datasource.webservice.param.QueryParam
import io.mochadwi.util.helper.AppLocationManager
import io.mochadwi.util.helper.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.serialization.Mapper
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.net.URI

/**
 * Created by mochadwi on 12/26/2018.
 */

val MEDIA_TYPE_TEXT = MediaType.parse("text/plain")
val MEDIA_TYPE_IMAGE = MediaType.parse("image/*")
val MEDIA_TYPE_BINARY = MediaType.parse("application/octet-stream")

fun String?.toRequestBody(type: MediaType? = MEDIA_TYPE_TEXT): RequestBody {
    return RequestBody.create(type, this ?: "")
}

fun ByteArray?.toRequestBody(type: MediaType? = MEDIA_TYPE_BINARY): RequestBody {
    return RequestBody.create(type, this ?: ByteArray(0))
}

fun File?.toRequestBody(type: MediaType? = MEDIA_TYPE_IMAGE): RequestBody {
    return RequestBody.create(type, this ?: File(URI.create(Uri.EMPTY.path)))
}

fun RequestBody.toMultipartBody(partName: String = "image", file: File?): MultipartBody.Part =
        MultipartBody.Part.createFormData(partName, file?.name, this)

fun Context.prepareFilePart(partName: String = "image", fileUri: Uri): MultipartBody.Part {
    // create RequestBody instance from file
    val file = FileUtils.getFile(this, fileUri)
    val requestFile = RequestBody.create(
            MediaType.parse(this.contentResolver.getType(fileUri) ?: "image/*"), file)

    // MultipartBody.Part is used to send also the actual this name
    return MultipartBody.Part.createFormData(partName, file?.name, requestFile)
}

@Suppress("UNCHECKED_CAST")
private fun <T : Any?> T.textToRequestBody(): T = this.toString().toRequestBody() as T

// TODO: Build like BundleExt.kt for readable code and SOLID principles
@Suppress("UNCHECKED_CAST")
private inline fun <reified T : Any?> T?.anyToRequestBody(): RequestBody? =
        when (T::class) {
            ByteArray::class ->
                (this as ByteArray).toRequestBody()
            File::class ->
                if (this is File? && this != null) toRequestBody()
                else this.toString().toRequestBody()
            else ->
                this.toString().toRequestBody()
        }

fun AppCompatActivity.isProviderEnabled(provider: String): Boolean =
        AppLocationManager(this).isProviderEnabled(provider)

fun AppCompatActivity.getLastKnownLocation(provider: String): Location? =
        AppLocationManager(this).getLastKnownLocation(provider)

@SuppressLint("MissingPermission")
fun Activity.getLocation(todo: (Float, Float) -> Unit) {
    try {// GET MY CURRENT LOCATION
        val mFusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocation.lastLocation.addOnSuccessListener(this) { location ->
            coroutineLaunch(Dispatchers.Main) {
                delay(100)
                location?.let {
                    val (lat, long) = Pair(it.latitude.toFloat(), it.longitude.toFloat())
                    todo(lat, long)
                    toastSpammable("Lat : $lat Long : $long")
                }
            }
        }
    } catch (e: Exception) {
        requestPermissionLocation()
    }
}

fun requestApiForecast(lat: Float, long: Float) = mapOf("q" to "$lat,$long", "days" to "5")

fun Activity.buildAlertMessageNoGps() {
    val builder = AlertDialog.Builder(this)
    builder.setMessage("Your GPS seems to be disabled, please enable it first.")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                finish()
            }
    val alert = builder.create()
    try {
        alert.show()
    } catch (e: Exception) {
        Toast.makeText(this, "you must turn on your GPS first", Toast.LENGTH_SHORT).show()
        finish()
    }

}

@SuppressWarnings("Unchecked cast")
inline fun <reified RESPONSE> Response<RESPONSE>.processData(errMsg: String = "Not Successfull"): RESPONSE? =
        with(this) {
            val errBody = errorBody()?.string()
            body().apply {
                when {
                    (isSuccessful && body() != null) -> body()
                    /*this is StoreResponse ->
                        StoreResponse(error = ErrorModel(msg = "${error?.msg ?: errBody}"))
                    */
                }
            }
        }

fun QueryParam.toQueryMap(): Map<String, String> {
    val res: HashMap<String, String> = hashMapOf()

    Mapper.map(this).forEach { (t, u) ->
        res[t] = "$u"
    }

    return res
}
