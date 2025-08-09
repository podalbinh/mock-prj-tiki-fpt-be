package com.vn.backend.exceptions;

import com.vn.backend.dto.response.ResponseData;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData<ErrorResponse> handleInvalidDataException(InvalidDataException ex, WebRequest request, HttpServletRequest requests) {
        logger.warn("[InvalidDataException] {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .path(request.getDescription(false))
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .build();

        return ResponseData.badRequest(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData<ErrorResponse> handleAllException(Exception ex, WebRequest request,HttpServletRequest requests) {
        logger.error("[Exception] {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .path(request.getDescription(false))
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Hệ thống đang bận, vui lòng thử lại sau.")
                .build();

        return ResponseData.error( errorResponse);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseData<ErrorResponse> handleNotFoundException(NotFoundException ex, WebRequest request,HttpServletRequest requests) {
        logger.warn("[NotFoundException] {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .path(request.getDescription(false))
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .build();
        return ResponseData.notFound(errorResponse);

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NullPointerException.class)
    public ResponseData<ErrorResponse> handleNullPointerException(NullPointerException ex, WebRequest request,HttpServletRequest requests) {
        logger.warn("[NullPointerException] {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .path(request.getDescription(false))
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .build();
        return ResponseData.notFound(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseData<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request,HttpServletRequest requests) {
        logger.warn("[AccessDeniedException] {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .path(request.getDescription(false))
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(ex.getMessage())
                .build();
        return ResponseData.notFound(errorResponse);
    }

    @ExceptionHandler({ExpiredJwtException.class, SignatureException.class, MalformedJwtException.class, UnsupportedJwtException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseData<ErrorResponse> handleJwtException(Exception ex,  HttpServletRequest request,HttpServletRequest requests) {
        logger.warn("[JwtException] {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .path(request.getRequestURI())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message("Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại.")
                .build();
        return ResponseData.unauthorized(errorResponse);
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentNotValidException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData<ErrorResponse> handleValidationException(Exception e, WebRequest request, HttpServletRequest httpRequest) {
        String path = request.getDescription(false).replace("uri=", "");
        String message = e.getMessage();
        String error;

        if (e instanceof MethodArgumentNotValidException) {
            int start = message.lastIndexOf("[") + 1;
            int end = message.lastIndexOf("]") - 1;
            message = message.substring(start, end);
            error = "Invalid Payload";
            logger.warn("[MethodArgumentNotValidException] {}", message);
        } else if (e instanceof MissingServletRequestParameterException) {
            error = "Invalid Parameter";
            logger.warn("[MissingServletRequestParameterException] {}", message);
        } else if (e instanceof ConstraintViolationException) {
            error = "Invalid Parameter";
            message = message.substring(message.indexOf(" ") + 1);
            logger.warn("[ConstraintViolationException] {}", message);
        }else {
            error = "Invalid Data";
            logger.warn("[InvalidDataException] {}", message);
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .path(path)
                .error(error)
                .message(message)
                .build();

        return ResponseData.badRequest( errorResponse);
    }
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request, HttpServletRequest httpRequest) {
        logger.warn("[MethodArgumentTypeMismatchException] {}", ex.getMessage());
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date())
                .path(httpRequest.getRequestURI())
                .error(error)
                .message(ex.getLocalizedMessage())
                .build();
        return ResponseData.badRequest(errorResponse);
    }
}