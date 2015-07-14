package ru.geevee.mapswitch

import kotlin.browser.window
import kotlin.text.Regex

native val chrome: dynamic

fun main(args: Array<String>) {
    chrome.tabs.onUpdated.addListener {
        tabId, info, tab ->
        val url: String = tab.url
        if (getApplicableService(url) != null) {
            chrome.pageAction.show(tabId)
        }
        else {
            chrome.pageAction.hide(tabId)
        }
    }

    chrome.pageAction.onClicked.addListener {
        tab ->
        val url: String = tab.url

        val mapService = getApplicableService(url)!!
        val coordinates = mapService.extractCoordinates(url)
        val otherService = mapService.getOther()
        val newUrl = otherService.getUrl(coordinates)

        // TODO
        js("chrome.tabs.update(tab.id, {url: newUrl});")
    }
}