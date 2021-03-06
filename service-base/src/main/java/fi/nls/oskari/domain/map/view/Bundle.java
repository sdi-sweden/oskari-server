package fi.nls.oskari.domain.map.view;

import java.io.Serializable;

public class Bundle implements Comparable, Serializable {
    private long bundleId = -1;
    private long viewId = -1;
    private int seqNo = -1;

    private String state;
    private String config;
    private String startup;
    private String name;
    private String bundleinstance;

    public String toString() {
        return
                "bundleId = '" + bundleId +"'\n" +
                "viewId   = '" + viewId +"'\n" +
                "seqNo    = '" + seqNo +"'\n" +
                "state    = '" + state +"'\n" +
                "config   = '" + config +"'\n" +
                "startup  = '" + startup +"'\n" +
                "bundleinstance  = '" + bundleinstance +"'\n" +
                "name     = '" + name +"'\n";

    }

    public int compareTo(Object o) throws ClassCastException {
	if (o instanceof Bundle) {
	    return (this.seqNo - ((Bundle) o).seqNo);
	} else {
	    throw new ClassCastException("Can't compare Bundle with " +
					 o.getClass().getName());
	}
    }

    public long getViewId() {
        return this.viewId;
    }
    public void setViewId(long viewId) {
        this.viewId = viewId;
    }

    public long getBundleId() {
        return this.bundleId;
    }
    public void setBundleId(long bundleId) {
        this.bundleId = bundleId;
    }
    
    public int getSeqNo() { return this.seqNo; }
    public void setSeqNo(int seqNo) { this.seqNo = seqNo; }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getStartup() {
        return startup;
    }

    public void setStartup(String startup) {
        this.startup = startup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBundleinstance() {
        if(bundleinstance == null || bundleinstance.isEmpty()) {
            return getName();
        }
        return bundleinstance;
    }

    public void setBundleinstance(String bundleinstance) {
        this.bundleinstance = bundleinstance;
    }
}
