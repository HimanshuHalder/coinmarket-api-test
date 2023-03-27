# coinmarket-api-test (Cucumber Rest Assured API tests Framework)

## Overview

This uses `Cucumber Rest Assured Framework` to validate functionality of [conimarketcap api's](https://coinmarketcap.com/api/documentation/v1/#section/Introduction).

##Software Pre-requisite
- JDK 8 recommended or above(Version supports lambda, annotation etc.)
- Maven - apache-maven-3.8.1
- IntelliJ idea latest version recommended or any idea supports maven, cucumber-java, gherkins.

After installation of software make sure all system path has been set correctly.
- to check java  - open terminal and check this command returning the version `java --version`
- to check maven  - open terminal and check this command returning the version `mvn -v`
- check all cucumber-java, gherkins plugins are installed(please check compatible version with your editor)

## Installation
Once you have all above prerequisite, clone the project in your preferred location. Then import the project in your editor as maven project and run below command.

`mvn clean install -DskipTests`

## Command line
`Pre-requisite:` Get apikey after registering your basic account with coinmarketcap to run these tests. 
1. register for a free account - https://coinmarketcap.com/api/documentation/v1/#section/Introduction
2. Get apikey and keep it secured.

The `apikey` can be set in `test-config-e1.properties` file or can be passed via arguments from commandline.

💡 Commandline argument will take precedence over `.properties` file

The validation can be triggered by executing the following command in the target project's main directory:
```
mvn clean test -Dcucumber.options="--tag=@currencyConversion" -Dprofile=e1 -Dapikey=<your account apikey>
```

`-Dprofile: ` is to set the environment variable which will help to load required config to execute tests in that environment. It is defaulted to `e1` if that is not passed.

`-Dapikey: ` is required to access coinmarketcap apis. if it's being set in the `test-config-e1.properties`, command line argument is not required.

`-Dcucumber.options` this is being used to run specific tests. there are few available tags that will allow you to run specific set of test with required test data.
Available tags are ``@currencyConversion, @functional and @get``

## Execution using Editor
Run `CucumberTests` file as JUnit. It will trigger test case execution based on tags mentioned in that class. If you want to run against different environment and different tags without changing anything in CucumberTests class, then just pass `-Dcucumber.options="@allTests" -Dprofile=e1` as jvm argument and modify these value as needed.

## Reporting
Execution reports will be avaiable in `target/reports` folder as `cucumber-html-report.html` which you can open in any browser(chrome recommended).
This report will hold all the record that you need to analyze in case you need to. It will provide in details execution report.

### Contribution Strategy

1. Create a fork of the project into your own repository.
2. Make all your necessary changes
3. create a pull request with a description on what was added or removed and details explaining the changes in lines of code.
4. If approved, project owners will merge it.

### Licensing

MIT


