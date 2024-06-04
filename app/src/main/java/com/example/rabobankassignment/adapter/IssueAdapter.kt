package com.example.rabobankassignment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rabobankassignment.R
import com.example.rabobankassignment.data.model.Issue
import com.example.rabobankassignment.databinding.ItemIssueBinding
import com.example.rabobankassignment.util.getProgressDrawable
import com.example.rabobankassignment.util.loadImage

class IssueAdapter (var datas:MutableList<Issue>): RecyclerView.Adapter<IssueAdapter.IssueViewHolder>() {

    fun updateDatas(newDatas:List<Issue>){
        datas.clear()
        datas.addAll(newDatas)
        notifyDataSetChanged()
    }

    class IssueViewHolder(private val binding:ItemIssueBinding): RecyclerView.ViewHolder(binding.root){
        private val progressDrawable= getProgressDrawable(binding.root.context)
        fun bind(issue: Issue){
            binding.tvFirstName.text=issue.firstName
            binding.tvSurname.text=issue.surName
            binding.tvIssueCount.text=issue.issueCount.toString()
            binding.tvDate.text=issue.dateOfBirth.toString()
            binding.ivAvatar.loadImage(issue.avatarUrl,progressDrawable)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        val itemViewBinding=ItemIssueBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return IssueViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount()=datas.size

}