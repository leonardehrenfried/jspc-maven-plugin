package org.apache.jasper.compiler;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TestJDTCompilerParallel {

  @Test
  public void should_return_false_when_isCheckFileNecessary_input_with_parent_jsp_package_01jsp() {
    // Given
    JDTCompilerParallel compiler = new JDTCompilerParallel();
    char[] packageName = "01.jsp".toCharArray();

    // When
    boolean isPackage = compiler.isCheckFileNecessary(packageName);

    // Then
    Assertions.assertThat(isPackage).isFalse();
  }

  @Test
  public void should_return_true_when_isCheckFileNecessary_input_with_parent_null_package_java() {
    // Given
    JDTCompilerParallel compiler = new JDTCompilerParallel();
    char[] packageName = "java".toCharArray();

    // When
    boolean isPackage = compiler.isCheckFileNecessary(packageName);

    // Then
    Assertions.assertThat(isPackage).isTrue();
  }

  @Test
  public void should_return_true_when_isCheckFileNecessary_input_with_parent_java_package_lang() {
    // Given
    JDTCompilerParallel compiler = new JDTCompilerParallel();
    char[] packageName = "lang".toCharArray();

    // When
    boolean isPackage = compiler.isCheckFileNecessary(packageName);

    // Then
    Assertions.assertThat(isPackage).isTrue();
  }

  @Test
  public void should_return_true_when_isCheckFileNecessary_input_with_parent_javax_servlet_package_http() {
    // Given
    JDTCompilerParallel compiler = new JDTCompilerParallel();
    char[] packageName = "http".toCharArray();

    // When
    boolean isPackage = compiler.isCheckFileNecessary(packageName);

    // Then
    Assertions.assertThat(isPackage).isTrue();
  }

  @Test
  public void should_return_false_when_isCheckFileNecessary_input_with_parent_java_util_package_Hashset() {
    // Given
    JDTCompilerParallel compiler = new JDTCompilerParallel();
    char[] packageName = "Hashset".toCharArray();

    // When
    boolean isPackage = compiler.isCheckFileNecessary(packageName);

    // Then
    Assertions.assertThat(isPackage).isFalse();
  }

  @Test
  public void should_return_true_when_isCheckFileNecessary_input_with_parent_org_apache_jsp_tag_package_a_tagfile() {
    // Given
    JDTCompilerParallel compiler = new JDTCompilerParallel();
    char[] packageName = "a_tagfile".toCharArray();

    // When
    boolean isPackage = compiler.isCheckFileNecessary(packageName);

    // Then
    Assertions.assertThat(isPackage).isTrue();
  }
}
