# C++ 基础与数据结构-笔记

[TOC]



## 1. 基础知识

### 1.1 虚函数

虚函数： 用 `virtual` 修饰成员函数， 可以有实现，为了让基类指针可以调用子类的这个函数。

- 普通函数（非类），不能修饰
- 静态函数（static）, 不能修饰
- 构造函数，不能修饰
- 内联函数，具有多态能力，不能修饰
  - 原因，虚函数是在运行时进行完成多态能力，inline会让编译器展开函数到具体的代码位置
  - 因此，通过指针，表现多态能力时，编译器只有在运行时才知道具体类，无法再编译时inline
    - `Base b; b.who();`  ok
    - `Base *ptr = new Derived(); ptr->who();`  Error



**虚析构函数**

- 用于通过基类指针指向派生的对象，并通过该指针`delete`
- 多态基类的析构函数，都应该声明为`virtual `
- 调用顺序
  - 先调用子类析构函数，再调用基类



**纯虚函数**

- 在基类（虚基类）中不能对虚函数给出有意义的实现， 而把它声明为纯虚函数， 它的实现留给该
  基类的派生类
- `virtual int A() =0;`
- 具有纯虚函数的类是抽象类，只能被继承，通过子类创建对象
- 继承虚基类的子类必须实现所有的纯虚函数。



### 1.2 多态

多态实现方式

- 函数重载，静态多态（参数不同）
- 虚函数（基类调用子类的方法）



## 2. 数据结构

### 2.1 栈Stack

栈的性质：

- FILO 先进后出

操作：

- push 入栈
- pop 出栈
- top/peek 栈顶元素

实现方式：

- 链表
- 数组

```C++
// 链表实现
template<typename T>
class Stack {
public:
	Stack() {
		head = new LinkNode<T>();
	}

	~Stack() {
		while(head->next != NULL) {
			pop();
		}
		delete head;
	}
	// 头插入法
	void push(T v) {
		LinkNode<T> * newNode = new LinkNode<T>(v);
		newNode->next = head->next;
		head->next = newNode;
	}

	void pop() {
		if (head->next != NULL){
			LinkNode<T> * del =  head->next;
			head->next = del->next;
			delete del;
		}
	}

	T top() {
		if (head->next != NULL) {
			return head->next->value;
		}
        throw runtime_error("No elements in the stack");
		//return NULL;
	}

	int size() {
		int count = 0;
		LinkNode<T> * p = head->next;
		while(p != NULL) {
			p = p->next;
			count++;
		}
		return count;
	}
private:
	LinkNode<T> * head;
};

// 数组实现
template<typename T>
class Stack {
public:
	// 构造函数
	//Stack(int max_size) ;
	Stack(int max_size) {
			this->max_size = max_size;
			arr = new T[max_size];
			top = -1;
	}

	// 析构函数
	~Stack() {
		delete [] arr;
	}

	bool empty() {
		return top == -1;
	}

	bool push(T value) {
		bool ret = false;
		if (top < max_size) {
			arr[++top] = value;
			ret = true;
		}
		return ret;
	}

	void pop() {
		if (top>=0) {
			top--;
		}
	}

	T peek() {
        if (top<0) {
            throw runtime_error("No elements in the stack");
        }
		return arr[top];
	}

	int size() {
		return top + 1;
	}

	int capcity() {
		return max_size;
	}

private:
	int top;
	T * arr; // T 类型的数组
	int max_size;

};
```

### 2.2 队列Queue





### 2.3 二叉树Binary tree

```C++
class TreeNode {
public:
	int value;
	TreeNode * left;
	TreeNode * right;
};
```

性质：

- 非空二叉树第 i 层最多 `2^(i-1)` 个节点
- 深度为k的二叉树，最多 `2^k -1` 个节点，最少k个节点
  - `2^0 + 2^1 + ... 2^(k-1)=2^k - 1`
- n个节点的完全二叉树深度 `[log2^n]下取整+1`  或者 `[log2^(n+1)]上取整`
  - `2^(h-1) -1 < n <= 2^h-1`
- n个节点的二叉树的分支数为`n-1`
  - 除根节点外，其他节点有且仅有一个父节点
