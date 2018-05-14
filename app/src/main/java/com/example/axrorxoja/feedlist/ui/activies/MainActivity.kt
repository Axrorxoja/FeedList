package com.example.axrorxoja.feedlist.ui.activies

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.animation.OvershootInterpolator
import com.example.axrorxoja.feedlist.App
import com.example.axrorxoja.feedlist.R
import com.example.axrorxoja.feedlist.common.PostGen
import com.example.axrorxoja.feedlist.storage.db.IPostDAO
import com.example.axrorxoja.feedlist.storage.db.Post
import com.example.axrorxoja.feedlist.storage.pref.IPreference
import com.example.axrorxoja.feedlist.ui.adapter.PostAdapter
import com.example.axrorxoja.feedlist.ui.customs.EndlessRecyclerOnScrollListener
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val pref: IPreference by lazy { (application as App).pref }
    private val postDAO: IPostDAO by lazy { (application as App).db.loadPostDAO() }
    private val postGen: PostGen by lazy { PostGen() }
    private val adapter: PostAdapter by lazy { PostAdapter() }
    private val cd = CompositeDisposable()
    private val handler: Handler by lazy { Handler() }
    /*
    * flag for check activity is visible and incrementing fake post data
    * if is #0 activity is not visible
    */
    private var isNotVisible = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        genData()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        initRV()
        tvClear.setOnClickListener { onClear() }
        btReload.setOnClickListener { btReload.postDelayed({ onReload() }, 1000) }
        swipe.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        loadNewerPosts()
        scrollToTop()
        swipe.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        isNotVisible = 0
    }

    private fun loadNewerPosts() {
        val id = adapter.getItemId(0) + 10
        val count = adapter.itemCount + 10
        log("loadNewerPosts:last item id $id count $count")
        loadPostPart(id, count)
    }

    private fun scrollToTop() = rv.postDelayed({ rv.smoothScrollToPosition(0) }, 200)

    private fun initRV() {
        rv.adapter = adapter
        rv.itemAnimator.addDuration = 1000
        rv.itemAnimator.removeDuration = 1000
        rv.itemAnimator.moveDuration = 1000
        rv.itemAnimator.changeDuration = 1000
        rv.itemAnimator = SlideInDownAnimator(OvershootInterpolator())
        val onScrollEndLess = EndlessRecyclerOnScrollListener(rv.layoutManager as LinearLayoutManager, this::onScrollEndless)
        rv.addOnScrollListener(onScrollEndLess)
    }

    private fun onScrollEndless() {
        val id = adapter.getItemId(0)
        val count = adapter.itemCount + 10
        log("loadPostPart: last item id $id count $count ")
        loadPostPart(id, count)
    }

    private fun log(message: String) = Log.d("some tag", message)

    private fun loadPostPart(id: Long, count: Int) {
        cd.add(postDAO.loadOlderPostsById(id, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::submitList)
        )
    }

    private fun genData() {
        if (pref.isFirst()) {
            handler.postDelayed(this::addPostTask, 1000)
            pref.setIsFirst()
        }
    }

    private fun addPostTask() {
        if (isNotVisible < 20) {
            cd.add(Single.fromCallable { postDAO.addPost(postGen.loadPosts()) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer { loadAll() }))
            handler.postDelayed(this::addPostTask, 1000)
            isNotVisible++
        }
    }

    @SuppressLint("SetTextI18n", "StringFormatInvalid")
    private fun loadAll() {
        if (rv.visibility == View.VISIBLE) {
            val newItemsCount = isNotVisible * 5 - adapter.itemCount
            updateView.visibility = View.VISIBLE
            updateView.text = getString(R.string.new_posts, newItemsCount)
            updateView.setOnClickListener { loadAllPosts() }
        }
    }

    private fun loadAllPosts() {
        cd.add(postDAO.loadAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoad))
    }

    private fun onLoad(list: List<Post>) {
        updateView.visibility = View.INVISIBLE
        adapter.submitList(list)
        scrollToTop()
    }

    private fun onReload() {
        hideWithAnimate(btReload)
        hideWithAnimate(tv)
        handler.postDelayed(this::otherTask, 200)
    }

    private fun otherTask() {
        rv.visibility = View.VISIBLE
        tvClear.isEnabled = true
        cd.add(postDAO.loadPart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::submitList))
    }

    private fun onClear() {
        pref.clear()
        updateView.visibility = View.INVISIBLE
        cd.add(Single.fromCallable { postDAO.deleteAll() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { adapter.submitList(emptyList()) }))
    }

    override fun onPause() {
        super.onPause()
        cd.clear()
    }

    private fun hideWithAnimate(view: View) {
        view.animate()
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setDuration(200)
                .setInterpolator(LinearOutSlowInInterpolator())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        view.visibility = View.VISIBLE
                    }

                    override fun onAnimationCancel(animation: Animator) = Unit

                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = View.INVISIBLE
                    }
                })
    }

}
