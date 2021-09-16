#include<iostream>
using namespace std;
const int MaxSize=100;
template<class Type>
class element
{
    template<class Type>
	friend class sortlist;
private:
	Type key; //����Ԫ�عؼ���
public:
	element():key(0){}  //���캯��
	Type GetKey(){return key;}  //ȡ�ؼ���
	void setKey(const Type k){key=k;}  //�޸Ĺؼ���
	element<Type> & operator =(element<Type>&x){key=x.key;return *this;}//��ֵ�����������=
};

template<class Type>
class sortlist{
	// template<class Type> friend void QuickSort(sortlist<Type> &table,int low,int high);Ҳ����
	friend void InsertionSort(sortlist<Type> &table);   //ֱ�Ӳ�������
	friend void BubbleSort(sortlist<Type> &table);      //ð������
	friend void SelectSort(sortlist<Type> &table);      //��ѡ������
	friend void QuickSort(sortlist<Type> &table,int low,int high);  //��������
	template<class Type>
	friend class MaxHeap;    //��
	friend void HeapSort(sortlist<Type> &table);    //������
	friend void merge(sortlist<Type> &sourceTable,sortlist<Type> & mergedTable,
		   const int left,const int mid,const int right);  //�鲢
	friend void MergePass(sortlist<Type> &sourceTable,sortlist<Type> & mergedTable,
		   const int len);     //һ�˹鲢
	friend void MergeSort(sortlist<Type> &table);  //��·�鲢
protected:
	element<Type> *Arr;  //�洢����Ԫ�ص�����
	int CurrentSize;  //���ݱ��е�Ԫ�ظ���
public:
	
	sortlist():CurrentSize(0){Arr=new element<Type>[MaxSize];}//���캯��
	~sortlist(){delete [] Arr;}//��������
	void Swap(element<Type>& x,element<Type> & y)  //����x��y
	{element<Type> temp=x;x=y;y=temp;}
	void Insert(const Type k){Arr[CurrentSize].setKey(k); CurrentSize++;}  //����k
	void Show()                  //��ʾ����Ԫ��
	{
		for(int i=0;i<CurrentSize;i++)
	   cout<<Arr[i].GetKey()<<"  ";
	}
};
//ֱ�Ӳ�������---ʹ�ü����ڸĽ�
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
//ð������
template <class Type>
void BubbleSort(sortlist<Type> &table)
{
	int i=1;int finish=0;
	while(i<table.CurrentSize&&!finish)
	{
		finish=1;  //������־
		for(int j=0;j<table.CurrentSize-i;j++)
			if(table.Arr[j].GetKey()>table.Arr[j+1].GetKey())
			{
				Swap(table.Arr[j],table.Arr[j+1]);
				finish=0;    //�ѽ���
			}
			i++;
	}
}
//��ѡ������
template<class Type>
void SelectSort(sortlist<Type> &table)
{
	int k;
	for(int i=0;i<table.CurrentSize-1;i++)
	{
		k=i;
		for(int j=i+1;j<table.CurrentSize;j++)
			if(table.Arr[j].GetKey()<table.Arr[k].GetKey())
				k=j;  //��ǰ��С�ؼ��ֵ�λ��
		if(k!=i)
			Swap(table.Arr[i],table.Arr[k]);
	}
}
//��������
template<class Type>
void QuickSort(sortlist<Type> &table,int low,int high)
{
	static int count=1;
	int i=low,j=high;
	element<Type>temp=table.Arr[low];// ȡ�����һ��λ��Ϊ��׼λ��
	if(i<j)
	{
		while(i<j){
			while(i<j&&temp.GetKey()<table.Arr[j].GetKey()) j--;
			if(i<j){table.Arr[i]=table.Arr[j];i++;}
			while(i<j&&temp.GetKey()>=table.Arr[i].GetKey())i++;
			if(i<j){table.Arr[j]=table.Arr[i];j--;}
		}
		table.Arr[i]=temp;  //��׼Ԫ�ع�Ϊ
		cout<<"��"<<count<<"�˽����";table.Show();cout<<endl;count++;
		QuickSort(table,low,i-1);  //������ݹ��������
		QuickSort(table,i+1,high);  //������ݹ��������
	}
}
//������
template<class Type>
class MaxHeap{
public:
	MaxHeap(int maxSize);  //���캯���������ն�
	MaxHeap(Type *&a,int n);  //���캯����������a[]��ֵ������
	~MaxHeap(){delete[] heapArr;
	}  //��������
	int Insert(Type &d);  //�ڶ��в���Ԫ��d
	Type DeleteTop();  //ɾ���Ѷ�Ԫ��
	Type GetTop()const  //ȡ�öѶ�Ԫ��
	{return  heapArr[0];}
	int IsEmpty()   //�ж϶��Ƿ�Ϊ��
	{return heapCurrentSize==0;}
	int IsFull() const  //�ж϶��Ƿ�Ϊ��
	{return heapCurrentSize==heapMaxSize;}
	void SetEmpty(){heapCurrentSize=0;}//�ö�Ϊ��
private:
    int heapMaxSize;  //��������Ԫ�ص������Ŀ
	Type *heapArr;  //��Ŷ�������Ԫ�ص�����
	int heapCurrentSize;  //���е�ǰԪ����Ŀ
	
