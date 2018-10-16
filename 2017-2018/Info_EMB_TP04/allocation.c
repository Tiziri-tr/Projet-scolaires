#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>
#include <time.h>
#include <sys/types.h>
#include <sys/wait.h>


// Tiziri GUELLAL


#define OCTET 4
#define NB   10


double octet(int octet, int num);

int main(){
	printf("\n- ");
	double res = octet(OCTET, NB);
	printf("Temps moyenne pour l'allocation de %d octets : %lf ns\n", OCTET, res);
	return 0;
}


double octet(int octet, int num){
	int i = 0;
	double time;
	struct timespec timeBefore, timeAfter;
	if(clock_gettime(CLOCK_REALTIME, &timeBefore) < 0){ // START
		perror("clock_gettime timeBefore");
		exit(EXIT_FAILURE);
	}
	for(i = 0; i < num; i++){
		malloc(octet);
	}
	if(clock_gettime(CLOCK_REALTIME, &timeAfter) < 0){ // STOP
		perror("clock_gettime timeAfter");
		exit(EXIT_FAILURE);
	}
	time = 1000000000L * (timeAfter.tv_sec - timeBefore.tv_sec) + timeAfter.tv_nsec - timeBefore.tv_nsec;
	return time / num;
}
