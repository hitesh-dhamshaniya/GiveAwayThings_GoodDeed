package give.away.good.deeds.core.base

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import give.away.good.deeds.core.extension.inflater


/**
 * @author Hitesh
 * @version 1.0
 * @since 06-07-2023
 */
abstract class BaseAdapter<T, VDB : ViewDataBinding, VH : RecyclerView.ViewHolder>(
    protected val itemList: MutableList<T> = ArrayList()
) : RecyclerView.Adapter<VH>() {

    companion object{
        fun <T : ViewDataBinding> getView(@LayoutRes res: Int, parent: ViewGroup) : T{
            return DataBindingUtil.inflate(parent.context.inflater, res, parent, false)
        }
    }

    constructor() : this(ArrayList())

    @LayoutRes
    protected abstract fun getLayout(): Int

    override fun getItemCount() = itemList.size

    open fun getItem(position: Int) = itemList[position]

    protected open fun getViewHolder(binding: VDB): VH {
        return object : RecyclerView.ViewHolder(binding.root) {} as VH
    }

    protected open fun getViewHolder(binding: VDB, viewType: Int): VH {
        return getViewHolder(binding)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val viewDataBinding: VDB = DataBindingUtil.inflate(parent.context.inflater, getLayout(), parent, false)
        return getViewHolder(viewDataBinding, viewType)
    }

    open fun refresh(list: List<T>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }
}