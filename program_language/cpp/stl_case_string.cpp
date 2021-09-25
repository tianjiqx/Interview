#include<iostream>
#include<string>
#include<cstring>
#include<algorithm>
using namespace std;

// 从字符串中获取值
int getNextVal(const char * str, int * start, char spliter) {
	int ret = 0;
	int pos = *start;
	while (*(str+pos) != '\0' && *(str+pos) != spliter) {
		ret = ret * 10 +  (*(str+pos)) - '0';
		pos++;
	}
	// remove spliter char
	if (*(str+pos) != '\0' && *(str+pos) == spliter) pos++;
	*start = pos;
	return ret;
}

int main() {

    string s1 = "abcd";

    string s2(s1);

    string s3(10, 'a'); // aaaaa.... a  (10)

    string s4;
    cin >> s4; //wait space , aa aaa => s4="aa"
    string s5;
    getline(cin, s5); //wait enter, aa aaa \n  -> s5='aa aaa'
    string s6;
    getline(cin, s6, 'a'); //end with 'a', dffgh f \n ff ba -> s6='dffgh f \n ff b'

    //size
    cout << s1.size() << " " << s1.length() << endl;


    //read
    cout << s1[0];
    //concate
    s4 = s1 + s2;
    //compare
    if (s1 == s2) {
        cout << "s1==s2\n";
    } else {
        cout << "s1!=s2\n";
    }


    //insert
    s2.insert(s2.begin(), 'b');

    //substr
    int strlen = 4;
    s1 = s1.substr(1, strlen); // retrun s1[1:1+strlen]
    s2 = s2.substr(1) ; // retrun s2[1:]

    //find
    s1.find("abc");  // return first index,or -1
    //compare
    s1.compare("abc"); // =  0, > 1, < -1
    //reverse
    reverse(s1.begin(), s1.end()); //



    string s9 = "123,23,44";
    s9 += "," + to_string(55);

	const char * str = s9.c_str();
	int pos = 0;
	while (*(str + pos) != '\0') {
		cout<< getNextVal(str, &pos, ',')<<" ";
	}
    // output 123 23 44 55


    // cstring function

	char str1[10] = "abc";
	char str2[10] = "def";
	char str3[10];

	// copy

	strcpy(str3, str1);
	cout<<"str3:" << str3<<endl; //abc

	cout<<"str1:" << str1<<endl; //abc
	strcat(str1,str2);
	cout<<"after strcat str1:" << str1<<endl; // abcdef

	// length

	cout<< "cur len: "<< strlen(str1)<<endl; // 6

    return 0;
}



