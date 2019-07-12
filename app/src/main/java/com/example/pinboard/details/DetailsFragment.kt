package com.example.pinboard.details

import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.cachelibrary.CacheApi.CacheApiModel
import com.example.cachelibrary.CacheLibrary
import com.example.pinboard.MainActivity
import com.example.pinboard.PinBoardApplication.Companion.applicationContext
import com.example.pinboard.R
import com.example.pinboard.model.Categories
import com.example.pinboard.model.PinModel
import com.example.pinboard.utils.Utilities

class DetailsFragment : Fragment() {

    private val fab by lazy { view!!.findViewById<View>(R.id.fab) }

    private val tvOutput by lazy { view!!.findViewById<TextView>(R.id.tvOutput) }

    private val imageViewPhoto by lazy { view!!.findViewById<ImageView>(R.id.imageViewPhoto) }

    private val imageViewCancel by lazy { view!!.findViewById<ImageView>(R.id.imageViewCancel) }
    private val imageViewReload by lazy { view!!.findViewById<ImageView>(R.id.imageViewReload) }

    private val textViewCategories by lazy { view!!.findViewById<TextView>(R.id.textViewCategories) }
    private val textViewLikes by lazy { view?.findViewById<TextView>(R.id.textViewLikes) }

    private val imageViewAvatar by lazy { view!!.findViewById<ImageView>(R.id.imageViewAvatar) }
    private val textViewAvatar by lazy { view?.findViewById<TextView>(R.id.textViewAvatar) }

    private lateinit var constraintDetails: ConstraintLayout

    private var pinModel: PinModel? = null

    private val cacheLibrary: CacheLibrary = CacheLibrary()
    private lateinit var cacheApiModel: CacheApiModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        cacheLibrary.init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pinModel = MainActivity.SELECTED_PIN
        if (pinModel == null) {
            tvOutput.setText(getString(R.string.pin_no_data))
        } else {
            val color = Color.parseColor(pinModel?.color)
            val dark: Boolean = Utilities().isDark(color)

            fab.setOnClickListener { result ->
                Utilities().openWebPage(pinModel?.links?.html.toString(), applicationContext())
            }

            constraintDetails = view.findViewById(R.id.constraintDetails)
            constraintDetails.setBackgroundColor(color)


            cacheApiModel = CacheApiModel(pinModel?.urls?.small.toString(), imageViewPhoto, R.drawable.nocover)
            cacheLibrary.load(cacheApiModel)


            imageViewPhoto.setOnClickListener({ result ->
                Utilities().openWebPage(pinModel?.urls?.full.toString(), applicationContext())
            })


            imageViewCancel.setOnClickListener({ result ->
                Toast.makeText(context, "Image pause request", Toast.LENGTH_SHORT).show()
                cacheLibrary.pauseRequest()
            })
            imageViewReload.setOnClickListener({ result ->
                Toast.makeText(context, "Image resume request", Toast.LENGTH_SHORT).show()
                cacheLibrary.resumeRequest(
                    CacheApiModel(
                        pinModel?.urls?.full.toString(),
                        imageViewPhoto,
                        R.drawable.nocover
                    )
                )
                cacheLibrary.resumeRequest(
                    CacheApiModel(
                        pinModel?.user?.profile_image?.small.toString(),
                        imageViewAvatar,
                        R.drawable.profile35
                    )
                )
            })


            val it: ListIterator<Categories>? = pinModel?.categories?.listIterator()
            if (listOf(it).count() > 0) {
                val stringBuilder = StringBuilder()
                stringBuilder.append(getString(R.string.category) + "\r\n")
                while (it!!.hasNext()) {
                    val item = it.next()
                    stringBuilder.append(item.title + " (" + item.photo_count + ")\r\n")
                }
                textViewCategories.setText(stringBuilder.toString())
                if (dark) textViewCategories?.setTextColor(Utilities().getResColor(applicationContext()))
            }

            textViewLikes?.setText(getString(R.string.likes, pinModel?.likes?.toString()))
            if (dark) textViewLikes?.setTextColor(Utilities().getResColor(applicationContext()))

            cacheApiModel =
                CacheApiModel(pinModel?.user?.profile_image?.small.toString(), imageViewAvatar, R.drawable.profile35)
            cacheLibrary.load(cacheApiModel)

            textViewAvatar?.setText(pinModel?.user?.name)

            imageViewAvatar.setOnClickListener({ result ->
                Utilities().openWebPage(pinModel?.user?.links?.html.toString(), applicationContext())
            })
            if (dark) textViewAvatar?.setTextColor(Utilities().getResColor(applicationContext()))

            textViewAvatar?.setOnClickListener({ result ->
                Utilities().openWebPage(pinModel?.user?.links?.html.toString(), applicationContext())
            })
        }
    }

}