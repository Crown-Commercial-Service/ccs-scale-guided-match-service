package uk.gov.crowncommercial.dts.scale.service.gm.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.RestClientException;

import com.rollbar.notifier.Rollbar;

import lombok.extern.slf4j.Slf4j;
import uk.gov.crowncommercial.dts.scale.service.gm.exception.MissingGMDataException;
import uk.gov.crowncommercial.dts.scale.service.gm.exception.ResourceNotFoundException;
import uk.gov.crowncommercial.dts.scale.service.gm.model.ApiError;
import uk.gov.crowncommercial.dts.scale.service.gm.model.ApiErrors;

/**
 * Centralised error handling for application and container derived error conditions.
 */
@ControllerAdvice
@RestController
@Slf4j
public class GlobalErrorHandler implements ErrorController {

  private static final String ERR_MSG_DEFAULT = "An error occured processing the request";
  private static final String ERR_MSG_VALIDATION = "Validation error processing the request";
  private static final String ERR_MSG_RESOURCE_NOT_FOUND = "Resource not found";
	
  @Autowired
  private Rollbar rollbar;

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ValidationException.class, HttpMessageNotReadableException.class})
  public ApiErrors handleValidationException(final Exception exception) {

    log.trace("Request validation exception", exception);
    rollbar.info(exception,"Request validation exception");

    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.toString(), ERR_MSG_RESOURCE_NOT_FOUND,
        exception.getMessage());
    return new ApiErrors(Arrays.asList(apiError));
  }

  @ExceptionHandler({RestClientException.class})
  public ResponseEntity<ApiErrors> handleRestClientException(final RestClientException exception) {

    log.error("Upstream (Decision Tree Service) error", exception);
    rollbar.error(exception, "Upstream (Decision Tree Service) error");
    
    if (exception instanceof NotFound) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiErrors(Arrays.asList(new ApiError(HttpStatus.NOT_FOUND.toString(),
              ERR_MSG_RESOURCE_NOT_FOUND, "Upstream resource not found"))));
    }
    return ResponseEntity.badRequest().body(new ApiErrors(Arrays
        .asList(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ERR_MSG_DEFAULT, ""))));
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({ResourceNotFoundException.class})
  public ApiErrors handleResourceNotFoundException(final ResourceNotFoundException exception) {

    log.info("Requested resource not found", exception);
    rollbar.info(exception, "Requested resource not found");

    ApiError apiError =
        new ApiError(HttpStatus.NOT_FOUND.toString(), ERR_MSG_VALIDATION, exception.getMessage());
    return new ApiErrors(Arrays.asList(apiError));
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({MissingGMDataException.class}) // Others
  public ApiErrors handleInternalException(final Exception exception) {

    log.error("Internal application exception", exception);
    rollbar.error(exception, "Internal application exception");

    ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ERR_MSG_DEFAULT,
        "Contact administrator");
    return new ApiErrors(Arrays.asList(apiError));
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ApiErrors handleUnknownException(final Exception exception) {

    log.error("Unknown application exception", exception);
    rollbar.error(exception, "Unknown application exception");

    ApiError apiError =
        new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ERR_MSG_DEFAULT, "");
    return new ApiErrors(Arrays.asList(apiError));
  }

  @RequestMapping("/error")
  public ResponseEntity<ApiErrors> handleError(final HttpServletRequest request,
      final HttpServletResponse response) {

    Object exception = request.getAttribute("javax.servlet.error.exception");

    log.error("Unknown container/filter exception", exception);
    rollbar.error((Throwable) exception, "Unknown container/filter exception");

    return ResponseEntity.badRequest().body(new ApiErrors(Arrays
        .asList(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ERR_MSG_DEFAULT, ""))));
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }

}
