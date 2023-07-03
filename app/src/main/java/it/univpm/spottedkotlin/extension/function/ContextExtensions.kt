package it.univpm.spottedkotlin.extension.function

import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.databinding.DataBindingUtil
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.view.MainActivity

fun Context.loadColor(res: Int) = this.resources.getColor(res, theme)
fun Context.loadDrawable(res: Int) = AppCompatResources.getDrawable(this, res)
inline fun <reified T : AppCompatActivity> Context.getActivity(): T? {
	var context = this
	while (context is ContextWrapper) {
		if (context is T) return context
		context = context.baseContext
	}
	return null
}

inline fun <reified T : AppCompatActivity> Context.restartActivity(){
	this.getActivity<T>()?.finish()
	ContextCompat.startActivity(this, Intent(this, T::class.java), null)
}

fun <T> Context.inflate(layout: Int, parent: ViewGroup? = null, attachToParent: Boolean = false) =
	DataBindingUtil.inflate(
		LayoutInflater.from(this), layout, parent, attachToParent
	) as T

fun Context.checkPermission(vararg permission: String): Boolean = permission.all {
	PermissionChecker.checkSelfPermission(this, it) == PermissionChecker.PERMISSION_GRANTED
}

fun Context.loadBitmapDrawable(res: Int, width: Int, height: Int) = BitmapDrawable(
	this.resources, Bitmap.createScaledBitmap(
		(this.loadDrawable(res) as BitmapDrawable).bitmap, width, height, true
	)
)

fun Context.loadBitmapDrawable(res: Bitmap, width: Int, height: Int) = BitmapDrawable(
	this.resources, Bitmap.createScaledBitmap(res, width, height, true)
)

fun Context.showAlertDialog(
	title: String,
	message: String? = null,
	view: View? = null,
	positiveText: String = "Si",
	positiveCallback: (() -> Unit)? = null,
	negativeText: String = "No",
	negativeCallback: (() -> Unit)? = null,
	neutralText: String = "Ok",
	neutralCallback: (() -> Unit)? = null,
	options: List<String> = listOf(),
	checked:Int=0,
	radioCallback: ((Int) -> Unit)? = null,
) {
	AlertDialog.Builder(this).setTitle(title).apply {
		message?.let { setMessage(it) }
		view?.let { setView(it) }
		positiveCallback?.let { setPositiveButton(positiveText) { _, _ -> it() } }
		negativeCallback?.let { setNegativeButton(negativeText) { _, _ -> it() } }
		neutralCallback?.let { setNeutralButton(neutralText) { _, _ -> it() } }
		radioCallback?.let {
			setSingleChoiceItems(
				ArrayAdapter(this@showAlertDialog, R.layout.simple_list_item_single_choice, options),checked
			) { _, position -> it(position) }
		}
	}.create().show()
}

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()