elasticsearch: /usr/local/Cellar/elasticsearch/6.7.0/bin, ./elasticsearch
kinbana:       /usr/local/Cellar/kibana/6.7.0/bin         ./kibana
kafka:         /usr/local/Cellar/kafka/2.5.0/bin          kafka-server-start /usr/local/etc/kafka/server.properties &     
zookeeper:     /usr/local/Cellar/kafka/2.5.0/libexec/bin  zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties &


kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic pvuv

kafka-console-consumer.sh --topic pvuv --bootstrap-server localhost:9092

创建模板
PUT /_template/template_pvuv
{
    "order" : 2,
    "index_patterns" : [
      "pvuv-*"
    ],
    "settings" : {
      "index" : {
        "number_of_shards" : "1",
        "number_of_replicas" : "1"
      }
    },
    "mappings" : {
      "default" : {
        "properties": {
             "window_start": { 
                "type": "text"
              }, 
              "window_end": { 
                  "type": "text"
              },
              "pv": { 
                  "type": "text"
              },
              "uv": { 
                  "type": "text"
              }
         }
      }
    },
    "aliases" : {
      "article" : { }
    }
  }