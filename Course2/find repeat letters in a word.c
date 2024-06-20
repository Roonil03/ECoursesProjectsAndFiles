/*You are still conducting linguistic research! This time, you'd like to write a program to find out how many letters occur multiple times in a given word. Your program should read a word from the input and then sort the letters of the word alphabetically (by their ASCII codes). Next, your program should iterate through the letters of the word and compare each letter with the one following it. If these equal each other, you increase a counter by 1, making sure to then skip ahead far enough so that letters that occur more than twice are not counted again. You may assume that the word you read from the input has no more than 50 letters, and that the word is all lowercase.*/

//Solution:
#include<stdio.h>
#include<string.h>
int main()
{
    //inputs
    int n=0,i,j;
    char str[50],temp;
    scanf("%s",str);
    
    //sorting
    for(i=0;i<strlen(str);i++)
    {
        for(j=i;j<strlen(str)-1;j++)
        {
            if(str[j]>str[j+1])
            {
                temp = str[j];
                str[j] = str[j+1];
                str[j+1] = temp;
            }
        }
    }
    
    //counting
    temp='/';
    for(i=0;i<strlen(str);i++)
    {
        if(str[i] == temp)
        continue;
        if(str[i] == str[i+1])
        {
            n++;
            temp=str[i];
        }
    }
    printf("%d",n);
    return 0;
}




//Solution with friend's help:
#include <stdio.h>

int main(void) {
  char word[50];
  char swap;
  int i, j;
  int len = 0;
  int numRepeats = 0;
  
  scanf("%s", word);
  
  //TODO combine sort and len() with while loop
  // get word length
  while (word[len] != '\0') {
    len++;
  }
  
  // bubble sort
  for (i=0; i<len; i++) {
    for (j=0; j<len-1; j++) {
      if (word[j] > word[j+1]) {
        swap = word[j+1];
        word[j+1] = word[j];
        word[j] = swap;
      }       
    }
  }
  
  //TODO clean up this loop & condition
  for (i=0; i<len-1; i++) {
    if (word[i] == word[i+1]) {
      numRepeats++;
      while (word[i] == word[i+1]) {
        i++;
      }
    }
  }
  
  printf("%d\n", numRepeats);
  
  return 0;
}