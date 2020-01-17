package ch.hearc.minigolf.gui

import android.app.Activity
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ch.hearc.minigolf.R
import ch.hearc.minigolf.data.User.User
import ch.hearc.minigolf.data.UserGps
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_geolocation.*
import kotlinx.coroutines.*
import org.w3c.dom.Node

class GeolocationActivity :
    AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    /*------------------------------------------------------------------*\
    |*							                ATTRIBUTES
    \*------------------------------------------------------------------*/

    private lateinit var map: GoogleMap
    private lateinit var user: UserGps


    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            coroutineScope.launch(Dispatchers.Main) { /* empty */ }
            GlobalScope.launch { Log.e("Error", "Cought $throwable") }
        }

    private val parentJob = Job()
    private val coroutineScope =
        CoroutineScope(Dispatchers.Main + parentJob + coroutineExceptionHandler)

    /*------------------------------------------------------------------*\
    |*							                INITIALIZATION
    \*------------------------------------------------------------------*/

    fun userInitialization() = coroutineScope.launch(Dispatchers.Main) {
        user = initializeUserAsync(this@GeolocationActivity)
        // citiesGraph = initializeGraphAsync(this@MainActivity)
        // insertUser(citiesGraph, user)
    }

    /*------------------------------------------------------------------*\
    |*							                HOOKS
    \*------------------------------------------------------------------*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST", "ICIIII")
        setContentView(R.layout.activity_geolocation)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        userInitialization()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)
        map.isMyLocationEnabled = true
    }

    override fun onMarkerClick(p0: Marker): Boolean = false

    override fun onDestroy() {
        super.onDestroy()
        parentJob.cancel()
    }

    /*------------------------------------------------------------------*\
    |*							                METHODES
    \*------------------------------------------------------------------*/

    // private fun insertUser(graph: Graph, user: UserGps) {
    //   graph.insertNode(Node(user))
    // }

    /*------------------------------*\
    |*			        ASYNC
    \*------------------------------*/

    private suspend fun initializeUserAsync(activity: Activity): UserGps =
      withContext(Dispatchers.Default) { UserGps(activity, map) }

    // private suspend fun initializeGraphAsync(activity: Activity): Graph =
    //   withContext(Dispatchers.Default) { Graph(Geocoder(activity)) }
}