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

  private String PROJECT_PATH = "target/test-classes/unit/project_include_exclude";
  private String TARGET_PATH = PROJECT_PATH + "/target/classes/jsp/jsp/";

  @Test
  public void shouldRespectIncludesAndExcludes() throws Exception {
    // Given
    File includeExcludeProject = new File(PROJECT_PATH);

    // When
    rule.executeMojo(includeExcludeProject, "compile");

    // Then
    Path jspPath = Paths.get(TARGET_PATH + "included_jsp.class");
    assertThat(jspPath).exists();

    Path jspfPath = Paths.get(TARGET_PATH + "excluded_jspf.class");
    assertThat(jspfPath).doesNotExist();
  }

}
