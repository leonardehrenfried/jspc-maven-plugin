package io.leonard.maven.plugins.jspc;

import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link JspcMojo}
 */
public class TestIncludesExcludesMojo {

  @Rule
  public MojoRule rule = new MojoRule();

  @Test
  public void shouldRespectIncludesAndExcludes() throws Exception {
    // Given
    File oneJspProject = new File("target/test-classes/unit/project_include_exclude");

    // When
    rule.executeMojo(oneJspProject, "compile");

    // Then
    Path jspPath = Paths.get("target/test-classes/unit/project_include_exclude/target/classes/jsp/jsp/_01_jsp.class");
    assertThat(jspPath).exists();

    Path jspfPath = Paths.get("target/test-classes/unit/project_include_exclude/target/classes/jsp/jsp/_02_jspf.class");
    assertThat(jspfPath).doesNotExist();
  }

}
