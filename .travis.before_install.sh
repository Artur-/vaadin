#!/usr/bin/env bash

# Disable all Travis default repositories 
sed -i "s/activeByDefault>true</activeByDefault>false</g"  ~/.m2/settings.xml

# Fetch latest screenshots
git clone http://dev.vaadin.com/review/p/vaadin-screenshots tests/screenshots

# Fetch Ant Ivy if needed
mvn dependency:get -DgroupId=org.apache.ivy -DartifactId=ivy -Dversion=2.4.0
