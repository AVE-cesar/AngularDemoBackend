# Generate an Angular JS Web App

[![Build Status](https://travis-ci.org/jaxio/angular-lab.svg?branch=master)](https://travis-ci.org/jaxio/angular-lab)

>>
>> NOTE: This is still a work in progress, we are looking for feedbacks from Angular JS experts.
>> 

Reverse a [sample SQL schema](https://github.com/AVE-cesar/AngularDemoBackend/blob/master/src/main/sql/H2/01-create.sql) 
and generate a full S-CRUD SpringBoot/JPA/AngularJS Web Application.

S-CRUD means: **S**earch, **C**reate, **R**ead, **U**pdate, **D**elete

The code generation is done by [Celerio](http://www.jaxio.com/en/).

The generated application relies on:

* Springboot
* JPA
* Angular JS

# Requirements

* Java 8
* Maven 3.1.1
* npm
* bower

# How to run it

## Step 0: Download and install npm from https://nodejs.org/en/

    try npm in a window shell to validate its installation
    
    then install bower via: npm install -g bower

## Step 1: Retrieve frontend libraries

    bower install

Note: this command will download powerful Javascript librairies into **src/main/resources/static/bower_components**.

## Step 2: reverse the sample SQL schema and generate the source code
    
From this folder run from:

    mvn -Pdb,metadata,gen
    
Note: This command will create the sample H2 database, analyze it and then generate all sources into **src/main/generated-java** and **src/test/generated-java**.
     
    
## Step 3: Run the app

    mvn spring-boot:run

## Step 4: access the app and play

    http://localhost:8080/

Note: use the account **admin** (password: **admin**) to log in

## Extra tip: delete generated code

    mvn -PcleanGen clean

# Contribute

You may contribute and you may of course [report issues](https://github.com/AVE-cesar/AngularDemoBackend/issues) and/or submit pull requests.

See the WIKI (https://github.com/AVE-cesar/AngularDemoBackend/wiki) for more informations.
  
Travis CI Status [![Build Status](https://travis-ci.org/AVE-cesar/AngularDemoBackend.svg)](https://travis-ci.org/AVE-cesar/AngularDemoBackend)

https://travis-ci.org/AVE-cesar/AngularDemoBackend.

[![Quality Gate](https://sonarqube.com/api/badges/gate?key=test:AngularDemoBackend)](https://sonarqube.com/dashboard/index/AngularDemoBackend)

https://sonarcloud.io/organizations/ave-cesar-github/projects

[![codecov](https://codecov.io/gh/AVE-cesar/AngularDemoBackend/branch/master/graph/badge.svg)](https://codecov.io/gh/AVE-cesar/AngularDemoBackend)

