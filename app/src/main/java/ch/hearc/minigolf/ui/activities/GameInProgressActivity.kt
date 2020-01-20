package ch.hearc.minigolf.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.hearc.minigolf.R
import ch.hearc.minigolf.data.models.Game
import ch.hearc.minigolf.data.models.Player
import ch.hearc.minigolf.data.models.Score
import ch.hearc.minigolf.data.models.User
import ch.hearc.minigolf.data.repositories.UserRepository
import ch.hearc.minigolf.data.stores.UserStore
import ch.hearc.minigolf.data.viewmodels.GamesViewModel
import ch.hearc.minigolf.ui.adapters.ScoreAdapterListener
import ch.hearc.minigolf.ui.adapters.ScoresAdapter
import ch.hearc.minigolf.utilities.InjectorUtils
import com.google.android.material.button.MaterialButton

class GameInProgressActivity : AppCompatActivity(), ScoreAdapterListener {

    companion object {
        const val EXTRA_GAME_TOKEN = "gameToken"
    }

    private lateinit var token: String
    private lateinit var game: LiveData<Game>
    private lateinit var vm: GamesViewModel
    private lateinit var btnTerminate: MaterialButton

    private val homeActivity: Intent by lazy {
        Intent(this, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_in_progress)

        token = intent.getStringExtra(EXTRA_GAME_TOKEN) as String

        val factory = InjectorUtils.provideGamesViewModelFactory()
        vm = ViewModelProviders.of(this, factory)
            .get(GamesViewModel::class.java)
        game = vm.getGame(token)

        btnTerminate = findViewById<MaterialButton>(R.id.mb_terminate)
        findViewById<TextView>(R.id.mtv_token).text = token
        val coursesRecyclerView = findViewById<RecyclerView>(R.id.rv_list_scores)
        val adapter = ScoresAdapter(this)
        coursesRecyclerView.layoutManager = LinearLayoutManager(this)

        coursesRecyclerView.adapter = adapter

        game.observe(this,
            androidx.lifecycle.Observer { game ->
                getPlayerMe(game)?.scores?.let { adapter.setScores(it) }
            })

        btnTerminate.setOnClickListener {
            startActivity(homeActivity)
        }
    }

    fun getPlayerMe(game: Game): Player? {
        val user = UserRepository.getInstance(UserStore()).getUser().value as User
        for (player in game?.players!!) {
            if (player.id_user == Integer.parseInt(user.id)) {
                return player
            }
        }
        return null
    }

    override fun isAllEditTextFilled(bool: Boolean) {
        Log.d("GameInProgresActivity", bool.toString())
        btnTerminate.isEnabled = bool
    }

    override fun scoreUpdated(score: Score) {
        vm.updateGame(score.id.toString(), score.score)
    }


}
