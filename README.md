# janusgraph-utils
## JanusGraph数据导入导出及Schema创建


此包中对janusgraph-api作了简单包装, 仅供学习参考使用.

# Schema创建注意事项
- 复合索引可能是唯一索引。
- 外部索引不能是唯一索引。
- 复合索引中PropertKey不能存在Parameter.
- 外部索引中PropertKey可以存在或不存在Parameter.

# 功能流程图
![功能流程图](./img/843a66a18edb725a8f61cd0563f5e17.png)

# 数据校验流程图
