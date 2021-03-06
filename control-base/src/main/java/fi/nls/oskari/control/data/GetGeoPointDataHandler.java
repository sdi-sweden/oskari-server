package fi.nls.oskari.control.data;

import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.log.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fi.mml.map.mapwindow.service.db.MapLayerService;
import fi.mml.map.mapwindow.service.db.MapLayerServiceIbatisImpl;
import fi.nls.oskari.control.ActionException;
import fi.nls.oskari.control.ActionHandler;
import fi.nls.oskari.control.ActionParameters;
import fi.nls.oskari.domain.User;
import fi.nls.oskari.domain.map.Layer;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.map.data.domain.GFIRequestParams;
import fi.nls.oskari.map.data.service.GetGeoPointDataService;
import fi.nls.oskari.map.data.service.WFSFeatureService;
import fi.nls.oskari.map.myplaces.service.GeoServerProxyService;
import fi.nls.oskari.util.ConversionHelper;
import fi.nls.oskari.util.RequestHelper;
import fi.nls.oskari.util.ResponseHelper;

@OskariActionRoute("GetFeatureInfoWMS")
public class GetGeoPointDataHandler extends ActionHandler {

	private final MapLayerService mapLayerService = new MapLayerServiceIbatisImpl();
	private final GetGeoPointDataService geoPointService = new GetGeoPointDataService();
    private final GeoServerProxyService myplacesService = new GeoServerProxyService();
    private final WFSFeatureService wfsFeatureService = new WFSFeatureService();
	
	private Logger log = LogFactory.getLogger(GetGeoPointDataHandler.class);
		
    private static final String PARAM_LAT = "lat";
	private static final String PARAM_LON = "lon";
    private static final String PARAM_LAYERS = "layerIds";
    private static final String PARAM_X = "x";
    private static final String PARAM_Y = "y";
    private static final String PARAM_BBOX = "bbox";
    private static final String PARAM_WIDTH = "width";
    private static final String PARAM_HEIGHT = "height";
    private static final String PARAM_STYLES = "styles";
    private static final String PARAM_ZOOM = "zoom";
    private static final String PARAM_GEOJSON = "geojson";

	@Override
    public void handleAction(final ActionParameters params) throws ActionException {
	     
		final String layerIds = params.getHttpParam(PARAM_LAYERS);
		final String[] layerIdsArr = layerIds.split(",");
		
        final User user = params.getUser();
        final double lat = ConversionHelper.getDouble(params.getHttpParam(PARAM_LAT), -1);
        final double lon = ConversionHelper.getDouble(params.getHttpParam(PARAM_LON), -1);
        final int zoom = ConversionHelper.getInt(params.getHttpParam(PARAM_ZOOM), 0);
        
        final JSONArray data = new JSONArray();
        JSONObject geojs = new JSONObject();
        try {
           
            geojs = new JSONObject(params.getHttpParam(
                    PARAM_GEOJSON, "{}"));

        } catch (JSONException ee) {
            log.warn("Couldn't parse geojson from POST request", ee);
        }

		for (String id : layerIdsArr) {
			if (id.indexOf('_') >= 0) {
			    if (id.startsWith("myplaces_")) {
			        // Myplaces wfs query modifier
                    final JSONObject response = myplacesService.getFeatureInfo(lat, lon, zoom, id, user.getUuid());
                    if(response != null) {
                        data.put(response);
                    }
			    }
			    continue;
			}
			
			final int layerId = ConversionHelper.getInt(id, -1);
			if(layerId == -1) {
                log.warn("Couldnt parse layer id", id);
                continue;
			}

			final Layer layer = mapLayerService.find(layerId);
			final String layerType = layer.getType();

			if (Layer.TYPE_WMS.equals(layerType)) {
			    final GFIRequestParams gfiParams = new GFIRequestParams();
			    gfiParams.setBbox(params.getHttpParam(PARAM_BBOX));
			    gfiParams.setCurrentStyle(params.getHttpParam(PARAM_STYLES));
			    gfiParams.setHeight(params.getHttpParam(PARAM_HEIGHT));
			    gfiParams.setLat(lat);
			    gfiParams.setLayer(layer);
			    gfiParams.setLon(lon);
			    gfiParams.setWidth(params.getHttpParam(PARAM_WIDTH));
			    gfiParams.setX(params.getHttpParam(PARAM_X));
			    gfiParams.setY(params.getHttpParam(PARAM_Y));
			    gfiParams.setZoom(zoom);
			    
			    final JSONObject response = geoPointService.getWMSFeatureInfo(gfiParams);
                if(response != null) {
                    data.put(response);
                }
				continue;
			} else if (Layer.TYPE_WFS.equals(layerType)) {
			    JSONArray features = null;
				try {
				    // Geojs and zoom is enough to select features
				    // If geojs is empty, lat, lon and zoom is in use
				    // default tolerance (buffer radius) is based on zoom
                    features = wfsFeatureService.getWFSFeatures(lat, lon, zoom, geojs, layerId);
                    
	                final JSONObject layerJson = new JSONObject();
	                layerJson.put(GetGeoPointDataService.TYPE, layerType);
	                layerJson.put(GetGeoPointDataService.LAYER_ID, layerId);
					layerJson.put("features", features);
		            data.put(layerJson);
				} catch (JSONException je) {
	                log.warn("Could not add features", features, "- for layerId", layerId);
				}
			}
		}

		try {
	        final JSONObject rootJson = new JSONObject();
            rootJson.put("data", data);
			rootJson.put("layerCount", data.length());
	        ResponseHelper.writeResponse(params, rootJson);
		} catch (JSONException je) {
		    throw new ActionException("Could not populate GFI JSON: " + log.getAsString(data), je);
		}
	}
}
