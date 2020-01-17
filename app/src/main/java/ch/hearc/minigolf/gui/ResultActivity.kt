package ch.hearc.minigolf.gui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import ch.hearc.minigolf.R
import ch.hearc.minigolf.ResultTable
import ch.hearc.minigolf.data.game.Game

class ResultActivity : AppCompatActivity() {

    private lateinit var game : Game
    private lateinit var table : TableLayout

    companion object {
        const val EXTRA_GAME_OBJECT = "game"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        game = intent.getParcelableExtra(EXTRA_GAME_OBJECT) as Game
        table = findViewById(R.id.tl_result)

        initTable()
    }

    fun initTable() {
        table.addView(createHeader())

        for(i in game.players.first().scores.indices)
        {
            table.addView(createScore(i))
        }

        table.addView(createTotal())

    }

    fun createHeader() : TableRow {
        val tableRow = TableRow(this)
        tableRow.layoutParams = TableLayout.LayoutParams (
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT
        )
        tableRow.addView(Cell(this, ""))
        for(player in game.players) {
            tableRow.addView(Cell(this, player.player))
        }

        return tableRow
    }

    fun createScore(numHole: Int) : TableRow {
        val tableRow = TableRow(this)
        tableRow.layoutParams = TableLayout.LayoutParams (
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT
        )
        tableRow.addView(Cell(this, game.players.first().scores[numHole].hole))

        for(player in game.players) {
            tableRow.addView(Cell(this, player.scores[numHole].score.toString()))
        }

        return tableRow
    }

    fun createTotal() : TableRow {
        val tableRow = TableRow(this)
        tableRow.layoutParams = TableLayout.LayoutParams (
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT
        )
        tableRow.addView(Cell(this, ""))

        for(player in game.players) {
            val total = player.scores.sumBy { score -> score.score }
            tableRow.addView(Cell(this, total.toString()))
        }

        return tableRow
    }
}
