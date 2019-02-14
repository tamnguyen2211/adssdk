# AndroidAdsSdk
**AndroidAdsSdk** is a ads controller.
## How to use sdk?
### import all of libs: android-firestore-sdk-release.aar, android-vpaid-sdk-release.aar, AndroidFplayAdsSdk-release.aar
in player manager:
```
  implements AdsListener.VideoProgress, AdsListener.PlayerStaus
```
then, override these methods:
```
    @Override
    public long setVideoDuaration() {
        // return duration of video content
    }

    @Override
    public long setVideoCurrentPosition() {
        // return current position of video content
    }

    @Override
    public void hiddenPlayer() {
        // ads is ready to show
    }

    @Override
    public void showPlayer() {
        // ads is completed
    }
```
init sdk:
```
AdsController adsController = AdsController.init(Context, AdsListener.VideoProgress, AdsListener.PlayerStaus);
```
show ads on vod instream:
```
// you need to init sdk first
/**
 *
 * @param uuid: 897fcb2a27d84ca0abd0571889e8e39b
 * @param placement: android_app: 306, android_tv:308
 * @param url: http://fptplay.net/xem-video/cham-vao-tim-em-touch-your-heart-5c4d6d0bfa9c5e094ea7ac97.html#tap-1
 * @param mAdUiContainer
 * @param vpaidView
 * @param skipButton
 */
adsController.startAdsVod(String uuid, int placement, String url, ViewGroup mAdUiContainer, VpaidView vpaidView, View skipButton);

// when video content is completed 
// or user seek to the end of video content call this method
adsController.onCompleted();
```
show ads on live tv:
```
// you need to init sdk first
/**
 *
 * @param uuid:897fcb2a27d84ca0abd0571889e8e39b
 * @param placement: android_app: 306, android_tv:308
 * @param channelId: vtv6-hd
 * @param deviceNameOnCloudFirestore: android_app: android, android_tv: smarttv-sony-android
 * @param vpaidView
 * @param skipButton
 */
adsController.startAdsLiveTV(String uuid, int placement, String channelId, String deviceNameOnCloudFirestore, ViewGroup mAdUiContainer);
```
stop ads:
```
// you need to init sdk first
adsController.stopAds();
```