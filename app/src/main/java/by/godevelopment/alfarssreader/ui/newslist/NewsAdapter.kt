package by.godevelopment.alfarssreader.ui.newslist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.godevelopment.alfarssreader.R
import by.godevelopment.alfarssreader.commons.TAG
import by.godevelopment.alfarssreader.databinding.ItemNewsListBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class NewsAdapter(
    private val onClickRead: (String) -> Unit,
    private val onClickAdd: (String) -> Unit
) : RecyclerView.Adapter<NewsAdapter.ItemViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<NewsItemModel>() {
        override fun areItemsTheSame(oldItem: NewsItemModel, newItem: NewsItemModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NewsItemModel, newItem: NewsItemModel): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)
    var itemList: List<NewsItemModel>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    inner class ItemViewHolder(private val binding: ItemNewsListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(newsItemModel: NewsItemModel) {
            binding.apply {
                textTitle.text = newsItemModel.textTitle
                textAuthor.text = newsItemModel.textAuthor
                textDescription.text = newsItemModel.textDescription
                textPublishedAt.text = newsItemModel.textPublishedAt

                newsItemModel.urlToImage?.let {
                    Glide.with(root)
                        .load(it)
                        .centerCrop() // .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .error(R.drawable.image_not_loaded)
                        .placeholder(R.drawable.image)
                        .into(image)
                }

                val favPicRes = if (newsItemModel.isFavorite)
                    R.drawable.ic_baseline_favorite_24
                    else R.drawable.ic_baseline_favorite_border_24
                buttonAddNews.setImageResource(favPicRes)
                buttonAddNews.setOnClickListener {
                    Log.i(TAG, "buttonAddNews.setOnClickListener")
                    onClickAdd.invoke(newsItemModel.url)
                }
                buttonReadNews.setOnClickListener {
                    onClickRead.invoke(newsItemModel.url)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ItemViewHolder {
        return ItemViewHolder(
            ItemNewsListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
    }

    override fun onBindViewHolder(holder: NewsAdapter.ItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}