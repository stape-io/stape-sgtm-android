package io.stape.sgtm.screens.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import io.stape.sgtm.App
import io.stape.sgtm.R
import io.stape.sgtm.databinding.FragmentMainBinding
import io.stape.sgtm.models.EventData

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabOpenLogs.setOnClickListener {
            findNavController().navigate(R.id.action_main_to_events_navigation)
        }

        binding.buttonFbSimple.setOnClickListener {
            App.instance.analytics.logEvent("simple_event") {
                param(FirebaseAnalytics.Param.ITEM_ID, "fbIdSimple")
                param(FirebaseAnalytics.Param.ITEM_NAME, "fbNameSimple")
                param(FirebaseAnalytics.Param.CONTENT_TYPE, "fbImageSimple")
                param(FirebaseAnalytics.Param.SCREEN_NAME, "db main screen")
            }
        }

        binding.buttonFbNested.setOnClickListener {
            App.instance.analytics.logEvent("nested_event") {
                param(FirebaseAnalytics.Param.SCREEN_NAME, "list of items")
                param(FirebaseAnalytics.Param.ITEM_LIST_ID, 123)
                param(FirebaseAnalytics.Param.ITEMS,
                        arrayOf(
                                bundleOf(
                                        FirebaseAnalytics.Param.ITEM_ID to "id1",
                                        FirebaseAnalytics.Param.ITEM_NAME to "name1",
                                        FirebaseAnalytics.Param.CONTENT_TYPE to "image1",
                                ),
                                bundleOf(
                                        FirebaseAnalytics.Param.ITEM_ID to "id2",
                                        FirebaseAnalytics.Param.ITEM_NAME to "name2",
                                        FirebaseAnalytics.Param.CONTENT_TYPE to "image2",
                                )
                        )
                )
            }
        }

        binding.buttonSgtmSimple.setOnClickListener {
            sendSGTMEvent(
                "data_test_event", EventData(
                    clientId = "clientId",
                    pageTitle = "Test Android App",
                )
            )
        }
        binding.buttonSgtmMap.setOnClickListener {
            App.instance.stape.sendEvent(
                "map_test_event", mapOf(
                    "clientId" to "clientId",
                    "pageTitle" to "Test Android App"
                )
            )
        }
        binding.buttonSgtmExtended.setOnClickListener {
            sendSGTMEvent(
                "extended_test_event", ExtendedEventData(
                    clientId = "clientId",
                    deviceType = Build.TYPE,
                    deviceManufacturer = Build.MANUFACTURER,
                    screenName = "Test Android App",
                )
            )
        }
    }

    private fun sendSGTMEvent(name: String, payload: EventData) {
        App.instance.stape.sendEvent(name, payload)
            .thenAccept { result ->
                result
                    .onSuccess { Log.d(javaClass.simpleName, "The SGTM returned OK result: $it") }
                    .onFailure { App.instance.eventStorage.add(it.message ?: "Unknown error") }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
