package utils

import data_stream.source_sink.datastream_2_redis.Raw
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

object RedisUtil {

    private val config: FlinkJedisPoolConfig = new FlinkJedisPoolConfig.Builder().setHost("127.0.0.1").setPort(6379).build()

    def getRedisSink(): RedisSink[Raw] = {
        new RedisSink[Raw](config, new MyRedisMapper)
    }


    class MyRedisMapper extends RedisMapper[Raw] {
        override def getCommandDescription: RedisCommandDescription = {
            new RedisCommandDescription(RedisCommand.HSET, "weibo_keyword")
        }

        // value
        override def getValueFromData(t: Raw): String = t.keywordList

        //key
        override def getKeyFromData(t: Raw): String = t.date_time
    }



}
