package ru.geevee.mapswitch

import kotlin.text.Regex

interface MapService {
    val name: String
    fun extractCoordinates(url: String): Coordinates?
    fun getUrl(coordinates: Coordinates): String
}

fun detectServiceAndCoordinates(url: String): Pair<MapService, Coordinates>? {
    for (service in services) {
        val coordinates = service.extractCoordinates(url)
        if (coordinates != null) {
            return Pair(service, coordinates)
        }
    }
    return null
}


object GoogleMaps: MapService {
    override val name: String
        get() = "Google"

    override fun extractCoordinates(url: String): Coordinates? {
        if ("google." !in url || "maps" !in url) return null

        val match = Regex("@([\\d.]+),([\\d.]+),([\\d.]+)z").matchAll(url).firstOrNull() ?: return null
        val latitude = match.groups[1]!!.value
        val longitude = match.groups[2]!!.value
        val zoomLevel = match.groups[3]!!.value

        return Coordinates(latitude, longitude, zoomLevel)
    }

    override fun getUrl(coordinates: Coordinates): String {
        return with(coordinates) {
            "https://www.google.com/maps/@$latitude,$longitude,${zoomLevel}z"
        }
    }
}

object YandexMaps: MapService {
    override val name: String
        get() = "Yandex"

    override fun extractCoordinates(url: String): Coordinates? {
        if ("maps.yandex.ru" !in url) return null

        val match = Regex("\\?ll=([\\d.]+)%2C([\\d.]+)&z=([\\d.]+)").matchAll(url).firstOrNull() ?: return null

        val latitude = match.groups[2]!!.value
        val longitude = match.groups[1]!!.value
        val zoomLevel = match.groups[3]!!.value

        return Coordinates(latitude, longitude, zoomLevel)
    }

    override fun getUrl(coordinates: Coordinates): String {
        return with(coordinates) {
            "https://maps.yandex.ru/?ll=$longitude%2C$latitude&z=$zoomLevel"
        }
    }
}

object OpenStreetMap: MapService {
    override val name: String
        get() = "OpenStreetMap"

    override fun extractCoordinates(url: String): Coordinates? {
        if ("openstreetmap.org" !in url) return null

        val match = Regex("#map=([\\d.]+)\\/([\\d.]+)\\/([\\d.]+)").matchAll(url).firstOrNull() ?: return null
        val latitude = match.groups[2]!!.value
        val longitude = match.groups[3]!!.value
        val zoomLevel = match.groups[1]!!.value

        return Coordinates(latitude, longitude, zoomLevel)
    }

    override fun getUrl(coordinates: Coordinates): String {
        return with(coordinates) {
            "http://www.openstreetmap.org/#map=$zoomLevel/$latitude/$longitude"
        }
    }
}

object Wikimapia: MapService {
    override val name: String
        get() = "Wikimapia"

    override fun extractCoordinates(url: String): Coordinates? {
        if ("wikimapia.org" !in url) return null

        val match = Regex("lat=([\\d.]+)&lon=([\\d.]+)&z=([\\d.]+)").matchAll(url).firstOrNull() ?: return null
        val latitude = match.groups[1]!!.value
        val longitude = match.groups[2]!!.value
        val zoomLevel = match.groups[3]!!.value

        return Coordinates(latitude, longitude, zoomLevel)
    }

    override fun getUrl(coordinates: Coordinates): String {
        return with(coordinates) {
            "http://wikimapia.org/#lat=$latitude&lon=$longitude&z=$zoomLevel"
        }
    }
}

// FIXME: property is here because of KT-4144: objects can be used only after they are declared
val services = listOf(GoogleMaps, YandexMaps, OpenStreetMap, Wikimapia)
