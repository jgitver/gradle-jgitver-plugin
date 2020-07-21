package fr.brouillard.oss.gradle.plugins;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import fr.brouillard.oss.jgitver.BranchingPolicy;
import fr.brouillard.oss.jgitver.metadata.MetadataProvider;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.tooling.BuildException;

import fr.brouillard.oss.jgitver.GitVersionCalculator;
import fr.brouillard.oss.jgitver.Strategies;
import fr.brouillard.oss.jgitver.metadata.Metadatas;

public class JGitverPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getExtensions().create("jgitver", JGitverPluginExtension.class, project);
        project.getTasks().create("version", JGitverVersionTask.class);

        JGitverPluginExtension jgitverConfiguration = project.getExtensions().findByType(JGitverPluginExtension.class);

        Consumer<GitVersionCalculator> versionCalculatorConsumer = versionCalculator -> {
            boolean isDirty = versionCalculator
                    .meta(Metadatas.DIRTY)
                    .map(Boolean::parseBoolean)
                    .orElse(Boolean.FALSE);

            if (jgitverConfiguration.useDirty && jgitverConfiguration.failIfDirty && isDirty) {
                IllegalStateException cause = new IllegalStateException("jgitver detected a dirty state, project is configured to fail");
                throw new BuildException("jgitver stopped the build for a git dirty state", cause);
            }           
            
            for (Metadatas metadata: Metadatas.values()) {
                versionCalculator.meta(metadata).ifPresent(metadataValue -> {
                    project.getExtensions().getExtraProperties().set(metadata.name().toLowerCase(Locale.ENGLISH), metadataValue);
                    project.getAllprojects().forEach(
                            subproject -> subproject.getExtensions().getExtraProperties().set(metadata.name().toLowerCase(Locale.ENGLISH), metadataValue));
                });
            }                
        };

        JGitverLazyVersionHolder versionHolder = new JGitverLazyVersionHolder(project, jgitverConfiguration, versionCalculatorConsumer);
        project.setVersion(versionHolder);
        project.getAllprojects().forEach(subproject -> subproject.setVersion(versionHolder));
    }
}
