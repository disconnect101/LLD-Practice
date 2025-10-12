#include<iostream>

using namespace std;

int main(int argc, char const *argv[]) {
	cout << argc;

	int i;
	for (i=0 ; i<argc ; i++) {
		cout << argv[i] << endl;
	}

	cout << "Hello World";
	return 0;
}

