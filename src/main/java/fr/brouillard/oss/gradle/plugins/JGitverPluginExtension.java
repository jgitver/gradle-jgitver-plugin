package fr.brouillard.oss.gradle.plugins;

import fr.brouillard.oss.jgitver.LookupPolicy;
import fr.brouillard.oss.jgitver.Strategies;
import groovy.lang.Closure;
import org.gradle.api.Project;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class JGitverPluginExtension {
    public Strategies strategy = Strategies.CONFIGURABLE;
    public LookupPolicy policy = LookupPolicy.MAX;
    @Deprecated
    public Boolean mavenLike = null;
    public Boolean autoIncrementPatch = Boolean.TRUE;
    public Boolean useDistance = Boolean.TRUE;
    public Boolean useDirty = Boolean.FALSE;
    public Boolean failIfDirty = Boolean.FALSE;
    public Boolean useGitCommitTimestamp = Boolean.FALSE;
    public Boolean useGitCommitID = Boolean.FALSE;
    public int gitCommitIDLength = 8;
    public int maxDepth = Integer.MAX_VALUE;
    public String nonQualifierBranches = "master";
    public String versionPattern = null;
    public String tagVersionPattern = null;
    public String regexVersionTag = null;
    public List<JGitverPluginExtensionBranchPolicy> policies;
    private Project project;

    @Inject
    public JGitverPluginExtension(Project project) {
        this.project = project;
        this.policies = new ArrayList<>();
    }

    public void policy(LookupPolicy policy) {
        this.policy = policy;
    }

    public void policy(String policy) {
        policy(LookupPolicy.valueOf(policy));
    }

    public void strategy(Strategies strategy) {
        this.strategy = strategy;
    }

    public void strategy(String strategy) {
        strategy(Strategies.valueOf(strategy));
    }

    public void mavenLike(boolean mavenLike) {
        this.mavenLike = mavenLike;
    }

    public void autoIncrementPatch(boolean autoIncrementPatch) {
        this.autoIncrementPatch = autoIncrementPatch;
    }

    public void useDistance(boolean useDistance) {
        this.useDistance = useDistance;
    }
    
    public void useDirty(boolean useDirty) {
        this.useDirty = useDirty;
    }

    public void failIfDirty(boolean failIfDirty) {
        this.failIfDirty = failIfDirty;
    }

    public void useGitCommitTimestamp(boolean useGitCommitTimestamp) {
        this.useGitCommitTimestamp = useGitCommitTimestamp;
    }

    public void regexVersionTag(String regexVersionTag) {
        this.regexVersionTag = regexVersionTag;
    }

    public void useGitCommitID(boolean useGitCommitID) {
        this.useGitCommitID = useGitCommitID;
    }

    public void gitCommitIDLength(int gitCommitIDLength) {
        this.gitCommitIDLength = gitCommitIDLength;
    }

    public void maxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public void nonQualifierBranches(String nonQualifierBranches) {
        this.nonQualifierBranches = nonQualifierBranches;
    }

    public void versionPattern(String pattern) {
        this.versionPattern = pattern;
    }

    public void tagVersionPattern(String pattern) {
        this.tagVersionPattern = pattern;
    }

    public void policy(Closure closure) {
        JGitverPluginExtensionBranchPolicy policy = new JGitverPluginExtensionBranchPolicy();
        project.configure(policy, closure);
        this.policies.add(policy);
    }
}
