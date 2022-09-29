
package com.ots.test.ui.common.utils.seleniumide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "version",
    "name",
    "url",
    "tests",
    "suites",
    "urls",
    "plugins"
})
public class SeleniumIDE {

    @JsonProperty("id")
    private String id;
    @JsonProperty("version")
    private String version;
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;
    @JsonProperty("tests")
    private List<Test> tests = null;
    @JsonProperty("suites")
    private List<Suite> suites = null;
    @JsonProperty("urls")
    private List<String> urls = null;
    @JsonProperty("plugins")
    private List<Object> plugins = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("tests")
    public List<Test> getTests() {
        return tests;
    }

    @JsonProperty("tests")
    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    @JsonProperty("suites")
    public List<Suite> getSuites() {
        return suites;
    }

    @JsonProperty("suites")
    public void setSuites(List<Suite> suites) {
        this.suites = suites;
    }

    @JsonProperty("urls")
    public List<String> getUrls() {
        return urls;
    }

    @JsonProperty("urls")
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @JsonProperty("plugins")
    public List<Object> getPlugins() {
        return plugins;
    }

    @JsonProperty("plugins")
    public void setPlugins(List<Object> plugins) {
        this.plugins = plugins;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("version", version).append("name", name).append("url", url).append("tests", tests).append("suites", suites).append("urls", urls).append("plugins", plugins).append("additionalProperties", additionalProperties).toString();
    }

}
