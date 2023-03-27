@currencyConversion @functional @get
Feature: Coin market price conversion api

  @HappyPath
  Scenario Outline: Price conversion from fiat currency to another fiat currency
    Given I have price conversion api "/v2/tools/price-conversion"
    When I call the api with the <amount> to convert Guatemalan Quetzal <currencyId1> to British pounds <currencyId2>
    Then I should receive equivalent price in response with <statusCode>
    When I call the api again to convert the British Pound <currencyId2> to crypto coin DOGE <currencyId3>
    Then I should receive equivalent price of doge in response with <statusCode>

    Examples:
      | currencyId1 | currencyId2 | currencyId3 | amount   | statusCode |
      | 3541        | 2791        | 74          | 10000000 | 200        |

  @ErrorCheck
  Scenario Outline: Error Scenario validation
    Given I have price conversion api "/v2/tools/price-conversion"
    When I call the api with invalid "<datapoint>"
    Then I should receive <errorCode> with message

    Examples:
      | datapoint       | errorCode |
      | InvalidApiKey   | 401       |
      | InvalidArgument | 400       |
      #| PermissionIssue | 403       | Need to set this test by using a user whi does not have permission to access this