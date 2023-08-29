package give.away.good.deeds.ui.screens.post_screens.messages

import androidx.lifecycle.ViewModelProvider
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseFragment
import give.away.good.deeds.databinding.FragmnetMessagesBinding

class MessageFragment : BaseFragment<FragmnetMessagesBinding, MessageViewModel>() {

    override fun getLayout(): Int = R.layout.fragmnet_messages

    override fun getViewModel(): MessageViewModel {
        return ViewModelProvider(this)[MessageViewModel::class.java]
    }

    override fun onCreateView() {
    }

    override fun initViewModel() {

    }
}