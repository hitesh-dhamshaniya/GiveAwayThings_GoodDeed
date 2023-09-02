package give.away.good.deeds.ui.screens.main.post

import give.away.good.deeds.ui.screens.main.messages.detail.ChatViewModel
import give.away.good.deeds.ui.screens.main.messages.list.ChatGroupViewModel
import give.away.good.deeds.ui.screens.main.post.add.AddPostViewModel
import give.away.good.deeds.ui.screens.main.post.common.PostViewModel
import give.away.good.deeds.ui.screens.main.post.detail.PostDetailViewModel
import give.away.good.deeds.ui.screens.main.post.list.PostListViewModel
import give.away.good.deeds.ui.screens.main.post.mypost.MyPostViewModel
import give.away.good.deeds.ui.screens.main.post.search.PostSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val postModule = module {
    viewModel {
        AddPostViewModel(get(), get(), get())
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
    viewModel {
        PostDetailViewModel(get(), get(), get(), get())
    }
    viewModel {
        PostViewModel(get())
    }

    viewModel {
        ChatGroupViewModel(get(), get())
    }
    viewModel {
        ChatViewModel(get(), get())
    }
}