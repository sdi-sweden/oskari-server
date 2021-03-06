package fi.nls.oskari.control.view.modifier.param;

import fi.nls.oskari.annotation.OskariViewModifier;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.view.modifier.ModifierException;
import org.json.JSONObject;

import fi.nls.oskari.log.Logger;
import fi.nls.oskari.view.modifier.ModifierParams;

@OskariViewModifier("wfsHighlightLayer")
public class LayerIdWFSHighlightParamHandler extends WFSHighlightParamHandler {

    private static final Logger log = LogFactory.getLogger(LayerIdWFSHighlightParamHandler.class);

    @Override
    public boolean handleParam(ModifierParams params)
            throws ModifierException {
        if(params.getParamValue() == null) {
            return false;
        }
        try {
            final JSONObject postprocessorState = getPostProcessorState(params);
            postprocessorState.put("highlightFeatureLayerId", params.getParamValue());
        }
        catch(Exception ex) {
            log.error(ex, "Couldn't set conf.highlightFeatureLayerId for postprocessor");
        }
        return false;
    }
}
