package com.cashzhang.nozdormu.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cashzhang.nozdormu.*
import com.cashzhang.nozdormu.Settings.email
import com.cashzhang.nozdormu.Settings.givenName
import com.cashzhang.nozdormu.databinding.LeftPageBinding
import com.cashzhang.nozdormu.network.FeedlyService
import com.cashzhang.nozdormu.service.UpdateService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

/**
 * Created by cz21 on 2018/5/22.
 */
class LeftFragment : Fragment() {
    private var binding: LeftPageBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = LeftPageBinding.inflate(inflater, container, false)

        binding?.accountsImg?.isClickable = true
        binding?.accountsImg?.setOnClickListener {
            val intent = Intent(getActivity(), LoginActivity::class.java)
            val authUrl = Constants.BASE_URL + Constants.AUTH_URL + Constants.RESPONSE_TYPE + Constants.CLIENT_ID + Constants.REDIRECT_URI + Constants.SCOPE
            val tokenUrl = Constants.BASE_URL + Constants.TOKEN_URL
            intent.putExtra("authurl", authUrl)
            intent.putExtra("tokenurl", tokenUrl)
            startActivityForResult(intent, 404)
        }
        if (email != null) {
            Log.d(TAG, "onCreate: Email=" + email)
            binding?.leftText!!.text = email
        }
        if (accessToken != "" && refreshToken != "") {
            Log.d(TAG, "token saved in SharedPreferences")
        }
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accessToken = Settings.accessToken
        Log.d(TAG, "onCreate: accessToken=" + accessToken)
        refreshToken = Settings.refreshToken
        Log.d(TAG, "onCreate: refreshToken=" + refreshToken)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == 404) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "auth success")
                accessToken = Settings.accessToken
                refreshToken = Settings.refreshToken
            }
        }
        val headers = HashMap<String, String?>()
        headers["Content-Type"] = "application/json"
        headers["X-Feedly-Access-Token"] = accessToken

        FeedlyService.INSTANCE.getProfile(headers)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {profile ->
                    Log.d(TAG, "onSuccess: profile.email = " + profile.email)
                    Log.d(TAG, "onSuccess: profile.FamilyName = Mr./Ms. " + profile.familyName)
                    Settings.id = profile.id
                    email = profile.email
                    givenName = profile.givenName
                    binding?.leftText?.text = email

                    Constants.appContext.startService(Intent(requireContext(), UpdateService::class.java))
                },
                onError = {

                }
            )

        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private val TAG = LeftFragment::class.java.simpleName
        private var accessToken: String? = null
        private var refreshToken: String? = null
        fun newInstance(): LeftFragment {
            return LeftFragment()
        }
    }
}