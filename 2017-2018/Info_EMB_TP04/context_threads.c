#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>
#include <time.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <pthread.h>
#include <semaphore.h>

// TP 4
// Tiziri GUELLAL

#define NUMBER_THREADS 10

sem_t *sem1;
sem_t *sem0;
void *terminate(void *ar);

int main(int argc , char ** argv){

	pthread_t th;
	double time=0;
	struct timespec begin, end;

	sem1 = sem_open("sem1", O_CREAT, 0666, 1);
	sem0 = sem_open("sem0", O_CREAT, 0666, 0);

	if(sem1 == SEM_FAILED || sem0 == SEM_FAILED){
		perror("sem_open");
		exit(EXIT_FAILURE);
	}


	if(pthread_create(&th, NULL, terminate, NULL)){
		printf("pthread_create");
		exit(EXIT_FAILURE);
	}


	if(clock_gettime(CLOCK_REALTIME,&begin) < 0){
		printf("erreur clock_gettime begin");
		exit(EXIT_FAILURE);
	}
	int n=0;
	for(n=0;n<NUMBER_THREADS;n++){
		sem_wait(sem1);
		sem_post(sem0);
	}

	if(pthread_join(th, NULL)){
		printf("pthread_join");
		exit(EXIT_FAILURE);
	}	
		

	if(clock_gettime(CLOCK_REALTIME,&end) < 0){
		printf("erreur clock_gettime end");
		exit(EXIT_FAILURE);
	}	
		
		

	sem_close(sem1);
	sem_close(sem0);

	time =1000000000L * (end.tv_sec - begin.tv_sec) + end.tv_nsec - begin.tv_nsec;

	printf("temps d execution moyen : %lf ns\n", time / (NUMBER_THREADS * 2));
	return 0;

}


void *terminate(void *ar){
	
	int i;
	for(i = 0; i < NUMBER_THREADS; i++){
		sem_wait(sem0);
		sem_post(sem1);
	}
	(void)ar;
	pthread_exit(NULL);
}
