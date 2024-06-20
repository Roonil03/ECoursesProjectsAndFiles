/*Please write a C-program that uses a recursive function called "sumOfDigits()" to compute the sum of the digits of a number. To do so, your main function should first read an integer number as input and then call sumOfDigits(), which should in turn call itself until there are no digits left to sum, at which point the final sum should display to the user.

Here is the main idea of how the recursion in sumOfDigits() should work:

sumOfDigits(6452) = 2 + sumOfDigits(645)

sumOfDigits(645) = 5 + sumOfDigits(64)

...

sumOfDigits(6) = 6*/


//Solution:
#include <stdio.h>
int sumOfDigits(int num)
{
    if(num!=0)
    {
        return ((num%10)+sumOfDigits(num/10));
    }
    else{
        return 0;
    }
}

int main()
{
    int n;
    scanf("%d",&n);
    printf("%d",sumOfDigits(n));
    return 0;
}