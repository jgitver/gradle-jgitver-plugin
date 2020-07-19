package fr.brouillard.oss.gradle.plugins;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.brouillard.oss.jgitver.BranchingPolicy;
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

        Optional<String> oldDistanceKindSystemProperty = Optional.ofNullable(jgitverConfiguration.distanceCalculatorKind)
                .map(kind -> System.setProperty("jgitver.calculator.kind", kind.name()));

        try {
            GitVersionCalculator versionCalculator = GitVersionCalculator.location(project.getRootDir());

            if (Boolean.TRUE.equals(jgitverConfiguration.mavenLike)) {
                project.getLogger().info(
                        "usage of deprecated 'mavenLike' parameter takes precedence over 'strategy: {}'",
                        jgitverConfiguration.strategy
                );
                versionCalculator.setStrategy(Strategies.MAVEN);
            } else {
                versionCalculator.setStrategy(jgitverConfiguration.strategy);
            }

            if (jgitverConfiguration.policy != null) {
                versionCalculator.setLookupPolicy(jgitverConfiguration.policy);
            }

            versionCalculator
                    .setMaxDepth(jgitverConfiguration.maxDepth)
                    .setAutoIncrementPatch(jgitverConfiguration.autoIncrementPatch)
                    .setUseDistance(jgitverConfiguration.useDistance)
                    .setUseDirty(jgitverConfiguration.useDirty)
                    .setUseGitCommitTimestamp(jgitverConfiguration.useGitCommitTimestamp)
                    .setUseGitCommitId(jgitverConfiguration.useGitCommitID)
                    .setGitCommitIdLength(jgitverConfiguration.gitCommitIDLength)
                    .setVersionPattern(jgitverConfiguration.versionPattern)
                    .setTagVersionPattern(jgitverConfiguration.tagVersionPattern)
                    .setNonQualifierBranches(jgitverConfiguration.nonQualifierBranches);

            if (!jgitverConfiguration.policies.isEmpty()) {
                List<BranchingPolicy> branchingPolicies = jgitverConfiguration.policies.stream()
                        .map(JGitverPlugin::toBranchingPolicy)
                        .collect(Collectors.toList());
                versionCalculator.setQualifierBranchingPolicies(branchingPolicies);
            }

            if (jgitverConfiguration.regexVersionTag != null) {
                versionCalculator = versionCalculator.setFindTagVersionPattern(jgitverConfiguration.regexVersionTag);
            }

            String gitCalculatedVersion = versionCalculator.getVersion();

            boolean isDirty = versionCalculator
                    .meta(Metadatas.DIRTY)
                    .map(Boolean::parseBoolean)
                    .orElse(Boolean.FALSE);

            if (jgitverConfiguration.useDirty && jgitverConfiguration.failIfDirty && isDirty) {
                IllegalStateException cause = new IllegalStateException("jgitver detected a dirty state, project is configured to fail");
                throw new BuildException("jgitver stopped the build for a git dirty state", cause);
            }

            project.setVersion(gitCalculatedVersion);
            project.getAllprojects().forEach(subproject -> subproject.setVersion(gitCalculatedVersion));

            for (Metadatas metadata: Metadatas.values()) {
                versionCalculator.meta(metadata).ifPresent(metadataValue -> {
                    project.getExtensions().getExtraProperties().set(metadata.name().toLowerCase(Locale.ENGLISH), metadataValue);
                    project.getAllprojects().forEach(
                            subproject -> subproject.getExtensions().getExtraProperties().set(metadata.name().toLowerCase(Locale.ENGLISH), metadataValue));
                });
            };
        } finally {
            oldDistanceKindSystemProperty.ifPresent(oldVAlue -> System.setProperty("jgitver.calculator.kind", oldVAlue));
        }
    }

    private static BranchingPolicy toBranchingPolicy(JGitverPluginExtensionBranchPolicy gradleBranchPolicy) {
        return new BranchingPolicy(gradleBranchPolicy.pattern, gradleBranchPolicy.transformations);
    }
}
