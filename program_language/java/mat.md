
# Memory Analyzer (MAT) 内存分析工具


## Overview


### detail
eg：

Size: 22.3 GB Classes: 44.5k Objects: 67.9m Class Loader: 3k

Unreachable Objects Histogram： 不可达对象，下次回收

### Histogram

选择 histogram 工具栏中的直方图图标列出每个类的实例数、浅层大小和保留大小。


重要功能是可以按类加载器、包或超类对任何直方图进行分组。


### Dominator Tree （支配树）

Dominator Tree 是堆转储分析中的一个重要概念，用于帮助开发者理解对象之间的引用关系，并识别内存中的主要占用者。它是从对象引用图（Object Graph）中衍生出来的一种树形结构，能够清晰地展示哪些对象“支配”了其他对象的内存。


immediate dominator 

如果对象图中从开始（或根）节点到y的每条路径都必须经过x，则对象x支配对象y。某个对象y的直接支配者x是最接近对象y的支配者。

支配者树是从对象图构建的。在支配者树中，每个对象都是其子对象的直接支配者，因此对象之间的依赖关系很容易识别。

immediate dominator 该查询查找并聚合在类级别上支配给定对象集的所有对象。快速找出谁对一组对象负责是非常有用的，因为它直接回答了“谁让这些对象存活”的问题，而不是回答“谁引用了这些对象”。利用每个对象只有一个直接支配者的事实（不像多个传入引用），该工具提供了过滤“不感兴趣的”支配者的可能性（例如java。类）并直接查看负责的应用程序类。


list objects：

Incoming References (传入引用):
显示哪些对象引用了当前这个对象
即谁持有了当前对象的引用（谁引用了这个对象）
帮助找到内存泄漏的源头，因为持有引用的对象阻止了当前对象被垃圾回收
示例：如果一个 ArrayList 被 Session 对象引用，那么这个引用会在 Incoming References 中显示


Outgoing References (传出引用):
显示当前对象引用了哪些其他对象
即当前对象持有了哪些其他对象的引用
帮助理解对象持有的资源和依赖关系
示例：一个 ArrayList 对象的 Outgoing References 会显示它内部的所有元素



### Path to GC Roots

从对象到GC根的（反向）引用链--所谓的GC根路径--解释了为什么对象不能被垃圾收集。路径有助于解决Java中的经典内存泄漏：这些泄漏的存在是因为对象仍然被引用，即使程序逻辑不再访问该对象。

### Generate the Leak Report  生成泄漏报告

可以选择最大的内存占用类，右键下钻 Generate the Leak Report。

[自动化堆转储分析：一键查找内存泄漏](https://memoryanalyzer.blogspot.com/2008/05/automated-heap-dump-analysis-finding.html)

## OQL 查询

```
SELECT memoryPeak FROM io.xx.matrix.physical.TaskStats 

toString(table) 

```

[mat OQL语法说明](https://zhuanlan.zhihu.com/p/590009803)

[官方](https://help.eclipse.org/latest/index.jsp?topic=%2Forg.eclipse.mat.ui.help%2Freference%2Foqlsyntax.html&cp=66_4_2)


## 默认报告

### Leak Suspects

### Top Consumers


列出最大的对象

Top Consumers查询列出按类、类装入器和包分组的最大对象。要运行查询，请在Leak Identification（泄漏识别）类别中选择Top Consumers（顶级用户）：




## REF

- [MAT](https://eclipse.dev/mat/)
    - [doc](https://help.eclipse.org/latest/index.jsp?topic=/org.eclipse.mat.ui.help/welcome.html)