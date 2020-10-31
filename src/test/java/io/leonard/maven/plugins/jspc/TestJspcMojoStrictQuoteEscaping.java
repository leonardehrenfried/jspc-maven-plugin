package io.leonard.maven.plugins.jspc;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Test {@link JspcMojo} with and without strict quote escape parameter
 */
public class TestJspcMojoStrictQuoteEscaping {

  @Rule
  public MojoRule rule = new MojoRule();

  @Test
  public void should_throw_error_when_not_escaped() throws Exception {

    // Given
    File project = new File("target/test-classes/unit/project_strict_quote_escaping");

    // Then
    try {
      rule.executeMojo(project, "compile");
    } catch (MojoExecutionException e) {
      return;
    }

    fail("Expected compilation failure on sloppy quote escaping");

  }

  @Test
  public void should_compile_even_if_not_escaped() throws Exception {

    // Given
    File project = new File("target/test-classes/unit/project_no_strict_quote_escaping");
    File target = new File(project, "/target/classes/jsp/jsp/_01_jsp.class");

    // When
    rule.executeMojo(project, "compile");

    // Then
    assertThat(target.exists()).isTrue();

  }

}
