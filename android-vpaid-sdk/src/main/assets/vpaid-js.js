var adsManager;
var adsLoader;
var adDisplayContainer;
var videoContent;
var btnPlay;
var adsInitialized;
var autoplayAllowed;
var autoplayRequiresMuted;
var count = 1;
var playClicked;

function initDesktopAutoplay() {
    videoContent = document.getElementById('playerVpaid');
    btnPlay = document.getElementById('playButton');
    btnPlay.style.display = 'none';
    btnPlay.addEventListener('click', onPlayClicked);
    setUpIMA();
    // Check if autoplay is supported.
    checkAutoplaySupport();
}

function checkAutoplaySupport() {
    // Test for autoplay support with our content player.
    var playPromise = videoContent.play();
    if (playPromise !== undefined) {
        playPromise.then(onAutoplayWithSoundSuccess).catch(onAutoplayWithSoundFail);
    }
}

function onAutoplayWithSoundSuccess() {
    // If we make it here, unmuted autoplay works.
    videoContent.pause();
    autoplayAllowed = true;
    autoplayRequiresMuted = false;
    autoplayChecksResolved();
    console.log('play with sound');
}

function onAutoplayWithSoundFail() {
    // Unmuted autoplay failed. Now try muted autoplay.
    checkMutedAutoplaySupport();
    console.log('cant play with sound');
}

function checkMutedAutoplaySupport() {
    videoContent.volume = 0;
    videoContent.muted = true;
    var playPromise = videoContent.play();
    if (playPromise !== undefined) {
        playPromise.then(onMutedAutoplaySuccess).catch(onMutedAutoplayFail);
    }
}

function onMutedAutoplaySuccess() {
    // If we make it here, muted autoplay works but unmuted autoplay does not.
    videoContent.pause();
    autoplayAllowed = true;
    autoplayRequiresMuted = true;
    autoplayChecksResolved();
}

function onMutedAutoplayFail() {
    videoContent.volume = 1.0;
    videoContent.muted = false;
    autoplayAllowed = false;
    autoplayRequiresMuted = false;
    autoplayChecksResolved();
}

function setUpIMA() {
    // Create the ad display container.
    createAdDisplayContainer();
    // Create ads loader.
    adsLoader = new google.ima.AdsLoader(adDisplayContainer);
    // Listen and respond to ads loaded and error events.
    adsLoader.addEventListener(
        google.ima.AdsManagerLoadedEvent.Type.ADS_MANAGER_LOADED,
        onAdsManagerLoaded,
        false);
    adsLoader.addEventListener(
        google.ima.AdErrorEvent.Type.AD_ERROR,
        onAdError,
        false);
}

function autoplayChecksResolved(vastContent) {
    // Request video ads.
    var adsRequest = new google.ima.AdsRequest();
    if (!vastContent) {
        adsRequest.adTagUrl = 'https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinearvpaid2js&correlator=';
    }

    // Specify the linear and nonlinear slot sizes. This helps the SDK to
    // select the correct creative if multiple are returned.
    adsRequest.linearAdSlotWidth = 1280;
    adsRequest.linearAdSlotHeight = 720;
    adsRequest.nonLinearAdSlotWidth = 1280;
    adsRequest.nonLinearAdSlotHeight = 720;

    adsRequest.setAdWillAutoPlay(autoplayAllowed);
    adsRequest.setAdWillPlayMuted(autoplayRequiresMuted);
    adsLoader.requestAds(adsRequest);
}

function createAdDisplayContainer() {
    // We assume the adContainer is the DOM id of the element that will house
    // the ads.
    google.ima.settings.setVpaidMode(google.ima.ImaSdkSettings.VpaidMode.ENABLED);
    adDisplayContainer = new google.ima.AdDisplayContainer(
        document.getElementById('adContainer'), videoContent);
}

