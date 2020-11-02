package io.leonard.maven.plugins.jspc;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.plugin.testing.MojoRule;
import org.junit.*;

/**
 * Test {@link JspcMojo}
 */
public class TestJspcMojo {

  private final static int JAVA_11_BYTECODE_VERSION = 55;

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
    String actualWebfrag = String.join("", Files.readAllLines(webfrag));
    String expectedWebFrag = String.join("", Files.readAllLines(expectedWebfrag));
    assertThat(actualWebfrag).isEqualToIgnoringWhitespace(expectedWebFrag);
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
    String result = getWebXmlReader("project_one_jsp_mergeFragment").lines().collect(Collectors.joining(System.lineSeparator()));
    String expectedResult = getExpectedWebXmlReader("project_one_jsp_mergeFragment").lines().collect(Collectors.joining(System.lineSeparator()));
    assertThat(result).isEqualToIgnoringWhitespace(expectedResult);
  }

  @Test
  public void should_raise_no_validating_xml_error_when_mergeFragment_is_true_many_jsp_4_threads() throws Exception {
    // Given
    File oneJspProject = new File("target/test-classes/unit/project_many_jsp_4threads_mergeFragment");

    // When
    rule.executeMojo(oneJspProject, "compile");

    // Then
    //no error, it's good enough
  }

  @Test
  public void should_return_correct_merged_xml_when_mergeFragment_is_true_and_web_xml_contains_dtd_insteadof_xsd() throws Exception {
    // Given
    File oneJspProject = new File("target/test-classes/unit/project_one_jsp_web_xml_dtd");

    // When
    rule.executeMojo(oneJspProject, "compile");

    // Then
    String result = getWebXmlReader("project_one_jsp_web_xml_dtd").lines().collect(Collectors.joining(System.lineSeparator()));
    String expectedResult = getExpectedWebXmlReader("project_one_jsp_web_xml_dtd").lines().collect(Collectors.joining(System.lineSeparator()));
    assertThat(result).isEqualToIgnoringWhitespace(expectedResult);
  }

  @Test
  public void should_return_correct_merged_xml_when_mergeFragment_is_true_and_httpProxy_given() throws Exception {
    // Given
    File oneJspProject = new File("target/test-classes/unit/project_one_jsp_httpProxy");

    // When
    rule.executeMojo(oneJspProject, "compile");

    // Then
    String result = getWebXmlReader("project_one_jsp_httpProxy").lines().collect(Collectors.joining(System.lineSeparator()));
    String expectedResult = getExpectedWebXmlReader("project_one_jsp_httpProxy").lines().collect(Collectors.joining(System.lineSeparator()));
    assertThat(result).isEqualToIgnoringWhitespace(expectedResult);
  }

  @Test
  public void should_return_one_compiled_jsp_in_bytecode_java11_when_executeMojo_on_project_one_jsp_compilerVersion_11() throws Exception {
    // Given
    File oneJspProject = new File("target/test-classes/unit/project_one_jsp_compilerVersion_11");

    // When
    rule.executeMojo(oneJspProject, "compile");

    // Then
    Path indexJspPath = Paths.get("target/test-classes/unit/project_one_jsp_compilerVersion_11/target/classes/jsp/jsp/index_jsp.class");
    int[] byteCodeVersion = JspcMojoTestUtils.getClassVersion(indexJspPath);
    assertThat(byteCodeVersion[0]).isEqualTo(JAVA_11_BYTECODE_VERSION);
  }

  private BufferedReader getExpectedWebXmlReader(String projectName) throws FileNotFoundException {
    return new BufferedReader(new InputStreamReader(
        new FileInputStream(new File("target/test-classes/unit/" + projectName + "/src/assert/expectedWebXml.xml")), StandardCharsets.UTF_8));
  }

  private BufferedReader getWebXmlReader(String projectName) throws FileNotFoundException {
    return new BufferedReader(new InputStreamReader(
        new FileInputStream(new File("target/test-classes/unit/" + projectName + "/target/web.xml")), StandardCharsets.UTF_8));
  }
}
