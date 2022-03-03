package com.kgcorner.mojos;


import com.kgcorner.models.DependencyArtifact;
import com.kgcorner.models.DependencyDetails;
import com.kgcorner.util.Utilities;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : <Write class Description>
 * Author: kumar
 * Created on : 03/03/22
 */
@Mojo(name = "report", defaultPhase = LifecyclePhase.COMPILE)
public class DependencyReporterMojo extends AbstractMojo {

    private static final String OUTPUT_DIR = "dependency-report";
    private static final String CSV_FILE = "report.csv";
    private static final String JSON_FILE = "report.json";
    private static final String D3_FILE = "report.html";
    public static final String CSV = "csv";
    public static final String JSON = "json";
    public static final String HTML = "html";

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "format", defaultValue = CSV)
    String format;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        List<DependencyArtifact> dependencies = new ArrayList<>();
        String projectName = project.getArtifact().getArtifactId();
        List<MavenProject> projects = project.getCollectedProjects();
        if(!format.equalsIgnoreCase(CSV) && !format.equalsIgnoreCase(JSON) && !format.equalsIgnoreCase(HTML))
            throw new IllegalArgumentException("Format can be either csv or json");
        if(format.equalsIgnoreCase("csv")) {
            for (MavenProject project : projects) {
                List<Dependency> dependencyList = project.getDependencies();
                for (Dependency dependency : dependencyList) {
                    DependencyArtifact dep = new DependencyArtifact();
                    dep.setProject(project.getArtifactId());
                    dep.setProjectVersion(project.getVersion());
                    dep.setDependency(dependency.getArtifactId());
                    dep.setDependencyVersion(dependency.getVersion());
                    dep.setProjectGroupId(project.getGroupId());
                    dep.setDependencyGroupId(dependency.getGroupId());
                    dependencies.add(dep);
                }
            }
        } else {
            for (MavenProject project : projects) {
                List<Dependency> dependencyList = project.getDependencies();
                DependencyArtifact dep = new DependencyArtifact();
                dep.setProject(project.getArtifactId());
                dep.setProjectVersion(project.getVersion());
                dep.setDependencies(new ArrayList<>());
                dep.setProjectGroupId(project.getGroupId());
                for (Dependency dependency : dependencyList) {
                    DependencyDetails details = new DependencyDetails();
                    details.setDependency(dependency.getArtifactId());
                    details.setDependencyVersion(dependency.getVersion());
                    details.setDependencyGroupId(dependency.getGroupId());
                    dep.getDependencies().add(details);
                }
                dependencies.add(dep);
            }

            for(DependencyArtifact artifact : dependencies) {
                artifact.setDependents(new ArrayList<>());
                for (DependencyArtifact dependentArtifacts : dependencies) {
                    for(DependencyDetails details : dependentArtifacts.getDependencies()) {
                        if(details.getDependency().equals(artifact.getProject())) {
                            DependencyDetails obj = new DependencyDetails();
                            obj.setDependency(dependentArtifacts.getProject());
                            obj.setDependencyVersion(dependentArtifacts.getProjectVersion());
                            obj.setDependencyGroupId(dependentArtifacts.getProjectGroupId());
                            artifact.getDependents().add(obj);
                            break;
                        }
                    }
                }
            }
        }

        String outputDirectory = project.getBuild().getDirectory();
        outputDirectory = outputDirectory + File.separator + OUTPUT_DIR;
        File file = new File(outputDirectory);
        if(!file.exists()) {
            file.mkdirs();
        }
        String outputFile = "";
        if(format.equalsIgnoreCase(CSV))
            outputFile = outputDirectory + File.separator + CSV_FILE;
        if(format.equalsIgnoreCase(JSON))
            outputFile = outputDirectory + File.separator + JSON_FILE;
        if(format.equalsIgnoreCase(HTML))
            outputFile = outputDirectory + File.separator + D3_FILE;
        try {
            Utilities.write(dependencies, outputFile, format, projectName);
        } catch (IOException e) {
            getLog().error(e);
        }
    }
}