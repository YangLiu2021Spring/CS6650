package io.swagger.api;

import io.swagger.model.LiftRide;
import io.swagger.model.SkierVertical;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.util.RequestUtils;
import io.swagger.util.ResponseEntityUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-10-02T23:08:56.616Z[GMT]")
@RestController
public class SkiersApiController implements SkiersApi {
    private static final Logger log = LoggerFactory.getLogger(SkiersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public SkiersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Integer> getSkierDayVertical(@Parameter(in = ParameterIn.PATH, description = "ID of the resort the skier is at", required=true, schema=@Schema()) @PathVariable("resortID") Integer resortID,@Parameter(in = ParameterIn.PATH, description = "ID of the ski season", required=true, schema=@Schema()) @PathVariable("seasonID") String seasonID,@DecimalMin("1") @DecimalMax("366") @Parameter(in = ParameterIn.PATH, description = "ID number of ski day in the ski season", required=true, schema=@Schema()) @PathVariable("dayID") String dayID,@Parameter(in = ParameterIn.PATH, description = "ID of the skier riding the lift", required=true, schema=@Schema()) @PathVariable("skierID") Integer skierID) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                //return new ResponseEntity<Integer>(objectMapper.readValue("34507", Integer.class), HttpStatus.NOT_IMPLEMENTED);
                return new ResponseEntity<Integer>(objectMapper.readValue("12345", Integer.class), HttpStatus.OK);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Integer>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<SkierVertical> getSkierResortTotals(@Parameter(in = ParameterIn.PATH, description = "ID the skier to retrieve data for", required=true, schema=@Schema()) @PathVariable("skierID") Integer skierID, @NotNull @Parameter(in = ParameterIn.QUERY, description = "resort to filter by" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "resort", required = true) List<String> resort, @Parameter(in = ParameterIn.QUERY, description = "season to filter by, optional" ,schema=@Schema()) @Valid @RequestParam(value = "season", required = false) List<String> season) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<SkierVertical>(objectMapper.readValue("{\n  \"resorts\" : [ {\n    \"seasonID\" : \"seasonID\",\n    \"totalVert\" : 0\n  }, {\n    \"seasonID\" : \"seasonID\",\n    \"totalVert\" : 0\n  } ]\n}", SkierVertical.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<SkierVertical>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<SkierVertical>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity writeNewLiftRide(
        @Parameter(in = ParameterIn.PATH, description = "ID of the resort the skier is at", required=true, schema=@Schema()) @PathVariable("resortID") Integer resortID,
        @Parameter(in = ParameterIn.PATH, description = "ID of the ski season", required=true, schema=@Schema()) @PathVariable("seasonID") String seasonID,
        @Parameter(in = ParameterIn.PATH, description = "ID number of ski day in the ski season", required=true, schema=@Schema()) @PathVariable("dayID") String dayID,
        @Parameter(in = ParameterIn.PATH, description = "ID of the skier riding the lift", required=true, schema=@Schema()) @PathVariable("skierID") Integer skierID,
        @Parameter(in = ParameterIn.DEFAULT, description = "Specify new Season value", required=true, schema=@Schema()) @Valid @RequestBody LiftRide liftRide
    ) {
        // returns bad request if the request does not accept media type JSON
        String accept = RequestUtils.getHeaderAccept(request);
        if (!RequestUtils.isAbleToAcceptMediaTypeJson(accept)) {
            return ResponseEntityUtils.buildBadRequestForInvalidMediaTypeJson(accept);
        }

        // returns not found request if the value is not between 1 and 10
        if (resortID == null || resortID.intValue() < 1 || resortID.intValue() > 10) {
            return ResponseEntityUtils.buildNotFoundRequest(String.format(
                "Expect resortID is between <1> and <10>, but it was <%s>.", resortID));
        }

        // returns not found request if the value is not 2022
        if (!"2022".equals(seasonID)) {
            return ResponseEntityUtils.buildNotFoundRequest(String.format(
                "Expect seasonID is <2022>, but it was <%s>.", seasonID));
        }

        // returns not found request if the value is not 1
        if (!"1".equals(dayID)) {
            return ResponseEntityUtils.buildNotFoundRequest(String.format(
                "Expect dayID is <1>, but it was <%s>.", dayID));
        }

        // returns not found request if the value is not between 1 and 100000
        if (skierID == null || skierID.intValue() < 1 || skierID.intValue() > 100000) {
            return ResponseEntityUtils.buildNotFoundRequest(String.format(
                "Expect skierID is between <1> and <100000>, but it was <%s>.", skierID));
        }

        // returns bad request if liftRide is null
        if (liftRide == null) {
            return ResponseEntityUtils.buildBadRequest("Expect liftRide is not null, but it was null.");
        }

        // returns not found request if the value is not between 1 and 40
        if (liftRide.getLiftID() == null
            || liftRide.getLiftID().intValue() < 1
            || liftRide.getLiftID().intValue() > 40
        ) {
            return ResponseEntityUtils.buildNotFoundRequest(String.format(
                "Expect liftID is between <1> and <40>, but it was <%s>.", liftRide.getLiftID()));
        }

        // returns bad request if the value is not between 1 and 360
        if (liftRide.getTime() == null
            || liftRide.getTime().intValue() < 1
            || liftRide.getTime().intValue() > 360
        ) {
            return ResponseEntityUtils.buildBadRequest(String.format(
                "Expect time is between <1> and <360>, but it was <%s>.", liftRide.getTime()));
        }

        // otheriwse, returns CREATED
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
