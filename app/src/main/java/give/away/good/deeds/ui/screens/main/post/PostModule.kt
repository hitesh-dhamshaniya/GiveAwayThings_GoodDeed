package give.away.good.deeds.ui.screens.main.post

import give.away.good.deeds.ui.screens.main.post.add.AddPostViewModel
import give.away.good.deeds.ui.screens.main.post.list.PostListViewModel
import give.away.good.deeds.ui.screens.main.post.mypost.MyPostViewModel
import give.away.good.deeds.ui.screens.main.post.search.PostSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val postModule = module {
    viewModel {
        AddPostViewModel(get(), get())
    }
    viewModel {
        PostListViewModel(get(), get())
    }
    viewModel {
        MyPostViewModel(get(), get())
    }
    viewModel {
        PostSearchViewModel(get(), get())
    }
}