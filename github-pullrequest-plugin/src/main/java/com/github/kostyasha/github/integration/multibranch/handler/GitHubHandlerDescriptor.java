package com.github.kostyasha.github.integration.multibranch.handler;

import hudson.DescriptorExtensionList;
import hudson.ExtensionPoint;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;

/**
 * @author Kanstantsin Shautsou
 */
public abstract class GitHubHandlerDescriptor extends Descriptor<GitHubHandler> implements ExtensionPoint {
    public static DescriptorExtensionList<GitHubHandler, GitHubHandlerDescriptor> getAllGitHubHandlerDescriptors() {
        return Jenkins.getInstance().getDescriptorList(GitHubHandler.class);
    }
}
