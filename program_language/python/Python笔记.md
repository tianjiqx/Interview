# Python笔记

[TOC]

## 1. 基础



## 1.1 多线程、进程池

```python
# 线程
from concurrent.futures import ThreadPoolExecutor
with ThreadPoolExecutor(max_workers=max_workers) as pool:
  with Timer() as t:
    results = pool.map(query, tasks)
    # wait end
    for res in results:
      pass


# 进程  
from multiprocessing import Pool, Process             
with Pool(max_workers) as pool:
   results = pool.map(query, tasks)
   # wait end
   for res in results:
     pass
```



## 2.高级

## 2.1 性能计时

```
import time

class Timer(object):
    """
    计时器，记录执行耗时
    """

    def __enter__(self):
        self.start = time.perf_counter()
        return self

    def __exit__(self, *args):
        self.end = time.perf_counter()
        self.interval = self.end - self.start

with Timer() as t:
	# xxx
print("spent time {} s".format(t.interval))
```

## 2.2 内存分析

`memory_profiler` 

```python
# 安装 
pip install -U memory_profiler

# 使用
from memory_profiler import profile

@profile
def fun():
	xxx
  
# 离开函数后，在控制台打印内存变化，可重定向到日志文件
```



## 3. 第三方库

### 3.1 Pandas

```python
# 1. 过滤缺失值
# 过滤缺失数据， 默认过滤所有包含NaN
df.dropna()
# how='all' 过滤全为NaN的行
df.dropna(how='all')
# axis=1 ，过滤全为NaN的列
df.dropna(axis=1,how="all")
# how="any" 过滤任何有NaN的列
df.dropna(axis=1,how="all")
# 保留至少有1个非NaN数据的行
df.dropna(thresh=1)



```







## REF

- [Python 文档目录](https://docs.python.org/zh-cn/3/contents.html)
- [如何使用**“memory_profiler”**通过 Python 代码分析内存使用情况？](https://coderzcolumn.com/tutorials/python/how-to-profile-memory-usage-in-python-using-memory-profiler)
- [Python 性能分析之内存使用](https://juejin.cn/post/7064746559514574878)
- [Python之路(第十七篇)logging模块](https://www.cnblogs.com/Nicholas0707/p/9021672.html)
- [Python 中更优雅的日志记录方案](https://mp.weixin.qq.com/s?__biz=Mzg3MjU3NzU1OA==&mid=2247496180&idx=1&sn=73aaf603b1d3a2dc36623f43d2b58a13&source=41#wechat_redirect)
- QA
  - [relative-imports-in-python-3](https://stackoverflow.com/questions/16981921/relative-imports-in-python-3)

