package fr.brouillard.oss.gradle.plugins;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import fr.brouillard.oss.jgitver.GitVersionCalculator;

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

                String gitCalculatedVersion = GitVersionCalculator.location(project.getRootDir())
                        .setMavenLike(jgitverConfiguration.mavenLike)
                        .setAutoIncrementPatch(jgitverConfiguration.autoIncrementPatch)
                        .setUseDistance(jgitverConfiguration.useDistance)
                        .setUseGitCommitId(jgitverConfiguration.useGitCommitID)
                        .setGitCommitIdLength(jgitverConfiguration.gitCommitIDLength)
                        .setNonQualifierBranches(jgitverConfiguration.nonQualifierBranches).getVersion();

                project.setVersion(gitCalculatedVersion);
                project.getAllprojects().forEach(subproject -> subproject.setVersion(gitCalculatedVersion));
            }
        });
    }
}
