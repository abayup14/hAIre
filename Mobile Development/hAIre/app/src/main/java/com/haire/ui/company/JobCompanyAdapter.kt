package com.haire.ui.company

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.haire.ListLowonganCompanyQuery
import com.haire.databinding.ItemJobsBinding
import java.util.Locale

class JobCompanyAdapter(
    private var listJobs: List<ListLowonganCompanyQuery.Lowongan?>,
    private val onItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<JobCompanyAdapter.JobViewHolder>(), Filterable {
    private var filteredList: List<ListLowonganCompanyQuery.Lowongan?> = listJobs

    inner class JobViewHolder(private var binding: ItemJobsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(jobs: ListLowonganCompanyQuery.Lowongan) {
            binding.apply {
//                Glide.with(root.context)
//                    .load("")
//                    .into(ivJobs)
                tvTitle.text = jobs.nama
                tvAddres.text = jobs.deskripsi
            }
            itemView.setOnClickListener {
                onItemClick(jobs.id ?: 0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemJobsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredList.size
    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(filteredList[position]!!)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val query = charSequence.toString().toLowerCase(Locale.getDefault())
                val filteredList = ArrayList<ListLowonganCompanyQuery.Lowongan>()
                for (item in listJobs) {
                    if (item?.nama?.toLowerCase(Locale.getDefault())?.contains(query) == true) {
                        filteredList.add(item)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredList = results.values as List<ListLowonganCompanyQuery.Lowongan>
                notifyDataSetChanged()
            }
        }
    }
}