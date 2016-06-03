package fr.brouillard.oss.gradle.plugins;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class JGitverVersionTask extends DefaultTask {
    @TaskAction
    public void version() {
        System.out.println(String.format("Version: %s", getProject().getVersion()));
    }
}
