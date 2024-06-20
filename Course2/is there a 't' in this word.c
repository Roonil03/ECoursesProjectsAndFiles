/*You are conducting a linguistic study and are interested in finding words that contain the letter 't' or 'T' in the first half of the word (including the middle letter if there is one). Specifically, if the first half of the word does contain a 't' or a 'T', your program should output a 1. If the first half does not contain the letter 't' or 'T', but the second half does, then your program should output a 2. Otherwise, if there is no 't' or 'T' in the word at all, your program's output should be -1. You may assume that the word entered does not have more than 50 letters.*/

//Solution:
#include<stdio.h>
#include<string.h>
int main()
{
    int n,half;
    char str[51];
    scanf("%s",str);
    n=strlen(str);
    if(n%2==0)
    {
        half = n/2;
    }
    else{
        half = n/2 +1;
    }
    int i=0,test = -1;
    while(str[i] != '\0')
    {
        if((str[i]=='T'||str[i] == 't')&&i<=half){
        test = 1;
        break;
        }
        else if((str[i]=='T'||str[i] == 't')&&i>half){
        test=2;
        break;
        }
        i++;
    }
    printf("%d",test);
    return 0;
}