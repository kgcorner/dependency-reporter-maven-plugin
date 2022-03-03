package com.kgcorner.models;


/**
 * Description : <Write class Description>
 * Author: kumar
 * Created on : 03/03/22
 */

public class DependencyDetails {
    private String dependency;
    private String dependencyVersion;

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
}