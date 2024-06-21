/*In this problem you will continue developing the data feature which you started implementing in the previous problem. You will implement a "tomorrow" feature in the C programming language via a function called "advanceDay()". The function advanceDay() should take as input a date (stored in a struct date) and return the date of the following day. You do not have to take into account leap years (although you may if you wish to). That is, it is okay for your function to always return March 1 as the day following February 28, no matter the year.

You are provided with a familiar date structure definition, a main function as well as the function prototypes for the readDate(), printDate(), and advanceDay() functions. Do not modify any of the given code. Simply add your function definitions underneath the main() function. For the readDate() and printDate() functions you may simply copy and paste the code you developed in the previous task.*/

//Solution:
#include <stdio.h>

struct date {
        int year;
        int month;
        int day;
    };

/* function prototypes */
void printDate(struct date);
void readDate(struct date *);
struct date advanceDay(struct date); 

int main(void) {
	struct date today, tomorrow;
	readDate(&today);
	printDate(today);
	tomorrow = advanceDay(today);
	printDate(tomorrow);
	return 0;
}
void printDate(struct date tod)
{
    printf("%02d/%02d/%4d\n",tod.month,tod.day,tod.year);
    return;
}
void readDate(struct date* exist)
{
    scanf("%d %d %d",&exist->year,&exist->month,&exist->day);
    return;
}
struct date advanceDay(struct date now)
{
    struct date next=now;
    next.day++;
    if((next.month == 1||next.month == 3||next.month == 5||next.month == 7 || next.month == 8||next.month == 10)&&next.day == 32)
    {
        next.month++;
        next.day = 1;
    }
    else if((next.month == 4||next.month == 6||next.month == 9||next.month == 11)&&next.day == 31)
    {
        next.month++;
        next.day = 1;
    }
    else if(next.month == 2 && next.day == 29)
    {
        next.month++;
        next.day = 1;
    }
    else if(next.month == 12 && next.day == 32)
    {
        next.year++;
        next.month = 1;
        next.day = 1;
    }
    return next;
}