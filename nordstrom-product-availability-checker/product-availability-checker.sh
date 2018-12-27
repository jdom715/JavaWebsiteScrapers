#!/bin/bash
if [ $# -lt 4 ]; then
    echo "Usage: sh nordstrom-website-scraper [AWS_ACCESS_KEY] [AWS_SECRET_KEY] [FROM_EMAIL_ADDRESS] [FROM_EMAIL_PASSWORD] "
    exit 1
fi

mvn clean package

export AWS_ACCESS_KEY=$1
export AWS_SECRET_KEY=$2
export FROM_EMAIL_ADDRESS=$3
export FROM_EMAIL_PASSWORD=$4

java -jar target/product-availability-checker-1.0.jar
