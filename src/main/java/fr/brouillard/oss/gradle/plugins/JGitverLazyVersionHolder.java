package fr.brouillard.oss.gradle.plugins;

import fr.brouillard.oss.jgitver.BranchingPolicy;
import fr.brouillard.oss.jgitver.GitVersionCalculator;
import fr.brouillard.oss.jgitver.Strategies;
import fr.brouillard.oss.jgitver.metadata.MetadataProvider;
import org.gradle.api.Project;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class JGitverLazyVersionHolder {
    private final Project project;
    private final JGitverPluginExtension jgitverConfiguration;
    private final Consumer<GitVersionCalculator> versionConsumer;
    private GitVersionCalculator versionCalculator;
    
    public JGitverLazyVersionHolder(
            Project project, 
            JGitverPluginExtension jgitverExtension,
            Consumer<GitVersionCalculator> metadataConsumer
    ) {
        this.project = Objects.requireNonNull(project);
        this.jgitverConfiguration = Objects.requireNonNull(jgitverExtension);
        this.versionConsumer = Objects.requireNonNull(metadataConsumer);
    }
    
    public String toString() {
        computeVersion();
        
        return versionCalculator.getVersion();
    }

    public synchronized void computeVersion() {
        if (versionCalculator == null) {
            Optional<String> oldDistanceKindSystemProperty = Optional.ofNullable(jgitverConfiguration.distanceCalculatorKind)
                    .map(kind -> System.setProperty("jgitver.calculator.kind", kind.name()));

            try {
                versionCalculator = GitVersionCalculator.location(project.getRootDir());

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
                            .map(JGitverLazyVersionHolder::toBranchingPolicy)
                            .collect(Collectors.toList());
                    versionCalculator.setQualifierBranchingPolicies(branchingPolicies);
                }

                if (jgitverConfiguration.regexVersionTag != null) {
                    versionCalculator = versionCalculator.setFindTagVersionPattern(jgitverConfiguration.regexVersionTag);
                }

                versionCalculator.getVersion(true);
                versionConsumer.accept(versionCalculator);
            } finally {
                oldDistanceKindSystemProperty.ifPresent(oldVAlue -> System.setProperty("jgitver.calculator.kind", oldVAlue));
            }
        }
    }

    private static BranchingPolicy toBranchingPolicy(JGitverPluginExtensionBranchPolicy gradleBranchPolicy) {
        return new BranchingPolicy(gradleBranchPolicy.pattern, gradleBranchPolicy.transformations);
    }
}
