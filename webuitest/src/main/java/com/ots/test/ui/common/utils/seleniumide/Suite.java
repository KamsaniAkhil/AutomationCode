
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
    "name",
    "persistSession",
    "parallel",
    "timeout",
    "tests"
})
public class Suite {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("persistSession")
    private Boolean persistSession;
    @JsonProperty("parallel")
    private Boolean parallel;
    @JsonProperty("timeout")
    private Integer timeout;
    @JsonProperty("tests")
    private List<Object> tests = null;
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

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("persistSession")
    public Boolean getPersistSession() {
        return persistSession;
    }

    @JsonProperty("persistSession")
    public void setPersistSession(Boolean persistSession) {
        this.persistSession = persistSession;
    }

    @JsonProperty("parallel")
    public Boolean getParallel() {
        return parallel;
    }

    @JsonProperty("parallel")
    public void setParallel(Boolean parallel) {
        this.parallel = parallel;
    }

    @JsonProperty("timeout")
    public Integer getTimeout() {
        return timeout;
    }

    @JsonProperty("timeout")
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @JsonProperty("tests")
    public List<Object> getTests() {
        return tests;
    }

    @JsonProperty("tests")
    public void setTests(List<Object> tests) {
        this.tests = tests;
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
        return new ToStringBuilder(this).append("id", id).append("name", name).append("persistSession", persistSession).append("parallel", parallel).append("timeout", timeout).append("tests", tests).append("additionalProperties", additionalProperties).toString();
    }

}
