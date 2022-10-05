package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * APIStatsEndpointStats
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-10-02T23:08:56.616Z[GMT]")


public class APIStatsEndpointStats   {
  @JsonProperty("URL")
  private String URL = null;

  @JsonProperty("operation")
  private String operation = null;

  @JsonProperty("mean")
  private Integer mean = null;

  @JsonProperty("max")
  private Integer max = null;

  public APIStatsEndpointStats URL(String URL) {
    this.URL = URL;
    return this;
  }

  /**
   * Get URL
   * @return URL
   **/
  @Schema(example = "/resorts", description = "")
  
    public String getURL() {
    return URL;
  }

  public void setURL(String URL) {
    this.URL = URL;
  }

  public APIStatsEndpointStats operation(String operation) {
    this.operation = operation;
    return this;
  }

  /**
   * Get operation
   * @return operation
   **/
  @Schema(example = "GET", description = "")
  
    public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public APIStatsEndpointStats mean(Integer mean) {
    this.mean = mean;
    return this;
  }

  /**
   * Get mean
   * @return mean
   **/
  @Schema(example = "11", description = "")
  
    public Integer getMean() {
    return mean;
  }

  public void setMean(Integer mean) {
    this.mean = mean;
  }

  public APIStatsEndpointStats max(Integer max) {
    this.max = max;
    return this;
  }

  /**
   * Get max
   * @return max
   **/
  @Schema(example = "198", description = "")
  
    public Integer getMax() {
    return max;
  }

  public void setMax(Integer max) {
    this.max = max;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    APIStatsEndpointStats apIStatsEndpointStats = (APIStatsEndpointStats) o;
    return Objects.equals(this.URL, apIStatsEndpointStats.URL) &&
        Objects.equals(this.operation, apIStatsEndpointStats.operation) &&
        Objects.equals(this.mean, apIStatsEndpointStats.mean) &&
        Objects.equals(this.max, apIStatsEndpointStats.max);
  }

  @Override
  public int hashCode() {
    return Objects.hash(URL, operation, mean, max);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class APIStatsEndpointStats {\n");
    
    sb.append("    URL: ").append(toIndentedString(URL)).append("\n");
    sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
    sb.append("    mean: ").append(toIndentedString(mean)).append("\n");
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
