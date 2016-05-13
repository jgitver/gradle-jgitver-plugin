package fr.brouillard.oss.gradle.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import fr.brouillard.oss.jgitver.GitVersionCalculator;

public class JGitverPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        String gitCalculatedVersion = GitVersionCalculator.location(project.getRootDir())
                .setMavenLike(false)
                .setAutoIncrementPatch(true)
                .setUseDistance(true)
                .getVersion();
        project.setVersion(gitCalculatedVersion);
        project.getAllprojects().forEach(subproject -> subproject.setVersion(gitCalculatedVersion));
    }
}
