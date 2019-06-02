package io.mochadwi.util.ext

import android.app.Activity
import android.app.AlertDialog

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 28/05/19
 * dedicated to build social-app
 *
 */

fun Activity.showDialogConfirmationUnit(
        title: String = "Pesan",
        msg: String = "logout",
        todo: () -> Unit
) {
    apply {
        AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Iya") { dialog, _ ->
                    dialog.apply {
                        cancel()
                        dismiss()
                    }
                    todo.invoke() // execute from outer dialog
                }
                .setNegativeButton("Tidak") { dialog, _ -> dialog.dismiss() }
                .show()
    }
}