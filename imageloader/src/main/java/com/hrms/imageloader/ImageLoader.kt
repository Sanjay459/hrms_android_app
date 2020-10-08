package com.hrms.imageloader

import android.view.Gravity
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject

class ImageLoader(private val picasso: Picasso) {

    fun load(imageRequest: ImageRequest): Single<Boolean> {

        val imageLoaded: SingleSubject<Boolean> = SingleSubject.create()

        return imageLoaded.hide()
            .doOnSubscribe {
                var requestCreator = if (imageRequest.url.isEmpty()) {
                    picasso.load(imageRequest.placeHolder)
                } else {
                    picasso.load(imageRequest.url)
                }

                if (imageRequest.placeHolder != -1) {
                    requestCreator = requestCreator.placeholder(imageRequest.placeHolder)
                }

                if (imageRequest.grayscale) {
                    requestCreator = requestCreator
                        .transform(GrayscaleTransformation())
                }

                imageRequest.imageView.let { view ->
                    if (view.width > 0 && view.height > 0) {
                        requestCreator = requestCreator
                            .resize(view.width, view.height)
                            .centerCrop(Gravity.CENTER_HORIZONTAL)
                    }
                }

                requestCreator
                    .into(imageRequest.imageView, object : Callback {

                        override fun onSuccess() {
                            imageLoaded.onSuccess(imageRequest.url.isNotEmpty())
                        }

                        override fun onError(e: Exception) {
                            imageLoaded.onSuccess(false)
                        }
                    })
            }
            .doOnDispose {
                picasso.cancelRequest(imageRequest.imageView)
            }
    }

    data class ImageRequest constructor(
        val url: String,
        val imageView: ImageView,
        val placeHolder: Int = -1,
        val grayscale: Boolean = false
    )
}
