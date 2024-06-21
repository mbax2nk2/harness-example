import com.pswidersk.gradle.python.PythonPluginExtension
import com.pswidersk.gradle.python.VenvTask
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * To learn more about Gradle by exploring our Samples at https://docs.gradle.org/8.8/samples
 */
group = "kz.nursultan.lambda"

plugins {
    id("com.jfrog.artifactory") version "5.2.2"
    id("com.pswidersk.python-plugin") version "2.7.1"
    id("nebula.release") version "19.0.9"
    `maven-publish`
}

configure<ArtifactoryPluginConvention> {
    publish {
        // Define the Artifactory URL for publishing artifacts
        contextUrl = "https://mbax2nk2.jfrog.io/artifactory"
        // Define the project repository to which the artifacts will be published
        repository {
            // Set the Artifactory repository key
            repoKey = "lambda-generic-local"
            // Specify the publisher username
            username = project.property("artifactory_user") as String
            // Provide the publisher password
            password = project.property("artifactory_password") as String
        }
        defaults {
            publications("ALL_PUBLICATIONS")
        }
    }
}
configure<PythonPluginExtension> {
    pythonVersion = "3.9"
}

tasks {
    register("projectVersion") {
        println(project.version)
    }
    register<Zip>("packageZip") {
        description = "Create zip file"
        group = "publishing"

        archiveFileName = "${project.version}.zip"
        destinationDirectory = layout.buildDirectory.dir("libs")

        from(layout.buildDirectory.dir("dist")) {
            exclude("requirements.txt")
            exclude("**/__pycache__")
            exclude("**/__init__.py")
        }

        dependsOn("build")
    }

    register("build") {
        dependsOn("copySrcFiles", "pipInstallSrc")

        description = "Build application"
        group = JavaBasePlugin.BUILD_TASK_NAME
    }

    register<Copy>("copySrcFiles") {
        description = "Copy source files into build directory"
        from(layout.projectDirectory.dir("src").dir("main"))
        into(layout.buildDirectory.dir("dist"))
        doFirst {
            delete(layout.buildDirectory.dir("dist").get().asFile)
        }
    }

    register<VenvTask>("pipInstallSrc") {
        description = "Install dependencies for source file"
        workingDir = layout.buildDirectory.dir("dist")
        venvExec = "pip"
        args = listOf("install", "--isolated", "-r", "requirements.txt", "-t", ".")
    }

    register<VenvTask>("pipInstallTest") {
        description = "Install dependencies for tests"
        workingDir = layout.projectDirectory.dir("src").dir("test")
        venvExec = "pip"
        args = listOf("install", "--isolated", "-r", "requirements.txt")

        val task = findByName("copyGitHooks")
        if (task != null) {
            dependsOn("copyGitHooks")
        }
    }
    register<VenvTask>("test") {
        description = "Test application"
        group = JavaBasePlugin.VERIFICATION_GROUP

        venvExec = "coverage run"
        workingDir = layout.projectDirectory.dir("src").dir("test")
        args = listOf("--source=../main -m pytest --junitxml=${layout.buildDirectory.file("reports/test/test_report.xml").get().asFile}")
        dependsOn("pipInstallTest")
    }

    register<VenvTask>("coverageTestReport") {
        description = "Coverage test report"
        group = JavaBasePlugin.VERIFICATION_GROUP

        venvExec = "coverage xml -o ${layout.buildDirectory.file("reports/test/coverage.xml").get().asFile}"
        workingDir = layout.projectDirectory.dir("src").dir("test")
        dependsOn("test")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenZip") {
            val packageZip by tasks.named("packageZip")
            artifact(packageZip)
            version = "${project.version}-${System.getenv()["BUILD_ID"].orEmpty()}"
        }
    }
}
