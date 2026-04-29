import unittest
from unittest.mock import MagicMock, patch
import pygame
from PokeTheDots import Game, Dot

class TestDot(unittest.TestCase):
    def setUp(self):
        self.dot = Dot('red', [100, 100], 10, [1, 1], 500, 400)

    def test_init(self):
        self.assertEqual(self.dot._color, 'red')
        self.assertEqual(self.dot._center, [100, 100])
        self.assertEqual(self.dot._radius, 10)
        self.assertEqual(self.dot._velocity, [1, 1])

    def test_move(self):
        self.dot.move()
        self.assertEqual(self.dot._center, [101, 101])

    def test_move_bounce_x(self):
        self.dot._center = [495, 100]
        self.dot.move()
        self.assertEqual(self.dot._velocity[0], -1)

    def test_move_bounce_y(self):
        self.dot._center = [100, 395]
        self.dot.move()
        self.assertEqual(self.dot._velocity[1], -1)

    def test_intersects_true(self):
        other = Dot('blue', [110, 100], 10, [0, 0], 500, 400)
        self.assertTrue(self.dot.intersects(other))

    def test_intersects_false(self):
        other = Dot('blue', [200, 200], 10, [0, 0], 500, 400)
        self.assertFalse(self.dot.intersects(other))

    def test_get_color(self):
        self.assertEqual(self.dot.get_color(), 'red')

    def test_randomize(self):
        self.dot.randomize()
        self.assertTrue(self.dot._radius <= self.dot._center[0] <= 500 - self.dot._radius)
        self.assertTrue(self.dot._radius <= self.dot._center[1] <= 400 - self.dot._radius)

    @patch('pygame.draw.circle')
    def test_draw(self, mock_circle):
        mock_surface = MagicMock()
        self.dot.draw(mock_surface)
        mock_circle.assert_called()

class TestGame(unittest.TestCase):
    @patch('pygame.display.set_mode')
    @patch('pygame.font.SysFont')
    def setUp(self, mock_font, mock_display):
        pygame.init()
        self.game = Game()

    def test_init(self):
        self.assertEqual(self.game._width, 500)
        self.assertEqual(self.game._height, 400)
        self.assertTrue(self.game._continue_game)
        self.assertEqual(self.game._score, 0)

    def test_handle_mouse_up(self):
        self.game._small_dot.randomize = MagicMock()
        self.game._big_dot.randomize = MagicMock()
        self.game.handle_mouse_up()
        self.game._small_dot.randomize.assert_called_once()
        self.game._big_dot.randomize.assert_called_once()

    def test_update_logic(self):
        self.game._clock.tick = MagicMock()
        self.game.update()
        self.assertIsInstance(self.game._score, int)

    def test_collision_detection(self):
        self.game._small_dot._center = [100, 100]
        self.game._big_dot._center = [100, 100]
        self.game._clock.tick = MagicMock()
        self.game.update()
        self.assertFalse(self.game._continue_game)

    @patch('pygame.Surface.blit')
    def test_draw_score(self, mock_blit):
        self.game.draw_score()
        self.game._screen.blit.assert_called()

    @patch('pygame.Surface.blit')
    def test_draw_game_over(self, mock_blit):
        self.game._continue_game = False
        self.game.draw_game_over()
        self.game._screen.blit.assert_called()

if __name__ == '__main__':
    unittest.main()