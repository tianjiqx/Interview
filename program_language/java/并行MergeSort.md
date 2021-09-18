# 并行MergeSort

目标：

int空间是4B， 10亿行（10^9），大小空间是4GB，加上mergesort的辅助空间， 需要8GB。

那么，100亿行，大小空间是40GB，加上mergesort的辅助空间， 需要80GB。现代的普通服务器，也基本都能满足。





思路：

尽量先按cpu 核数作为，线程数，排序自己负责的一部分数据据。合并任务数=sort线程数-1。

这里，sort线程数也可以配置超过物理cpu核数，但是过多线程数应该反而性能过低。



测试性能：

100M 行的int排序，400MB， 在2CPU的虚拟机环境下

单线程： `1thread, 400MB(100M), 16.4s,  6M/s`

2线程：`400MB, 100M, 10.6s, 9.4M/s`

10线程：`400MB, 100M, 23.5s, 4.2M/s`

大致符合预期，并行merge sort版本，由于实现问题：

- 通过主线程每隔10ms，对完成任务加锁，检查任务完成情况，进行merge
- 每次merge各子线程时从临时空间，再复制回原来的数组，带来额外O(2N) 复制时间
- 线程间的上下文切换带来开销



比较：

100M 行的int排序，400MB， 在2CPU的虚拟机环境下

`java.util.Arrays.parallelSort spend time total 10091 ms | 9804 ms`

Arrays.parallelSort使用的 ForkJoin 框架实现递归并行合并排序。



REF：

- [具有双重合并的并行归并排序](https://github.com/ahmet-uyar/parallel-merge-sort)  发现的一个并行归并实现，基于CyclicBarrier，同步线程的逻辑更加优雅，实现的性能比Arrays.parallelSort 略好。



个人实现的版本，在合并单个线程完成的sort的部分，根据从头到尾按顺序扫一遍，每次会被的大小可能并不相等，考虑到可能某些线程，因为数据倾斜的原因，sort的很快，所以选择抢占式。

第一个版本，只是检查过程通过定期轮询，感觉不够优雅。

第二个版本，是最初想法，添加已完成sort任务或者merge任务范围时，检查是否能够产生新的merge任务放到任务队列，主线程从merge任务队列中取，执行merge任务，应该更优雅。

可能受本地环境变化，Arrays.parallelSort 和自身测试都发生性能退化，理论上2cpu环境，两个版本的实现其实差异不会有明显影响。



TODO：

- 外部排序（文件），分布式
  - shuffle
- 其他数据库系统中的mergesort实现
- 其他语言实现：go，rust
- 通过docker部署，更加稳定的测试环境

