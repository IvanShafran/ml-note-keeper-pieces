package com.github.ivanshafran.padsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

        val isLandscapeTabletConfiguration = content_frame_layout == null
        val listContainer: Int
        if (isLandscapeTabletConfiguration) {
            listContainer = R.id.left_content_frame_layout
            detailContainer = R.id.right_content_frame_layout
        } else {
            listContainer = R.id.content_frame_layout
            detailContainer = R.id.content_frame_layout
        }

        val oldListFragment: Fragment? = supportFragmentManager.findFragmentByTag(LIST_TAG)
        val newListFragment: Fragment = ListFragment()
        if (oldListFragment != null) {
            // Pass state from old to the new instance
            val state = supportFragmentManager.saveFragmentInstanceState(oldListFragment)
            newListFragment.setInitialSavedState(state)

            // remove fragment from the previous container
            supportFragmentManager.beginTransaction().remove(oldListFragment).commit()
        }

        // add fragment to the new container
        supportFragmentManager
            .beginTransaction()
            .add(listContainer, ListFragment(), LIST_TAG)
            .commit()

        val oldDetailFragment: Fragment? = supportFragmentManager.findFragmentByTag(DETAIL_TAG)
        if (oldDetailFragment != null) {
            val state = supportFragmentManager.saveFragmentInstanceState(oldDetailFragment)
            val newDetailFragment = DetailFragment()
            newDetailFragment.setInitialSavedState(state)

            supportFragmentManager.popBackStack()

            supportFragmentManager
                .beginTransaction()
                .add(detailContainer, newDetailFragment, DETAIL_TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    fun onOpenClick() {
        if (supportFragmentManager.findFragmentByTag(DETAIL_TAG) != null) {
            supportFragmentManager.popBackStack()
        }

        supportFragmentManager
            .beginTransaction()
            .add(detailContainer, DetailFragment(), DETAIL_TAG)
            .addToBackStack(null)
            .commit()
    }
}
