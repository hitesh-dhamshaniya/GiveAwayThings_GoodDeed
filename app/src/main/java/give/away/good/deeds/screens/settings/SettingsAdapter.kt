package give.away.good.deeds.screens.settings

import androidx.recyclerview.widget.RecyclerView
import give.away.good.deeds.R
import give.away.good.deeds.core.RecyclerItemClickListener
import give.away.good.deeds.core.base.BaseAdapter
import give.away.good.deeds.databinding.AdapterSettingItemBinding
import give.away.good.deeds.network.model.SettingItemModel

class SettingsAdapter(settingItems: MutableList<SettingItemModel>, val listener: RecyclerItemClickListener) :
    BaseAdapter<SettingItemModel, AdapterSettingItemBinding, SettingsAdapter.SettingViewHolder>(settingItems) {

    override fun getLayout(): Int = R.layout.adapter_setting_item

    override fun getViewHolder(binding: AdapterSettingItemBinding): SettingViewHolder = SettingViewHolder(binding = binding)

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        val itemModel: SettingItemModel = getItem(position)
        holder.binding.model = itemModel
        holder.binding.ivIcon.setImageResource(itemModel.icon)
    }

    inner class SettingViewHolder(val binding: AdapterSettingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onItemClick(layoutPosition, getItem(layoutPosition))
            }
        }
    }
}