package fr.brouillard.oss.gradle.plugins;

public class JGitverPluginExtension {
    public Boolean mavenLike = Boolean.FALSE;
    public Boolean autoIncrementPatch = Boolean.TRUE;
    public Boolean useDistance = Boolean.TRUE;
    public Boolean useGitCommitID = Boolean.FALSE;
    public int gitCommitIDLength = 8;
    public String nonQualifierBranches = "master";
    
    public void mavenLike(boolean mavenLike) {
        this.mavenLike = mavenLike;
    }

    public void autoIncrementPatch(boolean autoIncrementPatch) {
        this.autoIncrementPatch = autoIncrementPatch;
    }

    public void useDistance(boolean useDistance) {
        this.useDistance = useDistance;
    }

    public void useGitCommitID(boolean useGitCommitID) {
        this.useGitCommitID = useGitCommitID;
    }

    public void gitCommitIDLength(int gitCommitIDLength) {
        this.gitCommitIDLength = gitCommitIDLength;
    }

    public void nonQualifierBranches(String nonQualifierBranches) {
        this.nonQualifierBranches = nonQualifierBranches;
    }
}
