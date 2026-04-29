import unittest
import os
import pygame

os.environ["SDL_VIDEODRIVER"] = "dummy"

from HackingMinigame import HackingGame

class TestHackingGame(unittest.TestCase):
    
    def setUp(self):
        self.game = HackingGame()

    def tearDown(self):
        pygame.quit()

    
    def test_initialization(self):
        self.assertEqual(self.game.attempts_left, 4, "Game should start with 4 attempts.")
        self.assertFalse(self.game.game_over, "Game should not be over on initialization.")
        self.assertEqual(len(self.game.session_words), 14, "Session should load exactly 14 words.")
        self.assertEqual(len(self.game.display_list), 14, "Display list should have 14 entries.")


    def test_display_list_formatting(self):
        for line in self.game.display_list:
            self.assertEqual(len(line), 20, f"Line '{line}' is not exactly 20 characters long.")


    def test_likeness_exact_match(self):
        self.game.secret_word = "CONFIGURATION"
        score = self.game.get_likeness("CONFIGURATION")
        self.assertEqual(score, 13, "Exact match should yield a likeness of 13.")

    def test_likeness_partial_match(self):
        self.game.secret_word = "DOCUMENTATION"
        # "CONSOLIDATION" matches 'O' at index 1, 'T' at 9, 'I' at 10, 'O' at 11, 'N' at 12
        # Actually let's test a very specific known match for safety
        # DOC...ATION vs CON...ATION -> 'ATION' matches (5 chars)
        guess = "CONSOLIDATION"
        expected_matches = 0
        for i in range(13):
            if self.game.secret_word[i] == guess[i]:
                expected_matches += 1
                
        score = self.game.get_likeness(guess)
        self.assertEqual(score, expected_matches, f"Expected {expected_matches} matches, got {score}.")

    def test_likeness_case_insensitivity(self):
        self.game.secret_word = "VULNERABILITY"
        score = self.game.get_likeness("vulnerability")
        self.assertEqual(score, 13, "Lowercase input failed to match uppercase secret word.")


    def test_handle_guess_incorrect(self):
        self.game.secret_word = "ACCESSIBILITY"
        initial_history_len = len(self.game.history)
        
        wrong_guess = "ADMINISTRATOR" 
        self.game.handle_guess(wrong_guess)
        
        self.assertEqual(self.game.attempts_left, 3, "Attempts did not decrement on wrong guess.")
        self.assertEqual(len(self.game.history), initial_history_len + 1, "History did not append the guess.")
        self.assertFalse(self.game.game_over, "Game over triggered prematurely on a standard wrong guess.")

    def test_handle_guess_warning_lockout(self):
        self.game.secret_word = "ACCESSIBILITY"
        self.game.attempts_left = 2
        
        self.game.handle_guess("ADMINISTRATOR")
        
        self.assertEqual(self.game.attempts_left, 1, "Attempts should be 1.")
        self.assertEqual(self.game.message, "*** WARNING: LOCKOUT IMMINENT ***", "Warning message did not display at 1 attempt.")

    def test_handle_guess_game_over_loss(self):
        self.game.secret_word = "ACCESSIBILITY"
        self.game.attempts_left = 1
        
        self.game.handle_guess("ADMINISTRATOR")
        
        self.assertEqual(self.game.attempts_left, 0)
        self.assertTrue(self.game.game_over, "Game did not set game_over state on final wrong guess.")
        self.assertEqual(self.game.message, "TERMINAL LOCKED. ACCESS DENIED.")

    def test_handle_guess_game_over_win(self):
        self.game.secret_word = "AUTHORIZATION"
        
        self.game.handle_guess("AUTHORIZATION")
        
        self.assertTrue(self.game.game_over, "Game did not set game_over state on correct guess.")
        self.assertEqual(self.game.message, "SUCCESS! SYSTEM ACCESSED.")

if __name__ == "__main__":
    unittest.main(verbosity=2)