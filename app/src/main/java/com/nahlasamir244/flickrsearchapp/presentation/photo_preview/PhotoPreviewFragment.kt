package com.nahlasamir244.flickrsearchapp.presentation.photo_preview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nahlasamir244.flickrsearchapp.R

class PhotoPreviewFragment : Fragment() {

    companion object {
        fun newInstance() = PhotoPreviewFragment()
    }

    private lateinit var viewModel: PhotoPreviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.photo_preview_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PhotoPreviewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}