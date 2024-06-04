package com.example.rabobankassignment.main

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rabobankassignment.R
import com.example.rabobankassignment.adapter.IssueAdapter
import com.example.rabobankassignment.databinding.FragmentMainBinding
import com.example.rabobankassignment.util.setupSnackbar
import com.example.rabobankassignment.util.showSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.jar.Manifest


@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel>()
    private val issueAdapter= IssueAdapter(arrayListOf())
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvList.apply {
            layoutManager= LinearLayoutManager(context)
            adapter=issueAdapter
        }
        observeViewModel()
        binding.btnDownload.setOnClickListener {
            viewModel.downloadFile(binding.etUrl.text.toString())
            it.isEnabled=false
        }
        checkStoragePermission()
    }

    private fun observeViewModel() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_LONG)
        viewModel.items.observe(viewLifecycleOwner, Observer {datas->
            datas?.let {
                binding.rcvList.visibility=View.VISIBLE
                issueAdapter.updateDatas(it)
            }
        })
        viewModel.dataLoading.observe(viewLifecycleOwner, Observer {isLoading->
            isLoading?.let {
                binding.btnDownload.isEnabled=true
                binding.clProgress.visibility=if(it)View.VISIBLE else View.GONE
                if(it){
                    binding.tvError.visibility=View.GONE
                    binding.rcvList.visibility=View.GONE
                }
            }
        })
        viewModel.isDataLoadingError.observe(viewLifecycleOwner) { isDataLoadingError ->
            isDataLoadingError?.let {
                binding.tvError.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val writePermissionGranted = permissions[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: false
        val readPermissionGranted = permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE] ?: false

        if (writePermissionGranted && readPermissionGranted) {
            // Permissions granted
            binding.btnDownload.isEnabled=true
        } else {
            // Permissions denied
            viewModel.showSnackbarMessage(getString(R.string.permissions_denied_to_read_write_external_storage))
        }
    }
    private fun checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.btnDownload.isEnabled = true
        } else {
            // For Android versions below Q, request both READ and WRITE permissions
            val hasWritePermission = ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            val hasReadPermission = ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasWritePermission || !hasReadPermission) {
                requestPermissionsLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
            } else {
                binding.btnDownload.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}