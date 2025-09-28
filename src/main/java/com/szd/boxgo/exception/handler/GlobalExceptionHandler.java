package com.szd.boxgo.exception.handler;

import com.szd.boxgo.exception.*;
import com.szd.boxgo.i18n.AppLocale;
import com.szd.boxgo.i18n.AppMessages;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private String lang(HttpServletRequest request) {
        String l = (String) request.getAttribute(AppLocale.ATTR_LANG);
        return (l != null) ? l : AppLocale.resolveLang(request);
    }

    private ErrorResponse buildError(HttpServletRequest req, HttpStatus status, String code, String message) {
        return new ErrorResponse(req.getRequestURL().toString(), status, code, message);
    }

    @ExceptionHandler(AppVersionTooLowException.class)
    @ResponseStatus(HttpStatus.UPGRADE_REQUIRED)
    public ErrorResponse handleVersion(
            AppVersionTooLowException exception, HttpServletRequest request) {
        return buildError(request, HttpStatus.UPGRADE_REQUIRED, "AppVersionTooLow",
                AppMessages.get(lang(request), AppMessages.KEY_APP_VERSION_EXPIRED));
    }

    @ExceptionHandler(UserDeletedOrBannedException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ErrorResponse handleUserGone(UserDeletedOrBannedException ex, HttpServletRequest req) {
        return buildError(req, HttpStatus.GONE, "UserDeletedOrBanned",
                AppMessages.get(lang(req), AppMessages.KEY_USER_BLOCKED));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(EntityNotFoundException ex, HttpServletRequest req) {
        return buildError(req, HttpStatus.NOT_FOUND, "EntityNotFound",
                AppMessages.get(lang(req), AppMessages.KEY_GENERIC_ERROR));
    }

    @ExceptionHandler({
            EmailAlreadyExistsException.class,
            CodeNotFoundException.class,
            DataValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(RuntimeException ex, HttpServletRequest req) {
        return buildError(req, HttpStatus.BAD_REQUEST, ex.getClass().getSimpleName(),
                AppMessages.get(lang(req), AppMessages.KEY_GENERIC_ERROR));
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(ConflictException ex, HttpServletRequest req) {
        return buildError(req, HttpStatus.CONFLICT, "ConflictException",
                AppMessages.get(lang(req), AppMessages.KEY_GENERIC_ERROR));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return buildError(req, HttpStatus.FORBIDDEN, "AccessDenied",
                AppMessages.get(lang(req), AppMessages.KEY_GENERIC_ERROR));
    }

    @ExceptionHandler({JwtException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleJwt(RuntimeException ex, HttpServletRequest req) {
        return buildError(req, HttpStatus.UNAUTHORIZED, "AuthenticationError",
                AppMessages.get(lang(req), AppMessages.KEY_TOKEN_EXPIRED));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        return buildError(req, HttpStatus.BAD_REQUEST, "ValidationError",
                AppMessages.get(lang(req), AppMessages.KEY_GENERIC_ERROR));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntime(RuntimeException ex, HttpServletRequest req) {
        return buildError(req, HttpStatus.INTERNAL_SERVER_ERROR, "RuntimeException",
                AppMessages.get(lang(req), AppMessages.KEY_GENERIC_ERROR));
    }
}