- 度为0的节点数为`n0`, 度为2的节点未`n2`，则 `n0 = n2 + 1`
  - 总节点数`n = n0 + n1 + n2` 总分支数`n-1 = 1*n1 + 2*n2` 联立即得



完全二叉树

- 高度为h的，1-(h-1)各层都达到最大个数，h层从左到右连续分布

满二叉树

- 达到高度，所能有的最大数目的节点



操作：

- 遍历
  - 前序preOrder
    - root->left->right
  - 中序midOrder
    - left->root->right
  - 后序postOrder
    - left->right->root
  - 层次LevelOrder
    - 借助队列，队列头元素出队时，左右孩子入队



存储结构：

- 数组
  - 以层次遍历的顺序，从0到n-1编号存储，存储空位置
    - 位置在i的孩子节点分别是`2i+1` 和 `2i+2`
  - 缺点
    - 空间利用率低，最坏情况，深度为k节点为k的二叉树，却需要2^k - 1空间
- 链表



递归遍历：

在树的深度不高时，对二叉树的遍历可以使用二叉树本身的递归定义结构，递归处理。

在数据库中的各种计划、表达式的TreeNode，也通常直接通过递归的方式进行处理。

```C++
// 前序
void preOrder(TreeNode* root) {
	if (root == NULL) return;
	cout<<root->value<<" ";
	if (root->left != NULL) {
		preOrder(root->left);
	}
	if (root->right != NULL) {
		preOrder(root->right);
	}
}
```

出于内存使用，时间常系数，以及栈空间的大小考虑，使用迭代的非递归算法，在处理有极高深度的二叉树时，有明显的优势。

**尾递归**

- 对递归使用的栈空间进行优化
- 把变化的参数传递给递归函数，避免需要回退栈
- 尾递归优化，需要编译器支持

非递归优化：

为了避免使用递归函数的栈，通常就需要引入栈的数据结构，来手动模拟一个递归的调用过程。

非递归的优化思想，根据递归过程，分解递归的逻辑，用各种手段如循环，数据结构（stack）完成分解的逻辑，最终完成相同作用的算法逻辑。

- 先序遍历
  - 递归的遍历过程，是从沿着最左侧的通路，自顶向下的访问各节点，然后自底向想的访问对应的右子树
    - 访问右子树的过程依然是，重复该过程
  - 所以，先序遍历，可以分成2个阶段
    - 循环访问左子树
    - 自底向上的访问右子树
- 中序遍历
  - 递归的遍历过程，是从沿着最左侧的通路，自底向上的访问节点，右子树
  - 与前序类似，只是全部都需要自底向上，因此把中间节点入栈，右子树的访问通过中间节点取右子树来处理
- 后序遍历
  - 后序需要用到一点技巧
    - 前序遍历是root-> left-> right ，而后序遍历是left->right->root
    - 如果前序遍历的变形是root-> right-> left，那么正好是后序遍历的逆序
    - 所以可以采用双栈的方式，先前序遍历，之后在逆序输出

```C++
// 前序， 循环访问左子树，并记录右孩子节点
void vistAllLeft(TreeNode* root, stack<TreeNode*> & stack) {
	while(root != NULL) {
		// visit root
		cout << root->value <<" ";
		// 为了能够自底向上的访问右子树，自顶向下的存储right,出栈式在访问
		if (root->right != NULL){
			stack.push(root->right);
		}
		root = root->left;
	}
}
void preOrder(TreeNode* root) {
	if (root == NULL) return;
	stack<TreeNode*> stack;
	while(true) {
		// 以该节点开始，访问全部左子树
		// 右节点全部入栈
		visitAllLeft(root, stack);
		// 栈空表示全部访问完毕
		if (stack.empty()) break;
		// 取最底层的右孩子作为新的访问起始位置
		root = stack.top();
		stack.pop();
	}
}
// 中序
void midVisitAllLeft(TreeNode* root, stack<TreeNode*> & stack) {
	while (root != NULL) {
		// root节点入栈，
		stack.push(root);
		root = root->left;
	}
}
void midOrder(TreeNode* root) {
	if (root == NULL) return;
	stack<TreeNode*> stack;
	while(true) {
		// 以该节点开始，访问全部左子树
		// 右节点全部入栈
		midVisitAllLeft(root, stack);
		// 栈空表示全部访问完毕
		if (stack.empty()) break;

		root = stack.top();
		cout<< root->value << " ";
		// 访问右子树
		// 取最底层的右孩子作为新的访问起始位置
		root = root->right;
		stack.pop();
	}
}

// 后序
void visitAllRight(TreeNode* root, stack<TreeNode*> & stack, deque<int> & stack2) {
	while(root != NULL) {
		// visit root
		//cout << root->value <<" ";
		stack2.push_back(root->value);
		// 为了能够自底向上的访问左子树，自顶向下的存储left,出栈式在访问
		if (root->left != NULL){
			stack.push(root->left);
		}
		root = root->right;
	}
}
void postOrder(TreeNode * root) {
	if (root == NULL)
		return;
	stack<TreeNode*> stack;
	deque<int> stack2;

	while (true) {
		// 以该节点开始，访问全部右子树
		// 左节点全部入栈
		visitAllRight(root, stack, stack2);
		// 栈空表示全部访问完毕
		if (stack.empty())
			break;
		// 取最底层的左孩子作为新的访问起始位置
		root = stack.top();
		stack.pop();
	}
	while(!stack2.empty()){
		cout<<stack2.back()<<" ";
		stack2.pop_back();
	}
}
```

