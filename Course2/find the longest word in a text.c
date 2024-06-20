/*Your job is to find the length of the longest word in a text with no punctuation or special characters of any kind - only contains words. To do so, please write a C-program that takes as a input first the number of words in a text, followed by all of the words in the text. The output of your program should be the length of the longest word in the text.

To simplify your program, you can assume that the longest word will not exceed 100 characters.*/

//Solution:
#include <stdio.h>
#include<string.h>
int main()
{
    int max=0,n,i;
    scanf("%d",&n);
    char str[100];
    for(i=0;i<n;i++)
    {
        scanf("%s",str);
        if(strlen(str)>max)
        {
            max = strlen(str);
        }
    }
    printf("%d",max);
}