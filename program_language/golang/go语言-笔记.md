# go语言-笔记

[TOC]

## 1. 基本语法

### 1.1 结构体

结构体是一种聚合的数据类型，是由零个或多个任意类型的值聚合成的实体  。

```go
type Employee struct {
ID int
Name string
Address string
DoB time.Time
Position string
Salary int
ManagerID int
}
// 访问
// 1. 直接点访问成员
dilbert.Salary -= 5000
// 2. 指针访问
position := &dilbert.Position
*position = "Senior " + *position

// 指针访问结构体
var employeeOfTheMonth *Employee = &dilbert
employeeOfTheMonth.Position += " (proactive team player)"

// 字面值
type Point struct{ X, Y int }
p := Point{1, 2}

```



### 1.2 函数

```go
// 函数声明包括函数名、形式参数列表、返回值列表（可省略） 以及函数体
func name(parameter-list) (result-list) {
body
}

func hypot(x, y float64) float64 {
return math.Sqrt(x*x + y*y)
} f
mt.Println(hypot(3,4)) // "5"


// 形参类型简写
func f(i, j, k int, s, t string) { /* ... */ }
// <=> 等价
func f(i int, j int, k int, s string, t string) { /* ... */ }

```

defer函数：defer语句中的函数会在return语句更新返回值变量后再执行。



### 1.3 方法

在函数声明时，在其名字之前放上一个变量，即是一个方法 。

这个附加的参数会将该函数附加到这种类型上，即相当于为这种类型定义了一个独占的方法。  

```go
type Point struct{ X, Y float64 }
// traditional function
func Distance(p, q Point) float64 {
return math.Hypot(q.X-p.X, q.Y-p.Y)
} /
/ same thing, but as a method of the Point type
func (p Point) Distance(q Point) float64 {
return math.Hypot(q.X-p.X, q.Y-p.Y)
}

//调用
p := Point{1, 2}
q := Point{4, 6}
fmt.Println(Distance(p, q)) // "5", function call
fmt.Println(p.Distance(q)) // "5", method call

```

参数p，方法的接收器(receiver)  。

接收器使用指针，避免大对象值拷贝。

```go
func (p *Point) ScaleBy(factor float64) {
p.X *= factor
p.Y *= factor
}
```

并且，接收器只有这两种：类型(Point)和指向他们的指针(*Point)  

方法的调用：

```go
r := &Point{1, 2}
r.ScaleBy(2)

p := Point{1, 2}
pptr := &p
pptr.ScaleBy(2)

p := Point{1, 2}
(&p).ScaleBy(2)
```





### 1.4 接口

对其他类型行为的抽象和概括。接口类型不会和特定的实现细节绑定。

隐式实现——不用想java类，对给定具体的类型时定义，定义需要实现的接口。

```go
// Writer is the interface that wraps the basic Write method.
type Writer interface {
// Write writes len(p) bytes from p to the underlying data stream.
// It returns the number of bytes written from p (0 <= n <= len(p))
// and any error encountered that caused the write to stop early.
// Write must return a non-nil error if it returns n < len(p).
// Write must not modify the slice data, even temporarily.
//
// Implementations must not retain p.
Write(p []byte) (n int, err error)
}

// ByteCounter实现 writer接口
func (c *ByteCounter) Write(p []byte) (int, error) {
*c += ByteCounter(len(p)) // convert int to ByteCounter
return len(p), nil
}
```

接口类型，描述了一系列方法的集合，一个实现了这些方法的具体类型是这个接口类型的
实例。  

```go
type Reader interface {
Read(p []byte) (n int, err error)
} t
ype Closer interface {
Close() error
}

// 接口内嵌
type ReadWriter interface {
Reader
Writer
}
```





### 1.5 模块管理

Go 在做依赖管理时会创建两个文件，`go.mod` 和 `go.sum`

- `go.mod` 提供了依赖版本的全部信息
  - `go env -w GO111MODULE=on`   #打开 Go modules
    - `go env | grep MODULE` 检查模块功能是否开启
    - 参数
      - `auto`  项目中包含go.mod 时启用
      - `on` 完全开启
      - `off` 完全禁用
  - 文件内容结构
    - module：用于定义当前项目的模块路径。
    - go：用于设置预期的 Go 版本。
    - require：用于设置一个特定的模块版本。
    - exclude：用于从使用中排除一个特定的模块版本。
    - replace：用于将一个模块版本替换为另外一个模块版本
      - 替换到国内镜像地址

demo(cockroachdb/pebble) go.mod

