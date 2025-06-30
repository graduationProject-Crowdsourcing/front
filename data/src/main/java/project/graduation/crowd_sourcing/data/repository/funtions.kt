package project.graduation.crowd_sourcing.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream


internal fun compressAndResizeImage(context: Context, uri: Uri, fileName:String = "compressed.jpg"): MultipartBody.Part {
    val maxWidth = 1024
    val maxHeight = 1024

    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }

    val ratio = minOf(
        maxWidth / bitmap.width.toFloat(),
        maxHeight / bitmap.height.toFloat(),
        1f
    )
    val resized = Bitmap.createScaledBitmap(
        bitmap,
        (bitmap.width * ratio).toInt(),
        (bitmap.height * ratio).toInt(),
        true
    )

    val outputStream = ByteArrayOutputStream()
    resized.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
    val byteArray = outputStream.toByteArray()

    val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("file", fileName, requestBody)
}
