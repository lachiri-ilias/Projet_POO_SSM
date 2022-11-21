else{
					pid_t pid;
					int status;
					int i=0;
					int nb_pipes=0;
					while(l->seq[nb_pipes+1]!=NULL){
						nb_pipes++;
					}
					int tuyaux[nb_pipes*2];

					for(int j=0; j<nb_pipesx; j++){
						pipe(tuyaux + j*2);
					}

					while(l->seq[i]!=NULL){
						pid = fork();
						if(pid == 0) { // si on est dans le fils

							if(l->seq[i+1]!=0) dup2(tuyaux[i+1],1);

							if(i != 0 ) dup2(tuyaux[i-2], 0);

							for(j = 0; j < 2*numPipes; j++){
											close(pipefds[j]);
							}

							execvp(l->seq[i][0], l->seq[i]);


						}
						else if(pid < 0){
							perror("error");
							exit(EXIT_FAILURE);
						}

						// command = command->next; ????
						j+=2;
					}

					for(j = 0; j < 2 * nb_pipes; j++){
							close(tuyaux[j]);
					}

					for(j = 0; j < nb_pipes + 1; j++)
							wait(&status);
					}

			}
