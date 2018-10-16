#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <semaphore.h>
#include <fcntl.h>
#include <sys/time.h>
#include <time.h>

// TP 4
// Tiziri GUELLAL

#define NOMBRE_PROCESSUS 10

int main(){


	pid_t pid;
	double time=0;
	struct timespec begin,end;
	
	sem_t *sem1;
	sem_t *sem0;
	sem1 = sem_open("sem1", O_CREAT, 0666, 1);
	sem0 = sem_open("sem0", O_CREAT, 0666, 0);
	
	if(sem1 == SEM_FAILED || sem0 == SEM_FAILED){
		perror("sem_open");
		exit(EXIT_FAILURE);
	}

	if((pid = fork()) < 0){
		perror("fork");
		exit(EXIT_FAILURE);
	}
	else if(pid==0){
		int n=0;
		for(n=0;n<NOMBRE_PROCESSUS;n++){
			sem_wait(sem0);
			sem_post(sem1);
		}
	}		
	else{
		int n=0;
		if(clock_gettime(CLOCK_REALTIME,&begin) < 0){
			printf("erreur clock_gettime begin");
			exit(EXIT_FAILURE);
		}
		for(n=0;n<NOMBRE_PROCESSUS;n++){
			sem_wait(sem1);
			sem_post(sem0);
		}
		wait(NULL);		
		if(clock_gettime(CLOCK_REALTIME,&end) < 0){
			printf("erreur clock_gettime end");
			exit(EXIT_FAILURE);
		}
		time =1000000000L * (end.tv_sec - begin.tv_sec) + end.tv_nsec - begin.tv_nsec;
		printf("Temps d execution moyen : %lf ns\n", time / (NOMBRE_PROCESSUS * 2));
	}
		
	

	return 0;



}
