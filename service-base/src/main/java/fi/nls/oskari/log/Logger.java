package fi.nls.oskari.log;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class Logger {

    public abstract boolean isDebugEnabled();

    public abstract void debug(Throwable t, final Object ... args);
    
    public abstract void debug(final Object ... args);

    public abstract void info(Throwable t, final Object ... args);
    
    public abstract void info(final Object ... args);
    
    public abstract void warn(Throwable t, final Object ... args);
    
    public abstract void warn(final Object ... args);
    
    public abstract void error(Throwable t, final Object ... args);
    
    public abstract void error(final Object ... args);
    
    public String getString(final Object ... args) {
        StringWriter w = new StringWriter();
        for(Object arg: args) {
            w.append(getAsString(arg));
            w.append(" ");
        }
        return w.toString();
    }

    private boolean hasToString(final Object arg) {
        if(arg == null) {
            return false;
        }
        try {
            final Class c = arg.getClass();
            return c.getMethod("toString").getDeclaringClass().equals(c);
        } catch (Exception e) {
            return false;
        }
    }

    private String mapToString(final Map arg) {
        StringWriter w = new StringWriter();
        w.append("Map [");
        boolean isFirst = true;
        for(Object o : arg.keySet()) {
            if(!isFirst) {
                w.append(",");
            }
            w.append("{");
            w.append(getAsString(o));
            w.append("=");
            w.append(getAsString(arg.get(o)));
            w.append("}");
            isFirst = false;
        }
        w.append("]");
        return w.toString();
    }
    private String listToString(final Collection arg) {
        StringWriter w = new StringWriter();
        w.append(arg.getClass().getCanonicalName());
        w.append(" [");
        boolean isFirst = true;
        for(Object o : arg) {
            if(!isFirst) {
                w.append(",");
            }
            w.append("{");
            w.append(getAsString(o));
            w.append("}");
            isFirst = false;
        }
        w.append("]");
        return w.toString();
    }
    public String getAsString(final Object arg) {
        if(arg == null) {
            return "<null value>";
        }
        // use objects toString if implemented
        if (hasToString(arg)) {
            return arg.toString();
        }
        // else do some formatting:
        if(arg instanceof String[]) {
            return Arrays.toString((String[])arg);
        }
        /*
        else if(arg instanceof Number[]) {
            return Arrays.toString((Number[])arg);
        }
        */
        else if(arg instanceof Collection) {
            return listToString((Collection)arg);
        }
        else if(arg instanceof Map) {
            return mapToString((Map)arg);
        }
        return ToStringBuilder.reflectionToString(arg,ToStringStyle.MULTI_LINE_STYLE);
    }
}
