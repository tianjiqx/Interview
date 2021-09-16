#include<iostream>
using namespace std;
const int MaxSize=100;
template<class Type>
class element
{
    template<class Type>
	friend class sortlist;
private:
	Type key; //数据元素关键字
public:
	element():key(0){}  //构造函数
	Type GetKey(){return key;}  //取关键字
	void setKey(const Type k){key=k;}  //修改关键字
	element<Type> & operator =(element<Type>&x){key=x.key;return *this;}//赋值，重载运算符=
};

template<class Type>
class sortlist{
	// template<class Type> friend void QuickSort(sortlist<Type> &table,int low,int high);也可以
	friend void InsertionSort(sortlist<Type> &table);   //直接插入排序
	friend void BubbleSort(sortlist<Type> &table);      //冒泡排序
	friend void SelectSort(sortlist<Type> &table);      //简单选择排序
	friend void QuickSort(sortlist<Type> &table,int low,int high);  //快速排序
	template<class Type>
	friend class MaxHeap;    //堆
	friend void HeapSort(sortlist<Type> &table);    //堆排序
	friend void merge(sortlist<Type> &sourceTable,sortlist<Type> & mergedTable,
		   const int left,const int mid,const int right);  //归并
	friend void MergePass(sortlist<Type> &sourceTable,sortlist<Type> & mergedTable,
		   const int len);     //一趟归并
	friend void MergeSort(sortlist<Type> &table);  //两路归并
protected:
	element<Type> *Arr;  //存储数据元素的向量
	int CurrentSize;  //数据表中的元素个数
public:
	
	sortlist():CurrentSize(0){Arr=new element<Type>[MaxSize];}//构造函数
	~sortlist(){delete [] Arr;}//析构函数
	void Swap(element<Type>& x,element<Type> & y)  //交换x和y
	{element<Type> temp=x;x=y;y=temp;}
	void Insert(const Type k){Arr[CurrentSize].setKey(k); CurrentSize++;}  //插入k
	void Show()                  //显示数据元素
	{
		for(int i=0;i<CurrentSize;i++)
	   cout<<Arr[i].GetKey()<<"  ";
	}
};
//直接插入排序---使用监视哨改进
template <class Type>
void InsertionSort(sortlist<Type> &table)
{
	element<Type> temp;
	int j;
	for(int i=2;i<table.CurrentSize;i++)
	{
		temp=table.Arr[0];j=i;
		while(j>=0&&temp.GetKey()<table.Arr[j-1].GetKey())
		{
			table.Arr[j]=table.Arr[j-1];
			j--;
		}
		table.Arr[j]=table.Arr[0];
	}
}
//冒泡排序
template <class Type>
void BubbleSort(sortlist<Type> &table)
{
	int i=1;int finish=0;
	while(i<table.CurrentSize&&!finish)
	{
		finish=1;  //交换标志
		for(int j=0;j<table.CurrentSize-i;j++)
			if(table.Arr[j].GetKey()>table.Arr[j+1].GetKey())
			{
				Swap(table.Arr[j],table.Arr[j+1]);
				finish=0;    //已交换
			}
			i++;
	}
}
//简单选择排序
template<class Type>
void SelectSort(sortlist<Type> &table)
{
	int k;
	for(int i=0;i<table.CurrentSize-1;i++)
	{
		k=i;
		for(int j=i+1;j<table.CurrentSize;j++)
			if(table.Arr[j].GetKey()<table.Arr[k].GetKey())
				k=j;  //当前最小关键字的位置
		if(k!=i)
			Swap(table.Arr[i],table.Arr[k]);
	}
}
//快速排序
template<class Type>
void QuickSort(sortlist<Type> &table,int low,int high)
{
	static int count=1;
	int i=low,j=high;
	element<Type>temp=table.Arr[low];// 取区间第一个位置为基准位置
	if(i<j)
	{
		while(i<j){
			while(i<j&&temp.GetKey()<table.Arr[j].GetKey()) j--;
			if(i<j){table.Arr[i]=table.Arr[j];i++;}
			while(i<j&&temp.GetKey()>=table.Arr[i].GetKey())i++;
			if(i<j){table.Arr[j]=table.Arr[i];j--;}
		}
		table.Arr[i]=temp;  //基准元素归为
		cout<<"第"<<count<<"趟结果：";table.Show();cout<<endl;count++;
		QuickSort(table,low,i-1);  //左区间递归快速排序
		QuickSort(table,i+1,high);  //右区间递归快速排序
	}
}
//堆排序
template<class Type>
class MaxHeap{
public:
	MaxHeap(int maxSize);  //构造函数，建立空堆
	MaxHeap(Type *&a,int n);  //构造函数，以数组a[]的值建立堆
	~MaxHeap(){delete[] heapArr;
	}  //析构函数
	int Insert(Type &d);  //在堆中插入元素d
	Type DeleteTop();  //删除堆顶元素
	Type GetTop()const  //取得堆顶元素
	{return  heapArr[0];}
	int IsEmpty()   //判断堆是否为空
	{return heapCurrentSize==0;}
	int IsFull() const  //判断堆是否为满
	{return heapCurrentSize==heapMaxSize;}
	void SetEmpty(){heapCurrentSize=0;}//置堆为空
private:
    int heapMaxSize;  //堆中数据元素的最大数目
	Type *heapArr;  //存放堆中数据元素的数组
	int heapCurrentSize;  //堆中当前元素数目
	
