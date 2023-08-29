package give.away.good.deeds.ui.screens.post_screens.post

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import give.away.good.deeds.core.extension.showToast
import com.google.firebase.Timestamp
import give.away.good.deeds.R
import give.away.good.deeds.core.base.BaseFragment
import give.away.good.deeds.databinding.FragmentAddPostBinding
import give.away.good.deeds.network.model.GiveAwayModel
import java.util.Calendar

class AddPostFragment : BaseFragment<FragmentAddPostBinding, AddPostViewModel>() {

    override fun getLayout(): Int = R.layout.fragment_add_post

    override fun getViewModel(): AddPostViewModel {
        return ViewModelProvider(this)[AddPostViewModel::class.java]
    }

    override fun initViewModel() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.createLiveData.observe(this){
            if(it){
                mActivity.showToast("Success")
            }else{
                mActivity.showToast("Fail")
            }
        }
    }

    override fun onCreateView() {


        mViewDataBinding.btnAddPost.setOnClickListener {
            mViewModel.createPost(
                GiveAwayModel(
                    "giveAwayPostId",
                    "https://firebasestorage.googleapis.com/v0/b/giveawaythings-gooddeed.appspot.com/o/teddy_bear.jpeg?alt=media&token=a0f05e90-3763-4841-9c21-fb522fdefbcf",
                    "Teddy Bear Kids Toys",
                    "https://firebasestorage.googleapis.com/v0/b/giveawaythings-gooddeed.appspot.com/o/UserProfile%2FAI_Photo.jpeg?alt=media&token=4f9f2687-8931-4916-b872-c88b6f976498",
                    1.5,
                    220,
                    "8NxqGzwWr9MSoHNaRVNypg6yr3l2",
                    21,
                    "Mon- Fri 10:00 - 5:00",
                    Timestamp(Calendar.getInstance().time),
                    50.81587925485042, -1.0800822101897536,
                    "Hitesh Dhamshaniya",
                    11,
                    false
                )
            )
        }


    }
}