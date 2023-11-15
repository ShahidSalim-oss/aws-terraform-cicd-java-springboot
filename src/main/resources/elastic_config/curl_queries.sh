#!/bin/bash

ELASTIC_LOCATION="http://localhost:9200"
INDEX_NAME="your_index_name"
TYPE_NAME="your_type"

## Create index
#curl -X PUT ${DEFAULT_HEADER} ${ELASTIC_LOCATION}/${INDEX_NAME}

## Configure mappings
#curl -X PUT -H 'Content-Type: application/json' ${ELASTIC_LOCATION}/${INDEX_NAME}/_mapping/${TYPE_NAME}?include_type_name=true --data-binary @./createMappings.json


## Create testItem for testing purposes
#curl -X POST -H 'Content-Type: application/json' ${ELASTIC_LOCATION}/${INDEX_NAME}/${TYPE_NAME}?pretty --data "@./createTestItem.json"

## Turn on logs
#curl -X PUT -H "Content-Type: application/json" \
# --data '{ "index.indexing.slowlog.threshold.index.warn": "0s", "index.indexing.slowlog.threshold.index.info": "0s", "index.indexing.slowlog.threshold.index.debug": "0s", "index.indexing.slowlog.threshold.index.trace": "0s", "index.indexing.slowlog.level": "info", "index.indexing.slowlog.source": "1000" }' \
# ${ELASTIC_LOCATION}/${INDEX_NAME}/_settings



## sample search query
curl -X GET -H "Content-Type: application/json" \
--data-binary "@./sampleQuery.json" \
"${ELASTIC_LOCATION}/${INDEX_NAME}/_search"

# List all results
#curl -X GET -H "Content-Type: application/json" \
#"${ELASTIC_LOCATION}/${INDEX_NAME}/_search/?size=1000&pretty"
