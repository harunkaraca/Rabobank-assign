package com.example.rabobankassignment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rabobankassignment.R
import com.example.rabobankassignment.adapter.IssueAdapter
import com.example.rabobankassignment.data.model.Issue
import com.example.rabobankassignment.databinding.FragmentMainBinding
import com.example.rabobankassignment.util.setupSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel>()
    private val issueAdapter= IssueAdapter(Issue())
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
            if(binding.etUrl.text.toString().isNotEmpty()){
                viewModel.downloadFile(binding.etUrl.text.toString())
            }else{
                viewModel.showSnackbarMessage(getString(R.string.url_can_not_be_empty))
            }
        }
    }

    private fun observeViewModel() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_LONG)
        viewModel.items.observe(viewLifecycleOwner, Observer {datas->
            datas?.let {
                if(it.issues.isNotEmpty()){
                    binding.rcvList.visibility=View.VISIBLE
                    issueAdapter.updateDatas(it)
                }
                else{
                    binding.rcvList.visibility=View.INVISIBLE
                    issueAdapter.updateDatas(it)
                }
            }
        })
        viewModel.dataLoading.observe(viewLifecycleOwner, Observer {isLoading->
            isLoading?.let {
                if(it){
                    binding.btnDownload.isEnabled=false
                    binding.clProgress.visibility=View.VISIBLE
                }else{
                    binding.btnDownload.isEnabled=true
                    binding.clProgress.visibility=View.GONE
                }
            }
        })
        viewModel.isDataLoadingError.observe(viewLifecycleOwner) { isDataLoadingError ->
            isDataLoadingError?.let {
                if(!it){
                    binding.rcvList.visibility=View.VISIBLE
                    binding.tvError.visibility=View.GONE
                }else{
                    binding.rcvList.visibility=View.GONE
                    binding.tvError.visibility=View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}