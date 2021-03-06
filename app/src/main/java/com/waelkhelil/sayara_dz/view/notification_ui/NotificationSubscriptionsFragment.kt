package com.waelkhelil.sayara_dz.view.notification_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.waelkhelil.sayara_dz.R


class NotificationSubscriptionsFragment : Fragment() {

    companion object {
        fun newInstance() = NotificationSubscriptionsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification_subscriptions, container, false)
    }

}
