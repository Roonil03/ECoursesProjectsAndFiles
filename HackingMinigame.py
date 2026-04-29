import pygame
import random
import sys

SCREEN_WIDTH = 800
SCREEN_HEIGHT = 700
FPS = 60

BLACK = (0, 0, 0)
GREEN = (0, 255, 0)
DARK_GREEN = (0, 100, 0)

MAX_ATTEMPTS = 4
SYMBOLS = ['!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '[', ']', '{', '}', '/', '\\', '|', ';', ':', '?', '.']

# UALBERTA REQUIREMENT: All passwords must be exactly 13 letters long.
WORD_POOL = [
    "AUTHENTICATED", "CONFIGURATION", "AUTHORIZATION", "COMMUNICATION", 
    "SPECIFICATION", "DOCUMENTATION", "VULNERABILITY", "UNCOMPLICATED", 
    "UNDERSTANDING", "ARCHITECTURES", "CONSOLIDATION", "MODIFICATIONS", 
    "MULTITHREADED", "PREPROCESSORS", "ACCESSIBILITY", "ADMINISTRATOR"
]

class HackingGame:
    def __init__(self):
        pygame.init()
        self.screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
        pygame.display.set_caption("Hacking")  # Exact case match for UAlberta
        self.clock = pygame.time.Clock()
        
        self.font = pygame.font.SysFont("monospace", 16)
        self.large_font = pygame.font.SysFont("monospace", 18)
        
        self.session_words = random.sample(WORD_POOL, 14)
        self.secret_word = random.choice(self.session_words)
        self.display_list = self._generate_display_list()
        
        self.attempts_left = MAX_ATTEMPTS
        self.game_over = False
        self.message = "ENTER PASSWORD"
        self.history = []
        self.input_text = ""

    def _generate_display_list(self):
        temp_words = self.session_words[:]
        random.shuffle(temp_words)        
        display = []
        for word in temp_words:
            # UALBERTA REQUIREMENT: Exactly 7 symbols per line.
            # Distributed as 3 symbols before the word, and 4 after.
            symbols = "".join(random.choices(SYMBOLS, k=7))
            line = f"{symbols[:3]}{word}{symbols[3:]}"
            display.append(line)
        return display

    def get_likeness(self, guess):
        # UALBERTA REQUIREMENT: Lowercase inputs match uppercase targets.
        guess = guess.upper()
        likeness = 0
        for i in range(min(len(guess), len(self.secret_word))):
            if guess[i] == self.secret_word[i]:
                likeness += 1
        return likeness

    def handle_guess(self, guess):
        guess = guess.upper()
        if guess == self.secret_word:
            self.message = "SUCCESS! SYSTEM ACCESSED."
            self.game_over = True
        else:
            self.attempts_left -= 1
            likeness = self.get_likeness(guess)
            self.history.append(f"{guess} - LIKENESS: {likeness}")            
            
            if self.attempts_left <= 0:
                self.message = "TERMINAL LOCKED. ACCESS DENIED."
                self.game_over = True
            elif self.attempts_left == 1:
                # UALBERTA REQUIREMENT: Display a lockout warning at 1 attempt left.
                self.message = "*** WARNING: LOCKOUT IMMINENT ***"
            else:
                self.message = f"WRONG PASSWORD. {self.attempts_left} ATTEMPTS LEFT."

    def draw(self):
        self.screen.fill(BLACK)
        
        # Display the 13-letter + 7-symbol password lines
        for i, line in enumerate(self.display_list):
            color = GREEN if not self.game_over else DARK_GREEN
            line_surf = self.font.render(line, True, color)
            self.screen.blit(line_surf, (20, 20 + (i * 35)))

        input_prompt = self.large_font.render(f"> {self.input_text}_", True, GREEN)
        status_msg = self.large_font.render(self.message, True, GREEN)
        self.screen.blit(status_msg, (20, 600)) 
        self.screen.blit(input_prompt, (20, 640))

        header_text = self.large_font.render(f"ATTEMPTS LEFT: {'#' * self.attempts_left}", True, GREEN)
        self.screen.blit(header_text, (500, 20))
        
        hint_header = self.large_font.render("--- HINT HISTORY ---", True, GREEN)
        self.screen.blit(hint_header, (500, 70))

        for i, entry in enumerate(self.history):
            hist_surf = self.font.render(entry, True, GREEN)
            self.screen.blit(hist_surf, (500, 110 + (i * 30)))

        pygame.display.flip()

    def run(self):
        while True:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    sys.exit()                
                if not self.game_over and event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_RETURN:
                        if self.input_text.strip():
                            self.handle_guess(self.input_text.strip())
                            self.input_text = ""
                    elif event.key == pygame.K_BACKSPACE:
                        self.input_text = self.input_text[:-1]
                    else:
                        # Ensures only alphabetic characters are recorded
                        if event.unicode.isalpha():
                            self.input_text += event.unicode.upper()
            self.draw()
            self.clock.tick(FPS)

if __name__ == "__main__":
    game = HackingGame()
    game.run()