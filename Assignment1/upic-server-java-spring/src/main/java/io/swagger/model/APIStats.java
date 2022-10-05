package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.APIStatsEndpointStats;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * APIStats
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-10-02T23:08:56.616Z[GMT]")


public class APIStats   {
  @JsonProperty("endpointStats")
  @Valid
  private List<APIStatsEndpointStats> endpointStats = null;

  public APIStats endpointStats(List<APIStatsEndpointStats> endpointStats) {
    this.endpointStats = endpointStats;
    return this;
  }

  public APIStats addEndpointStatsItem(APIStatsEndpointStats endpointStatsItem) {
    if (this.endpointStats == null) {
      this.endpointStats = new ArrayList<APIStatsEndpointStats>();
    }
    this.endpointStats.add(endpointStatsItem);
    return this;
  }

  /**
   * Get endpointStats
   * @return endpointStats
   **/
  @Schema(description = "")
      @Valid
    public List<APIStatsEndpointStats> getEndpointStats() {
    return endpointStats;
  }

  public void setEndpointStats(List<APIStatsEndpointStats> endpointStats) {
    this.endpointStats = endpointStats;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    APIStats apIStats = (APIStats) o;
    return Objects.equals(this.endpointStats, apIStats.endpointStats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endpointStats);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class APIStats {\n");
    
    sb.append("    endpointStats: ").append(toIndentedString(endpointStats)).append("\n");
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
