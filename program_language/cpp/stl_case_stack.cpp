#include <iostream>
#include <stack>
#include <iterator>
#include <algorithm>
#include <numeric>


using namespace std;

void main() {

    stack<int> stack;

	stack.push(1);
	stack.push(2);
	stack.push(3);

	int size = stack.size();
	for (int i = 0; i < size; i++) {
		cout << stack.top() << " ";
		stack.pop();
	}
    // output: 3 2 1 
    stack.push(1);
	stack.push(2);
	stack.push(3);
	while(!stack.empty()){
		cout << stack.top() << " ";
		stack.pop();
	}
}
