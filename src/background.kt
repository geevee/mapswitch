package ru.geevee.mapswitch

import kotlin.browser.window
import kotlin.text.Regex

native val chrome: dynamic

fun main(args: Array<String>) {
    chrome.tabs.onUpdated.addListener {
        tabId, info, tab ->
        val url: String = tab.url
        if ("maps" in url && "google" in url) {
            chrome.pageAction.show(tabId)
        }
        else {
            chrome.pageAction.hide(tabId)
        }
    }

    chrome.pageAction.onClicked.addListener {
        tab ->
        @suppress("NAME_SHADOWING")
        val url: String = tab.url

        val match = Regex("@([\\d.]+),([\\d.]+),([\\d.]+)z").matchAll(url).firstOrNull()
        if (match == null) {
            window.alert("no match :(")
        } else {
            val latitude = match.groups[1]!!.value
            val longitude = match.groups[2]!!.value
            val zoomLevel = match.groups[3]!!.value

            val newUrl = "https://maps.yandex.ru/?ll=$longitude%2C$latitude&z=$zoomLevel"

            // TODO
            js("chrome.tabs.update(tab.id, {url: newUrl});")
        }
    }
}