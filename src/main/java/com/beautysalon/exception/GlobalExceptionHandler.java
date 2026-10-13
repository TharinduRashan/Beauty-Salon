package com.beautysalon.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException ex, Model model) {
        model.addAttribute("errorTitle", "Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException ex, Model model) {
        model.addAttribute("errorTitle", "Access Denied");
        model.addAttribute("errorMessage", "You do not have permission to access this page.");
        return "error/403";
    }

    @ExceptionHandler(ConflictException.class)
    public String handleConflict(ConflictException ex, Model model) {
        model.addAttribute("errorTitle", "Conflict");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/500";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        log.error("Unhandled exception", ex);
        model.addAttribute("errorTitle", "Server Error");
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
        return "error/500";
    }
}
