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

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "format", defaultValue = "csv")
    String format;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        List<DependencyArtifact> dependencies = new ArrayList<>();
        List<MavenProject> projects = project.getCollectedProjects();
        if(!format.equalsIgnoreCase("csv") && !format.equalsIgnoreCase("json") && !format.equalsIgnoreCase("d3"))
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
                for (Dependency dependency : dependencyList) {
                    DependencyDetails details = new DependencyDetails();
                    details.setDependency(dependency.getArtifactId());
                    details.setDependencyVersion(dependency.getVersion());
                    dep.getDependencies().add(details);
                }
                dependencies.add(dep);
            }
        }

        String outputDirectory = project.getBuild().getDirectory();
        outputDirectory = outputDirectory + File.separator + OUTPUT_DIR;
        File file = new File(outputDirectory);
        if(!file.exists()) {
            file.mkdirs();
        }
        String outputFile = "";
        if(format.equalsIgnoreCase("csv"))
            outputFile = outputDirectory + File.separator + CSV_FILE;
        if(format.equalsIgnoreCase("json"))
            outputFile = outputDirectory + File.separator + JSON_FILE;
        if(format.equalsIgnoreCase("d3"))
            outputFile = outputDirectory + File.separator + D3_FILE;
        try {
            Utilities.write(dependencies, outputFile, format);
        } catch (IOException e) {
            getLog().error(e);
        }
    }
}