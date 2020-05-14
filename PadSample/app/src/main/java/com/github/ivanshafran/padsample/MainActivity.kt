package com.github.ivanshafran.padsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val LIST_TAG = "LIST"
        private const val DETAIL_TAG = "DETAIL"
    }

    private var detailContainer: Int = R.id.content_frame_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isTabletConfiguration = content_frame_layout == null
        val listContainer: Int
        if (isTabletConfiguration) {
            listContainer = R.id.left_content_frame_layout
            detailContainer = R.id.right_content_frame_layout
        } else {
            listContainer = R.id.content_frame_layout
            detailContainer = R.id.content_frame_layout
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(listContainer, ListFragment(), LIST_TAG)
                .commit()
        }
    }

    fun onOpenClick() {
        if (supportFragmentManager.findFragmentByTag(DETAIL_TAG) != null) {
            supportFragmentManager.popBackStack()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(detailContainer, DetailFragment(), DETAIL_TAG)
            .addToBackStack(null)
            .commit()
    }
}
