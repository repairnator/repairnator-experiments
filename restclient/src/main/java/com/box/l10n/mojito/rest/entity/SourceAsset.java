package com.box.l10n.mojito.rest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is an exact copy of {@link com.box.l10n.mojito.rest.entity.SourceAsset}
 * This should be updated if it either one changes.
 *
 * @author wyau
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SourceAsset {

    private Long repositoryId;
    private String path;
    private String content;
    private Long addedAssetId;
    private PollableTask pollableTask;
    private FilterConfigIdOverride filterConfigIdOverride;


    public Long getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAddedAssetId() {
        return addedAssetId;
    }

    public void setAddedAssetId(Long addedAssetId) {
        this.addedAssetId = addedAssetId;
    }

    public FilterConfigIdOverride getFilterConfigIdOverride() {
        return filterConfigIdOverride;
    }

    public void setFilterConfigIdOverride(FilterConfigIdOverride filterConfigIdOverride) {
        this.filterConfigIdOverride = filterConfigIdOverride;
    }

    @JsonProperty
    public PollableTask getPollableTask() {
        return pollableTask;
    }

    /**
     * @JsonIgnore because this pollableTask is read only data generated by the
     * server side, it is not aimed to by external process via WS
     *
     * @param pollableTask
     */
    @JsonIgnore
    public void setPollableTask(PollableTask pollableTask) {
        this.pollableTask = pollableTask;
    }
}
