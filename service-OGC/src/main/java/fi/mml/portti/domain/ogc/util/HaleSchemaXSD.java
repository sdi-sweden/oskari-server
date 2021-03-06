package fi.mml.portti.domain.ogc.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import fi.nls.oskari.log.LogFactory;
import org.eclipse.xsd.XSDSchema;
import org.geotools.data.DataUtilities;
import org.geotools.gml3.GML;
import org.geotools.xml.SchemaLocationResolver;
import org.geotools.xml.XSD;

import fi.mml.portti.domain.ogc.util.GmlHelper.ConfigurationType;
import fi.nls.oskari.log.Logger;


/**
 * 
 *
 * @author Simon Templer
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 * @version $Id$ 
 */
public class HaleSchemaXSD extends XSD {
        
        private static final Logger log = LogFactory.getLogger(HaleSchemaXSD.class);

        /** application schema namespace */
    private String namespaceURI;

    /** location of the application schema itself */
    private String schemaLocation;
    
    private final ConfigurationType type;

    /**
     * Constructor
     * 
     * @param type the configuration type
     * @param namespaceURI the schema namespace
     * @param schemaLocation the schema location
     */
    public HaleSchemaXSD(ConfigurationType type, String namespaceURI, String schemaLocation) {
        this.namespaceURI = namespaceURI;
        this.schemaLocation = schemaLocation;
        
        this.type = type;
    }

    @SuppressWarnings("unchecked")
        @Override
        protected void addDependencies(Set dependencies) {
        switch (type) {
                case GML2:
                        dependencies.add(org.geotools.gml2.GML.getInstance());
                        break;
                case GML3_2:
                        dependencies.add(org.geotools.gml3.v3_2.GML.getInstance());
                        break;
                case GML3:
                        // fall through
                default:
                        dependencies.add(GML.getInstance());
                        break;
                }
    }

    @Override
        public String getNamespaceURI() {
        return namespaceURI;
    }

    @Override
        public String getSchemaLocation() {
        return schemaLocation;
    }
        
        @Override
        public SchemaLocationResolver createSchemaLocationResolver() {
        return new SchemaLocationResolver(this) {
                
                @Override
                public String resolveSchemaLocation(XSDSchema schema, String uri, String location) {
                    String schemaLocation;

                    if (schema == null) {
                        schemaLocation = getSchemaLocation();
                    } else {
                        schemaLocation = schema.getSchemaLocation();
                    }

                    String locationUri = null;

                    if ((null != schemaLocation) && !("".equals(schemaLocation))) {
                        String schemaLocationFolder = schemaLocation;
                        int lastSlash = schemaLocation.lastIndexOf('/');

                        if (lastSlash > 0) {
                            schemaLocationFolder = schemaLocation.substring(0, lastSlash);
                        }

                        if (schemaLocationFolder.startsWith("file:")) {
                            try {
                                schemaLocationFolder = DataUtilities.urlToFile(
                                        new URL(schemaLocationFolder)).getPath();
                            } catch (MalformedURLException e) {
                                // this can't be a good outcome, but try anyway
                                schemaLocationFolder = schemaLocationFolder.substring("file:".length());
                            }
                        }

                        File locationFile = new File(schemaLocationFolder, location);

                        if (locationFile.exists()) {
                            locationUri = locationFile.toURI().toString();
                        }
                    }

                    if ((locationUri == null) && (location != null) && location.startsWith("http:")) {
                        locationUri = location;
                    }
                    
                    if (locationUri != null) {
                        java.net.URI u;
                                                try {
                                                        u = new java.net.URI(locationUri);
                                                        u = u.normalize();
                                    locationUri = u.toString();
                                                } catch (Exception e) {
                                                        log.warn("Normalizing URI for schema resolving failed", e);
                                                }
                        }
                                        
                    return locationUri;
                }
            };
    }

}
