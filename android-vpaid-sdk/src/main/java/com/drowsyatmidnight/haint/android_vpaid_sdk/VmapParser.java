package com.drowsyatmidnight.haint.android_vpaid_sdk;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class VmapParser {
    public static final String vmapHead = "<vmap:VMAP xmlns:vmap=\"http://www.iab.net/videosuite/vmap\" version=\"1.0\"><vmap:AdBreak timeOffset=\"start\" breakType=\"linear\" breakId=\"preroll\"><vmap:AdSource id=\"6969\" allowMultipleAds=\"false\" followRedirects=\"true\"><vmap:VASTAdData>";
    public static final String vmapFooter = "</vmap:VASTAdData></vmap:AdSource></vmap:AdBreak></vmap:VMAP>";

    public static Map<Integer, String> getCorrectVastXML(long milis, String vastResponse) {
        Map<Long, Map<Integer, String>> vastData = getListVast(vastResponse);
        long pos = -69;
        long min = -96;
        if (vastData != null) {
            for (long timeOffset : vastData.keySet()) {
                if (pos == -69) {
                    pos = timeOffset;
                    min = Math.abs(timeOffset - milis);
                } else {
                    if (min > Math.abs(timeOffset - milis)) {
                        pos = timeOffset;
                        min = Math.abs(timeOffset - milis);
                    }
                }
            }
            return vastData.get(pos);
        }
        return null;
    }

    public static Map<Long, Map<Integer, String>> getListVast(String vastResponse) {
        Map<Long, Map<Integer, String>> listVast = new HashMap<>();
        List<Long> vastOffset = new ArrayList<>();
        List<Integer> skipOffsetList = new ArrayList<>();
        List<String> vastTextList = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(vastResponse.getBytes(Charset.forName("UTF-8")));
            Document document = documentBuilder.parse(stream);
            document.getDocumentElement().normalize();
            NodeList nodeAdBreakList = document.getElementsByTagName("vmap:AdBreak");
            long endOffset = 0;
            for (int i = 0; i < nodeAdBreakList.getLength(); i++) {
                Node nodeAdBreak = nodeAdBreakList.item(i);
                if (nodeAdBreak.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodeAdBreak;
                    if (element.hasAttribute("timeOffset")) {
                        String[] time = element.getAttribute("timeOffset").split(":");
                        if (time[0].equals("start")) {
                            vastOffset.add(60L);
                            endOffset = 60L;
                        } else {
                            if (time[0].equals("end")) {
                                vastOffset.add(999999999999L);
                            } else {
                                long miliFromHour = (1000 * Long.parseLong(time[2])) + (60000 * Long.parseLong(time[1])) + (3600000 * Long.parseLong(time[0]));
                                if (miliFromHour > endOffset) {
                                    endOffset = miliFromHour;
                                }
                                vastOffset.add(miliFromHour);
                            }
                        }
                    }
                }
            }
            NodeList nodeSkipOffsetList = document.getElementsByTagName("Linear");
            for (int i = 0; i < nodeSkipOffsetList.getLength(); i++) {
                Node nodeSkipOffset = nodeSkipOffsetList.item(i);
                if (nodeSkipOffset.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nodeSkipOffset;
                    int skipOffset;
                    if (element.hasAttribute("skipoffset")) {
                        String[] time = element.getAttribute("skipoffset").split(":");
                        skipOffset = (Integer.parseInt(time[2])) + (60 * Integer.parseInt(time[1])) + (3600 * Integer.parseInt(time[0]));
                    } else {
                        skipOffset = 0;
                    }
                    skipOffsetList.add(skipOffset);
                }
            }
            NodeList nodeList = document.getElementsByTagName("VAST");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                StringWriter stringWriter = new StringWriter();
                StreamResult result = new StreamResult(stringWriter);
                DOMSource domSource = new DOMSource(node);
                transformer.transform(domSource, result);
                vastTextList.add(vmapHead + stringWriter.toString() + vmapFooter);
            }
            if (vastOffset.size() == vastTextList.size()) {
                for (int i = 0; i < vastOffset.size(); i++) {
                    Map<Integer, String> data = new HashMap<>();
                    data.put(skipOffsetList.get(i), vastTextList.get(i));
                    if (i + 1 == vastOffset.size()) {
                        listVast.put(endOffset + 500, data);
                    } else {
                        listVast.put(vastOffset.get(i), data);
                    }
                }
                return listVast;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
