package com.example.w3d1retrofit

import android.util.Log
import java.util.ArrayList

object DataProvider {

    data class President(
        val name: String,
        val startDuty: Int,
        val endDuty: Int,
        val description: String
    ) : Comparable<President> {

        override fun compareTo(other: President): Int {
            return name.compareTo(other.name)
        }
    }
    val presidents: MutableList<President> = ArrayList()
    init {
        Log.d("USR", "This ($this) is a singleton")
        // construct the data source
        presidents.add(President("Kaarlo Stahlberg", 1919, 1925, "Eka presidentti"))
        presidents.add(President("Lauri Relander", 1925, 1931, "Toka presidentti"))
        presidents.add(President("P. E. Svinhufvud", 1931, 1937, "Kolmas presidentti"))
        presidents.add(President("Kyösti Kallio", 1937, 1940, "Neljas presidentti"))
        presidents.add(President("Risto Ryti", 1940, 1944, "Viides presidentti"))
        presidents.add(President("Carl Gustaf Emil Mannerheim", 1944, 1946, "Kuudes presidentti"))
        presidents.add(President("Juho Kusti Paasikivi", 1946, 1956, "Äkäinen ukko"))
        presidents.add(President("Urho Kekkonen", 1956, 1982, "Pelimies"))
        presidents.add(President("Mauno Koivisto", 1982, 1994, "Manu"))
        presidents.add(President("Martti Ahtisaari", 1994, 2000, "Mahtisaari"))
        presidents.add(President("Tarja Halonen", 2000, 2012, "Eka naispresidentti"))
        presidents.add(President("Sauli Niinistö", 2012, 2024, "Ensimmäisen koiran, Oskun, omistaja"))
        presidents.sort()
    } }
