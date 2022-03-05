# maven-dependency-reporter-plugin
<img src="https://app.travis-ci.com/kgcorner/maven-dependency-reporter-plugin.svg?branch=master" /> 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.kgcorner/dependency-reporter-maven-plugin/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.kgcorner/dependency-reporter-maven-plugin)

Maven dependency Reporter plugin creates dependency map of all modules of given maven project

It exports dependency data in three formats
* CSV
* JSON
* HTML

## How To Use

In your pom.xml add this plugin as given below
```
<plugin>
        <groupId>com.kgcorner</groupId>
        <artifactId>maven-dependency-reporter-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <configuration>
            <format>html</format>
        </configuration>
    </plugin>
```

Allowed format values are html | csv | json

Run below command to generate report
```
    mvn dependency-reporter:report
```
The report will be generated inside directory named `dependency-report` inside target folder.

## Visual Dependency Report

To get visual dependency report use html. Below is one such sample report

![Maven dependency Reporter plugin](https://i.pinimg.com/564x/74/53/a0/7453a080c12c47e03714845dff85e585.jpg)

Each node represents a module in your project. Use search box to filter project using a particular dependency.


## Use case
Few days ago we were introduced with a 0 day vulnerability of log4j. Imagine your project have 100s of submodules, 
nested inside multiple module. In such cases it is very tedious and time taking job to check dependency of each module.
Here maven dependency reporter plugin can help you. 

Just add the plugin on master module and run. It will create a directory named `dependency-report` and will put the report there.
Using search box, you can easily see modules that are using log4j(for example). 