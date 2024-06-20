/*Write a C-program that prints out a word as many times as specified. The number of repetitions and the word should be given as input to the program. You may assume that the word has no more than 100 characters (be sure to also reserve space for the null terminator, \0, though!).*/

//Solution:
#include <stdio.h>
int main()
{
    int n,i;
    char str[101];
    scanf("%d %s",&n,str);
    for(i = 0; i<n; i++)
    {
        printf("%s\n",str);
    }
    return 0;
}