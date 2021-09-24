#include <iostream>
#include <deque>
#include <iterator>
#include <algorithm>
#include <numeric>

using namespace std;

void main() {
    
    //deque<int> queue;
    deque<int> queue{1,2,3};

	queue.push_front(0);
	queue.push_back(4);

	int size = queue.size();
	cout<< size << endl;

	for (int i=0; i< size; i ++) {
		cout<< queue[i]<< " "; // or cout<< queue.at(i)<< " ";
	}
	cout<<endl;

	while(!queue.empty()) {
		cout<< queue.front() << " ";
		queue.pop_front();
	}
	cout << endl;
	// output: 0 1 2 3 4
	queue.push_back(0);
	queue.push_back(1);
	queue.push_back(2);
	queue.push_back(3);
	queue.push_back(4);
	// reverse
	while(!queue.empty()) {
		cout<< queue.back() << " ";
		queue.pop_back();
	}
	// output: 4 3 2 1 0

}