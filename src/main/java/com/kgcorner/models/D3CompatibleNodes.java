package com.kgcorner.models;


import java.util.List;

/**
 * Description : <Write class Description>
 * Author: kumar
 * Created on : 03/03/22
 */

public class D3CompatibleNodes {
    private List<DependencyArtifact> nodes;
    private List<Link> links;

    public List<DependencyArtifact> getNodes() {
        return nodes;
    }

    public void setNodes(List<DependencyArtifact> nodes) {
        this.nodes = nodes;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}