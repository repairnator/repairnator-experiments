#!/bin/sh

java -DKEYCLOAK_AUTH_SERVER=$KC_AUTH_SERVER -DKEYCLOAK_REALM=$KC_REALM -DKEYCLOAK_CLIENT=$KC_CLIENT -jar target/keycloak-cli-sso-3.3.0.CR1-SNAPSHOT.jar logout

unset KC_ACCESS_TOKEN




