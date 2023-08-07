package give.away.good.deeds.screens.home

import androidx.recyclerview.widget.RecyclerView
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseAdapter
import give.away.good.deeds.databinding.AdapterGiveAwayItemsBinding
import give.away.good.deeds.network.model.GiveAwayModel

class GiveAwayAdapter(giveAwayItems: MutableList<GiveAwayModel>) : BaseAdapter<GiveAwayModel, AdapterGiveAwayItemsBinding, GiveAwayAdapter.ViewHolder>(giveAwayItems) {
    override fun getLayout(): Int = R.layout.adapter_give_away_items

    override fun getViewHolder(binding: AdapterGiveAwayItemsBinding): ViewHolder = ViewHolder(binding)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.model = item
    }

    class ViewHolder(val binding: AdapterGiveAwayItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }
}