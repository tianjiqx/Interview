Java笔记

[TOC]

## 1. 语法和特性

### 1.1 反射与代理

反射：运行过程中，对于任意类，可以获取该类的所有属性和方法。任意对象，调用它的任意方法和属性（包括私有）。

优点：

- 动态加载类，可被用于完成代理

缺点：

- 性能瓶颈

  

（argodbbar client 与master通信，rpc框架）

其他参考：笔记[UML与设计模式-笔记.md](https://github.com/tianjiqx/notes/blob/master/software_project/UML/UML%E4%B8%8E%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F-%E7%AC%94%E8%AE%B0.md) 介绍了远程代理



代理：

- 控制和管理访问
  - 远程对象，智能引用，多线程访问的同步代理，隐藏复杂功能，写时复制等
  - 保护代理，保护对象特定属性
    - 不同的调用处理器InvocationHandler，所能做事情不同



#### REF

- [java反射机制](https://mp.weixin.qq.com/s?__biz=MzI1NDU0MTE1NA==&mid=2247483785&idx=1&sn=f696c8c49cb7ecce9818247683482a1c&chksm=e9c2ed84deb564925172b2dd78d307d4dc345fa313d3e44f01e84fa22ac5561b37aec5cbd5b4&scene=0#rd)  java核心技术卷1 5.7    代理 6.5 
- hadoop 技术内幕common，mapreduce，yarn
- hadoop 权威指南4版



### 1.2 java classloader

classloader技术：

双亲委托

- 避免重复加载class
- 保留优先层级关系

特性：

- java class标识， 二进制的class定义（来源与jar包或其他方式）和对应的classLoader唯一标识该类
  - 两个class比较的时候， 如isInstance, asInstance，任意一个不匹配将认为不等

- 通过new创建的对象，继承父对象的Class loader，在一个已经存在的对象，方法中通过setContextClassLoader修改classloader不会生效，new的对象仍然是默认的AppClassLoader
  Thread.currentThread().setContextClassLoader(classLoader)可修改线程的ClassLoader
  只有在修改之后，通过反射创建的对象，这个对象再创建的对象会是修改后的classloader

- 继承的对象load时，先load自身，再load父类
  依然使用该class loader并且子类与类自身的classLoader可不同（通过自定义的classLoader，override loadClass方法中指定其他的classloader可以做到）
  但是，子类与父类接口参数返回值使用的class的class Loader，需要保持一致，否则运行时报错



应用：spark 支持兼容版本hive，Presto 支持不同数据源， 联邦计算项目



tips:

- 生命周期
  - 加载
    - 方法区，运行时数据结构
    - 堆区，java.lang.Class对象，访问方法区数据结构的入口
  - 链接
    - 验证
    - 准备
    - 解析
  - 初始化
  - 使用
  - 卸载
- 加载一个类时，其内部类不会同时被加载。
  - 一个类被加载，当且仅当其某个静态成员（静态域、构造器、静态方法等）被调用时发生。 for 单例模式实现
  - 被动引用
    - 通过子类引用父类静态字段，不会导致**子类**初始化
      - 父类会
    - 通过数组定义引用类，不会触发此类的初始化
      - `SuperClass[] supArr= new SuperClass[10];`
    - 引用常量时，常量在编译阶段会存入类的常量池中，本质上并没有直接引用到定义常量的类，因此也不会触发类的初始化。
- 加载器
  - 启动类BootStrap class Loader的类加载器
  - 扩展类Extension class Loader
  - 应用程序类Application class Loader
  - 用户自定义类加载器，继承URLClassLoader
- 加载类的方式
  - new关键字
  - 反射Class.forName
    - 默认初始化
  - 调用classLoder的loadClass方法
    - 默认不进行链接，则也不做后面的初始化
- 触发类的初始化（主动引用）
  - 使用 new 关键字实例化对象的时候，读取或设置一个类的静态字段（该字段不被 final 修饰）的时候，以及调用一个类的静态方法的时候；
  - 使用 java.lang.reflect 包的方法对类进行反射调用
  - 初始化类时，父类未初始化，需要先初始化父类
  - jvm启动，的main类
  - 一个 java.lang.invoke.MethodHandle 实例最后的解析结果为 REFgetStatic、REFputStatic、REF_invokeStatic 的方法句柄,并且这个方法句柄所对应的类没有进行过初始化（未理解）
- 类加载的执行顺序优先级：静态块>main()>构造块>构造方法
  - 静态代码块：用staitc声明，jvm加载类时执行，仅执行一次
  - 构造代码块：类中直接用{}定义，每一次创建对象时执行。
  - 静态块>main()是指一个类包含main()方法，同时有静态块，类被加载时会先执行静态块，之后才会执行main方法。



#### REF

- 深入理解Java虚拟机JVM高级特性与最佳实践 第2版-周志明
- [ava 虚拟机 3 ： Java的类加载机制](https://crazyfzw.github.io/2018/07/12/classloader/)
- JVM和ClassLoader
  https://www.cnblogs.com/Ming8006/p/11818218.html
- Spark对HiveMetastore客户端的多版本管理、兼容性探究以及栅
  栏实现 https://blog.csdn.net/zhanyuanlin/article/details
  /95898018 
- [slides: classloader](https://github.com/tianjiqx/slides/blob/master/Java%20ClassLoader.pdf)



### 1.3 JNI参考文档

（NBU项目，使用到jni技术）

官方API说明：

https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/jniTOC.html

Java Native Interface(JNI)从零开始详细教程：

https://blog.csdn.net/createchance/article/details/53783490#t18

编译相关：在 Linux 平台下使用 JNI

https://developer.ibm.com/zh/articles/l-linux-jni/

JNI最佳实践（编码的性能与正确性）

https://developer.ibm.com/articles/j-jni/

JNI语法相关（注意接口需要  (*env)->，方式，并传递env）：

https://github.com/glumes/AndroidDevWithCpp

JNI技术规范

**https://www.jianshu.com/p/88fbe27621fc**



### 1.4 inline

java中方法内联Method Inlining，指由编译器（无c++ inline关键字，JIT在运行期进行）将函数体插入调用处，减少函数调用开销,同时内联的代码为编译器提供更多的优化机会。是编译器的最重要优化手段。

- 主要开销：**方法栈帧的生成、参数字段的压入、栈帧的弹出，指令执行地址的跳转**
- 内联发生情形:
  - 热点代码，编译器根据-XX:CompileThreshold判断代码执行次数超过阈值后，将其内联；
    - 如果方法体过大，影响能缓存的方法数量，也不会内联， -XX:MaxFreqInlineSize，-XX:MaxInlineSize
      - <325B、 <35B
  - 方法很小，调用不频繁，进行常规内联（如Getter，Setter）
    - 无多个实现类，C2编译器会智能的将公有方法的virtual_call，优化为optimized virtual_call；
    - 有多个实现类，但是未实际使用多态特性，也尝试通过内联缓存（Inline Cache）完成方法内联
- 推荐**private，static、final修饰**，可以直接内联
  - Java中所有非私有的成员函数的调用都是虚调用
  - 由于public，protect类型的虚方法可以被继承然后override，jvm则需要额外进行类型判断，通过实例对象找到VMT，通过VMT找到对应方法的地址，可能导致没有达到性能优化的效果



REF

- [jvm之方法内联优化](https://zhuanlan.zhihu.com/p/55630861)
- [Java方法内联](https://www.cnblogs.com/xyz-star/p/10152564.html)
- [《Java性能权威指南》笔记----JIT编译器](https://www.cnblogs.com/zaizhoumo/p/7562786.html)
  - Java性能权威指南 第8章



扩展材料：

- [JIT & Code Cache](https://juejin.cn/post/6945654875468660772) JIT热点代码编译成机器码缓存
  - 若jdk发生bug，导致code cache满，清空，编译未重新开始而解释执行，会产生奇怪的性能回退现象
- [基本功 | Java即时编译器原理解析及实践](https://zhuanlan.zhihu.com/p/268042053) 美团，推荐
  - 中间表达式IR
  - 方法内联
  - 逃逸分析
  - 循环转换（展开，分离）
  - （解释执行，未必一定比编译执行慢）
- [运行期优化](https://klose911.github.io/html/jvm/runtime_optimize.html) 推荐



### 1.5 泛型方法

除了泛型类和泛型接口，泛型方法是java中使用泛型的第三种方式。不同于泛型类在实例化类的时候指明泛型具体类型，而是在调用方法的时候指明。

> *泛型方法*是引入自己的类型参数的方法。
>
> 这类似于声明泛型类型，但类型参数的范围仅限于声明它的方法。
>
> 允许使用静态和非静态泛型方法，以及泛型类构造函数。
>
> 可以在泛型类和非泛型中的使用。

```java
// 定义将数组转换为列表的通用方法
// 方法签名中 <T, G> 声明了泛型
public static <T, G> List<G> fromArrayToList(T[] a, Function<T, G> mapperFunction) {
    return Arrays.stream(a)
      .map(mapperFunction)
      .collect(Collectors.toList());
}
// 使用
Integer[] intArray = {1, 2, 3, 4, 5};
List<String> stringList = Generics.fromArrayToList(intArray, Object::toString);
assertThat(stringList, hasItems("1", "2", "3", "4", "5"));
// 指定获取map中的类型
public <T> T getObjectParamOrDefault(String key, Object defaultValue) {
  // params type is Map<String, Object>
  Object obj = params.get(key);
  if (obj == null) {
    return (T) defaultValue;
  }
  return (T) obj;
}
String value = getObjectParamOrDefault("key", null);
```



REF

- [generics/methods](https://docs.oracle.com/javase/tutorial/extra/generics/methods.html)
- [The Basics of Java Generics](https://www.baeldung.com/java-generics)





### 1.6 正则表达式

java正则表达式进行字符串匹配。用法

```java
Pattern pattern = Pattern.compile("regex");
Matcher matcher = pattern.matcher("test string");
if (matcher.matches()){
  // match then do something
  // 捕获组，commands 是组名称
  String commands = matcher.group("commands").trim();
}
```



- `\\s` 表示空格
- `(?s)` 表示单行模式，字符串中的`\n` 也将被任意符号`.` 匹配到
  - `(?m)` 表示多行模式
  - `(?i)` 表示匹配时不区分大小写
  - `(?x)`表达式中的空白字符将会被忽略，除非它已经被转义
  - `(?:`  表示不捕获模式，只是用来分组
- 捕获组 格式：` (?<名称>模式)`

#### REF

- [Java 正则表达式](https://www.runoob.com/java/java-regular-expressions.html)
- [Java正则表达式实例教程](https://www.yiibai.com/java/java-regular-expression-tutorial.html)
- [Java正则表达式——group方法详解](https://blog.csdn.net/u013514928/article/details/85473592)
  - 捕获组是通过从左至右计算其开括号来编号
  - group 0 表示匹配的字符串


## 2. 并发

### 2.1 线程池ThreadPoolExecutor

导入导出项目中，并发的实现。

```java
//ThreadPoolExecutor
  
 /*
 public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler)
corePoolSize- 要保留在池中的线程数，即使它们处于空闲状态，除非allowCoreThreadTimeOut已设置
maximumPoolSize - 池中允许的最大线程数
                - 当工作队列满时，提交新任务时，会尝试创建非core线程进行处理
keepAliveTime - 当线程数大于核心数时，这是多余空闲线程在终止前等待新任务的最长时间。
unit-keepAliveTime参数的时间单位
workQueue- 用于在执行任务之前保存任务的队列。这个队列将只保存方法Runnable 提交的任务execute。
threadFactory - 执行器创建新线程时使用的工厂
handler - 由于达到线程边界和队列容量而阻塞执行时使用的处理程序，饱和策略
**/
private ThreadPoolExecutor downloadThreadPool;
// Init the thread pool to download file
downloadThreadPool = new ThreadPoolExecutor(impExpContext.exportThreadPoolSize,
                                            impExpContext.exportThreadPoolSize, 
                                            0L,
                                            TimeUnit.MICROSECONDS,
                                            // 默认值是Integer.MAX_VALUE，
                                            // 一般情况下不会饱和，但可能jvm OOM
                                            new LinkedBlockingQueue<Runnable>(impExpContext.exportThreadPoolQueueSize),
                                            Executors.defaultThreadFactory(), 
                                            new BlockWhenQueueFullHandler()
                                           );
// 存放需要下载文件的任务，后面通过downloadThreadPool.execute() 
// 封装为可执行对象，逐一提交到线程池的任务队列
fileTaskQueue = new LinkedBlockingQueue<>(impExpContext.exportFileQueueSize);

// execute(Runnable command)
// 提交可执行任务，SubFileDownloader继承了runable 接口
downloadThreadPool.execute(new SubFileDownloader(subFileInfo, activeCount,
                                                 hadoopContext.dfs, hadoopContext.ccf));
/**
ThreadPoolExecutor是线程池的核心实现类，用来执行被提交的任务。
ScheduledThreadPoolExecutor是一个实现类，可以在给定的延迟后运行命令，或者定期执行命令。 ScheduledThreadPoolExecutor比Timer更灵活，功能更强大。
*/
```

在hadoop的源码中的使用线程池，发现也是继承ThreadPoolExecutor的HadoopThreadPoolExecutor（org.apache.hadoop.util.concurrent包目录下），只是增加日志。



**Java创建线程有三种方式**

- 实现Runable接口
  - 需要实现`void run()` 方法
  - 通过`new Thread(Runable r).start()` 运行 
  - 优势：
    - Runable是接口，可以有多个实现
    - 可以被多个Thread实例，共用Runable对象，共享数据
- 实现Callable接口
  - 与 Runnable 相比， Callable 可以有返回值，抛出异常， 返回值通过 FutureTask 进行封装
    - `V call() throws Exception;`
  - `new Thread(new FutureTask<T>(Callable<T> c)).start()` 运行
  - `FutureTask.get()` 获取接口
- 直接继承Thread类



**BlockingQueue 阻塞队列**

- `LinkedBlockingQueue`
  - 链式阻塞队列，底层数据结构是链表，默认大小是Integer.MAX_VALUE，可以指定大小
- `ArrayBlockingQueue`
  - 数组阻塞队列，底层数据结构是数组，需要指定队列的大小
  - `new ArrayBlockingQueue<>(QUEUE_CAPACITY)`
- `SynchronousQueue`
  - 同步队列，内部容量为，不存储元素，每个put操作必须等待一个take操作
- `DelayQueue`
  - 延迟队列，想要获取的元素需要等待指定延迟时间后，才能从队列中获取到该元素

**RejectedExecutionHandler 饱和策略**

- ThreadPoolExecutor.AbortPolicy：默认拒绝处理策略，丢弃任务并抛出RejectedExecutionException异常。
- ThreadPoolExecutor.DiscardPolicy：丢弃新来的任务，但是不抛出异常。
- ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列头部（最旧的）的任务，然后重新尝试执行程序（如果再次失败，重复此过程）。
- ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务。（推荐）
  - `new ThreadPoolExecutor.CallerRunsPolicy()`
  - 由调用线程中执行被拒绝的任务，可能影响提交任务（执行`ThreadPool.execute()`的线程，主线程或者说提交线程）



阿里 Java 开发手册的建议：

- 不允许自己显示创建线程，而创建线程池
  - 减少在创建和销毁线程上所花的时间以及系统资源的开销（内存，切换）
- 也不允许Executors创建，而是通过ThreadPoolExecutor 的方式（手动设置任务队列容量，以及饱和策略）
  - FixedThreadPool 和 SingleThreadPool，允许的请求队列长度为 Integer.MAX_VALUE ，可能OOM
  - CachedThreadPool 和 ScheduledThreadPool，允许的创建线程数量为 Integer.MAX_VALUE，也可能导致OOM



#### REF

- [jdk1.8 ThreadPoolExecutor](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadPoolExecutor.html)
- [Java多线程之ThreadPoolExecutor详解使用](https://www.cnblogs.com/dim2046/p/12059073.html)
- [java&android线程池-Executor框架之ThreadPoolExcutor&ScheduledThreadPoolExecutor浅析（多线程编程之三）](http://blog.csdn.net/javazejian/article/details/50890554)
- [jdk1.8 java.lang.Runnable](https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html)
- [jdk1.8 java.util.concurrent.Callable](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Callable.html)
- [深入浅出Java多线程-ThreadPoolExecutor](https://crazyfzw.github.io/2020/11/13/concurrent-thread-pool-executor/)  demo使用，原理  推荐



### 2.2 ThreadLocal

目的：

多线程执行的代码，访问的数据，每个单线程独立使用，提供线程本地实例对象，线程局部变量。

#### 2.2.1 使用场景：

- 消息队列，消费者处理持续处理自身负责的消息
- 线程内上线文管理器、数据库连接，RPC通信 客户端
- 每个线程维护一个用户session，持续处理用户请求
- 一个线程，需要传递的对象（上下文（Context）），横跨若干方法进行处理，适合通过ThreadLocal完成在一个线程中传递同一个对象
  - 一般定义为私有静态字段



#### 2.2.2 使用范例

```java
 import java.util.concurrent.atomic.AtomicInteger;
 // 线程ID生成、获取器
 public class ThreadId {
     // Atomic integer containing the next thread ID to be assigned
     private static final AtomicInteger nextId = new AtomicInteger(0);

     // Thread local variable containing each thread's ID
     private static final ThreadLocal<Integer> threadId =
         new ThreadLocal<Integer>() {
         	// 定义创建线程局部变量的方法
             @Override protected Integer initialValue() {
                 return nextId.getAndIncrement();
         }
     };
     /*
     另一种初始化方法：
     private static ThreadLocal<Integer> mLocal = 
     	ThreadLocal.withInitial(() -> nextId.getAndIncrement());
     */

     // Returns the current thread's unique ID, assigning it if necessary
     public static int get() {
         // 通过get()方法获取线程自己的对象，不存在创建。
         return threadId.get();
         // 其他公有的方法： 
         // set(T value)  设置线程副本的执行
         // remove() 删除线程局部变量的当前线程值，再次get时，通过initialValue()方法重新初始化
         // 静态方法withInitial(Supplier<? extends S> supplier) 创建线程局部变量
      }
 }
```

#### 2.2.3 基本原理

为了达到ThreadLocal的作用，一种可能的实现是，ThreadLocal 变量 自己维护一个ConcurrentHashMap\<thread， value> ，但是该方法的缺点是：

- 需要加锁访问ConcurrentHashMap\<thread， value>，保证线程安全
- 线程结束，需要释放资源，避免内存泄漏

（BAR项目 master对于client连接，也是通过该方式完成，注册加入，长时间无请求时的释放，注册不频繁，性能影响不大）



解决ThreadLocal变量 访问冲突的方式是，每个线程维护ThreadLocal 变量与值的映射ThreadLocalMap< ThreadLocal, value>，但是需要注意对ThreadLocal 变量引用的回收，避免内存泄漏。



JDK的实际的实现：

```java
public class ThreadLocal<T> {

    private final int threadLocalHashCode = nextHashCode();
    private static AtomicInteger nextHashCode =
        new AtomicInteger();
    private static final int HASH_INCREMENT = 0x61c88647;

    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }
    public T get() {
        Thread t = Thread.currentThread();
        // 从线程中获取ThreadLocalMap
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }
    public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }
     public void remove() {
         ThreadLocalMap m = getMap(Thread.currentThread());
         if (m != null)
             m.remove(this);
     }
	// 从线程中获取ThreadLocalMap
    ThreadLocalMap getMap(Thread t) {
        return t.threadLocals;
    }
    // 为线程创建ThreadLocalMap
    void createMap(Thread t, T firstValue) {
        // key: this 是ThreadLocal对象 value: firstValue 
        t.threadLocals = new ThreadLocalMap(this, firstValue);
    }
	// 每个线程自己维护的map，ThreadLocal变量和该线程的值
    static class ThreadLocalMap {
		// 对ThreadLocal变量的弱引用，当没有强引用指向ThreadLocal变量时，它可被回收，避免内存泄漏
        // 对值是强引用，值可能由于key为null，无法移除entry，导致泄漏
        // 解决办法是:set方法中通过replaceStaleEntry/cleanSomeSlots清除所有null的key
        // rehash中，expungeStaleEntry清除所有null的key
        static class Entry extends WeakReference<ThreadLocal<?>> {
            /** The value associated with this ThreadLocal. */
            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }

        private static final int INITIAL_CAPACITY = 16;
        private Entry[] table;
        private int size = 0;
        // resize
        private int threshold; // Default to 0

        ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
            table = new Entry[INITIAL_CAPACITY];
            int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
            table[i] = new Entry(firstKey, firstValue);
            size = 1;
            setThreshold(INITIAL_CAPACITY);
        }
        
        private Entry getEntry(ThreadLocal<?> key) {
            int i = key.threadLocalHashCode & (table.length - 1);
            Entry e = table[i];
            if (e != null && e.get() == key)
                return e;
            else
                return getEntryAfterMiss(key, i, e);
        }

        private Entry getEntryAfterMiss(ThreadLocal<?> key, int i, Entry e) {
            Entry[] tab = table;
            int len = tab.length;

            while (e != null) {
                ThreadLocal<?> k = e.get();
                if (k == key)
                    return e;
                if (k == null)
                    expungeStaleEntry(i);
                else
                    i = nextIndex(i, len);
                e = tab[i];
            }
            return null;
        }
    }    
}
```

最佳实践：

对于内存泄漏，Thread.exit()会令threadLocals=null，最终会释放ThreadLocalMap，让其能够回收。

但是如果使用的是线程池，那么就有可能因为，线程一直活着，导致ThreadLocal对象的泄漏。

那么最好的做法，是避免key为null的ThreadLocalMap的entry产生，在线程对自己ThreadLocal对象使用完后，调用remove方法，去除自己线程管理的entry（ThreadLocal变量的弱引用和值）。



#### REF

- [jdk8 api: ThreadLocal<T>](https://docs.oracle.com/javase/8/docs/api/java/lang/ThreadLocal.html)
- [深入浅出Java多线程-ThreadLocal](https://crazyfzw.github.io/2020/11/25/threadlocal/)
- [使用ThreadLocal](https://www.liaoxuefeng.com/wiki/1252599548343744/1306581251653666)
- [一篇文章，从源码深入详解ThreadLocal内存泄漏问](https://github.com/CL0610/Java-concurrency/blob/master/18.%E4%B8%80%E7%AF%87%E6%96%87%E7%AB%A0%EF%BC%8C%E4%BB%8E%E6%BA%90%E7%A0%81%E6%B7%B1%E5%85%A5%E8%AF%A6%E8%A7%A3ThreadLocal%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E9%97%AE%E9%A2%98/%E4%B8%80%E7%AF%87%E6%96%87%E7%AB%A0%EF%BC%8C%E4%BB%8E%E6%BA%90%E7%A0%81%E6%B7%B1%E5%85%A5%E8%AF%A6%E8%A7%A3ThreadLocal%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E9%97%AE%E9%A2%98.md)



### 2.3 synchronized

`synchronized`同步关键字，用于保证只有单个线程进程操作。

使用方式：

- 普通方法
  - 对当前对象加锁
- 静态方法
  - 当前 `Class` 对象
- 代码块
  - `synchronized() {}`
  - `()` 中的对象
    - `(xxx.class)` class的所有对象
    - `(this)`,`(object)` 单个对象

原理：

java 编译期，生成对monitor的 enter和exit指令，排他的对monitor的占用。

java底层对加锁过程，锁的变化，有一定的优化。



REF

- [synchronized 关键字原理](https://crossoverjie.top/2018/01/14/Synchronize/)



### 2.4 BlockingQueue & BlockingDeque

单向队列BlockingQueue 关键方法：

|      | 抛出异常   | 返回特定值(true/false) | 阻塞    | 超时                        |
| ---- | ---------- | ---------------------- | ------- | --------------------------- |
| 插入 | add(o)     | offer(o)               | put(o)  | offer(o, timeout, timeunit) |
| 移除 | remove(o)  | poll(o)                | take(o) | poll(timeout, timeunit)     |
| 检查 | element(o) | peek(o)                |         |                             |

双端队列BlockingDeque。同名函数增加First/Last后缀。



常用的实现类：

- ArrayBlockingQueue 数组实现，有界
- LinkedBlockingQueue 链表实现，可以设置大小
- DelayQueue 维持特定的延迟
- PriorityBlockingQueue 优先级，无界
  - ` java.util.PriorityQueue `
- SynchronousQueue 同步队列，单元素



REF

- [JUC集合: BlockingQueue详解](https://www.pdai.tech/md/java/thread/java-thread-x-juc-collection-BlockingQueue.html)



## 3.IO

### 3.1 Java NIO

（ArgoDBBAR项目，网络通信机制依赖netty；数据拷贝性能改进使用到filechannel）

#### IO分类

- 同步阻塞I/O：socket.read()，如果TCP RecvBuffer里没有数据，函数会一直阻塞，直到收到数据，返回读到的数据
- 非阻塞IO：数据未准备好时，也能正常返回，准备好时，正常处理数据
  - 需要轮询IO是否准备好（polling）
- IO复用：linux的select/poll机制，支持多个fd的读写，顺序扫描fd状态，以支持IO复用
  - epoll：基于事件驱动代替select/poll的顺序扫描
  - 单个线程程能够处理多个I/O事件，避免为每个IO创建一个一个线程
    - 但是处理I/O事件可能是阻塞式处理的
- NIO：基于IO复用的非阻塞IO，事件驱动
  - selector 轮询Channel
- AIO(异步IO)：不但等待就绪是非阻塞的，就连数据从网卡到内存的过程也是异步的。内核通知IO完成
  - 订阅-通知机制，应用注册，内核主动通知，非应用主动轮询内核

![](java笔记-图片/Snipaste_2021-06-15_19-32-00.png)

![](java笔记-图片/Snipaste_2021-06-15_19-33-01.png)

![](java笔记-图片/Snipaste_2021-06-15_19-33-17.png)

![](java笔记-图片/Snipaste_2021-06-15_19-33-42.png)



![](java笔记-图片/Snipaste_2021-06-15_19-37-31.png)





**IO复用**

![](java笔记-图片/java-io-nio-1.png)

#### 概念

- Selector：多路复用器。用以轮询注册的channel是否就绪，就绪后进行IO操作
  - 事件订阅
    - 注册，处理OS的事件
  - Channel管理
    - 注册
    - 分发事件

- Channel：通道

- Buffer：缓冲区
  - 对通道的读写操作进行合并



#### 传统IO模型

![](java笔记-图片/java-io-reactor-1.png)

Server端使用OS线程池处理任务。

缺点：

- 服务器的并发量取决于服务器工作线程数，工作线程数无法提到很高
- IO读写，CPU计算耦合，无法充分利用资源
- IO是阻塞类型，受会受网络波动，降低服务器利用资源率

#### Reactor事件驱动模型

![](java笔记-图片/java-io-reactor-2.png)

优点：

- 单线程处理网络请求，一定程度解耦，网络请求与处理分离，提高处理效率
- 异步非阻塞模型，不受网络请求IO波动影响



**业务处理与IO分离**

![](java笔记-图片/java-io-reactor-3.png)

- 一个线程进行客户端连接的接收以及网络读写事件的处理
  - reactor线程
- 客户端连接之后，将该连接交由线程池进行数据的编解码以及业务计算

优点：

- 进一步的解耦处理逻辑，网络读写与业务计算分离

缺点：

- 单线程处理高并发的网络请求，存在瓶颈

**并发读写**

![](java笔记-图片/java-io-reactor-4.png)

- Reactor拆分
  - mainReactor 负责客户端连接的处理
  - subReactor 通过线程池，处理网络IO读写
- 业务逻辑计算，也通过线程池进行处理

#### 异步IO

![](java笔记-图片/java-io-aio-1.png)

操作系统内核的支持：

- windows  IOCP
- liunx epoll模拟
  - 回调机制通知？



java NIO 与 AIO 区别：

- NIO，（Non-blocking I/O）
  - 实现了 IO 多路复用中的 Reactor 模型
  - 通过配置监听的通道 Channel 为非阻塞，能够轮询其他Channel
    - Socket Channel 配置为非阻塞
    - FileChannel无法配置为非阻塞（针对本地文件，本身就不会阻塞，更多用于零拷贝，并发多线程写单个文件）
  - 零拷贝支持
    - MappedByteBuffer，抽象类，堆外的虚拟内存，内存文件映射，手动反射调用释放
    - DirectByteBuffer，MappedByteBuffer的实现类，对象在堆内，内部缓冲区在堆外
    - FileChannel
      - 文件读写、映射和操作的通道，线程安全
      - `transferTo()/transferFrom()` 零拷贝
        - 检查内核是否支持sendfile，再尝试内存文件映射，目的通道必须是FileChannelImpl 或者 SelChImpl 类型，最后使用普通IO（零拷贝原理，具有多种，可见[IO性能改进技术](https://github.com/tianjiqx/notes/blob/master/performance/IO%E6%80%A7%E8%83%BD%E6%94%B9%E8%BF%9B%E6%8A%80%E6%9C%AF.md)）
- AIO，重点在异步，操作系统内核通知事件是IO完成，而NIO是通知可以开始IO



**Netty框架**

- 高性能、异步事件驱动的基于NIO的网络应用程序框架，提供了对TCP、UDP和文件传输的支持
  - 自身是异步的，但是可以上层封装为同步的接口
    - 基本原理是Netty 为要传递的消息创建一个会话session，具有唯一的sessionId，正常的异步发送消息，Future机制响应异常处理，然后阻塞该会话，直到检查到session标记完成，或者超时。Netty 接受消息时，handler根据sessionid对session状态进行改变，以及存放消息处理结果到session中，然被阻塞的session能够及时处理响应，取出结果。
- 可以用来实现客户端与服务端的RPC通信



Fork/Join 框架

- 并行执行框架，将大任务分割成小任务，最后汇总小任务结果
  - fork 分裂产生子任务，并计算
    - 实际是将自身作为子任务加入队列，创建/唤醒工作线程处理(执行`compute`)
  - join等待子任务执行结果，并得到结果
- 实现原理
  - 每个工作线程，维护自己的双端队列存放子任务`ForkJoinTask`
    - 双端便于使用工作窃取算法
  - 工作线程池ForkJoinPool，处理子任务ForkJoinTask
    - ForkJoinTask关键方法`compute()`
      - `compute` 做任务切分，合并结果，或者最粒度任务的执行
- ForkJoinTask子类
  - RecursiveAction 没有返回结果的任务
  - RecursiveTask 有返回结果的任务



#### REF

- [Java NIO浅析-美团技术团队](https://tech.meituan.com/2016/11/04/nio.html)
- [Java NIO 系列教程-并发编程网译文](https://ifeve.com/java-nio-all/)
- Netty权威指南 第2版-李林峰
- [ByteBuffer浅显易懂的图解原理](https://greedypirate.github.io/2019/12/01/ByteBuffer%E6%B5%85%E6%98%BE%E6%98%93%E6%87%82%E7%9A%84%E5%9B%BE%E8%A7%A3%E5%8E%9F%E7%90%86/#ByteBuffer)
- [Java NIO - IO多路复用详解](https://www.pdai.tech/md/java/io/java-io-nio-select-epoll.html)
- [Java AIO](https://www.pdai.tech/md/java/io/java-io-aio.html)
- [Java NIO 零拷贝](https://www.pdai.tech/md/java/io/java-io-nio-zerocopy.html)
- [Linux下的I/O复用与epoll详解](https://www.cnblogs.com/lojunren/p/3856290.html)
  - select 轮询FD状态
  - poll 取消监听文件数量限制（max 65535 fd）
  - epoll 采样回调方式检查到绪事件，避免轮询
    - 65535 fd
- java并发的艺术 infoq   Fork/Join

扩展：

- 《Java并发编程之美》翟陆续





### 3.2 JAVA 异步编程

原始线程 thread 其实也可以异步完成任何操作，java8后，`thread()` 还可以接受lambada 表达式，来执行。

缺点:

- 无法直接返回结果，只能通过传递线程安全的对象，进行数据共享和返回结果。
- 可扩展性不强，受限线程数量
- 需要丑陋的代码检查线程是否执行完毕，结果是否返回，异常处理。
  - 改善的方式使用CountDownlatch
    - `private static CountDownLatch countDownLatch = new CountDownLatch(2) `
    - 子线程`countDownLatch.countDown()`
    - 主线程`countDownLatch.await()` 阻塞等待子线程完成



异步编程的优点：

- 处理逻辑与IO的解耦，资源的充分利用，提供并发量
  - IO密集型
- 作为一种并行手段，充分利用多核性能
  - CPU密集型



#### 3.2.1 FutureTask和CompletableFuture

**Future & FutureTask**

- Future
  - 代表一个异步计算的结果
  - 方法
    - `isDone` 是否完成
    - `isCancelled` 是否取消
    - `get()/get(long timeout, TimeUnit unit)`  阻塞式获取结果
    - `cancel(boolean mayInterruptIfRunning)` 尝试取消异步计算任务
- FutureTask
  - 表示一个可被取消的异步计算任务，实现`Runnable`,`Future`接口
    - 因此可以通过线程池的`execute()` 方法提交执行
  - 关键方法
    - 构造函数
      - 接受`Callable` 对象
      - 接受的Runnable和Future对象
    - `run ` 执行逻辑

```java
// 这里可以用ThreadPoolExecutor，手动设置线程池参数，更好
ExecutorService threadpool = Executors.newCachedThreadPool();
Future<Long> futureTask = threadpool.submit(() -> factorial(number));
// futureTask.isDone() 获取Future状态
while (!futureTask.isDone()) {
    System.out.println("FutureTask is not finished yet..."); 
}
// futureTask.get() 获取Future结果，阻塞式
long result = futureTask.get(); 

threadpool.shutdown();
```



缺点：

- 无法表达多个FutureTask之间的关系
  - 组合多个异步计算
- 获取Future结果，需要get()阻塞调用线程

**CompletableFuture**

- 实现`Future`,`CompletionStage` 接口
- 通过编程方式显式地设置计算结果和状态
  - `future.complete(result);`
- 可以作为一个CompletionStage（计算阶段），当它的计算完成时可以触发一个函数或者行为
- 当多个线程企图调用同一个CompletableFuture的complete、cancel方式时只有一个线程会成功
- 所有异步的方法在没有显式指定Executor参数的情形下都是复用ForkJoinPool的commonPool()线程池来执行
- 方法
  - `runAsync` 无返回值的异步计算
  - `supplyAsync` 带有返回值的异步计算，可以通过get获取
  - `thenRunAsync` 执行完成任务后，激活其他任务
  - `thenAcceptAsync` 执行完成任务后，激活其他任务，并获取之前任务的返回值
  - `whenCompleteAsync` 设置回调函数
  - `thenCompose` 组合，顺序执行另一个CompletableFuture
  - `thenCombine` 同时并发CompletableFuture
  - `allOf` 等待所有并发执行CompletableFuture完成
  - `anyOf` 等待任一并发执行CompletableFuture完成

```java
CompletableFuture<Long> completableFuture = CompletableFuture.supplyAsync(() -> factorial(number));
// 检查任务状态
while (!completableFuture.isDone()) {
    System.out.println("CompletableFuture is not finished yet...");
}
// 获取future结果，阻塞
long result = completableFuture.get();
```



缺点:

- 回调地狱
- 多个Future依然难以编排
- 不支持延迟计算和高级错误处理

（rust async/awit 语法就明显更简单）

#### 3.2.2 异步框架

- RxJava

- Reactor
- Spring @Async
- Web Serverlet
- Netty

- [guava ListenableFutureExplained](https://github.com/google/guava/wiki/ListenableFutureExplained)

REF

- [Java 异步编程](https://www.baeldung.com/java-asynchronous-programming)  一些库，框架
- [Java FutureTask](https://www.pdai.tech/md/java/thread/java-thread-x-juc-executor-FutureTask.html)
- [以两种异步模型应用案例，深度解析 Future 接口](https://xie.infoq.cn/article/d42e22d85f37b9596e47b2c33)
- [Java技术域中的异步编程](https://chinalhr.github.io/post/java-asyncprogram/)
- 《Java异步编程实战》翟陆续
- [一文带你彻底了解Java异步编程](http://ifeve.com/%E4%B8%80%E6%96%87%E5%B8%A6%E4%BD%A0%E5%BD%BB%E5%BA%95%E4%BA%86%E8%A7%A3java%E5%BC%82%E6%AD%A5%E7%BC%96%E7%A8%8B/)  工程实践角度



## 4. 最佳实践

### 4.1 Java性能诊断技巧

- jps：查看java进程，默认只
  - `jps -v` 输出虚拟机进程启动时JVM参数
  - `jps -l` 输出主类的全名,jar包路径
  - `jps -m` 输出传递给主类main()函数的参数
  - `jps -q` 只输出pid
  
- jinfo：查看和调整虚拟机运行参数，环境变量，classpath等信息
  - `jinfo <pid>`

- jstack：线程分析，找项目相关类方法
  - `jstack <pid>`

- jmap：定期打印jvm状态，查看异常变量，对象
  - `jmap -histo:live <pid> |head 30`  显示堆中对象统计信息，包括类、实例数量、合计容量
    - 有时，需要`sudo -u hive` 来指定用户
  - `jmap -heap <pid>` 显示堆详细信息
  - `jmap -dump <pid>`  生成Java堆转储快照
    - -dump:[live, ]format=b,file=filename,其中live子参数说明是否只dump出存活的对象

- jhat：分析jmap 打印的dump文件，可在浏览器中查看
  - `jhat <dumpfile>`
  
- jstat: 查看GC频率
  - `jstat -gcutil <pid> [interval] [count]` 百分比显示，JVM堆使用（Young,Old等区）情况
  
    - | 列   | 说明                                                       |
      | ---- | ---------------------------------------------------------- |
      | S0   | 第 0 个 survivor（幸存区）使用的百分比                     |
      | S1   | 第 1 个 survivor（幸存区）使用的百分比                     |
      | E    | `Eden` 区使用内存的百分比                                  |
      | O    | 老生代内存使用的百分比                                     |
      | P/M  | `PermGen`/`MetaSpace` 的内存使用百分比                     |
      | YGC  | 程序启动以来 Young GC 发生的次数                           |
      | YGCT | 程序启动以来 Young GC 共消耗的时间(s)                      |
      | FGC  | 程序启动以来 Full GC 发生的次数                            |
      | FGCT | 程序启动以来 Full GC 共消耗的时间(s)                       |
      | CGC  | 程序启动以来 并发（Concurrent） GC 发生的次数，java11      |
      | CGCT | 程序启动以来 并发（Concurrent） GC 共消耗的时间(s)，java11 |
      | GCT  | 程序启动以来 GC 的总用时(s)                                |
  
    - `-gc` 按KB显示
  
  - `jstat -gccause <pid> [interval] [count]` 显示最后一次或当前正在发生的垃圾回收的诱因
  
  - `jstat -gcmetacapacity` 显示metaspace大小的统计信息
  
- arthas: trace,watch命令，分析性能，变量，查找抛出异常的位置
  - `watch <class name> <method name> -x 2 `

- jar tvf xxx.jar 查看jar中包含的类 

GC日志打印，停顿时间

扩展材料：

- [调试排错 - Java问题排查：工具单](https://www.pdai.tech/md/java/jvm/java-jvm-debug-tools-list.html)  btrace, dmesg
- [jstat命令总结](https://blog.csdn.net/u010648555/article/details/81089323)
- [java11 jstat](https://docs.oracle.com/en/java/javase/11/tools/jstat.html#GUID-5F72A7F9-5D5A-4486-8201-E1D1BA8ACCB5)



### 4.2 JVM 参数

#### 4.2.1 堆配置

例子：-vmargs -Xms128M -Xmx512M -XX:PermSize=64M -XX:MaxPermSize=128M

-vmargs 声明后面是VM的参数

- 堆内存
  - -Xms 128m JVM初始分配的堆内存
  - **-Xmx** 512m JVM最大允许分配的堆内存，按需分配
- 新生代内存(Young Ceneration, YC) 
  - 默认1310MB,最大无限制
  - -XX:NewSize= \<young size>[unit] 
  - -XX:MaxNewSize= \<young size>[unit]
  - -Xmn \<young size>[unit] 
    - NewSize与MaxNewSize 设置为一致
- 永久代/元空间
  - -XX:PermSize=64M JVM初始分配的非堆内存（方法区），在jdk1.8 无效参数
    - 方法区 (永久代) 初始大小
    - 存储类的信息、常量池、方法数据、方法代码等
    - 所有线程共享
  - -XX:MaxPermSize=128M JVM最大允许分配的非堆内存，按需分配
    - 方法区 (永久代) 最大大小,超过这个值将会抛出 OutOfMemoryError 异常:java.lang.OutOfMemoryError: PermGen
      - “PermGen space“ HotSpot的实现
  - JDK 1.8 方法移除永久代，使用元空间（本地内存）
    - 元空间，JVM方法区的新实现
      - 元空间的大小仅受本地内存限制
        - 风险：没有指定 Metaspace 的大小时，消耗系统所有内存
      - 异常信息：**:java.lang.OutOfMemoryError: Metaspace**
      - 替代原因：
        - 字符串存在永久代中，容易出现性能问题和内存溢出
        - 类及方法的信息等比较难确定其大小，因此对于永久代的大小指定比较困难
    - -XX:MetaspaceSize=N
    - -XX:MaxMetaspaceSize=N
  - -XX:NewRatio=2   Old 和 Yong 的比例



#### **4.2.1 GC 回收器**

- 指定垃圾回收器(分代的垃圾回收期)
  - -XX:+UseSerialGC  串行垃圾回收器
    - Serial + Serial Old 
  - -XX:+USeParNewGC 并发串行垃圾收集器， 多线程并发，减少回收时间
    - ParNew + Serial Old 
  - -XX:+UseParallelGC 并行收集器，多CPU物理并行，最大化的提高程序吞吐量，同时缩短程序停顿时间
    - ParallelScavenge  + Serial Old
      - ParNew 的升级版本，主要区别在于提供了两个参数：-XX:MaxGCPauseMillis 最大垃圾回收停顿时间；-XX:GCTimeRatio 垃圾回收时间与总时间占比
  - -XX:+UseConcMarkSweepGC   CMS 回收器，基于“标记-清除”算法，以获取最短回收停顿时间为目标
    - ParNew + CMS + Serial Old，作为并发失败后备 Serial Old
    - 并行清理过程中，其他线程依然工作不会造成Stop The world（STW），但是清理过程中可能产生新的垃圾对象
    - 存在两次短暂的STW 进行Inital mark GC Root的直接对象和remark
  - -XX:+UseG1GC  G1垃圾收集器，多线程执行，既用于新生代收集，也用于老生代收集
    - 分治思想，大小相等的region 存放各种大小的对象（Eden、Survivor 、Old、Humongous角色）
      - Garbage-First 有限回收region中对象少的region
    - -XX:MaxGCPauseMillis 参数可指定预期停顿值
      - 根据历史数据预测本地满足预期STW时间，需要回收的region数量
    - 与 CMS 相比，G1 有内存整理过程（标记-压缩），避免了内存碎片；STW 时间可控（能预测 GC 停顿时间）
    - STW：初始标记，再标记，清理，复制（主要）
  - -XX:+UnlockExperimentalVMOptions -XX:+UseZGC ZGC在G1基础上的优化
    - 同样是region，但还是不分代
    - Colored Pointer，64 bit，读屏障位进行并发转移，避免STW
    - 设计
      - STW时间不超过10ms
      - STW时间不会随着堆的大小，或者活跃对象的大小而增加

- -XX:+PrintCompressedOopsMode 指针压缩模式（jdk7，8 默认开启）

  - 压缩模式即使用每隔8个字节保存一个引用，32 bit 指针可以应用35位（32G）的空间，通常 jvm 堆空间大小配置成31g
  - oop (ordinary object pointer) 普通对象指针

- -XX:ConcGCThreads：并发回收垃圾的线程数。

  - 默认是总核数的12.5%，8核CPU默认是1

- -XX:ParallelGCThreads：STW阶段使用线程数，默认是总核数的60%。

- 指定GC日志

  - ```
    -XX:+PrintGC  // 别名 -verbose:gc
    -XX:+PrintGCDetails
    -Xloggc:/path/to/gc.log
    ```

活跃数据：Full GC后，堆中老年代占用空间的大小

各区分配：`总堆=3~4 * 活跃数据 = 1~1.15 （新）+ 2~3 （老） + 1.2~1.5 * 活跃数据 (永久) `



GC调优经验： Full GC 的成本远高于 Minor GC，尽量将新对象预留在新生代，大对象/长期存活的对象进老年代

#### 4.2.3 其他

- -XX:ReservedCodeCacheSize=256m -XX:InitialCodeCacheSize=256m CodeCache的大小，JIT编译的代码



#### REF

- [最重要的JVM参数指南](https://github.com/Snailclimb/JavaGuide/blob/master/docs/java/jvm/%E6%9C%80%E9%87%8D%E8%A6%81%E7%9A%84JVM%E5%8F%82%E6%95%B0%E6%8C%87%E5%8D%97.md)
- [Java8内存模型—永久代(PermGen)和元空间(Metaspace)](https://www.cnblogs.com/paddix/p/5309550.html)
- [java8添加并查看GC日志(ParNew+CMS)](https://segmentfault.com/a/1190000021453229)
- [JVM GC 日志详解](https://juejin.cn/post/6844903791909666823)
- [java9 gc log参数迁移](https://www.jianshu.com/p/a99dec3230c9)
- [Java中9种常见的CMS GC问题分析与解决-美团](https://tech.meituan.com/2020/11/12/java-9-cms-gc.html)
  - GC cuase主要原因
    - **System.gc()：** 手动触发GC操作
    - CMS：  Initial Mark + Final Remark
    - **Promotion Failure：** Old 区没有足够的空间分配给 Young 区晋升的对象
    - **Concurrent Mode Failure：** CMS GC 运行期间，Old 区预留的空间不足以分配给新的对象

  - **GC问题排查**（原因还是现象？）
    - 时序分析，先发生的事件往往是根因，CPU 负载高 -> 慢查询增多（数据过多，移动到old区，回收代价增加） -> GC 耗时增大 -> 线程Block增多 -> RT 上涨
    - 概率分析，历史原因概率，慢查询增多 -> GC 耗时增大 -> CPU 负载高 -> 线程 Block 增多 -> RT上涨
    - 实验分析，尝试问题复现，触发条件，线程Block增多 -> CPU 负载高 -> 慢查询增多 -> GC 耗时增大 -> RT 上涨
    - 反证分析，表象的发不发生跟结果是否有相关性，例如集群的角度观察到某些节点慢查和 CPU 都正常，但也出了问题，那么问题可能是：GC 耗时增大 -> 线程 Block 增多 -> RT 上涨
      - 原因

- [新一代垃圾回收器ZGC的探索与实践-美团](https://tech.meituan.com/2020/08/06/new-zgc-practice-in-meituan.html) java11
- [浅析JAVA的垃圾回收机制](https://www.jianshu.com/p/5261a62e4d29) java8
- [一文看懂 JVM 内存布局及 GC 原理-infoq](https://www.infoq.cn/article/3wyretkqrhivtw4frmr3)
  - YG: eden ->  S0 <-> S1
  - OG: full GC
- [【博客大赛】JVM深入探索之初探ZGC的领域](https://blog.51cto.com/alex4dream/2738057)
- [ZGC的开发者博客 zgc-jdk17](https://malloc.se/blog/zgc-jdk17) 
  - jdk17新增特性 ZGC Pauses 的GarbageCollectorMXBean获取真实STW时间和次数
    - -XX:+UseDynamicNumberOfGCThreads  动态gc线程数enable
    - 支持macOS/AArch64

  - jdk16  max STW <1ms
    - 平均 0.05 ms (50μs) 最大约0.5ms (500us)

  - jdk15 生产级ZGC、支持堆分配在NVRAM上，支持OOPS
  - Jdk14 开始支持macos，支持JFR leak profiler
  - jdk11 支持zgc

- [JVM Tuning with G1 GC](https://marknienaber.medium.com/jvm-tuning-with-g1-gc-76f27535f054)
- [Java Hotspot G1 GC的一些关键技术](https://tech.meituan.com/2016/09/23/g1.html)


- hprof 文件堆栈内存分析工具 [Memory Analyzer (MAT)](https://eclipse.dev/mat/) 



## 5. JMH

Java 微基准测试工具 (JMH，Java Microbenchmark Harness)，是一个 Java 工具，用于构建、运行和分析用 Java 和其他面向 JVM 的语言编写的 nano/micro/milli/macro 基准测试。



优点：

- 使用简便（依赖JMH的jar包，使用@Benchmark即可），自动处理JVM预热，JVM优化处理等问题
- 主要测试单位时间测试的操作次数来反应代码的性能，甚至可以测试调试日志打印的性能时间
  - 调试日志打印方式：使用字符串连接、使用变量参数和使用If进行debug可用检测
- 可自定义配置（是否共享线程对象，预热次数，运行基准测试次数）
- 提供生成jar包方式执行



使用：

官方提供maven构建脚本，推荐创建独立项目使用 Maven 设置依赖于应用程序 jar 文件。

对于大型项目，将基准放在单独的子项目中，依赖于待测试模块。

源码jmh-samples 目录下提供了一些编写的基准测试的样例。

presto demo:[RegexpFunctionsBenchmark.java](https://github.com/prestodb/presto/blob/master/presto-main/src/test/java/com/facebook/presto/operator/scalar/RegexpFunctionsBenchmark.java)



社区提供

- [Gradle JMH 插件](https://github.com/melix/jmh-gradle-plugin)
- [Scala SBT JMH 插件](https://github.com/ktoso/sbt-jmh)
- IDE插件（官方不推荐在IDE中运行，因为环境可能变化，而直接在命令行运行）
  - [IntelliJ IDEA JMH 插件](https://github.com/artyushov/idea-jmh-plugin)



### REF

- [github: openjdk/jmh](https://github.com/openjdk/jmh)
- [Code Tools: jmh](http://openjdk.java.net/projects/code-tools/jmh/)
- [JMH Samples](http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/)
- [[译]使用JMH进行微基准测试：不要猜，要测试！](http://www.hollischuang.com/archives/1072)
  - JVM一直在改进，对代码的微小改动，凭借最佳实践经验无法反应真实的情况，直接测试是写最好性能的代码的方式
- [JMH-大厂是如何使用JMH进行Java代码性能测试的？必须掌握！](https://zhuanlan.zhihu.com/p/197257423)
- [基准测试神器JMH——详解36个官方例子](https://zhuanlan.zhihu.com/p/381283590)





## 6. Mockito

Mockito 是一个java程序单元测试的mock框架，并宣称是最流行mock测试框架。

Mockito可以创建 Mock 对象来进行测试，避免需要创建真实对象，尤其是一些服务类（数据库，缓存等）对象。

基本使用：

```
repositories { mavenCentral() }
dependencies { testImplementation "org.mockito:mockito-core:3.+" }
```

```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>2.0.111-beta</version>
</dependency>
```



主要功能：

- `mock()` 创建mock对象，可以mock接口，和具体类
  - `doReturn(obj).when(mock).someMethod(someArgs)` 指定特定参数，特定的返回值
  - `anser()` 通用的行为
- `spy()` 部分模拟，替换部分真实对象的返回结果
-  `verify()` 检查mock对象的方法调用结果，例如调用次数

```java
import org.mockito.Mockito;
// 创建mock对象
Service cacheService = Mockito.mock(Service.class);
// 配置对象行为, Service.getObject() 方法接受 “obj1” 参数时，返回 obj1, 其他参数将返回null/0/false等
Mockito.doReturn(obj1).when(cacheService).getObject("obj1");

// 通用的模拟, 当对象mock，执行 someMethod()方法， 并接受任意字符串参数，将执行answer()动作, 返回其结果
Mockito.when(mock.someMethod(Mockito.anyString())).thenAnswer(
     new Answer() {
         public Object answer(InvocationOnMock invocation) {
             Object[] args = invocation.getArguments();
             Object mock = invocation.getMock();
             return "called with arguments: " + Arrays.toString(args);
         }
 });

//Following prints "called with arguments: [foo]"
System.out.println(mock.someMethod("foo"));
// 检查调用次数
Mockito.verify(mock, Mockito.times(5)).someMethod("was called five times");
```

特别说明：

-  即使Service的构造函数传递的类被混淆，依然可以mock出Service对象

###  REF

- [github: mockito](https://github.com/mockito/mockito)
- [site:mockito](https://site.mockito.org/)
- [doc: mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [手把手教你 Mockito 的使用](https://segmentfault.com/a/1190000006746409)
- [中文翻译](https://github.com/hehonghui/mockito-doc-zh)




## 7. Java 公共库 

### 7.1 Guava 

Guava 是一组来自 Google 的核心 Java 库，其中包括新的集合类型（例如 multimap 和 multiset）、不可变集合、图形库以及用于并发、I/O、散列、缓存、原语、字符串等库。



#### 常用库

- 预先检查
  - 实际java 11 也有类似 `requireNonNull(metric, "metric is null");`
- 不可变集合
- 字符串

```java
// 预先检查
// 在一些构造函数或方法中使用
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

public static List<XX> newXXXList(List<XXX> list) {
  checkNotNull(list, "list is null"); 
  // 变种
  checkArgument(i >= 0, "Argument was %s but expected nonnegative", i);
}

// 不可变集合(Map,list,set)
public static final ImmutableSet<String> COLOR_NAMES = ImmutableSet.of(
  "red",
  "orange",
  "yellow",
  "green",
  "blue",
  "purple");

ImmutableSet<Bar> bars = ImmutableSet.copyOf(bars);

ImmutableSet<Bar> bars = ImmutableSet.<Bar>builder().
  .add(new Bar("xx"))
  .build();

// Iterables 
Iterable<Integer> concatenated = Iterables.concat(
  Ints.asList(1, 2, 3),
  Ints.asList(4, 5, 6));
// concatenated has elements 1, 2, 3, 4, 5, 6
String lastAdded = Iterables.getLast(myLinkedHashSet);

// 函数式风格计算
// list<CommonStats> 转换 Iterable<XXX> 
Iterable<XXX> iter = Iterables.transform(stats, CommonStats::getXXX);
ImmutableList<XXX> list = ImmutableList.copyOf(iter);

// 可变集合
Map<KeyType, LongishValueType> map = Maps.newLinkedHashMap();

// ToString
// Returns "ClassName{}"
MoreObjects.toStringHelper(this)
  .toString();

// Returns "ClassName{x=1}"
MoreObjects.toStringHelper(this)
  .add("x", 1)
  .toString();

// Returns "ClassName{x=1}"
MoreObjects.toStringHelper(this)
  .omitNullValues()
  .add("x", 1)
  .add("y", null)  // 自动处理null值
  .toString();
```



#### REF

- [github: guava](https://github.com/google/guava)
- [guava wiki](https://github.com/google/guava/wiki)
- [Google Guava官方教程（中文版）](https://wizardforcel.gitbooks.io/guava-tutorial/content/1.html)
- [guava-userguide-cn](https://www.bookstack.cn/read/guava-userguide-cn/README.md)



### 7.2 AirLift

AirLift 是一个用 Java 构建 REST 服务的框架。

该项目用作 prestodb,  trino（原presto sql）等分布式系统的基础。

功能模块：

- slice 用于高效地处理堆和堆外内存
- joni 字符串工具类，正则匹配
- Units 时间，数据量等单位格式化显示



#### REF

- [github: airlift](https://github.com/airlift/airlift)



## 7.3 Netty

```
//  池化，非池化内存
io.netty.allocator.type=pooled
io.netty.allocator.type=unpooled

// 默认false，默认堆外内存， 由反射调用使用 noCleaner策略
// 设置为 true 后，默认使用堆内内存，DirectByteBuffer 堆外内存使用Cleaner策略
io.netty.noUnsafe=false

// 默认的堆外内存方式切换到堆内, 默认值false
io.netty.noPreferDirect = true

// 限制Netty中hasCleaner策略的DirectByteBuffer堆外内存的大小
-XX:MaxDirectMemorySize
// 限制noCleaner策略下Netty的DirectByteBuffer分配的最大堆外内存的大小，如果该值为0，则使用hasCleaner策略
-Dio.netty.maxDirectMemory

// 观察堆外内存使用
-XX:NativeMemoryTracking=detail
jcmd pid VM.native_memory detail


// top 的 RES 实际进程使用内存大小，包括堆外内存
```



### REF

- [Netty 核心原理剖析与 RPC 实践-完](https://learn.lianglianglee.com/专栏/Netty 核心原理剖析与 RPC 实践-完)
- [Netty的内存使用方法与原理——池化与堆外内存的开启与关闭](https://www.jianshu.com/p/05f99a85b977)
- [Netty堆外内存泄漏排查，这一篇全讲清楚了](https://segmentfault.com/a/1190000021469481) Netty noCleaner策略
- [Netty堆外内存泄露排查盛宴](https://tech.meituan.com/2018/10/18/netty-direct-memory-screening.html)
- [Netty防止内存泄漏措施](https://mp.weixin.qq.com/s/IusIvjrth_bzvodhOMfMPQ)
- [疑案追踪：Spring Boot内存泄露排查记](https://mp.weixin.qq.com/s/aYwIH0TN3nSzNaMR2FN0AA)



## Java Stream

```java
// list -> stream -> array
String[] arr = list.stream().toArray(String[] ::new);

// [] -> list
Arrays.asList(arr);

// replace null -> 0.0
values.replaceAll(value -> Objects.requireNonNullElse(value, 0.0));

```

### 日期时间解析

```
FixedDateFormat

ISO_OFFSET_DATE_TIME

2024-10-25T20:52:51+08:00
yyyy-MM-dd'T'HH:mm:ss


```



## 扩展材料

- [Effective Java笔记](https://github.com/keyvanakbary/learning-notes/blob/master/books/effective-java.md)

