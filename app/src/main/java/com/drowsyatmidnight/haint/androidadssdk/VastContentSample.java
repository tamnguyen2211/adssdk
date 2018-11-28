package com.drowsyatmidnight.haint.androidadssdk;

public class VastContentSample {
    public static final String vastContent = "<vmap:VMAP\n" +
            "\txmlns:vmap=\"http://www.iab.net/videosuite/vmap\" version=\"1.0\">\n" +
            "\t<vmap:AdBreak timeOffset=\"start\" breakType=\"linear\" breakId=\"preroll\">\n" +
            "\t\t<vmap:AdSource id=\"800501\" allowMultipleAds=\"false\" followRedirects=\"true\">\n" +
            "\t\t\t<vmap:VASTAdData>\n" +
            "\t\t\t\t<VAST version=\"2.0\">\n" +
            "\t\t\t\t\t<Ad id=\"800501\">\n" +
            "\t\t\t\t\t\t<Wrapper>\n" +
            "\t\t\t\t\t\t\t<AdSystem version=\"1.0\">AdsPLAY</AdSystem>\n" +
            "\t\t\t\t\t\t\t<AdTitle>\n" +
            "\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "VPAID_Testing_New_SDK_Mobile_SmartTV_at_FPTPlay_side\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t</AdTitle>\n" +
            "\t\t\t\t\t\t\t<VASTAdTagURI>\n" +
            "\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://search.spotxchange.com/vast/2.0/85394?VPAID=JS&app[bundle]=REPLACE_ME&media_transcoding=low&app[domain]=REPLACE_ME&device[ifa]=REPLACE_ME&app[name]=REPLACE_MEt\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t</VASTAdTagURI>\n" +
            "\t\t\t\t\t\t\t<Error>\n" +
            "\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://log.adsplay.net/track/videoads_v2?event=error&adId=800501&placementId=306&campaignId=80&flightId=8005&contentId=5b18fa6e55832069e1f9a367&buyType=cpm&weightKey=w_800501&uuid=6f9a9581913dcc4e&t=1540372708&errorCode=[ERRORCODE]\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t</Error>\n" +
            "\t\t\t\t\t\t\t<Impression>\n" +
            "\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://log.adsplay.net/track/videoads_v2?event=impression&adId=800501&placementId=306&campaignId=80&flightId=8005&contentId=5b18fa6e55832069e1f9a367&buyType=cpm&weightKey=w_800501&uuid=6f9a9581913dcc4e&t=1540372708\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t</Impression>\n" +
            "\t\t\t\t\t\t\t<Creatives>\n" +
            "\t\t\t\t\t\t\t\t<Creative sequence=\"1\" id=\"800501\">\n" +
            "\t\t\t\t\t\t\t\t\t<Linear>\n" +
            "\t\t\t\t\t\t\t\t\t\t<TrackingEvents>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<Tracking event=\"creativeView\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://log.adsplay.net/track/videoads_v2?event=creativeView&adId=800501&placementId=306&campaignId=80&flightId=8005&contentId=5b18fa6e55832069e1f9a367&buyType=cpm&weightKey=w_800501&uuid=6f9a9581913dcc4e&t=1540372708\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</Tracking>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<Tracking event=\"start\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://log.adsplay.net/track/videoads_v2?event=played0&adId=800501&placementId=306&campaignId=80&flightId=8005&contentId=5b18fa6e55832069e1f9a367&buyType=cpm&weightKey=w_800501&uuid=6f9a9581913dcc4e&t=1540372708\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</Tracking>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<Tracking event=\"firstQuartile\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://log.adsplay.net/track/videoads_v2?event=played25&adId=800501&placementId=306&campaignId=80&flightId=8005&contentId=5b18fa6e55832069e1f9a367&buyType=cpm&weightKey=w_800501&uuid=6f9a9581913dcc4e&t=1540372708\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</Tracking>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<Tracking event=\"midpoint\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://log.adsplay.net/track/videoads_v2?event=played50&adId=800501&placementId=306&campaignId=80&flightId=8005&contentId=5b18fa6e55832069e1f9a367&buyType=cpm&weightKey=w_800501&uuid=6f9a9581913dcc4e&t=1540372708\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</Tracking>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<Tracking event=\"thirdQuartile\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://log.adsplay.net/track/videoads_v2?event=played75&adId=800501&placementId=306&campaignId=80&flightId=8005&contentId=5b18fa6e55832069e1f9a367&buyType=cpm&weightKey=w_800501&uuid=6f9a9581913dcc4e&t=1540372708\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</Tracking>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<Tracking event=\"complete\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://log.adsplay.net/track/videoads_v2?event=played100&adId=800501&placementId=306&campaignId=80&flightId=8005&contentId=5b18fa6e55832069e1f9a367&buyType=cpm&weightKey=w_800501&uuid=6f9a9581913dcc4e&t=1540372708\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</Tracking>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<Tracking event=\"close\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://log.adsplay.net/track/videoads_v2?event=close&adId=800501&placementId=306&campaignId=80&flightId=8005&contentId=5b18fa6e55832069e1f9a367&buyType=cpm&weightKey=w_800501&uuid=6f9a9581913dcc4e&t=1540372708\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</Tracking>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<Tracking event=\"pause\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://log.adsplay.net/track/videoads_v2?event=paused&adId=800501&placementId=306&campaignId=80&flightId=8005&contentId=5b18fa6e55832069e1f9a367&buyType=cpm&weightKey=w_800501&uuid=6f9a9581913dcc4e&t=1540372708\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</Tracking>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<Tracking event=\"mute\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://log.adsplay.net/track/videoads_v2?event=mute&adId=800501&placementId=306&campaignId=80&flightId=8005&contentId=5b18fa6e55832069e1f9a367&buyType=cpm&weightKey=w_800501&uuid=6f9a9581913dcc4e&t=1540372708\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</Tracking>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<Tracking event=\"unmute\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://log.adsplay.net/track/videoads_v2?event=unmute&adId=800501&placementId=306&campaignId=80&flightId=8005&contentId=5b18fa6e55832069e1f9a367&buyType=cpm&weightKey=w_800501&uuid=6f9a9581913dcc4e&t=1540372708\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</Tracking>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<Tracking event=\"skip\">\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://log.adsplay.net/track/videoads_v2?event=skip&adId=800501&placementId=306&campaignId=80&flightId=8005&contentId=5b18fa6e55832069e1f9a367&buyType=cpm&weightKey=w_800501&uuid=6f9a9581913dcc4e&t=1540372708\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</Tracking>\n" +
            "\t\t\t\t\t\t\t\t\t\t</TrackingEvents>\n" +
            "\t\t\t\t\t\t\t\t\t\t<VideoClicks>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t<ClickTracking>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t<![CDATA[\n" +
            "https://log.adsplay.net/track/videoads_v2?event=click&adId=800501&placementId=306&campaignId=80&flightId=8005&contentId=5b18fa6e55832069e1f9a367&buyType=cpm&weightKey=w_800501&uuid=6f9a9581913dcc4e&t=1540372708\n" +
            "]]>\n" +
            "\t\t\t\t\t\t\t\t\t\t\t</ClickTracking>\n" +
            "\t\t\t\t\t\t\t\t\t\t</VideoClicks>\n" +
            "\t\t\t\t\t\t\t\t\t</Linear>\n" +
            "\t\t\t\t\t\t\t\t</Creative>\n" +
            "\t\t\t\t\t\t\t</Creatives>\n" +
            "\t\t\t\t\t\t</Wrapper>\n" +
            "\t\t\t\t\t</Ad>\n" +
            "\t\t\t\t</VAST>\n" +
            "\t\t\t</vmap:VASTAdData>\n" +
            "\t\t</vmap:AdSource>\n" +
            "\t</vmap:AdBreak>\n" +
            "</vmap:VMAP>";
}
