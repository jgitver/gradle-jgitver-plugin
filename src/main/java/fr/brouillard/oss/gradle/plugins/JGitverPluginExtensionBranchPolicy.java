package fr.brouillard.oss.gradle.plugins;

import java.util.ArrayList;
import java.util.List;

public class JGitverPluginExtensionBranchPolicy {
    public String pattern;
    public List<String> transformations = new ArrayList<>();

    public void pattern(String pattern) {
        this.pattern = pattern;
    }
    public void transformations(List<String> transformations) {
        if (transformations != null) {
            this.transformations = new ArrayList<>(transformations);
        }
    }
}
