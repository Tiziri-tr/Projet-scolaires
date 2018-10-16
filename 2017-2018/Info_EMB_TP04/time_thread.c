
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


#define NOMBRE_THREADS 10



void *fonction_start();

int main(){

  pthread_t pid;
  int i;
  struct timespec start,end;
  double timenow=0;
  double exectime=0;
  for(i=0;i<numb_thread;i++){
    clock_gettime(CLOCK_REALTIME,&start );
    pthread_create(&pid, NULL, fonction_start,NULL);
    pthread_join(pid,NULL);
    clock_gettime( CLOCK_REALTIME,&end );
    timenow = (int)(end.tv_nsec - start.tv_nsec) + (int)(end.tv_sec-start.tv_sec) * 1000000000;
  	exectime=exectime+timenow;
  }


  printf("temps d execution d un thread %lfns pour %d threads lances.\n",exectime/NOMBRE_THREADS,NOMBRE_THREADS);



  return 0;

}

void *fonction_start(){
  pthread_exit(NULL);
}