```
module github.com/cockroachdb/pebble

require (
	github.com/DataDog/zstd v1.4.5
	github.com/cespare/xxhash/v2 v2.1.1
	github.com/cockroachdb/errors v1.8.1
	github.com/cockroachdb/redact v1.0.8
	github.com/codahale/hdrhistogram v0.0.0-20161010025455-3a0bb77429bd
	github.com/ghemawat/stream v0.0.0-20171120220530-696b145b53b9
	github.com/golang/snappy v0.0.3
	github.com/klauspost/compress v1.11.7
	github.com/kr/pretty v0.1.0
	github.com/pmezard/go-difflib v1.0.0
	github.com/spf13/cobra v0.0.5
	github.com/stretchr/testify v1.6.1
	golang.org/x/exp v0.0.0-20200513190911-00229845015e
	golang.org/x/sync v0.0.0-20201020160332-67f06af15bc9
	golang.org/x/sys v0.0.0-20210909193231-528a39cd75f3
)

go 1.13
```

- `go.sum` 提供防篡改的保障
  - `<module> <version> <hash>` 
    - module是依赖的路径
    - version是依赖的版本号
    - hash是以`h1:`开头的字符串, sha256
  - 作用
    - 提供分布式环境下的包管理依赖内容校验
    - 作为 transparent log 来加强安全性
      - 记录历史的checksum



用法：

- 创建go.mod文件
  - `go mod init demo`  demo 是模块名，一般是当前目录名，go.mod文件也创建在当前目录下
- 构建项目` go build`，生成 go.sum 文件
  - 创建go.sum、demo文件
- 编辑go.mod 
  - `go mod edit [editing flags] [go.mod]`
  - 也可以手动修改go.mod 文件，然后`go mod edit -fmt` 格式化
  - `go mod tidy` 命令，只需要修改代码，会增加缺少的module，删除无用的module
- 更新依赖版本
  - `go list -m -versions github.com/gin-gonic/gin` 查看gin所有历史版本
  - `go mod edit -require="github.com/gin-gonic/gin@v1.3.0"` 更新版本
  - `go tidy` #更新现有依赖
  - `go mod vendor` 将依赖的代码下载到vendor目录
- 查看项目依赖的包
  - `go list -m all`



注意事项：

- 无法外部模块 import  ，另一个模块的内部模块例如`"github.com/cockroachdb/pebble/internal/base"` 带有internal， 只能import `github.com/cockroachdb/pebble` 需要使用内部的常量，错误码，需要间接使用`pebble` 中定义的常量，或者变量，例如`ErrNotFound = base.ErrNotFound`  ，使用方式`pebble.ErrNotFound`



REF：

