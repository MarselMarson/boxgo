package com.szd.boxgo.i18n;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public final class AppLocale {

    public static final String ATTR_LANG = "X-Resolved-Lang";
    public static final String HEADER_LANG = "X-Lang";
    public static final String HEADER_LANG_ALT = "X-App-Lang";
    public static final String HEADER_ACCEPT_LANG = "Accept-Language";

    // фронт шлёт: en ru es pt fr at in tr th vi
    private static final String[] SUPPORTED = {"en","ru","es","pt","fr","at","in","tr","th","vi"};

    public static String resolveLang(HttpServletRequest req) {
        String raw = header(req, HEADER_LANG);
        if (raw == null) raw = header(req, HEADER_LANG_ALT);
        if (raw == null) raw = header(req, HEADER_ACCEPT_LANG);
        String code = normalize(raw);
        return isSupported(code) ? code : "en";
    }

    private static String header(HttpServletRequest req, String name) {
        String v = req.getHeader(name);
        if (StringUtils.isBlank(v)) return null;
        return v.trim();
    }

    private static String normalize(String raw) {
        if (StringUtils.isBlank(raw)) return null;
        String lower = raw.toLowerCase();

        // берём первые два символа до '-', если нужно
        if (lower.contains("-")) {
            lower = lower.substring(0, lower.indexOf('-'));
        }

        // фронт использует "in" (Индонезия) и "at" (арабский), не трогаем
        return switch (lower) {
            case "id" -> "in"; // если вдруг пришлют ISO-код для индонезийского
            case "ar" -> "at"; // если вдруг пришлют ISO-код арабского
            default -> lower;
        };
    }

    private static boolean isSupported(String code) {
        if (code == null) return false;
        for (String s : SUPPORTED) if (s.equals(code)) return true;
        return false;
    }

    private AppLocale() {}
}
