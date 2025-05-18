package com.studheupno.sqsbackend;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ArchUnitTests {
    @Test
    public void annotationNames() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests()).importPackages("com.studheupno.sqsbackend");

        ArchRule rule = classes().that().areAnnotatedWith(Service.class).should().haveSimpleNameEndingWith("Service");
        rule.check(importedClasses);

        rule = classes().that().areAnnotatedWith(RestController.class).should().haveSimpleNameEndingWith("Controller");
        rule.check(importedClasses);

        rule = classes().that().areAnnotatedWith(Configuration.class).should().haveSimpleNameEndingWith("Config");
        rule.check(importedClasses);

        rule = classes().that().areAnnotatedWith(Repository.class).should().haveSimpleNameEndingWith("Repo");
        rule.check(importedClasses);
    }

    @Test
    public void packageContainmentTest() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests()).importPackages("com.studheupno.sqsbackend");

        ArchRule rule = classes().that().areNotAnnotations().and().haveSimpleNameEndingWith("Service").should().resideInAPackage("..service");
        rule.check(importedClasses);

        rule = classes().that().areNotAnnotations().and().haveSimpleNameEndingWith("Entity").should().resideInAPackage("..entity..");
        rule.check(importedClasses);

        rule = classes().that().areNotAnnotations().and().haveSimpleNameEndingWith("Request").should().resideInAPackage("..dto");
        rule.check(importedClasses);

        rule = classes().that().areNotAnnotations().and().haveSimpleNameEndingWith("Response").should().resideInAPackage("..dto");
        rule.check(importedClasses);

        rule = classes().that().areNotAnnotations().and().haveSimpleNameEndingWith("Dto").should().resideInAPackage("..dto");
        rule.check(importedClasses);

        rule = classes().that().areNotAnnotations().and().haveSimpleNameEndingWith("Filter").should().resideInAPackage("..filter");
        rule.check(importedClasses);

        rule = classes().that().areNotAnnotations().and().haveSimpleNameEndingWith("Controller").should().resideInAPackage("..controller");
        rule.check(importedClasses);

        rule = classes().that().areNotAnnotations().and().haveSimpleNameEndingWith("Config").should().resideInAPackage("..config");
        rule.check(importedClasses);
    }
}
