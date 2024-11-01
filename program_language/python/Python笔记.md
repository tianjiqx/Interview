# Python笔记

[TOC]

## 1. 基础

```python

# for 表达式
ids = ",".join(f"`{id.strip()}`" for id in metric_ids.split(","))


```


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

多线程ThreadPoolExecutor与异步asyncio 区别

- 执行模型:
ThreadPoolExecutor 是基于多线程的执行模型。它使用线程池来并行执行任务，适用于 I/O 密集型操作，如文件操作、网络请求等，可以充分利用多核 CPU。
asyncio 是基于单线程事件循环的执行模型。它使用异步编程和非阻塞 I/O 来实现并发，适用于高并发的 I/O 操作，如异步网络通信、协程等。由于是单线程，不适用于 CPU 密集型操作。


- 并发模式:
ThreadPoolExecutor 提供真正的并行执行，多个线程可以同时执行任务，因此可以充分利用多核 CPU。
asyncio 提供的是协程并发，是在单线程中通过事件循环来调度多个任务，它不会并行执行任务，但可以在任务之间高效切换，避免阻塞。

- 阻塞和非阻塞:
ThreadPoolExecutor 中的每个线程通常会阻塞在执行的任务上，直到任务完成。
asyncio 使用非阻塞的方式处理任务，允许任务在 I/O 操作等待结果时释放 CPU 给其他任务执行，从而提高了并发度。

- 可扩展性:
ThreadPoolExecutor 的线程数通常是有限的，如果线程数不足，可能会导致性能瓶颈。
asyncio 在适当的情况下可以处理大量并发连接，因为它不会创建大量的线程。

- 编程模型:
ThreadPoolExecutor 使用传统的同步编程模型，通过线程来实现并发，可能需要处理线程同步和共享数据的复杂性。
asyncio 使用异步编程模型，通常使用 async 和 await 关键字，编写的代码更加紧凑和易于维护，但也需要理解事件循环和协程的概念。


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


### 3.2 虚拟环境构建方法

1.venv
```
# 1.创建虚拟环境kt
python -m venv ~/PYTHON_ENV/kt
# 2.进入kt目录下, 安装依赖
~/PYTHON_ENV/kt/bin/pip install -r requirements.txt
# 3.需要命令行使用创建的该python，可以激活虚拟环境，否则需要使用绝对路径
source  ~/PYTHON_ENV/kt/bin/Activate
# 4.退出虚拟环境
deactivate
```
venv环境，仍然依赖系统环境运行python。

2.通过conda构建本地虚拟环境
```
# 1.下载操作系统环境对应的 miniconda
https://docs.conda.io/en/latest/miniconda.html#linux-installers
#linux：weget https://repo.anaconda.com/miniconda/Miniconda3-py39_22.11.1-1-Linux-x86_64.sh
# 2. 安装，注意使用bash，而非sh
bash Miniconda3-py39_22.11.1-1-Linux-x86_64.sh
# 初始化
/root/miniconda3/bin/conda init
# 3.安装conda pack
/root/miniconda3/bin/conda install conda-pack -y


# 1.创建虚拟环境kt_python_env
conda create -n kt_python_env python=3.9 -y
# 2.启用虚拟环境
conda activate kt_python_env
# 3.进入kt目录下，安装依赖包
pip install -r requirements.txt
# 4.导出环境， 假设当前环境是linunx
conda pack -n kt_python_env -o kt_python_env_liunx_x86_64.tar.gz
# 解压环境
mkdir kt_python_env
tar -xzf kt_python_env_liunx_x86_64.tar.gz -C kt_python_env
# 5.退出虚拟环境
conda deactivate

```
- ubuntu20 venv 方式提供的离线python包，不支持kylinos v10。但支持ubuntu 16。
- ubuntu20 conda pack 出包支持运行在kylinos v10 环境，centos 7。


3.pip离线包
```

# requiremnets.txt 文件生成
pipreqs --force --encoding utf-8 --savepath requirements.txt .


mkdir /tmp/pip
# 导出环境python指定依赖到/tmp/pip目录，requirements.txt 文件即keta-ml的项目依赖
pip install --download /tmp/pip -r requirements.txt

# 压缩
cd /tmp
tar -czf keta-ml-deps.tar.gz pip

# 安装离线包
# 注意requirements.txt不要包含 -i xxxx 指定的pip源，否则可能因为无法联网而执行失败
pip install --no-index --find-links=/keta-ml-deps/pip/ -r requirements.txt
```

