package com.solantec.contro;

import java.util.HashMap;
import java.util.Map;

public abstract class ControVariableUtils {

    protected static ThreadLocal<Map<String, Object>> params = ThreadLocal.withInitial(() -> new HashMap<>());

    protected static final String IS_FAIlURE = qualify("is_failure");

    protected static final String REQ_RESOURCEDEFINDATION = qualify("req_resourcedefindation");

    protected static final String DEFAULT_FALLBACK_PATH = "/sContro/default_fallback_path";

    protected static final String CURRENT_CONTRO = qualify("current_contro");

    private static String qualify(String string) {
        return ControVariableUtils.class.getName() + string;
    }

    protected Contro getCurrentContro() {
        return (Contro) params.get().get(CURRENT_CONTRO);
    }

    protected boolean isFail() {
        return (boolean) params.get().get(IS_FAIlURE);
    }

    protected String getDefaultFallbackPath() {
        return (String) params.get().get(DEFAULT_FALLBACK_PATH);
    }

    protected void setFail(boolean fail) {
        params.get().put(IS_FAIlURE, fail);
    }

    protected void setCurrentContro(Contro contro) {
        params.get().put(CURRENT_CONTRO, contro);
    }

}
