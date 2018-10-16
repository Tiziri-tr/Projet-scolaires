CFLAGS=-Wall -pedantic
LPTHREAD=-lpthread
LRT=-lrt


context_processus: context_processus.o
	gcc -o context_processus context_processus.o $(LPTHREAD) $(LRT)

context_processus.o: context_processus.c
	gcc -o context_processus.o -c context_processus.c $(CFLAGS)

context_threads: context_threads.o
	gcc -o context_threads context_threads.o $(LPTHREAD) $(LRT)

context_threads.o: context_threads.c
	gcc -o context_threads.o -c context_threads.c $(CFLAGS)

allocation: allocation.o
	gcc -o allocation allocation.o $(LRT)

allocation.o: allocation.c
	gcc -o allocation.o -c allocation.c $(CFLAGS)


clean:
	rm -rf *~ *.o


