package io.leonard.maven.plugins.jspc;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.plugin.testing.MojoRule;
import org.junit.*;

/**
 * Test {@link JspcMojo}
 */
public class TestJspcMojo {

  @Rule
  public MojoRule rule = new MojoRule();

  @Test
  public void should_return_one_compiled_jsp_when_executeMojo_on_project_one_jsp_with_no_options() throws Exception {
    // Given
    File oneJspProject = new File("target/test-classes/unit/project_one_jsp");

    // When
    rule.executeMojo(oneJspProject, "compile");

    // Then
    Path indexJspPath = Paths.get("target/test-classes/unit/project_one_jsp/target/classes/jsp/jsp/index_jsp.class");
    assertThat(indexJspPath).isNotNull();
  }

  @Test
  public void should_return_webfrag_equal_to_reference_when_executeMojo_on_project_one_jsp_with_no_options()
      throws Exception {
    // Given
    File oneJspProject = new File("target/test-classes/unit/project_one_jsp");
    Path expectedWebfrag = Paths.get("target/test-classes/unit/project_one_jsp/src/assert/webfrag.xml");

    // When
    rule.executeMojo(oneJspProject, "compile");

    // Then
    Path webfrag = Paths.get("target/test-classes/unit/project_one_jsp/target/webfrag.xml");
    List<String> actualWebfrag = Files.readAllLines(webfrag);
    List<String> expectedWebFrag = Files.readAllLines(expectedWebfrag);
    assertThat(actualWebfrag).isEqualTo(expectedWebFrag);
  }

  @Test
  public void should_return_one_compiled_jsp_when_executeMojo_on_project_one_jsp_with_space_with_no_options()
      throws Exception {
    // Given
    File oneJspProject = new File("target/test-classes/unit/project_one_jsp with space");

    // When
    rule.executeMojo(oneJspProject, "compile");

    // Then
    Path indexJspPath = Paths
        .get("target/test-classes/unit/project_one_jsp with space/target/classes/jsp/jsp/index_jsp.class");
    assertThat(indexJspPath).isNotNull();
  }

  @Test
  public void should_return_correct_merged_xml_when_mergeFragment_is_true() throws Exception {
    // Given
    File oneJspProject = new File("target/test-classes/unit/project_one_jsp_mergeFragment");

    // When
    rule.executeMojo(oneJspProject, "compile");

    // Then
    String result = getWebXmlReader().lines().collect(Collectors.joining(System.lineSeparator()));
    String expectedResult = getExpectedWebXmlReader().lines().collect(Collectors.joining(System.lineSeparator()));
    assertThat(result).isEqualTo(expectedResult);
  }

  private BufferedReader getExpectedWebXmlReader() throws FileNotFoundException {
    return new BufferedReader(new InputStreamReader(
        new FileInputStream(new File("target/test-classes/unit/project_one_jsp_mergeFragment/src/assert/expectedWebXml.xml"))));
  }

  private BufferedReader getWebXmlReader() throws FileNotFoundException {
    return new BufferedReader(new InputStreamReader(
        new FileInputStream(new File("target/test-classes/unit/project_one_jsp_mergeFragment/target/web.xml"))));
  }
}
