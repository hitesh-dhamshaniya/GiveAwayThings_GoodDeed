package give.away.good.deeds.screens.search

import androidx.lifecycle.ViewModelProvider
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseFragment
import give.away.good.deeds.databinding.FragmentSearchBinding

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {
    override fun getLayout(): Int = R.layout.fragment_search

    override fun getViewModel(): SearchViewModel {
        return ViewModelProvider(this)[SearchViewModel::class.java]
    }

    override fun initViewModel() {

    }

    override fun onCreateView() {
    }
}