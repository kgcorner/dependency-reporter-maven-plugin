package com.kgcorner.models;


/**
 * Description : Separate model for details of dependency
 * Author: kumar
 * Created on : 03/03/22
 */

public class DependencyDetails {
    private String dependency;
    private String dependencyVersion;
    private String dependencyGroupId;

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

    public String getDependencyGroupId() {
        return dependencyGroupId;
    }

    public void setDependencyGroupId(String dependencyGroupId) {
        this.dependencyGroupId = dependencyGroupId;
    }
}