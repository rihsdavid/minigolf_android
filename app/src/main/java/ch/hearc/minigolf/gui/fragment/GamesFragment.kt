package ch.hearc.minigolf.gui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hearc.minigolf.R
import ch.hearc.minigolf.gui.ResultActivity
import ch.hearc.minigolf.data.score.Score
import ch.hearc.minigolf.gui.JoinGameActivity
import ch.hearc.minigolf.gui.adapter.GamesAdapter
import ch.hearc.minigolf.ui.games.GamesViewModel
import ch.hearc.minigolf.utilities.InjectorUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.random.Random

class GamesFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    private var games = mutableListOf<Score>()

    private val intentJoinParty: Intent by lazy { Intent(activity, JoinGameActivity::class.java) }
    private val intentResult: Intent by lazy { Intent(activity, ResultActivity::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inflaterList = inflater.inflate(R.layout.fragment_list_results, container, false)

        val floatingButton =
            inflaterList.findViewById<FloatingActionButton>(R.id.fab_join)
        floatingButton.setOnClickListener { startActivity(intentJoinParty) }

        recyclerView = inflaterList.findViewById(R.id.rv_list_result)
        recyclerView.layoutManager = LinearLayoutManager(context)

        initScores(10)
        initGames()

        recyclerView.adapter =
            GamesAdapter(
                games,
                View.OnClickListener { startActivity(intentResult) })

        return inflaterList
    }

    private fun initGames() {
        val factory = InjectorUtils.provideGamesViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory)
            .get(GamesViewModel::class.java)
    }


    fun initScores(nb: Int) {
        games.addAll((1..nb).map { getFakeScore(it) })
        recyclerView.adapter?.notifyDataSetChanged()
//        for (i in 1..nb) {
//            games.add(getFakeScore())
//        }
    }

    fun getFakeScore(i: Int) =
        Score(i, Random.nextInt(40, 60), Date(), "Neuchatel")
}