*conda pack导出的环境仍然依赖glibc*
>conda pack工具本质上只是将整个环境打包为一个单独的文件，包括Python解释器、依赖库和其他必要的文件。当你在另一台计算机上解压并运行这个离线环境时，它仍然需要与操作系统共享的基础库，其中包括glibc。
glibc（GNU C库）是一个C语言库，提供了许多与操作系统交互的功能。Python解释器和其他一些库在运行时可能需要依赖这些功能，因此glibc是Python环境正常工作所必需的。

*glibc兼容性*
>glibc的版本兼容性是一个复杂的问题，因为它涉及到不同的操作系统和软件库之间的交互。在一般情况下，向后兼容性是相对较好的，即较新版本的glibc可以与较旧版本的软件一起使用。然而，向前兼容性则较为有限，即较旧版本的glibc可能无法与较新版本的软件一起使用。
关于glibc版本兼容性的一般原则：
>1. 相同的主要版本：具有相同主要版本号的glibc通常是兼容的，例如glibc 2.27和glibc 2.28。这意味着从较旧的glibc版本升级到稍微更新的版本通常是安全的。
>2. 不同的主要版本：在不同主要版本之间，glibc的向前兼容性可能会有限。较新的glibc版本通常包含对较旧版本中不可用或不支持的功能的更改。因此，较旧的软件可能无法与较新的glibc版本一起正常工作。
>3. 操作系统之间的差异：不同操作系统可能使用不同版本的glibc，并且它们之间的差异可能很大。如果将离线环境从一个操作系统移动到另一个操作系统，可能需要确保目标操作系统上的glibc版本与环境的要求兼容。
综上所述，尽管glibc具有某种程度的向后兼容性，但在处理离线环境或移动环境到不同操作系统时，仍然需要注意glibc版本兼容性的问题。最好在目标操作系统上进行测试，以确保环境能够正常工作，并根据需要进行必要的调整和更新。


### 3.3 pyinstaller 二进制打包



### 3.4 NLP libs

- [JioNLP](https://github.com/dongrixinyu/JioNLP) 中文 NLP 预处理、解析工具包, 时间语义解析

- [TextBlob](https://github.com/sloria/TextBlob) 词性标记、名词短语提取、情感分析、分类、翻译等。
- [spaCy](https://github.com/explosion/spaCy)

- [snownlp](https://github.com/isnowfy/snownlp)

#### REF
 - [awesome-nlp](https://github.com/keon/awesome-nlp#python)


### 3.5 Flask

python web 应用框架。
[flash doc](https://dormousehole.readthedocs.io/en/latest/)


### 3.6 FastAPI

FastAPI 是一个用于构建 API 的现代、快速（高性能）的 web 框架，使用 Python 3.8+ 并基于标准的 Python 类型提示。

[教程 - 用户指南](https://fastapi.tiangolo.com/zh/tutorial/)

## REF

- [Python 文档目录](https://docs.python.org/zh-cn/3/contents.html)
- [如何使用**“memory_profiler”**通过 Python 代码分析内存使用情况？](https://coderzcolumn.com/tutorials/python/how-to-profile-memory-usage-in-python-using-memory-profiler)
- [Python 性能分析之内存使用](https://juejin.cn/post/7064746559514574878)
- [Python之路(第十七篇)logging模块](https://www.cnblogs.com/Nicholas0707/p/9021672.html)
- [Python 中更优雅的日志记录方案](https://mp.weixin.qq.com/s?__biz=Mzg3MjU3NzU1OA==&mid=2247496180&idx=1&sn=73aaf603b1d3a2dc36623f43d2b58a13&source=41#wechat_redirect)
- QA
  - [relative-imports-in-python-3](https://stackoverflow.com/questions/16981921/relative-imports-in-python-3)

- https://note.qidong.name/2020/12/pyinstaller/ 
  - pyinstaller 需要依赖外部libc.so
  - 不支持Alpine等发行版
  - 向后兼容，低版本环境打包在高版本环境可以运行


  