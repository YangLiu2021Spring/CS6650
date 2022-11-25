package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ResortIDSeasonsBody
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-10-02T23:08:56.616Z[GMT]")


public class ResortIDSeasonsBody   {
  @JsonProperty("year")
  private String year = null;

  public ResortIDSeasonsBody year(String year) {
    this.year = year;
    return this;
  }

  /**
   * 4 character string specifying new season start year
   * @return year
   **/
  @Schema(example = "2019", description = "4 character string specifying new season start year")
  
    public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResortIDSeasonsBody resortIDSeasonsBody = (ResortIDSeasonsBody) o;
    return Objects.equals(this.year, resortIDSeasonsBody.year);
  }

  @Override
  public int hashCode() {
    return Objects.hash(year);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResortIDSeasonsBody {\n");
    
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
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
