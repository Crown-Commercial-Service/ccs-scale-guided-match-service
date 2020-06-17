package uk.gov.crowncommercial.dts.scale.service.gm.exception;

/**
 * Expected GM data not found or mis-configured
 */
public class MissingGMDataException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public MissingGMDataException(final String msg) {
    super(msg);
  }

  public MissingGMDataException(final String msg, final Throwable cause) {
    super(msg, cause);
  }

}
