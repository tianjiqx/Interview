## 阿里java规约笔记

### 一、编程规约
#### 1.命名风格
- a.抽象类命名使用 Abstract 或 Base 开头；
- b.异常类命名使用 Exception 结尾；
- c.测试类命名以它要测试的类的名称开始，以 Test 结尾
- d.POJO类（Plain OrdinaryJava Object，javabean类）中布尔类型的变量，都不要加is，部分框架解析会引起序列化错误
- e.接口类中的方法和属性不要加任何修饰符号（public 也不要加），保持代码的简洁性，并加上有效的Javadoc注释

#### 2.常量使用
- a.常量的复用层次有五层：跨应用共享常量、应用内共享常量、子工程内共享常量、包内共享常量、类内共享常量。
跨应用共享常量：放置在二方库中，通常是 client.jar 中的 constant 目录下
应用内共享常量：放置在一方库中， 通常是 modules 中的 constant 目录下
...
类内共享常量：直接在类内部 private static final 定义。

#### 3.代码格式
- a.大括号内为空，则简洁地写成{}即可，不需要换行.
- b. // 注释内容， 注意在//和注释内容之间有一个空格

#### 4.OOP规约
- a.所有的覆写方法，必须加@Override 注解。
- b.Object 的 equals 方法容易抛空指针异常，应使用常量或确定有值的对象来调用equals。
"test".equals(object);
- c. 关于基本数据类型与包装数据类型的使用标准如下：
1） 【强制】 所有的 POJO 类属性必须使用包装数据类型。
2） 【强制】 RPC 方法的返回值和参数必须使用包装数据类型。
3） 【推荐】 所有的局部变量使用基本数据类型。
说明： POJO 类属性没有初值是提醒使用者在需要使用时，必须自己显式地进行赋值，任何NPE 问题，或者入库检查，都由使用者来保证。
- d.类内方法定义顺序依次是：公有方法或保护方法 > 私有方法 > getter/setter方法。

#### 5.集合
- a.使用entrySet遍历Map类集合 KV，而不是 keySet 方式进行遍历.
keySet 其实是遍历了 2 次, JDK8，使用 Map.foreach 方法。
- b.高度注意 Map 类集合 K/V 能不能存储 null 值的情况
HashMap key和value允许为null，treeMap的value允许为null，其他hashtable，ConcurrentHashMap的kv不允许为null
- c.集合的有序性(sort)和稳定性(order)
ArrayList是order/unsort;HashMap是unorder/unsort;TreeSet是order/sort。

#### 6.并发处理
- a.线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，规避资源耗尽的风险。
- b.SimpleDateFormat是线程不安全的类，一般不要定义为static变量

#### 7.控制语句
- a.不要在条件判断中执行其它复杂的语句，将复杂逻辑判断的结果赋值给一个有意义的布尔变量名，以提高可读性。
- b. 需要进行参数校验的情形
执行时间开销很大的方法，对外提供的开放接口，RPC/API/HTTP接口

#### 8.注释规约
- a.所有的类都必须添加创建者和创建日期
- b.所有的枚举类型字段必须要有注释，说明每个数据项的用途
- c.谨慎注释掉代码。 在上方详细说明，而不是简单地注释掉。 如果无用，则删除。

#### 9.其他
- a.在使用正则表达式时，利用好其预编译功能，可以有效加快正则匹配速度
在方法体外static final Pattern regex = Pattern.compile("xxx");
- b.获取当前毫秒数 System.currentTimeMillis();而不是new Date().getTime()

未理解，或者理解不深的点：
1.枚举类名建议带上 Enum 后缀
该条是参考，目前使用该参考的似乎较少。
2.关于集合处理中转换操作使用较少，看来有许多坑。
3.并发处理，使用也较少，理解不深。



### 二、异常日志
#### 1. RuntimeException处理
- 1.使用预检查代替try catch异常，该条理解是try catch机制避免滥用，过多的try-catch对代码逻辑处理也会形成障碍，以及第二条try-catch处理效率。
try-catch使用对可预见而又无法掌控的情况进行处理，将问题向更上一层面传递，将处理权让渡给caller，其余情况应该就地解决。
- 2.区分稳定代码和非稳定代码（需要try-catch）
- 3.方法的返回值可以为null，不强制返回空集合，或者空对象等，必须添加注释充分
说明什么情况下会返回null值
防止NPE是调用者的责任。
- 4.NPE的产生
	- a.返回类型为基本数据类型， return 包装数据类型的对象。
	- b.集合里的元素即使 isNotEmpty，取出的数据元素也可能为 null
	- c.远程调用返回对象时，一律要求进行空指针判断，防止 NPE
使用jdk8，Optinal防止NPE，是一个包含有可选值的包装类。（ofNullable，ifPresent的使用）
https://www.cnblogs.com/zhangboyu/p/7580262.html

- 5.在代码中使用“抛异常”还是“返回错误码”
- a.公司外的 http/api 开放接口必须使用“错误码”
- b.应用内部推荐异常抛出；
- c.跨应用间 RPC 调用优先考虑使用 Result 方式，封装 isSuccess()方法、 “错误码”、 “错误简短信息”
(ArgodbbarClient的返回信息)

未理解，或者理解不深的点：
1.第5条，手动回滚事务，回滚事务的实际做法。

### 日志规约
- 1.日志分类（stats/desc/monitor/visit）
目前只做到的是日志信息中标识，未独立成单独的文件，确实分析的时候因为日志过多有一定困扰，观察shiva服务会按日志级别进行分割。
- 2.使用条件或占位符方式，减少字符拼接。
占位符方式，比较方便，但是需要日志框架支持，使用条件isDeBugEnable，目前更通用一点。
- 3.异常信息打印，带上堆栈
logger.error(各类参数或者对象 toString + "_" + e.getMessage(), e);


### 三、单元测试
- 1.AIR原则
(A自动化、I独立性、R可重复)
自动化：使用断言保证结果正确性
独立性：需要保证待测的，函数模块，分割良好。
可重复性：个人认为是难点，某些接口、方法方法中外部环境的依赖，涉及外部接口调用和对象，例如测试文件系统的接口，规约引入Mock方式，增加了一些测试工作量，有时也不容易实现，不过也无更好的办法。
- 2.关于测试覆盖率
核心模块，语句覆盖率和分支覆盖率100%，注意语句覆盖100%,不一定保证分支覆盖率100%。基本语句覆盖率70%。
- 3.BCDE原则，保证测试质量
	- B:边界值测试，包括循环边界、特殊取值、特殊时间点、数据顺序，这里个人增加单测时，时间开发关系，很少能够保证列举全。
	- C:正确性测试，符合预期功能，一般基本能保证。
	- D:根据设计文档，编写测试，也算是函数，方法的功能测试。
	- E:强制错误信息输入，（如：非法数据、异常流程、非业务允许输入等），异常输入测试，开发核心功能阶段，习惯预计后期统一检查，但是通常不会给予专门时间进行测试，有时判断收益不大，或假设输入合法而容易被忽略。
- 4.不可测代码的重构
通常只能是自己维护的代码，对于他人的代码，以及被他人修改过的代码后补测试会变得很难，所以应当尽量先写好测试。
- 5.降低编写单测的难度
	- a.待测方法的内部状态，控制参数等应当尽量少
	- b.不直接调用依赖外部，而是依赖外部结果传入，这样便于mock
