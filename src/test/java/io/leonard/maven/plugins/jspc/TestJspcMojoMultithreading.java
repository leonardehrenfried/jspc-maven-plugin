package io.leonard.maven.plugins.jspc;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.*;

import org.apache.maven.plugin.testing.MojoRule;
import org.junit.*;

/**
 * Test {@link JspcMojo} in multithreading mode
 */
public class TestJspcMojoMultithreading {

  @Rule
  public MojoRule rule = new MojoRule();

  @Test
  public void should_return_same_compiled_jsp_as_monothread_reference_when_executeMojo_on_project_many_jsp_with_4_threads() throws Exception {
    // Given
    File manyJspProject = new File("target/test-classes/unit/project_many_jsp");
    File manyJspProject4Threads = new File("target/test-classes/unit/project_many_jsp_4_threads");

    // When
    rule.executeMojo(manyJspProject, "compile");
    rule.executeMojo(manyJspProject4Threads, "compile");

    // Then
    assertThat(Files.readAllBytes(Paths.get("target/test-classes/unit/project_many_jsp_4_threads/target/classes/jsp/jsp/_01_jsp.class")))
      .isEqualTo(Files.readAllBytes(Paths.get("target/test-classes/unit/project_many_jsp/target/classes/jsp/jsp/_01_jsp.class")));
    assertThat(Files.readAllBytes(Paths.get("target/test-classes/unit/project_many_jsp_4_threads/target/classes/jsp/jsp/_02_jsp.class")))
      .isEqualTo(Files.readAllBytes(Paths.get("target/test-classes/unit/project_many_jsp/target/classes/jsp/jsp/_02_jsp.class")));
    assertThat(Files.readAllBytes(Paths.get("target/test-classes/unit/project_many_jsp_4_threads/target/classes/jsp/jsp/_03_jsp.class")))
      .isEqualTo(Files.readAllBytes(Paths.get("target/test-classes/unit/project_many_jsp/target/classes/jsp/jsp/_03_jsp.class")));
    assertThat(Files.readAllBytes(Paths.get("target/test-classes/unit/project_many_jsp_4_threads/target/classes/jsp/jsp/_04_jsp.class")))
      .isEqualTo(Files.readAllBytes(Paths.get("target/test-classes/unit/project_many_jsp/target/classes/jsp/jsp/_04_jsp.class")));
  }
}
