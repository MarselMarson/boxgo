package com.szd.boxgo.i18n;

import java.util.HashMap;
import java.util.Map;

public final class AppMessages {

    public static final String KEY_GENERIC_ERROR = "GENERIC_ERROR";
    public static final String KEY_APP_VERSION_EXPIRED = "APP_VERSION_EXPIRED";
    public static final String KEY_TOKEN_EXPIRED = "TOKEN_EXPIRED";
    public static final String KEY_USER_BLOCKED = "USER_BLOCKED";
    public static final String KEY_TOKEN_IS_MISSING = "TOKEN_IS_MISSING";

    // Поддерживаемые коды языков со стороны фронта: en ru es pt fr at in tr th vi
    private static final Map<String, Map<String, String>> DICT = new HashMap<>();

    static {
        // EN
        put("en", KEY_GENERIC_ERROR, "Sorry, something went wrong. Please try again.");
        put("en", KEY_APP_VERSION_EXPIRED, "Your app version is no longer supported. Please update to continue.");
        put("en", KEY_TOKEN_EXPIRED, "Your session has expired. Please sign in again.");
        put("en", KEY_USER_BLOCKED, "Your account was deleted or blocked.");
        put("en", KEY_TOKEN_IS_MISSING, "Authorization token is missing");

        // RU
        put("ru", KEY_GENERIC_ERROR, "К сожалению, произошла ошибка. Попробуйте ещё раз.");
        put("ru", KEY_APP_VERSION_EXPIRED, "Ваша версия приложения устарела. Пожалуйста, обновите приложение, чтобы продолжить.");
        put("ru", KEY_TOKEN_EXPIRED, "Сессия истекла. Пожалуйста, войдите снова.");
        put("ru", KEY_USER_BLOCKED, "Ваш аккаунт удалён или заблокирован.");
        put("ru", KEY_TOKEN_IS_MISSING, "Отсутствует токен авторизации");

        // ES (LatAm)
        put("es", KEY_GENERIC_ERROR, "Lo sentimos, algo salió mal. Inténtalo de nuevo.");
        put("es", KEY_APP_VERSION_EXPIRED, "Tu versión de la app ya no es compatible. Actualiza para continuar.");
        put("es", KEY_TOKEN_EXPIRED, "Tu sesión ha expirado. Vuelve a iniciar sesión.");
        put("es", KEY_USER_BLOCKED, "Tu cuenta fue eliminada o bloqueada.");
        put("es", KEY_TOKEN_IS_MISSING, "Falta el token de autorización");

        // PT (Brazil)
        put("pt", KEY_GENERIC_ERROR, "Desculpe, algo deu errado. Tente novamente.");
        put("pt", KEY_APP_VERSION_EXPIRED, "A sua versão do app não é mais compatível. Atualize para continuar.");
        put("pt", KEY_TOKEN_EXPIRED, "Sua sessão expirou. Faça login novamente.");
        put("pt", KEY_USER_BLOCKED, "Sua conta foi excluída ou bloqueada.");
        put("pt", KEY_TOKEN_IS_MISSING, "Token de autorização ausente");

        // FR (Maghreb-friendly)
        put("fr", KEY_GENERIC_ERROR, "Désolé, une erreur s’est produite. Veuillez réessayer.");
        put("fr", KEY_APP_VERSION_EXPIRED, "Votre version de l’application n’est plus prise en charge. Veuillez mettre à jour pour continuer.");
        put("fr", KEY_TOKEN_EXPIRED, "Votre session a expiré. Veuillez vous reconnecter.");
        put("fr", KEY_USER_BLOCKED, "Votre compte a été supprimé ou bloqué.");
        put("fr", KEY_TOKEN_IS_MISSING, "Le jeton d’autorisation est manquant");

        // AT (Arabic NA) — фронт шлёт 'at'
        put("at", KEY_GENERIC_ERROR, "عذرًا، حدث خطأ ما. يرجى المحاولة مرة أخرى.");
        put("at", KEY_APP_VERSION_EXPIRED, "إصدار تطبيقك لم يعد مدعومًا. يرجى التحديث للمتابعة.");
        put("at", KEY_TOKEN_EXPIRED, "انتهت صلاحية الجلسة. يرجى تسجيل الدخول مرة أخرى.");
        put("at", KEY_USER_BLOCKED, "تم حذف حسابك أو حظره.");
        put("at", KEY_TOKEN_IS_MISSING, "رمز التفويض مفقود");

        // IN (Indonesian) — фронт шлёт 'in'
        put("in", KEY_GENERIC_ERROR, "Maaf, terjadi kesalahan. Silakan coba lagi.");
        put("in", KEY_APP_VERSION_EXPIRED, "Versi aplikasi Anda sudah tidak didukung. Harap perbarui untuk melanjutkan.");
        put("in", KEY_TOKEN_EXPIRED, "Sesi Anda telah kedaluwarsa. Silakan masuk kembali.");
        put("in", KEY_USER_BLOCKED, "Akun Anda telah dihapus atau diblokir.");
        put("in", KEY_TOKEN_IS_MISSING, "Token otorisasi hilang");

        // TR
        put("tr", KEY_GENERIC_ERROR, "Üzgünüz, bir şeyler ters gitti. Lütfen tekrar deneyin.");
        put("tr", KEY_APP_VERSION_EXPIRED, "Uygulamanızın sürümü artık desteklenmiyor. Devam etmek için lütfen güncelleyin.");
        put("tr", KEY_TOKEN_EXPIRED, "Oturumunuzun süresi doldu. Lütfen yeniden giriş yapın.");
        put("tr", KEY_USER_BLOCKED, "Hesabınız silindi veya engellendi.");
        put("tr", KEY_TOKEN_IS_MISSING, "Yetkilendirme jetonu eksik");

        // TH
        put("th", KEY_GENERIC_ERROR, "ขออภัย เกิดข้อผิดพลาด กรุณาลองอีกครั้ง");
        put("th", KEY_APP_VERSION_EXPIRED, "แอปเวอร์ชันของคุณไม่ได้รับการรองรับแล้ว กรุณาอัปเดตเพื่อใช้งานต่อ");
        put("th", KEY_TOKEN_EXPIRED, "เซสชันของคุณหมดอายุ กรุณาเข้าสู่ระบบอีกครั้ง");
        put("th", KEY_USER_BLOCKED, "บัญชีของคุณถูกลบหรือถูกระงับ");
        put("th", KEY_TOKEN_IS_MISSING, "ไม่มีโทเค็นการยืนยัน");

        // VI
        put("vi", KEY_GENERIC_ERROR, "Rất tiếc, đã xảy ra lỗi. Vui lòng thử lại.");
        put("vi", KEY_APP_VERSION_EXPIRED, "Phiên bản ứng dụng của bạn không còn được hỗ trợ. Vui lòng cập nhật để tiếp tục.");
        put("vi", KEY_TOKEN_EXPIRED, "Phiên đăng nhập của bạn đã hết hạn. Vui lòng đăng nhập lại.");
        put("vi", KEY_USER_BLOCKED, "Tài khoản của bạn đã bị xóa hoặc bị chặn.");
        put("vi", KEY_TOKEN_IS_MISSING, "Thiếu mã thông báo xác thực");
    }

    private static void put(String lang, String key, String val) {
        DICT.computeIfAbsent(lang, k -> new HashMap<>()).put(key, val);
    }

    public static String get(String lang, String key) {
        Map<String, String> map = DICT.getOrDefault(lang, DICT.get("en"));
        return map.getOrDefault(key, DICT.get("en").getOrDefault(key, "Error"));
    }

    private AppMessages() {
    }
}
