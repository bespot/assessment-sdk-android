package com.bespot.sdk.mock.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bespot.sdk.Store
import com.bespot.sdk.mock.sample.databinding.ActivitySessionBinding

class SessionActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SessionActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
        }
    }

    private lateinit var model: SessionViewModel
    private lateinit var binding: ActivitySessionBinding
    private lateinit var adapter: StatusListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this).get(SessionViewModel::class.java)
        binding = ActivitySessionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle()

        setSupportActionBar(binding.toolbar)

        model.lastStatus().observe(
            this,
            {
                binding.lastStatus.text =
                    if (it.status == InOutStatus.UNVERIFIED) getString(R.string.status_calculating) else it.status.name
                when (it.status) {
                    InOutStatus.INSIDE -> {
                        binding.lastStatus.setChipBackgroundColorResource(R.color.status_in)
                        binding.lastStatus.setChipIconResource(R.drawable.ic_status_in)
                    }
                    InOutStatus.OUTSIDE -> {
                        binding.lastStatus.setChipBackgroundColorResource(R.color.status_out)
                        binding.lastStatus.setChipIconResource(R.drawable.ic_status_out)
                    }
                    InOutStatus.ERROR -> {
                        binding.lastStatus.setChipBackgroundColorResource(R.color.status_error)
                        binding.lastStatus.setChipIconResource(R.drawable.ic_status_error)
                        model.unsubscribe()
                    }
                    else -> {
                        binding.lastStatus.setChipIconResource(R.drawable.ic_status_unknown)
                    }
                }
            }
        )

        // Setup status list
        adapter = StatusListAdapter()
        binding.list.adapter = adapter
        model.statusList().observe(
            this,
            {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
                binding.list.postDelayed(
                    { binding.list.scrollToPosition(adapter.itemCount - 1) },
                    10
                )
            }
        )

        // Setup subscribe/unsubscribe button
        binding.subscribe.setOnClickListener { model.subscribe() }
        binding.unsubscribe.setOnClickListener {
            binding.lastStatus.setText(R.string.status_waiting)
            binding.lastStatus.setChipBackgroundColorResource(R.color.status_unknown)
            binding.lastStatus.setChipIconResource(R.drawable.ic_status_unknown)
            model.unsubscribe()
        }
        model.isSubscribed().observe(
            this,
            {
                binding.subscribe.visibility = if (it) View.GONE else View.VISIBLE
                binding.unsubscribe.visibility = if (it) View.VISIBLE else View.GONE
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        model.unsubscribe()
        super.onDestroy()
    }

    private fun setTitle() {
        binding.toolbar.title = getString(R.string.no_store_mode)
    }
}
