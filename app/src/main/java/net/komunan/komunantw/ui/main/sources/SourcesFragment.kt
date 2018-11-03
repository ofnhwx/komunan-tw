package net.komunan.komunantw.ui.main.sources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.simple_recycler_view.*
import net.komunan.komunantw.R
import net.komunan.komunantw.observeOnNotNull
import net.komunan.komunantw.ui.common.TWBaseFragment

class SourcesFragment: TWBaseFragment() {
    companion object {
        @JvmStatic
        fun create() = SourcesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.simple_recycler_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(SourcesViewModel::class.java)
        viewModel.sources.observeOnNotNull(this) { sources ->
            container.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            container.adapter = SourcesAdapter(sources)
        }
    }
}
