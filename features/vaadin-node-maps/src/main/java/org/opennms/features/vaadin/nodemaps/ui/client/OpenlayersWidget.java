package org.opennms.features.vaadin.nodemaps.ui.client;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;

public class OpenlayersWidget extends Widget {
	private final DivElement m_div;

	public OpenlayersWidget() {
		super();
		m_div = Document.get().createDivElement();
		setElement(m_div);

		init(m_div.getId());
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}


	private final native void init(final String divId) /*-{
            var map = new OpenLayers.Map({
                div: divId,
                displayProjection: "EPSG:900913",
                projection: "EPSG:4326",
                controls: [
                    new OpenLayers.Control.Navigation(),
                    new OpenLayers.Control.PanZoomBar(),
                    new OpenLayers.Control.LayerSwitcher(),
                    new OpenLayers.Control.MousePosition()
                ]
            });
            
            // Main Layer
            
            map.addLayer(new OpenLayers.Layer.Google("GoogleMaps", {sphericalMercator: true}));
            map.addLayer(new OpenLayers.Layer.OSM("OpenStreetMaps"));
            map.addLayer(new OpenLayers.Layer.XYZ(
                "MapQuest", 
                [
                    "http://otile1.mqcdn.com/tiles/1.0.0/osm/${z}/${x}/${y}.png",
                    "http://otile2.mqcdn.com/tiles/1.0.0/osm/${z}/${x}/${y}.png",
                    "http://otile3.mqcdn.com/tiles/1.0.0/osm/${z}/${x}/${y}.png",
                    "http://otile4.mqcdn.com/tiles/1.0.0/osm/${z}/${x}/${y}.png"
                ],
                {
                    attribution: "Data, imagery and map information provided by <a href='http://www.mapquest.com/'  target='_blank'>MapQuest</a>, <a href='http://www.openstreetmap.org/' target='_blank'>Open Street Map</a> and contributors, <a href='http://creativecommons.org/licenses/by-sa/2.0/' target='_blank'>CC-BY-SA</a>  <img src='http://developer.mapquest.com/content/osm/mq_logo.png' border='0'>",
                    transitionEffect: "resize",
                    sphericalMercator: true
                }
            ));
	}-*/;
}
