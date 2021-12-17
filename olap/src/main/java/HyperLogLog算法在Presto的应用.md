### 第一题： 写出5条HyperLogLog的用途或大数据场景下的实际案例。

- 在metric基数统计场景中，使用HLL对时序进行预估，可以保证较低且稳定的内存占用，又可以保证误差率在一个较低的可接受范围内。
- elasticsearch的聚合方法中，提到了其基数聚合（cardinality aggregation）就是采用的hyperloglog算法
- 在 Kylin中的 COUNT DISTINCT 使用HLL
- spark中的approx_count_distinct函数就是基于HyperLogLog实现的


### 在Presto中使用HyperLogLog计算近似基数



