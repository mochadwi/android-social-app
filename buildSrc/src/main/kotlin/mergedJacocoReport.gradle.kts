plugins {
    jacoco
}

jacoco {
    toolVersion = "0.8.3"
    reportsDir = file("$buildDir/customJacocoReportDir")
}

/**
 * The correct path of the report is $rootProjectDir/app/build/reports/jacoco/index.html
 * to run this task use: ./gradlew clean :moduleName:mergedJacocoReport
 */
task<JacocoReport>("mergedJacocoReport") {

    // todo use variant.getFlavourName() instead?
    dependsOn("testDevelopmentDebugUnitTest", "testProductionDebugUnitTest",
            "createDevelopmentDebugCoverageReport", "createProductionDebugCoverageReport")

    reports {
        xml.isEnabled = true
        csv.isEnabled = false
        html.destination = file("$buildDir/reports/jacocoHtml")
        xml.destination = file("$buildDir/reports/jacoco.xml")
    }

    val fileFilter = mutableSetOf("**/R.class", "**/R$*.class", "**/BuildConfig.*",
            "**/Manifest*.*", "**/*Test*.*", "android/**/*.*",
            "**/*\$ViewInjector*.*",
            "**/*\$ViewBinder*.*",
            // Jacoco can not handle several "$" in class name.
            "**/*\$Lambda$*.*",
            // Anonymous classes generated by kotlin
            "**/*$*$*.*",
            //add libraries
            "android/**/*.*",
            "com/**/*.*",
            "uk/**/*.*",
            "io/**/*.*",
            //remove what we don"t coverage
            "androidTest/**/*.*",
            "test/**/*.*",
            "**/injector/**/*.*",
            "**/model/**/*.*",
            "**/mock/**/*.*",
            "**/event/**/*.*",
            "**/**_ViewBinding**",
            "**/*EventType.*",
            "**/**Mocked"
    )
    val javaDebugTree = fileTree("${project.buildDir}/intermediates/javac/debug") {
        exclude(fileFilter)
    }
    val kotlinDebugTree = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }
    val mainSrc = "${project.projectDir}/src/main/kotlin"

    sourceDirectories.from(files(mainSrc))
    // we need to target both java and kotlin build folder
    classDirectories.from(files(javaDebugTree, kotlinDebugTree))
    executionData.from(
            fileTree(project.buildDir) {
                //we use "debug" build type for test coverage (can be other)
                include(
                        "jacoco/testDevelopmentDebugUnitTest.exec",
                        "jacoco/testProductionDebugUnitTest.exec",
                        "outputs/code_coverage/debugAndroidTest/connected/*coverage.ec")
            }
    )
}

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
    }
}