	void FilterDown(int p); //向下调整使以结点p为根的子树成为堆
	void FilterUp(int p); //  向上调整使以结点p为根的子树成为堆
};

template<class Type>
int MaxHeap<Type>::Insert(Type &d)
{
	if(IsFull())
	{cout<<"堆已满"<<endl;return 0;}
	heapArr[heapCurrentSize]=&d; //在堆尾插入数据元素d
	FilterUp(heapCurrentSize);  //向上调整为堆
	heapCurrentSize++;
	return 1;
}

template<class Type>
MaxHeap<Type>::MaxHeap(int maxSize){
	//根据maxSize,建立空堆
	if(maxSize<=0)
	{
		cerr<<"堆的大小不能小于1"<<endl;
		exit(1);
	}
	heapMaxSize=maxSize;
	heapArr=new Type[heapMaxSize]; // 建立堆空间
	heapCurrentSize=0;
}
template<class Type>
MaxHeap<Type>::MaxHeap(Type *&a,int n){  //根据数组a[]中的数据，建立堆，n为数组的大小
	if(n<=0)
	{cerr<<"堆的大小不能小于1"<<endl;
		exit(1);
	}
	heapMaxSize=n;  //确定堆的最大空间
	//heapArr=new Type[heapMaxSize]; //创建堆空间
	//heapArr=a;     //?直接赋值？
	for(int i=0;i<n;i++)
		heapArr[i]=&a[i];
	heapCurrentSize=n;  //当前堆大小
	int i=(heapCurrentSize-2)/2;  //找最后一个分支结点的序号
	while(i>=0)
	{
		FilterDown(i);  //从开始到heapCurrentSize-1为止进行调整
		i--;
	}
}
template<class Type>
void MaxHeap<Type>::FilterDown(const int start)
{
	//向下调整从start开始到heapCurrentSize-1为止的子序列成为最大堆
	int i=start,j;
	element<Type> temp=table.Arr[i];
	j=2*i+1;    //j为i的左孩子
	while(j<table.CurrentSize){
		 if(table.Arr[j].GetKey()<table.Arr[j+1].GetKey())
		 j++;  //从左右孩子中选较大者
	 if(temp.GetKey()<=heapArr[j].GetKey()) break;
	 else {
	table.Arr[i]=table.Arr[j];
	 i=j;j=2*j+1;}
	}
	table.Arr[i]=temp;
}
template<class Type>
Type  MaxHeap<Type>::DeleteTop()
{
	if(IsEmpty())
	{cout<<"堆已空"<<endl;return NULL;}
	Type temp=heapArr[0]; 
	heapArr[0]=heapArr[heapCurrentSize-1];   //堆末元素移到堆顶
	heapCurrentSize--;
	FilterDown(0);//自顶向下调整堆
	return temp;
}
template<class Type>
void HeapSort(sortlist<Type> &table)
{
	int tablesize=table.CurrentSize;
	for(int i=(table.CurrentSize-2)/2;i>=0;i++)
		FilterDown(i);   //初始建堆
	for(i=table.CurrentSize-1;i>=1;i--)
	{
		Swap(table.Arr[0],table.Arr[i]);   //堆顶与最后一个元素交换
	table.CurrentSzie--;
	FilterDown(0);   //重建最大堆
	}
}
//归并排序
template<class Type>
void merge(sortlist<Type> &sourceTable,sortlist<Type> & mergedTable,
		   const int left,const int mid,const int right)
{
	int i=left,j=mid+1,k=left;  //指针初始化
	while(i<=mid&&j<=right)
		if(souceTable.Arr[i].GetKey()<=souceTable.Arr[j].GetKey())
		{mergedTable.Arr[k]=souceTable.Arr[i];i++;k++;}
		else{mergedTable.Arr[k]=souceTable.Arr[j];j++;k++;}
	if(i<=mid)
		for(int p=k,q=i;q<=mid;p++,q++)
			mergedTable.Arr[p]=souceTable.Arr[q];
	else
		for(int p=k,q=j;q<=right;p++,q++)
			mergedTable.Arr[p]=souceTable.Arr[q];
}
//一趟归并
template<class Type>
void MergePass(sortlist<Type> &sourceTable,sortlist<Type> & mergedTable,
		   const int len)
{
	int i=0;
	while(i+2*len<=CurrentSize-1)
	{
		merge(sourceTable,mergedTable,i,i+len-1,i+2*len-1);
		i+=2*len;
	}
	if(i+len<=CurrentSize-1)
		merge(sourceTable,mergedTable,i,i+len-1,CurrentSize-1);
	else
		for(int j=i;j<=CurrentSize-1;j++)
			mergedTable.Arr[j]=souceTable.Arr[j];
}
//两路归并
template<class Type>
void MergeSort(sortlist<Type> &table)
{
	sortlist<Type> &tempTable;
	int len=1;
	while(len<table.CurrentSize)
	{
		MergePass(table,tempTable,len); len*=2;
		MergePass(tempTable,table,len); len*=2;
	}
	delete[] tempTable;
}
void main()
{
	sortlist<int> SL;int dain;
	sortlist<char *> ssl;
	cout<<"请输入序列：\n";
	for(int i=0;i<8;i++)
	{  cin>>dain;
	SL.Insert(dain);
	}
	//SL.Show();
	 QuickSort<int>(SL,0,7);
	// system("pause");
}