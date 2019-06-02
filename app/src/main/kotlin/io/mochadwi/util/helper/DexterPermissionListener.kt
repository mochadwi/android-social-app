package io.mochadwi.util.helper

import android.app.Activity
import androidx.fragment.app.Fragment
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import io.mochadwi.R
import io.mochadwi.util.ext.openCamera
import io.mochadwi.util.ext.setPermission
import io.mochadwi.util.helper.AppHelper.Const.REQUEST_CODE_DIRECTORY
import io.mochadwi.util.helper.AppHelper.Const.REQUEST_CODE_PHOTO

open class DexterPermissionListener(private val activity: Activity,
                                    private val fragment: Fragment,
                                    private var requestCode: Int = 0) : PermissionListener {

    // TODO: Not suitable for singleton, due to requestCode will be changed on the runtime

    private var dialogPermissionListener: PermissionListener
    // TODO: Separate this, create a builder pattern instead
    val title = when (requestCode) {
        REQUEST_CODE_PHOTO -> R.string.message_cameratitle
        REQUEST_CODE_DIRECTORY -> R.string.message_directorytitle
        else -> R.string.message_defaulttitle
    }
    val message = when (requestCode) {
        REQUEST_CODE_PHOTO -> R.string.message_cameramessage
        REQUEST_CODE_DIRECTORY -> R.string.message_directorymessage
        else -> R.string.message_defaultmessage
    }

    init {
        dialogPermissionListener = DialogOnDeniedPermissionListener.Builder
                .withContext(activity)
                .withTitle(activity.getString(title))
                .withMessage(activity.getString(message))
                .withButtonText(activity.getString(android.R.string.ok))
                .build()
    }

    fun buildPermission(permission: String) {
        activity.setPermission(listener = this@DexterPermissionListener, strPermission = permission)
    }

    fun buildCompositePermission(permission: String, listener: () -> PermissionListener) {
        activity.setPermission(
                listener = CompositePermissionListener(dialogPermissionListener, listener.invoke()),
                strPermission = permission)
    }

    // TODO: Build multiple permission

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        // TODO: Change this allow access other feature, rather than only Camera
        response?.let {
            with(fragment) {
                when (it.permissionName) {
                    android.Manifest.permission.CAMERA -> openCamera(requestCode)
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE -> openCamera(requestCode)
                }
            }
        }
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {

    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {

    }
}