- [GO 依赖管理工具go Modules（官方推荐）](https://segmentfault.com/a/1190000020543746)
- [go module 使用](https://segmentfault.com/a/1190000022868683)
- [go.sum](https://studygolang.com/articles/25658)

### 

### 1.6 控制流程

#### 1.6.1 条件

```go
if true {
} else {
}
// switch
switch var1 {
    case val1:
        ...
    case val2,val3:
        ...
    default:
        ...
}
// type switch
switch x.(type){
    case nil:
       statement(s);      
    case int:
       statement(s); 
}
```

#### 1.6.2 循环

```go
// for
for init; condition; post { }

// while
for true {
    // dead loop
}

// for range, 迭代slice、map、数组、字符串
for key, value := range oldMap {
    newMap[key] = value
}
for i, s := range strings {
    fmt.Println(i, s)
}
```



## 2. 并发

### 2.1 通道channels

创建

`ch := make(chan int) // ch has type 'chan int'  `

使用

```go
// 发送
ch <- x // a send statement
// 接收
x = <-ch // a receive expression in an assignment statement
<-ch // a receive statement; result is discarded
// 关闭
// 发送禁止，产生panic
// 但是可以继续接受已关闭通道的值，通道中无值时，产生零值
close(ch)
```



无缓冲通道的阻塞：

- 通道中无数据，但执行读通道

- 通道中无数据，向通道写数据，但无协程读取

有缓冲通道的阻塞：

- 通道的缓存无数据，但执行读通道
- 通道的缓存已经占满，向通道写数据，但无协程读



### 2.2 基于select的多路复用

多路复用，即在监听会阻塞的多个通道中（可以是接收，发送），等待任意通道准备好，执行其处理逻辑。

- select 会等待有能够执行的case时，执行
  - 特殊select {} 会永远等待

- 多个case同时就绪时，select会随机地选择一个执行
- default来设置当其它的操作都不能够马上被处理时程序需要执行哪些逻辑  

```go
select {
    case <-ch1: // 从通道ch1接受值，抛弃结果
    // ...
    case x := <-ch2: //从通道ch2接受值，x引用结果
    // ...use x...
    case ch3 <- y:
    // ...
    default:
    // ...
}
```



## 3. 标准库

### 3.1 Strings

go 中使用 [utf-8 编码](https://github.com/tianjiqx/notes/blob/master/distributed_system/%E5%BA%8F%E5%88%97%E5%8C%96%E4%B8%8E%E5%8F%8D%E5%BA%8F%E5%88%97%E5%8C%96SerDe.md)存储字符串字面量（literal）, 通过 Slice 直接使用index 是操作字节的位置。

```go
s := "abcdefghijklmnop" 
//cdefghi
fmt.Println(s[2:9])  

s = "维基百科:关于中文维基百科" 
// 百科:关于中文
fmt.Println(string([]rune(s)[2:9])) 

/*
U+65E5 '日' starts at byte position 0
U+672C '本' starts at byte position 3
U+8A9E '語' starts at byte position 6
*/
const nihongo = "日本語"
for index, runeValue := range nihongo {
  fmt.Printf("%#U starts at byte position %d\n", runeValue, index)
}


	// These names of these constants are chosen to give nice alignment in the
	// table below. The first nibble is an index into acceptRanges or F for
	// special one-byte cases. The second nibble is the Rune length or the
	// Status for the special one-byte case.
	xx = 0xF1 // invalid: size 1
	as = 0xF0 // ASCII: size 1
	s1 = 0x02 // accept 0, size 2
	s2 = 0x13 // accept 1, size 3
	s3 = 0x03 // accept 0, size 3
	s4 = 0x23 // accept 2, size 3
	s5 = 0x34 // accept 3, size 4
	s6 = 0x04 // accept 0, size 4
	s7 = 0x44 // accept 4, size 4


// uft-8 库
// RuneStart reports whether the byte could be the first byte of an encoded,
// possibly invalid rune. Second and subsequent bytes always have the top two
// bits set to 10.
// 0xC0 => 1100 0000
func RuneStart(b byte) bool { return b&0xC0 != 0x80 }
// DecodeRune unpacks the first UTF-8 encoding in p and returns the rune and
// its width in bytes. If p is empty it returns (RuneError, 0). Otherwise, if
// the encoding is invalid, it returns (RuneError, 1). Both are impossible
// results for correct, non-empty UTF-8.
//
// An encoding is invalid if it is incorrect UTF-8, encodes a rune that is
// out of range, or is not the shortest possible UTF-8 encoding for the
// value. No other validation is performed.
func DecodeRune(p []byte) (r rune, size int) {
	n := len(p)
	if n < 1 {
		return RuneError, 0
	}
	p0 := p[0]
	x := first[p0]
	if x >= as {
		// The following code simulates an additional check for x == xx and
		// handling the ASCII and invalid cases accordingly. This mask-and-or
		// approach prevents an additional branch.
		mask := rune(x) << 31 >> 31 // Create 0x0000 or 0xFFFF.
		return rune(p[0])&^mask | RuneError&mask, 1
	}
	sz := int(x & 7)
	accept := acceptRanges[x>>4]
	if n < sz {
		return RuneError, 1
	}
	b1 := p[1]
	if b1 < accept.lo || accept.hi < b1 {
		return RuneError, 1
	}
	if sz <= 2 { // <= instead of == to help the compiler eliminate some bounds checks
		return rune(p0&mask2)<<6 | rune(b1&maskx), 2
	}
	b2 := p[2]
	if b2 < locb || hicb < b2 {
		return RuneError, 1
	}
	if sz <= 3 {
		return rune(p0&mask3)<<12 | rune(b1&maskx)<<6 | rune(b2&maskx), 3
	}
	b3 := p[3]
	if b3 < locb || hicb < b3 {
		return RuneError, 1
	}
	return rune(p0&mask4)<<18 | rune(b1&maskx)<<12 | rune(b2&maskx)<<6 | rune(b3&maskx), 4
}
```

Runne： 即unicode的“Code point” 表示utf-8单个字符，int32。





#### REF

- [Strings, bytes, runes and characters in Go](https://go.dev/blog/strings)
- [utf8string](https://pkg.go.dev/golang.org/x/exp/utf8string) 包 utf8string 提供了一种按字符而不是按字节索引字符串的有效方法。
- [utf8](https://go.dev/pkg/unicode/utf8/) 包 utf8 实现函数和常量以支持以 UTF-8 编码的文本。它包括在符文和 UTF-8 字节序列之间进行转换的函数。



## 附：常用命令



- `go run  xxx.go`  运行单个，带main的文件

- `go test -v  xxx_test.go` 测试文件，所有该文件下的单元测试
- `go test -v -test.run TestXXX` 运行单个单元测试



## REF

- Go语言圣经The Go Programming Language (Alan A.A. Donovan)
- [Go 指南](http://tour.studygolang.com/) head first go
- [Go语言标准库](http://books.studygolang.com/The-Golang-Standard-Library-by-Example/)
- [官方 effective go](https://golang.org/doc/effective_go)
- [golang 中 sync.Mutex 和 sync.RWMutex](https://www.jianshu.com/p/679041bdaa39)
  - 阻塞的写锁，优先级高于后续的读锁
- [Go之定时器的使用](https://cloud.tencent.com/developer/article/1640646)
- [Go (Golang) 编码指南](https://wiki.crdb.io/wiki/spaces/CRDB/pages/181371303/Go+Golang+coding+guidelines)  CRDB的go编码最佳实践
- [分析 Go 程序](https://go.dev/blog/pprof)  go tool pprof

