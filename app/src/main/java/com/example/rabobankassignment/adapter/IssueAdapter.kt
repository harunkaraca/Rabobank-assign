package com.example.rabobankassignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rabobankassignment.data.model.Headers
import com.example.rabobankassignment.data.model.Issue
import com.example.rabobankassignment.data.model.IssueDetail
import com.example.rabobankassignment.databinding.ItemIssueBinding
import com.example.rabobankassignment.util.getProgressDrawable
import com.example.rabobankassignment.util.loadImage

class IssueAdapter (var datas:Issue): RecyclerView.Adapter<IssueAdapter.IssueViewHolder>() {

    fun updateDatas(newDatas:Issue){
        datas.issues.clear()
        datas=newDatas
        notifyDataSetChanged()
    }

    class IssueViewHolder(private val binding:ItemIssueBinding): RecyclerView.ViewHolder(binding.root){
        private val progressDrawable= getProgressDrawable(binding.root.context)
        fun bind(headers: Headers,issue: IssueDetail){
            binding.tvTitleFirstName.text = headers.firstName
            binding.tvFirstName.text=issue.firstName
            binding.tvTitleSurname.text = headers.surName
            binding.tvSurname.text=issue.surName
            binding.tvTitleIssueCount.text = headers.issueCount
            binding.tvIssueCount.text=issue.issueCount.toString()
            binding.tvTitleDate.text = headers.dateOfBirth
            binding.tvDate.text=issue.dateOfBirth.toString()
            binding.ivAvatar.loadImage(issue.avatarUrl,progressDrawable)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        val itemViewBinding=ItemIssueBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return IssueViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        holder.bind(datas.headers,datas.issues[position])
    }

    override fun getItemCount()=datas.issues.size

}