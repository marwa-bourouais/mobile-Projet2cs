package com.waelkhelil.sayara_dz.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.waelkhelil.sayara_dz.R


class UserContentFragment : Fragment() {

    companion object {
        fun newInstance() = UserContentFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_content, container, false)
    }

}
