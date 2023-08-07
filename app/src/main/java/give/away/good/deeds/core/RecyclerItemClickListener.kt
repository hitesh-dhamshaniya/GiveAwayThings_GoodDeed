package give.away.good.deeds.core

interface RecyclerItemClickListener {
    fun <T> onItemClick(position: Int, item: T)
}