package com.drowsyatmidnight.haint.android_vpaid_sdk;

public class RenderingIMA {
    public static final String folderPath = "file:android_asset/";
    public static final String fileName = "vpaid.html";

    public static String getHtmlWithVastContent(int width, int height, String vastContent, int skipOffset, int currentTime) {
        vastContent = vastContent.replace("", "").replace("\r", "").replace("\n", "");
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Fpt Ads</title>" +
                "<style>" +
                "#mainContainer {" +
                "position: relative;" +
                "width: " + width + "px;" +
                "height: " + height + "px;" +
                "}" +
                "#content, #adContainer {" +
                "position: absolute;" +
                "top: 0px;" +
                "left: 0px;" +
                "width: " + width + "px;" +
                "height: " + height + "px;" +
                "}" +
                "#contentElement {" +
                "width: " + width + "px;" +
                "height: " + height + "px;" +
                "overflow: hidden;" +
                "}" +
                "a, u {" +
                "text-decoration: white !important;" +
                "}" +
                "</style>" +
                "<script type=\"text/javascript\" src=\"https://imasdk.googleapis.com/js/sdkloader/ima3.js\"></script>" +
                "</head>" +
                "<body onload=\"initDesktopAutoplay()\" style=\"background-color: black;\">" +
                "<div id=\"mainContainer\">" +
                "<div id=\"adContainer\"></div>" +
                "</div>" +
                "<script type=\"text/javascript\">" +
                "var adsManager;" +
                "var adsLoader;" +
                "var adDisplayContainer;" +
                "var adsInitialized;" +
                "var count = 1;" +
                "var listener = {}, currentTime = -1, duration = null, videoContent = {" +
                "    volume: 1," +
                "    muted: false," +
                "    src: ''," +
                "    currentSrc: ''," +
                "    playsinline: null," +
                "    get currentTime() {" +

                "        return currentTime;" +
                "    }," +
                "    get duration() {" +

                "        return duration;" +
                "    }," +
                "    addEventListener: function (name, callback) {" +
                "        listener[name] = callback;" +
                "        console.log('addEventListener', name, callback);" +
                "    }," +
                "    removeEventListener: function (name, callback) {" +
                "        console.log('removeEventListener', name, callback);" +
                "    }," +
                "    getAttribute: function (name) {" +

                "        return null;" +
                "    }," +
                "    canPlayType: function () {" +
                "        return true;" +
                "    }," +
                "    pause: function () {" +
                "" +
                "    }," +
                "    load: function () {" +
                "" +
                "    }" +
                "};" +
                "" +
                "function setTime(seconds) {" +

                "currentTime = seconds;" +
                "}" +
                "function setDuration(seconds) {" +

                "duration = seconds;" +
                "}" +
                "function initDesktopAutoplay() {" +
                "setDuration(12000);" +
                "setTime(" + currentTime + ");" +
                "setUpIMA();" +
                "autoplayChecksResolved();" +
                "}" +
                "function setUpIMA() {" +
                "createAdDisplayContainer();" +
                "adsLoader = new google.ima.AdsLoader(adDisplayContainer);" +
                "adsLoader.addEventListener(" +
                "google.ima.AdsManagerLoadedEvent.Type.ADS_MANAGER_LOADED," +
                "onAdsManagerLoaded," +
                "false);" +
                "adsLoader.addEventListener(" +
                "google.ima.AdErrorEvent.Type.AD_ERROR," +
                "onAdError," +
                "false);" +
                "}" +
                "function autoplayChecksResolved(vastContent) {" +
                "var adsRequest = new google.ima.AdsRequest();" +
                "if (!vastContent) {" +
                "adsRequest.adsResponse = '" + vastContent + "';" +
                "}" +
                "adsRequest.linearAdSlotWidth = " + width + ";" +
                "adsRequest.linearAdSlotHeight = " + height + ";" +
                "adsRequest.nonLinearAdSlotWidth = " + width + ";" +
                "adsRequest.nonLinearAdSlotHeight = " + height + ";" +
                "adsRequest.setAdWillAutoPlay(false);" +
                "adsRequest.setAdWillPlayMuted(false);" +
                "adsLoader.requestAds(adsRequest);" +
                "}" +
                "function createAdDisplayContainer() {" +
                "google.ima.settings.setVpaidMode(google.ima.ImaSdkSettings.VpaidMode.ENABLED);" +
                "adDisplayContainer = new google.ima.AdDisplayContainer(" +
                "document.getElementById('adContainer'), videoContent);" +
                "}" +
                "function playAds() {" +
                "try {" +
                "if (!adsInitialized) {" +
                "adDisplayContainer.initialize();" +
                "adsInitialized = true;" +
                "}" +
                "adsManager.init(" + width + ", " + height + ", google.ima.ViewMode.NORMAL);" +
                "adsManager.start();" +
                "} catch (adError) {" +
                "Android.adsIsFailure();" +
                "}" +
                "}" +
                "function onAdsManagerLoaded(adsManagerLoadedEvent) {" +
                "var adsRenderingSettings = new google.ima.AdsRenderingSettings();" +
                "adsRenderingSettings.restoreCustomPlaybackStateOnAdBreakComplete = true;" +
                "adsManager = adsManagerLoadedEvent.getAdsManager(" +
                "videoContent, adsRenderingSettings);" +
                "adsManager.addEventListener(" +
                "google.ima.AdErrorEvent.Type.AD_ERROR," +
                "onAdError);" +
                "adsManager.addEventListener(" +
                "google.ima.AdEvent.Type.ALL_ADS_COMPLETED," +
                "onAdEvent);" +
                "adsManager.addEventListener(" +
                "google.ima.AdEvent.Type.CONTENT_PAUSE_REQUESTED," +
                "onContentPauseRequested);" +
                "adsManager.addEventListener(" +
                "google.ima.AdEvent.Type.CONTENT_RESUME_REQUESTED," +
                "onContentResumeRequested);" +
                "adsManager.addEventListener(" +
                "google.ima.AdEvent.Type.LOADED," +
                "onAdEvent);" +
                "adsManager.addEventListener(" +
                "google.ima.AdEvent.Type.STARTED," +
                "onAdEvent);" +
                "adsManager.addEventListener(" +
                "google.ima.AdEvent.Type.COMPLETE," +
                "onAdEvent);" +
                "adsManager.addEventListener(" +
                "google.ima.AdEvent.Type.IMPRESSION," +
                "onAdEvent);" +
                "adsManager.addEventListener(" +
                "google.ima.AdEvent.Type.FIRST_QUARTILE," +
                "onAdEvent);" +
                "adsManager.addEventListener(" +
                "google.ima.AdEvent.Type.MIDPOINT," +
                "onAdEvent);" +
                "adsManager.addEventListener(" +
                "google.ima.AdEvent.Type.THIRD_QUARTILE," +
                "onAdEvent);" +
                "playAds();" +
                "}" +
                "var skipDuaration = " + skipOffset + ";" +
                "var adsSkip = document.createElement('button');" +
                "adsSkip.id = 'skip-button';" +
                "adsSkip.style = \"display: none; background: rgba(0, 0, 0, 0.8); color: rgb(255, 255, 255); font-size: 2vh; font-family: arial, sans-serif; position: absolute; text-align: center; right: 0px; bottom: 10%; padding: 2%; cursor: pointer; z-index: 9999; border: 2px solid yellow;\";" +
                "var textSkip = document.createElement('span');" +
                "textSkip.id = 'spanSkip';" +
                "textSkip.innerHTML = 'You can skip this ad in ' + skipDuaration;" +
                "textSkip.style = \"vertical-align: middle;\";" +
                "adsSkip.appendChild(textSkip);" +
                "var skipImg = document.createElement('img');" +
                "skipImg.src = \"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAABGdBTUEAALGPC/xhBQAAAKBJREFUOBFjYBgFsBD4////TCBmg/Hx0UB1FUD8CYi7cKoDSoLAMSCWxKkIKgFU8xSkGAh+4VQLkQeTz4CkJU6FQAmg/CuYepzqYAqg9E8gnYZLMVCOZANh5mMNV0oMBBmMEa6UGggyFCVckQ1kwhUuVBcHOQMHoKqXqRYpVE02KBGALWyBQUN0OsQILxwGEpX1sIYXDgNhhUM3NvlhLgYAYuCVi7Jf+AUAAAAASUVORK5CYII=\";" +
                "skipImg.style = \"display: inline; vertical-align: middle; height: 2vh;\";" +
                "function skipAds(){" +
                "if (adsManager.getAdSkippableState()){" +
                "adsManager.skip();" +
                "}else {" +
                "console.log(\"discardAdBreak\");" +
                "adsManager.discardAdBreak();" +
                "Android.adsIsComplete();" +
                "}" +
                "adsSkip.style.display = '';" +
                "}" +
                "function destroyIma() {" +
                "adDisplayContainer.destroy();" +
                "adsSkip.style.display = 'none';" +
                "}" +
                "var isFirst = true;" +
                "var totalAds = 0;" +
                "function onAdEvent(adEvent) {" +
                "var textSkip = document.getElementById('spanSkip');" +
                "var btnSkip = document.getElementById('skip-button');" +
                "switch (adEvent.type) {" +
                "case google.ima.AdEvent.Type.LOADED:" +
                "if (isFirst) {" +
                "var ad = adEvent.getAd();" +
                "var adPodInfo = ad.getAdPodInfo();" +
                "totalAds = adPodInfo.getTotalAds();" +
                "isFirst = false;" +
                "}"+
                "console.log('loaded');" +
                "document.getElementById('adContainer').appendChild(adsSkip);" +
                "break;" +
                "case google.ima.AdEvent.Type.IMPRESSION:" +
                "console.log('impression');" +
                "Android.adsIsReady();" +
                "var timeleft = skipDuaration;"+
                "if (timeleft > 0) {" +
                "btnSkip.style.display = 'block';" +
                "btnSkip.focus();" +
                "var downloadTimer = setInterval(function () {" +
                "if (timeleft >= 0) {" +
                "if (timeleft !== 0) {" +
                "--timeleft;" +
                "textSkip.innerHTML = 'You can skip this ad in ' + timeleft;" +
                "} else {" +
                "textSkip.innerText = 'Skip Ad';" +
                "textSkip.appendChild(skipImg);" +
                "btnSkip.onclick = function () {" +
                "skipAds();" +
                "};" +
                "Android.adsCanSkip();" +
                "clearInterval(downloadTimer);" +
                "}" +
                "} else {" +
                "clearInterval(downloadTimer);" +
                "}" +
                "}, 1000);" +
                "}" +
                "break;" +
                "case google.ima.AdEvent.Type.FIRST_QUARTILE:" +
                "console.log('1/4');" +
                "break;" +
                "case google.ima.AdEvent.Type.COMPLETE:" +
                "console.log('completed_view');" +
                "btnSkip.style.display = 'none';" +
                "textSkip.innerHTML = 'You can skip this ad in ' + skipDuaration;"+
                "totalAds -= 1;\n" +
                "if (totalAds == 0) {\n" +
                "console.log('completed_view_all')\n" +
                "Android.adsIsComplete();" +
                "}"+
                "break;" +
                "case google.ima.AdEvent.Type.ALL_ADS_COMPLETED:" +
                "console.log('all_ads_completed_view');" +
                "btnSkip.style.display = 'none';" +
                "textSkip.innerHTML = 'You can skip this ad in ' + skipDuaration;"+
                "Android.adsIsComplete();" +
                "break;" +
                "case google.ima.AdEvent.Type.SKIPPED:" +
                "console.log('skipped');" +
                "btnSkip.style.display = 'none';" +
                "textSkip.innerHTML = 'You can skip this ad in ' + skipDuaration;"+
                "totalAds -= 1;"+
                "Android.adsIsComplete();" +
                "break;" +
                "}" +
                "}" +
                "function onAdError(adErrorEvent) {" +
                "console.log(adErrorEvent.getError());" +
                "if (count > 0) {" +
                "--count;" +
                "autoplayChecksResolved();" +
                "} else {" +
                "if (adsManager) {" +
                "adsManager.destroy();" +
                "}" +
                "Android.adsIsFailure();" +
                "}" +
                "}" +
                "function onPlayClicked() {" +
                "playClicked = true;" +
                "if (adsManager) {" +
                "playAds();" +
                "}" +
                "}" +
                "" +
                "function onContentPauseRequested() {" +
                "}" +
                "function onContentResumeRequested() {" +
                "}" +
                "</script>" +
                "</body>" +
                "</html>";
    }
}