还有不用栈的方式，利用线索二叉树的思想，时间换空间。

```C++
/*
leetcode: 二叉树层序遍历
给你一个二叉树，请你返回其按 层序遍历 得到的节点值。 （即逐层地，从左到右访问所有节点）。
    3
   / \
  9  20
    /  \
   15   7
输出
[
  [3],
  [9,20],
  [15,7]
]
*/  
vector<vector<int>> levelOrder(TreeNode* root) {
	deque<TreeNode*> queue;
	vector<vector<int>> ret;

	if (root == NULL) {
		return ret;
	}
	// 特殊节点，占位，标记当前层开始
	// 从队列出队时，在队尾中再次进入
	TreeNode * mark = new TreeNode();
	queue.push_back(mark);
	queue.push_back(root);
	int depth = -1;
	while (queue.size() > 1){
		TreeNode* node = queue.front();
		queue.pop_front();
		// 标记节点再次入队, 并弹出下一个节点（一定存在，queue.size() > 1）
		if (node == mark) {
			queue.push_back(node);
			node = queue.front();
			queue.pop_front();
			// 开启新行
			depth++;
			vector<int> v;
			ret.push_back(v);
		}
		ret[depth].push_back(node->value);

		if (node->left != NULL) {
			queue.push_back(node->left);
		}
		if (node->right != NULL) {
			queue.push_back(node->right);
		}
	}
	return ret;
}
// 其他解法：内层增加循环，记录当前队列大小，一次性全取遍历
```

打印SQL树形的执行计划（explain），可以采用前序递归的方式，并记录深度，根据深度设置前置的空格长度。



## 3. STL

C++ STL（标准模板库）实现的常用数据结构。

```C++
#include <iostream>
#include <vector>
#include <deque>
#include <stack>
#include <list>
#include <map>
#include <iterator>
#include <algorithm>
#include <numeric>
#include <string>
#include <cstring>
using namespace std;
```





## REF

- [C++ 面试基础总结](http://www.bookstack.cn/books/cpp-interview  )
- [为何某些公司不允许使用 C++ STL？](https://www.zhihu.com/question/20201972)
  - 环境依赖
  - [为何某些公司不允许使用 C++ STL？ - 陈甫鸼的回答 - 知乎](https://www.zhihu.com/question/20201972/answer/23454845)
    - 二进制边界混乱，编译问题
    - 异常使用，性能，代码膨胀
    - 与C兼容性，C/C++混用项目
  - [为何某些公司不允许使用 C++ STL？ - 姚冬的回答 - 知乎](https://www.zhihu.com/question/20201972/answer/41324520)
    - 代码膨胀问题
    - 内存使用效率
    - 容器deep copy性能
    - 隐式类型转换
- [STL教程：C++ STL快速入门（非常详细）](http://c.biancheng.net/stl/)
- [什么是尾递归？ - 罗宸的回答 - 知乎](https://www.zhihu.com/question/20761771/answer/20672305)
- 《数据结构》-邓俊辉
- [C++ string类（C++字符串）完全攻略](http://c.biancheng.net/view/400.html)

