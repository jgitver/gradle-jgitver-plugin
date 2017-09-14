package fr.brouillard.oss.gradle.plugins;

import java.util.Arrays;
import java.util.Locale;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import fr.brouillard.oss.jgitver.GitVersionCalculator;
import fr.brouillard.oss.jgitver.metadata.Metadatas;

public class JGitverPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getExtensions().add("jgitver", JGitverPluginExtension.class);
        project.getTasks().create("version", JGitverVersionTask.class);

        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project evaluatedProject) {
                JGitverPluginExtension jgitverConfiguration = project.getExtensions()
                        .findByType(JGitverPluginExtension.class);

                if (jgitverConfiguration == null) {
                    // use defaults
                    jgitverConfiguration = new JGitverPluginExtension();
                }

                GitVersionCalculator versionCalculator = GitVersionCalculator.location(project.getRootDir())
                        .setMavenLike(jgitverConfiguration.mavenLike)
                        .setAutoIncrementPatch(jgitverConfiguration.autoIncrementPatch)
                        .setUseDistance(jgitverConfiguration.useDistance)
                        .setUseGitCommitId(jgitverConfiguration.useGitCommitID)
                        .setGitCommitIdLength(jgitverConfiguration.gitCommitIDLength)
                        .setNonQualifierBranches(jgitverConfiguration.nonQualifierBranches);

                String gitCalculatedVersion = versionCalculator.getVersion();
                project.setVersion(gitCalculatedVersion);
                project.getAllprojects().forEach(subproject -> subproject.setVersion(gitCalculatedVersion));

                Arrays.asList(Metadatas.values()).forEach(metadata -> {
                    versionCalculator.meta(metadata).ifPresent(metadataValue -> {
                        project.getExtensions().getExtraProperties().set(metadata.name().toLowerCase(Locale.ENGLISH), metadataValue);
                        project.getAllprojects().forEach(
                                subproject -> subproject.getExtensions().getExtraProperties().set(metadata.name().toLowerCase(Locale.ENGLISH), metadataValue));
                    });
                });
            }
        });
    }
}
