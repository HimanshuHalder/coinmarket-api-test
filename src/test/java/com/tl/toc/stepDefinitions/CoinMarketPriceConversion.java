package com.tl.toc.stepDefinitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.tl.toc.utils.CommonUtil;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;

public class CoinMarketPriceConversion {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;
    private String requestUrl;
    private Scenario scenario = null;
    private String baseUrl;
    private Properties props = null;
    private String propertiesFileName;
    private String apiKey;
    private int amount, currencyCode1, currencyCode2, currencyCode3, price;
    private String convertedAmount;

    /**
     * Before method which will load all pre-requisite to run tests
     *
     * @param scenario
     */
    @Before
    public void initialization(Scenario scenario) {
        this.scenario = scenario;
        String profile = System.getProperty("profile") != null ? System.getProperty("profile") : "e1";
        propertiesFileName = "test-config-" + profile + ".properties";
        if (props == null) {
            props = new CommonUtil().readPropertiesFile(propertiesFileName);
        }
        baseUrl = props.getProperty(profile + ".url");
        apiKey = System.getProperty("apikey") != null ? System.getProperty("apikey"):props.getProperty(profile + ".apikey");
    }

    @Given("I have price conversion api {string}")
    public void i_have_price_conversion_api(String apiUrl) {
        RestAssured.baseURI = baseUrl;
        requestUrl = apiUrl;
        request = RestAssured.given().relaxedHTTPSValidation();
    }

    @When("I call the api with the {int} to convert Guatemalan Quetzal {int} to British pounds {int}")
    public void i_call_the_api_with_the_amount_to_convert_guatemalan_quetzal_to_british_pounds(Integer amount, Integer currencyCode1, Integer currencyCode2) {
        try {
            this.amount = amount;
            this.currencyCode1 = currencyCode1;
            this.currencyCode2 = currencyCode2;
            scenario.log(requestUrl);
            response = request.param("amount", amount)
                    .param("id", currencyCode1)
                    .param("convert_id", currencyCode2)
                    .contentType("applicaiton/json")
                    .accept("*/*")
                    .header("X-CMC_PRO_API_KEY", apiKey)
                    .get(requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("I should receive equivalent price in response with {int}")
    public void i_should_receive_equivalent_price_in_response(Integer statusCode) {
        scenario.log(response.statusLine());
        response.then()
                .assertThat()
                .statusCode(statusCode);
        JsonNode responseBody = new CommonUtil().getJsonBody(response.asPrettyString());
        assertThat("Validating id", responseBody.get("data").get("id").asInt() == currencyCode1);
        assertThat("Validating name", responseBody.get("data").get("amount").asText().equals(String.valueOf(this.amount)));
        convertedAmount = responseBody.get("data").get("quote").get(String.valueOf(currencyCode2)).get("price").asText();
        scenario.log("Converted amount ====> " + convertedAmount);
    }

    @Then("I call the api again to convert the British Pound {int} to crypto coin DOGE {int}")
    public void i_call_the_api_again_to_convert_the_british_pound_to_crypto_coin_doge(Integer currencyCode2, Integer currencyCode3) {
        try {
            Thread.sleep(1000);
            this.currencyCode3 = currencyCode3;
            request = RestAssured.given().relaxedHTTPSValidation();
            this.currencyCode3 = currencyCode3;
            scenario.log(requestUrl);
            response = request.param("amount", convertedAmount)
                    .param("id", currencyCode2)
                    .param("convert_id", currencyCode3)
                    .contentType("applicaiton/json")
                    .accept("*/*")
                    .header("X-CMC_PRO_API_KEY", apiKey)
                    .get(requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("I should receive equivalent price of doge in response with {int}")
    public void i_should_receive_equivalent_price_of_doge_in_response(Integer statusCode) {
        scenario.log(response.statusLine());
        response.then()
                .assertThat()
                .statusCode(statusCode);
        JsonNode responseBody = new CommonUtil().getJsonBody(response.asPrettyString());
        assertThat("Validating id", responseBody.get("data").get("id").asInt() == currencyCode2);
        BigDecimal de = new BigDecimal(responseBody.get("data").get("quote").get(String.valueOf(currencyCode3)).get("price").asDouble());
        de.round(new MathContext(6, RoundingMode.HALF_UP));
        scenario.log("Converted amount ====> " + de);
    }

    @After
    public void after(){
        //TODO: Implement if any data teardown or database connection tear down
        // or any other action required post execution of this test
    }
}



