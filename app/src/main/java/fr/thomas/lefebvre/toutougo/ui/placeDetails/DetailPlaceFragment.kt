package fr.thomas.lefebvre.toutougo.ui.placeDetails


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentDetailPlaceBinding
import fr.thomas.lefebvre.toutougo.ui.place.PlaceViewModel
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import fr.thomas.lefebvre.toutougo.database.model.Comment
import fr.thomas.lefebvre.toutougo.ui.userDashboard.DashBoardViewModel
import kotlinx.android.synthetic.main.alert_dialog_confirm.view.*
import kotlinx.android.synthetic.main.alert_dialog_save.view.*


/**
 * A simple [Fragment] subclass.
 */
class DetailPlaceFragment : Fragment() {

    private lateinit var viewModel: PlaceViewModel
    private lateinit var viewModelDashBord: DashBoardViewModel
    private lateinit var binding: FragmentDetailPlaceBinding

    private lateinit var adapter: CommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(PlaceViewModel::class.java)

        viewModelDashBord = ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)


        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail_place, container, false)
        binding.viewModel = viewModel

        binding.lifecycleOwner = activity


        Log.d("DETAIL PLACE", viewModel.detailPlace.value!!.name)
        viewModel.getPhoto()

        viewModel.listPhotoPlace.observe(this, Observer { listPhoto ->
            if (listPhoto != null) {
                val carouselView = binding.carouselViewDetailPlacePhoto as CarouselView
                carouselView.setImageListener(imageListener)
                carouselView.pageCount = viewModel.listPhotoPlace.value!!.size

            }

        })

        adapter = CommentAdapter(CommentListener { comment ->
                clickDeleteComment(comment)
        }, viewModelDashBord.currentUserUid)

        binding.recyclerViewComment.adapter = adapter

        viewModel.getComment()

        viewModel.listComment.observe(this, Observer { comment ->

            adapter.submitList(comment)


            viewModel.descriptionComment.value = null
        })


        clickOnButtonMap()

        clickOnAddComment()

        onBackButton()


        return binding.root
    }


    //-------------IMAGE LISTENER FOR CAROUSEL VIEW----------

    var imageListener: ImageListener = object : ImageListener {
        override fun setImageForPosition(position: Int, imageView: ImageView?) {
            Picasso.get().load(viewModel.listPhotoPlace.value?.get(position)?.photoUrl)
                .into(imageView)
        }

    }


    //-------------BUTTON CLICK----------

    private fun clickDeleteComment(comment:Comment) {

        alertDialogCommentDelete(comment)
    }

    private fun clickOnButtonMap() {
        binding.imageButtonNavigationMap.setOnClickListener {
            // Creates an Intent that will navigate on place
            val latString = viewModel.detailPlace.value!!.lat.toString()
            val lngString = viewModel.detailPlace.value!!.lng.toString()

            val gmmIntentUri = Uri.parse("google.navigation:q=$latString,$lngString")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private fun clickOnAddComment() {
        binding.buttonAddComment.setOnClickListener {
            if (viewModel.checkAllInfosComment()) {
                alertDialogComment()
            } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.complete_infos),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }

        }
    }

    private fun onBackButton() {
        binding.imageButtonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    //-------------ALERT DIALOG----------

    private fun alertDialogComment() {
        val mDialog = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialog_confirm, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialog)
        val mAlertDialog = mBuilder.show()
        mDialog.buttonLike.setOnClickListener {

            viewModel.createComment(true)
            viewModel.getComment()


            mAlertDialog.dismiss()
        }
        mDialog.buttonDisLike.setOnClickListener {

            viewModel.createComment(false)
            mAlertDialog.dismiss()
            viewModel.getComment()
        }

    }

    private fun alertDialogCommentDelete(comment:Comment) {
        val mDialog = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialog_save, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialog)
        val mAlertDialog = mBuilder.show()
        mDialog.buttonYes.setOnClickListener {

            viewModel.deleteComment(comment.uid)
            viewModel.getComment()


            mAlertDialog.dismiss()
        }
        mDialog.buttonNo.setOnClickListener {


            mAlertDialog.dismiss()

        }

    }


}
