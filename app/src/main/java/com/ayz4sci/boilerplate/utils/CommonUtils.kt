package com.ayz4sci.boilerplate.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.LabeledIntent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.telephony.PhoneNumberUtils
import android.text.Html
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object CommonUtils {

    val EMAIL_REGEX = "^[a-zA-Z][\\w\\.-]*[\\+]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$"
    val VALID_STRING_REGEX = "^[A-Za-z, ]++\$"

    fun setStatusBarColor(activity: Activity, colorResource: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.statusBarColor = activity.resources.getColor(colorResource)
        }
    }

    fun setTopStatusBarInset(ctx: Context, view: View) {
        // Set paddingTop of toolbar to height of status bar.
        // Fixes statusbar covers toolbar issue
        view.setPadding(0, getStatusBarHeight(ctx), 0, 0);
    }

    private fun getStatusBarHeight(ctx: Context): Int {
        var result = 0
        val resourceId = ctx.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = ctx.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getViewFromActivity(activity: Activity): View {
        return activity.window.decorView.rootView
    }

    fun getViewMeasuredHeight(activity: Activity, textView: View): Int {
        val measureWidth = View.MeasureSpec.makeMeasureSpec(getScreenWidth(activity), View.MeasureSpec.AT_MOST)
        val measureHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        textView.measure(measureWidth, measureHeight)
        return textView.measuredHeight
    }

    fun getScreenWidth(activity: Activity): Int {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels
    }

    fun getScreenHeight(activity: Activity): Int {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.heightPixels
    }

    fun getDIPValue(context: Context, pxValue: Int): Int {
        return (pxValue * getDeviceDensityScale(context) + 0.5f).toInt()
    }

    fun convertDpToPixel(context: Context, dp: Float): Int {
        return convertDpToFloatPixel(context, dp).toInt()
    }

    fun convertDpToFloatPixel(context: Context, dp: Float): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return px.toInt().toFloat()
    }

    fun getDeviceDensityScale(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    fun hasFragmentRemoved(fragment: Fragment?): Boolean {
        return (fragment == null
                || fragment.isRemoving
                || fragment.activity == null
                || fragment.activity!!.isFinishing)
    }

    fun dialPhoneNumber(activity: Activity, telNumber: String) {
        val number = "tel:$telNumber"
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse(number))
        activity.startActivity(callIntent)
    }

    fun openBrowser(activity: Activity, url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        activity.startActivity(i)
    }

    fun openDefaultMailbox(activity: Activity) {
        try {
            val emailIntent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"))

            val pm = activity.packageManager
            val resInfo = pm.queryIntentActivities(emailIntent, 0)
            if (resInfo.size > 0) {
                val ri = resInfo.get(0)
                // First create an intent with only the package name of the first registered email app
                // and build a picked based on it
                val intentChooser = pm.getLaunchIntentForPackage(ri.activityInfo.packageName)
                val openInChooser = Intent.createChooser(intentChooser, "Open email using: ")

                // Then create a list of LabeledIntent for the rest of the registered email apps
                val intentList = ArrayList<LabeledIntent>()
                resInfo.forEach {
                    val packageName = it.activityInfo.packageName
                    val intent = pm.getLaunchIntentForPackage(packageName)
                    intentList.add(LabeledIntent(intent, packageName, it.loadLabel(pm), it.icon))
                }
                // Add the rest of the email apps to the picker selection
                openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toTypedArray())
                activity.startActivity(openInChooser)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideSoftKeyboard(activity: Activity) {
        if (activity.currentFocus != null) {
            val inm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inm.hideSoftInputFromWindow(activity.currentFocus.windowToken, 0)
        }
    }

    fun showSoftKeyboard(activity: Activity, view: View) {
        if (activity.currentFocus != null) {
            val inm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            view.requestFocus()
            inm.showSoftInput(view, 0)
        }
    }

    fun isValidEmail(text: String): Boolean {
        return !text.isEmpty() && validateStringWithRegex(text, EMAIL_REGEX)
    }

    fun isValidPhone(text: String): Boolean {
        return !text.isEmpty() && PhoneNumberUtils.isGlobalPhoneNumber(text) && text.length == 10
    }

    fun isValidStringNoNumbers(text: String): Boolean {
        return !text.isEmpty() && validateStringWithRegex(text, VALID_STRING_REGEX)
    }

    private fun validateStringWithRegex(string: String, regex: String): Boolean {
        // Create a Pattern object
        val pattern = Pattern.compile(regex)

        // Now create matcher object.
        val matcher = pattern.matcher(string)
        return matcher.matches()
    }

    fun formatNumberToCurrency(amount: Long): String {
        return NumberFormat.getNumberInstance(Locale.US).format(amount)
    }

    fun saveFileToExternalStorage(fileContents: ByteArray, directoryName: String, fileName: String) {
        val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
        val folder = File(extStorageDirectory, directoryName)
        folder.mkdir()

        val pdfFile = File(folder, fileName)

        try {
            pdfFile.createNewFile()
            val fileOutputStream = FileOutputStream(pdfFile, false)
            fileOutputStream.write(fileContents)
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun getCurrentAppVersion(context: Context): Int {
        var versionCode = -1
        try {
            versionCode = context
                    .packageManager
                    .getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
                    .versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return versionCode
    }

    fun getFormattedDate(unformattedDate: String): String {
        var formattedDate: String
        val formatTo = SimpleDateFormat("dd/MM/yy")
        val formatFrom = SimpleDateFormat("yyyy-MM-dd")
        try {
            val date = formatFrom.parse(unformattedDate)
            formattedDate = formatTo.format(date)
        } catch (e: ParseException) {
            formattedDate = unformattedDate
        }

        return formattedDate
    }

    fun getDateFromString(stringDate: String): Date? {
        val formatFrom = SimpleDateFormat("yyyy-MM-dd")
        try {
            return formatFrom.parse(stringDate)
        } catch (e: ParseException) {
            return null
        }

    }

    fun getFormattedDateWithMonthName(unformattedDate: String): String {
        var formattedDate: String
        val formatTo = SimpleDateFormat("MMMM dd, yyyy")
        val formatFrom = SimpleDateFormat("yyyy-MM-dd")
        try {
            val date = formatFrom.parse(unformattedDate)
            formattedDate = formatTo.format(date)
        } catch (e: ParseException) {
            formattedDate = unformattedDate
        }

        return formattedDate
    }

    fun getFormattedDateFromDDMMYY(unformattedDate: String): String {
        var formattedDate: String
        val formatTo = SimpleDateFormat("MMMM dd, yyyy")
        val formatFrom = SimpleDateFormat("dd/MM/yy")
        try {
            val date = formatFrom.parse(unformattedDate)
            formattedDate = formatTo.format(date)
        } catch (e: ParseException) {
            formattedDate = unformattedDate
        }

        return formattedDate
    }

    fun underLineTextView(textView: TextView, text: String) {
        textView.text = Html.fromHtml("<b><u>$text</u></b>")
    }

    fun loadJSONFromAsset(context: Context, jsonFileName: String): String? {
        val json: String?
        try {
            val `is` = context.assets.open(jsonFileName)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    fun isYesterday(date: Long): Boolean {
        val now = Calendar.getInstance()
        val cdate = Calendar.getInstance()
        cdate.timeInMillis = date

        now.add(Calendar.DATE, -1)

        return (now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE))
    }

    fun isTomorrow(date: Long): Boolean {
        val now = Calendar.getInstance()
        val cdate = Calendar.getInstance()
        cdate.timeInMillis = date

        now.add(Calendar.DATE, +1)

        return (now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE))
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    fun querifyJSONString(unparsedString: String): String {
        try {
            val sb = StringBuilder()
            val json: JSONObject?
            json = JSONObject(unparsedString)
            val keys = json.keys()
            //            sb.append("?"); //start of query args
            while (keys.hasNext()) {
                val key = keys.next()
                sb.append(key)
                sb.append("=")
                sb.append(json.get(key))

                if (keys.hasNext()) {
                    sb.append("&")
                }
            }
            return sb.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
            return ""
        }

    }

    fun getEncodedBytes(data: String?, charset: String?): ByteArray {
        if (data == null) {
            throw IllegalArgumentException("data may not be null")
        }

        if (charset == null || charset.length == 0) {
            throw IllegalArgumentException("charset may not be null or empty")
        }

        try {
            return data.toByteArray(charset(charset))
        } catch (e: UnsupportedEncodingException) {
            return data.toByteArray()
        }

    }

    fun extractDecimalsFromDouble(amount: Double): String {
        return String.format("%02d", (amount % 1 * 100).toInt())
    }
}
