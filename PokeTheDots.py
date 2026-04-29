import pygame
import random
import math

def main():
    pygame.init()
    game = Game()
    game.play()
    pygame.quit()

class Game:
    def __init__(self):
        self._width = 500
        self._height = 400
        self._screen = pygame.display.set_mode((self._width, self._height))
        pygame.display.set_caption('Poke the Dots')
        self._font = pygame.font.SysFont('arial', 64)
        self._frame_rate = 90
        self._close_selected = False
        self._clock = pygame.time.Clock()
        self._small_dot = Dot('red', [50, 75], 30, [1, 2], self._width, self._height)
        self._big_dot = Dot('blue', [200, 100], 40, [2, 1], self._width, self._height)
        self._small_dot.randomize()
        self._big_dot.randomize()
        self._score = 0
        self._continue_game = True

    def play(self):
        while not self._close_selected:
            self.handle_events()
            self.draw()
            self.update()
        
    def handle_events(self):
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                self._close_selected = True
            elif self._continue_game and event.type == pygame.MOUSEBUTTONUP:
                self.handle_mouse_up()

    def handle_mouse_up(self):
        self._small_dot.randomize()
        self._big_dot.randomize()

    def draw(self):
        self._screen.fill(pygame.Color('black'))
        self.draw_score()
        self._small_dot.draw(self._screen)
        self._big_dot.draw(self._screen)
        if not self._continue_game:
            self.draw_game_over()
        pygame.display.update()

    def update(self):
        if self._continue_game:
            self._small_dot.move()
            self._big_dot.move()
            self._score = pygame.time.get_ticks() // 1000
        self._clock.tick(self._frame_rate)
        if self._small_dot.intersects(self._big_dot):
            self._continue_game = False

    def draw_game_over(self):
        text = 'GAME OVER'
        font_color = pygame.Color(self._small_dot.get_color())
        bg_color = pygame.Color(self._big_dot.get_color())
        text_surface = self._font.render(text, True, font_color, bg_color)
        y_pos = self._height - text_surface.get_height()
        self._screen.blit(text_surface, (0, y_pos))

    def draw_score(self):
        text = 'Score: ' + str(self._score)
        text_surface = self._font.render(text, True, pygame.Color('white'))
        self._screen.blit(text_surface, (0, 0))

class Dot:
    def __init__(self, color, center, radius, velocity, win_width, win_height):
        self._color = color
        self._center = center
        self._radius = radius
        self._velocity = velocity
        self._win_width = win_width
        self._win_height = win_height

    def move(self):
        size = (self._win_width, self._win_height)
        for i in range(2):
            self._center[i] += self._velocity[i]
            if (self._center[i] < self._radius) or (self._center[i] + self._radius > size[i]):
                self._velocity[i] = -self._velocity[i]

    def draw(self, surface):
        pygame.draw.circle(surface, pygame.Color(self._color), (int(self._center[0]), int(self._center[1])), self._radius)

    def intersects(self, other):
        dist = math.sqrt((self._center[0] - other._center[0])**2 + (self._center[1] - other._center[1])**2)
        return dist <= self._radius + other._radius

    def get_color(self):
        return self._color

    def randomize(self):
        self._center[0] = random.randint(self._radius, self._win_width - self._radius)
        self._center[1] = random.randint(self._radius, self._win_height - self._radius)

if __name__ == '__main__':
    main()