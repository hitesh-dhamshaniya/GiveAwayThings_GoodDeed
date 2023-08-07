package give.away.good.deeds.screens.mylisting

import androidx.recyclerview.widget.RecyclerView
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseAdapter
import give.away.good.deeds.databinding.AdapterMyGaListBinding
import give.away.good.deeds.network.model.MyGiveAwayModel

class MyGiveAwayListAdapter(myGiveAwayList: MutableList<MyGiveAwayModel>) :
    BaseAdapter<MyGiveAwayModel, AdapterMyGaListBinding, MyGiveAwayListAdapter.ViewHolder>(myGiveAwayList) {
    override fun getLayout(): Int = R.layout.adapter_my_ga_list

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.model = item
    }

    override fun getViewHolder(binding: AdapterMyGaListBinding): ViewHolder = ViewHolder(binding)

    class ViewHolder(val binding: AdapterMyGaListBinding) : RecyclerView.ViewHolder(binding.root) {}
}