function playAds() {
    try {
        if (!adsInitialized) {
            adDisplayContainer.initialize();
            adsInitialized = true;
        }
        // Initialize the ads manager. Ad rules playlist will start at this time.
        adsManager.init(1280, 720, google.ima.ViewMode.NORMAL);
        // Call play to start showing the ad. Single video and overlay ads will
        // start at this time; the call will be ignored for ad rules.
        adsManager.start();
        if (playClicked) {
            btnPlay.style.display = 'block';
        }
    } catch (adError) {
        // An error may be thrown if there was a problem with the VAST response.
        videoContent.play();
    }
}

function onAdsManagerLoaded(adsManagerLoadedEvent) {
    // Get the ads manager.
    var adsRenderingSettings = new google.ima.AdsRenderingSettings();
    adsRenderingSettings.restoreCustomPlaybackStateOnAdBreakComplete = true;
    // videoContent should be set to the content video element.
    adsManager = adsManagerLoadedEvent.getAdsManager(
        videoContent, adsRenderingSettings);

    // Add listeners to the required events.
    adsManager.addEventListener(
        google.ima.AdErrorEvent.Type.AD_ERROR,
        onAdError);
    adsManager.addEventListener(
        google.ima.AdEvent.Type.ALL_ADS_COMPLETED,
        onAdEvent);
    adsManager.addEventListener(
        google.ima.AdEvent.Type.CONTENT_PAUSE_REQUESTED,
        onContentPauseRequested);
    adsManager.addEventListener(
        google.ima.AdEvent.Type.CONTENT_RESUME_REQUESTED,
        onContentResumeRequested);

    // Listen to any additional events, if necessary.
    adsManager.addEventListener(
        google.ima.AdEvent.Type.LOADED,
        onAdEvent);
    adsManager.addEventListener(
        google.ima.AdEvent.Type.STARTED,
        onAdEvent);
    adsManager.addEventListener(
        google.ima.AdEvent.Type.COMPLETE,
        onAdEvent);
    adsManager.addEventListener(
        google.ima.AdEvent.Type.IMPRESSION,
        onAdEvent);
    adsManager.addEventListener(
        google.ima.AdEvent.Type.FIRST_QUARTILE,
        onAdEvent);
    adsManager.addEventListener(
        google.ima.AdEvent.Type.MIDPOINT,
        onAdEvent);
    adsManager.addEventListener(
        google.ima.AdEvent.Type.THIRD_QUARTILE,
        onAdEvent);


    if (playClicked || autoplayAllowed) {
        playAds();
    } else {
        if (!autoplayAllowed) {
            btnPlay.style.display = 'block';
            console.log('Khong ho tro auto play');
        }
    }
}

function onAdEvent(adEvent) {
    // Retrieve the ad from the event. Some events (e.g. ALL_ADS_COMPLETED)
    // don't have ad object associated.
    switch (adEvent.type) {
        case google.ima.AdEvent.Type.LOADED:
            console.log('loaded');
            Android.adsIsReady();
            break;
        case google.ima.AdEvent.Type.IMPRESSION:
            console.log('impression');
            break;
        case google.ima.AdEvent.Type.FIRST_QUARTILE:
            console.log('1/4');
            break;
        case google.ima.AdEvent.Type.COMPLETE:
            console.log('completed_view');
            Android.showToast('completed_view');
            Android.adsIsComplete();
            break;
    }
}

function onAdError(adErrorEvent) {
    // Handle the error logging.
    console.log(adErrorEvent.getError());
    if (count > 0) {
        --count;
        autoplayChecksResolved();
    } else {
        try {
            console.log('run local vast');
            //autoplayChecksResolved(vast);
            videoContent.play();
        } catch (ex) {
            console.log(ex);
            if (adsManager) {
                adsManager.destroy();
            }
            videoContent.play();
        }
    }
}

function onPlayClicked() {
    playClicked = true;
    if (adsManager) {
        playAds();
    }
}

function onContentPauseRequested() {

}

function onContentResumeRequested() {

}