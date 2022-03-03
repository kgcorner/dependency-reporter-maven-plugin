package com.kgcorner.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kgcorner.models.D3CompatibleNodes;
import com.kgcorner.models.DependencyArtifact;
import com.kgcorner.models.DependencyDetails;
import com.kgcorner.models.Link;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : <Write class Description>
 * Author: kumar
 * Created on : 03/03/22
 */

public class Utilities {
    private static final String D3HTML_TEMPLATE="<!DOCTYPE html><meta charset=\"utf-8\"><style>.link{stroke:#ccc}.node text{pointer-events:none;font:10px sans-serif}</style><body><input type=\"text\" name=\"dependency\" placeholder=\"enter dependency\" id=\"filterDep\" /><input type=\"submit\" onclick=\"filter()\" /><div class=\"network\"></div><script src=\"https://d3js.org/d3.v3.min.js\"></script><script>;let data=%s;var width=960,height=500;function drawNetwork(e){var a=d3.select('.network').append('svg').attr('width',width).attr('height',height),n=d3.layout.force().gravity(0.05).distance(100).charge(-100).size([width,height]);n.nodes(e.nodes).links(e.links).start();var r=a.selectAll('.link').data(e.links).enter().append('line').attr('class','link'),t=a.selectAll('.node').data(e.nodes).enter().append('g').attr('class','node').call(n.drag);t.append('image').attr('xlink:href','https://icon-icons.com/downloadimage.php?34541&root=321/ICO/64/&file=Circle_34541.ico').attr('x',-8).attr('y',-8).attr('width',16).attr('height',16);t.append('text').attr('dx',12).attr('dy','.35em').text(function(e){return e.project});n.on('tick',function(){r.attr('x1',function(e){return e.source.x}).attr('y1',function(e){return e.source.y}).attr('x2',function(e){return e.target.x}).attr('y2',function(e){return e.target.y});t.attr('transform',function(e){return'translate('+e.x+','+e.y+')'})})};drawNetwork(data);function filter(){var t=document.getElementById('filterDep'),n=t.value,e=[];let newData={};data.nodes.forEach(node=>{node.dependencies.forEach(dep=>{if(dep.dependency.indexOf(n)>-1){e.push(node)}})});newData.nodes=e;let newLinks=[];for(let i=0;i<newData.nodes.length;i++){let node=newData.nodes[i];node.dependencies.forEach(dep=>{let index=getIndex(newData.nodes,dep.dependency);if(index>-1){let link={source:index,target:i};newLinks.push(link)}})};newData.links=newLinks;console.log(newData);d3.select('svg').remove();drawNetwork(newData)};function getIndex(e,t){for(let i=0;i<e.length;i++){if(e[i].project.indexOf(t)>-1)return i};return-1};</script></body>";
    private static void writeCsv(List<DependencyArtifact> data, String path) throws IOException {
        String pattern = "\"%s\",\"%s\",\"%s\",\"%s\"\n";
        String header = "Project, Project Version, Dependency, Dependency version\n";
        FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
        fileOutputStream.write(header.getBytes());
        for(DependencyArtifact record : data) {
            String entry = String.format(pattern, record.getProject(), record.getProjectVersion(),
                record.getDependency(), record.getDependencyVersion());
            fileOutputStream.write(entry.getBytes());
        }
        fileOutputStream.close();
    }

    public static void write(List<DependencyArtifact> dependencies, String outputFile, String format) throws IOException {
        if(format.equalsIgnoreCase("csv")) {
            writeCsv(dependencies, outputFile);
        }
        if(format.equalsIgnoreCase("json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonData = gson.toJson(dependencies);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFile));
            fileOutputStream.write(jsonData.getBytes());
        }
        if(format.equalsIgnoreCase("d3"))
            writeD3CompatibleData(dependencies, outputFile);
    }

    private static void writeD3CompatibleData(List<DependencyArtifact> dependencies, String outputFile) throws IOException {
        D3CompatibleNodes d3CompatibleNodes = new D3CompatibleNodes();
        d3CompatibleNodes.setNodes(dependencies);
        d3CompatibleNodes.setLinks(new ArrayList<>());
        List<Link> links = new ArrayList<>();
        for (int i = 0; i < dependencies.size(); i++) {
            DependencyArtifact artifact = dependencies.get(i);
            List<DependencyDetails> dependenciesList = artifact.getDependencies();
            for(DependencyDetails details : dependenciesList) {
                int index = getIndexOfProject(dependencies, details.getDependency());
                if(index != -1) {
                    Link link = new Link();
                    link.setSource(index);
                    link.setTarget(i);
                    d3CompatibleNodes.getLinks().add(link);
                }

            }
        }
        //String htmlFile = Utilities.class.getResource("/index.html").getPath();
        String htmlData = D3HTML_TEMPLATE;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonData = gson.toJson(d3CompatibleNodes);
        htmlData = String.format(htmlData, jsonData);
        FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFile));
        fileOutputStream.write(htmlData.getBytes());
    }

    private static int getIndexOfProject(List<DependencyArtifact> dependencies, String projectName) {
        for (int i = 0; i < dependencies.size(); i++) {
            if(dependencies.get(i).getProject().equals(projectName))
                return i;
        }
        return -1;
    }

    private static String readFile(String fileAddress) throws IOException {
        File file = new File(fileAddress);
        BufferedReader br
            = new BufferedReader(new FileReader(file));

        StringBuffer sb = new StringBuffer();
        String st;
        while ((st = br.readLine()) != null) {
            sb.append(st);
        }
        return sb.toString();
    }
}
