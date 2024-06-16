#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <ctype.h>
#include "cards.h"
#include "deck.h"
#include "eval.h"
#include "future.h"
#include "input.h"


int main(int argc, char ** argv) {
  if (argc > 3 || argc < 2) {
    fprintf(stderr, "Usage: enter hand and number of trials\n");
    return EXIT_FAILURE;
  }
  unsigned trials = 10000;
  if (argc==3){
    trials = atoi(argv[2]);
  }

  FILE * f = fopen(argv[1], "r");
  if (f==NULL){
    fprintf(stderr, "File not found\n");
    return EXIT_FAILURE;
  }

  future_cards_t * fc = malloc(sizeof(* fc));
  fc->decks = NULL;
  fc->n_decks = 0;
  size_t n_hands = 0;
  deck_t ** input = read_input(f, &n_hands, fc);
  deck_t * remaining_cards = build_remaining_deck(input, n_hands);
  unsigned w_array[n_hands+1];
  for (int i=0; i<n_hands+1; i++){
    w_array[i]=0;
  }

  for (int i = 0; i<trials; i++){
    shuffle(remaining_cards);
    future_cards_from_deck(remaining_cards, fc);
    int inc_array_at=0;
    int index=0;
    for( int x = 1; x<n_hands; x++){
      if (compare_hands(input[index], input[x])==0) inc_array_at = n_hands;
      if (compare_hands(input[index], input[x])<0) {
	inc_array_at = x;
	index=x;
      }
    }
    w_array[inc_array_at]++;
  }
    
  
  for (size_t i=0; i<n_hands; i++){
    printf("Hand %zu won %u / %u times (%.2f%%)\n", i, w_array[i], trials, (double)w_array[i]/(double)trials*100);
}
  printf("And there were %u ties\n", w_array[n_hands]);

  if (fclose(f)!=0){
    fprintf(stderr, "Cannot close file\n");
    return EXIT_FAILURE;
  }
  for(size_t n = 0; n<n_hands; n++)
  {
    free_deck(input[n]);
  }
  free(input);
  free_deck(remaining_cards);
  for (size_t i=0; i<fc->n_decks; i++) free(fc->decks[i].cards);
  free(fc->decks);
  free(fc);
  
  return EXIT_SUCCESS;
}
