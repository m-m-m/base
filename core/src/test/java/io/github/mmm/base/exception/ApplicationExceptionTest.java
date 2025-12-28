package io.github.mmm.base.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link ApplicationException}.
 */
class ApplicationExceptionTest extends Assertions {

  /** Test {@link ApplicationException#getCode()} */
  @Test
  void testWithCode() {

    // arrange
    String message = "Something went wrong.";
    String code = "MagicCode";
    // act
    ApplicationException exception = new ApplicationException(message) {
      @Override
      public String getCode() {

        return code;
      }
    };
    StringWriter sw = new StringWriter(1024);
    PrintWriter pw = new PrintWriter(sw);
    exception.printStackTrace(pw);
    String stackTrace = sw.toString();
    // assert
    assertThat(exception.getLocalizedMessage()).isEqualTo(message);
    assertThat(exception.getLocalizedMessage(Locale.ROOT)).isEqualTo(message);
    assertThat(exception.getNlsMessage().getMessage()).isEqualTo(message);
    String msg = code + ": " + message + System.lineSeparator() + exception.getUuid();
    assertThat(exception.getMessage()).isEqualTo(msg);
    String toString = exception.getClass().getName() + ": " + msg;
    assertThat(exception.toString()).isEqualTo(toString);
    assertThat(stackTrace).startsWith(toString)
        .contains("io.github.mmm.base.exception.ApplicationExceptionTest.testWithCode(");
  }

}
