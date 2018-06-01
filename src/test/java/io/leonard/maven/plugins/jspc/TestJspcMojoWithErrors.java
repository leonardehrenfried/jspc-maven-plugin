package io.leonard.maven.plugins.jspc;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.project.MavenProject;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test {@link JspcMojo} with errors
 */
public class TestJspcMojoWithErrors {

  @Rule
  public MojoRule rule = new MojoRule();

  @Test(expected=MojoFailureException.class)
  public void should_return_same_compiled_jsp_as_monothread_reference_when_executeMojo_on_project_many_jsp_with_4_threads() throws Exception {
    // Given
    File manyJspProject = new File("target/test-classes/unit/project_many_jsp_with_errors");
    
    MavenProject project = rule.readMavenProject(new File("target/test-classes/unit/project_many_jsp_with_errors"));
    JspcMojo mojo = (JspcMojo) rule.lookupConfiguredMojo(project, "compile");
    assertThat(mojo).isNotNull();
    mojo.execute();

    // When
    rule.executeMojo(manyJspProject, "compile");
  }
}
