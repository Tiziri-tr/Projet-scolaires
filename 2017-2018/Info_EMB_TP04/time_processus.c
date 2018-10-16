
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <errno.h>
#include <sys/types.h>
#include <unistd.h>
#include <pthread.h>
#include <errno.h>
#include <string.h>
#include <sys/wait.h>


// TP 4
// Tiziri GUELLAL

#define NOMBRE_PROCESSUS 10

int main(){

	
	int i;
	pid_t pid;
	struct timespec start,end;
	double timenow=0;
	double exectime=0;
	for(i=0;i<NOMBRE_PROCESSUS;i++){
		clock_gettime(CLOCK_REALTIME,&start );
		
		if((pid=fork())==0){
	  		exit(0);
		}else{
			wait(NULL);
			clock_gettime( CLOCK_REALTIME,&end );
		}

		timenow = (int)(end.tv_nsec - start.tv_nsec) + (int)(end.tv_sec-start.tv_sec) * 1000000000;
		exectime=exectime+timenow;

	}
  
  
  printf("temps d execution d un processus %lfns pour %d processus lances.\n",exectime/NOMBRE_PROCESSUS,NOMBRE_PROCESSUS);

  
  
  return 0;
}
