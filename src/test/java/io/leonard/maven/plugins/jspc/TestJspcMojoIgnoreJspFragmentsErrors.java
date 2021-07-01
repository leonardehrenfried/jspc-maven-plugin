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
public class TestJspcMojoIgnoreJspFragmentsErrors {

  @Rule
  public MojoRule rule = new MojoRule();

  private String PROJECT_PATH = "target/test-classes/unit/project_ignore_jsp_fragments_errors";
  private String TARGET_PATH = PROJECT_PATH + "/target/classes/jsp/jsp/";

  @Test
  public void shouldRespectIncludesAndExcludes() throws Exception {
    // Given
    File includeExcludeProject = new File(PROJECT_PATH);

    // When
    rule.executeMojo(includeExcludeProject, "compile");

    // Then
    Path jspPath = Paths.get(TARGET_PATH + "_01_jsp.class");
    assertThat(jspPath).exists();

    Path jspfPath = Paths.get(TARGET_PATH + "_01_jspf.class");
    assertThat(jspfPath).doesNotExist();
  }

}