	void FilterDown(int p); //���µ���ʹ�Խ��pΪ����������Ϊ��
	void FilterUp(int p); //  ���ϵ���ʹ�Խ��pΪ����������Ϊ��
};

template<class Type>
int MaxHeap<Type>::Insert(Type &d)
{
	if(IsFull())
	{cout<<"������"<<endl;return 0;}
	heapArr[heapCurrentSize]=&d; //�ڶ�β��������Ԫ��d
	FilterUp(heapCurrentSize);  //���ϵ���Ϊ��
	heapCurrentSize++;
	return 1;
}

template<class Type>
MaxHeap<Type>::MaxHeap(int maxSize){
	//����maxSize,�����ն�
	if(maxSize<=0)
	{
		cerr<<"�ѵĴ�С����С��1"<<endl;
		exit(1);
	}
	heapMaxSize=maxSize;
	heapArr=new Type[heapMaxSize]; // �����ѿռ�
	heapCurrentSize=0;
}
template<class Type>
MaxHeap<Type>::MaxHeap(Type *&a,int n){  //��������a[]�е����ݣ������ѣ�nΪ����Ĵ�С
	if(n<=0)
	{cerr<<"�ѵĴ�С����С��1"<<endl;
		exit(1);
	}
	heapMaxSize=n;  //ȷ���ѵ����ռ�
	//heapArr=new Type[heapMaxSize]; //�����ѿռ�
	//heapArr=a;     //?ֱ�Ӹ�ֵ��
	for(int i=0;i<n;i++)
		heapArr[i]=&a[i];
	heapCurrentSize=n;  //��ǰ�Ѵ�С
	int i=(heapCurrentSize-2)/2;  //�����һ����֧�������
	while(i>=0)
	{
		FilterDown(i);  //�ӿ�ʼ��heapCurrentSize-1Ϊֹ���е���
		i--;
	}
}
template<class Type>
void MaxHeap<Type>::FilterDown(const int start)
{
	//���µ�����start��ʼ��heapCurrentSize-1Ϊֹ�������г�Ϊ����
	int i=start,j;
	element<Type> temp=table.Arr[i];
	j=2*i+1;    //jΪi������
	while(j<table.CurrentSize){
		 if(table.Arr[j].GetKey()<table.Arr[j+1].GetKey())
		 j++;  //�����Һ�����ѡ�ϴ���
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
	{cout<<"���ѿ�"<<endl;return NULL;}
	Type temp=heapArr[0]; 
	heapArr[0]=heapArr[heapCurrentSize-1];   //��ĩԪ���Ƶ��Ѷ�
	heapCurrentSize--;
	FilterDown(0);//�Զ����µ�����
	return temp;
}
template<class Type>
void HeapSort(sortlist<Type> &table)
{
	int tablesize=table.CurrentSize;
	for(int i=(table.CurrentSize-2)/2;i>=0;i++)
		FilterDown(i);   //��ʼ����
	for(i=table.CurrentSize-1;i>=1;i--)
	{
		Swap(table.Arr[0],table.Arr[i]);   //�Ѷ������һ��Ԫ�ؽ���
	table.CurrentSzie--;
	FilterDown(0);   //�ؽ�����
	}
}
//�鲢����
template<class Type>
void merge(sortlist<Type> &sourceTable,sortlist<Type> & mergedTable,
		   const int left,const int mid,const int right)
{
	int i=left,j=mid+1,k=left;  //ָ���ʼ��
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
//һ�˹鲢
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
//��·�鲢
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
	cout<<"���������У�\n";
	for(int i=0;i<8;i++)
	{  cin>>dain;
	SL.Insert(dain);
	}
	//SL.Show();
	 QuickSort<int>(SL,0,7);
	// system("pause");
}