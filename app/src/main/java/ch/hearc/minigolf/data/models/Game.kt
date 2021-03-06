package ch.hearc.minigolf.data.models

import android.os.Parcel
import android.os.Parcelable
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Game(
    val course: String,
    val id_course: Int,
    val creator: String,
    val date: String,
    val id: Int,
    val minigolf: String,
    val id_minigolf: Int,
    val players: Array<Player>,
    val started: Int,
    val token: String
) : Parcelable {

    companion object CREATOR : Parcelable.Creator<Game> {
        override fun createFromParcel(parcel: Parcel): Game {
            return Game(parcel)
        }

        override fun newArray(size: Int): Array<Game?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.createTypedArray(Player) as Array<Player>,
        parcel.readInt(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(course)
        parcel.writeInt(id_course)
        parcel.writeString(creator)
        parcel.writeString(date)
        parcel.writeInt(id)
        parcel.writeString(minigolf)
        parcel.writeInt(id_minigolf)
        parcel.writeTypedArray(players, flags)
        parcel.writeInt(started)
        parcel.writeString(token)
    }

    class Deserializer : ResponseDeserializable<Game> {
        override fun deserialize(content: String): Game? =
            Gson().fromJson(content, Game::class.java)
    }

    override fun describeContents(): Int = 0

}

data class GameDeserializerUtils(val data: Array<Game>) {
    class Deserializer : ResponseDeserializable<GameDeserializerUtils> {
        override fun deserialize(content: String): GameDeserializerUtils? =
            Gson().fromJson(content, GameDeserializerUtils::class.java)
    }
}
