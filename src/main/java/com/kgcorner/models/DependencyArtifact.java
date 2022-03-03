package com.kgcorner.models;


import java.util.List;
import java.util.Objects;

/**
 * Description : <Write class Description>
 * Author: kumar
 * Created on : 03/03/22
 */

public class DependencyArtifact {
    private String project;
    private String projectVersion;
    private String dependency;
    private String dependencyVersion;

    private List<DependencyDetails> dependencies;


    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public String getDependencyVersion() {
        return dependencyVersion;
    }

    public void setDependencyVersion(String dependencyVersion) {
        this.dependencyVersion = dependencyVersion;
    }

    public List<DependencyDetails> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<DependencyDetails> dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DependencyArtifact artifact = (DependencyArtifact) o;
        return project.equals(artifact.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project);
    }
}