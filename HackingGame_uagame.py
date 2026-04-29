from uagame import Window
from time import sleep
import random

def main():
    window = create_window()
    location = [0, 0]
    attempts = 4
    password = 'HUNTING'
    display_header(window, location, attempts)
    display_password_list(window, location)
    get_guesses(window, password, location, attempts)
    window.close()

def create_window():
    window = Window('Hacking', 600, 500)
    window.set_font_name('couriernew')
    window.set_font_size(18)
    window.set_font_color('green')
    window.set_bg_color('black')
    return window

def display_header(window, location, attempts):
    display_line(window, 'DEBUG MODE', location)
    display_line(window, str(attempts) + ' ATTEMPTS LEFT', location)
    display_line(window, '', location)

def display_password_list(window, location):
    passwords = ['PROVIDING', 'SETTING', 'CANT', 'HUNTING', 'SURVIVING', 'PRAYING', 'SPENDING', 'FITTING', 'WHINING', 'PLAYING', 'KNOWING', 'GETTING', 'WORKING']
    for p in passwords:
        embedded = embed_password(p, 20)
        display_line(window, embedded, location)
    display_line(window, '', location)

def embed_password(password, size):
    fill = '!@#$%^*()-+=~[]{}'
    res = ''
    while len(res) < (size - len(password)) // 2:
        res += random.choice(fill)
    res += password
    while len(res) < size:
        res += random.choice(fill)
    return res

def get_guesses(window, password, location, attempts_left):
    prompt = 'ENTER PASSWORD > '
    hint_location = [window.get_width() // 2, 0]
    guess = prompt_user(window, prompt, location)
    while guess != password and attempts_left > 1:
        attempts_left -= 1
        display_line(window, str(attempts_left) + ' ATTEMPTS LEFT', location)
        display_hint(window, password, guess, hint_location)
        check_warning(window, attempts_left)
        guess = prompt_user(window, prompt, location)
    end_game(window, guess, password)
    return guess

def display_hint(window, password, guess, location):
    display_line(window, 'Incorrect', location)
    matches = 0
    for i in range(min(len(password), len(guess))):
        if password[i] == guess[i]:
            matches += 1
    hint_msg = str(matches) + ' out of ' + str(len(password)) + ' letters are in the matching position'
    display_line(window, hint_msg, location)

def check_warning(window, attempts_left):
    if attempts_left == 1:
        warning = '*** LOCKOUT IMMINENT ***'
        x = window.get_width() - window.get_string_width(warning)
        y = window.get_height() - window.get_font_height()
        window.draw_string(warning, x, y)
        window.update()
        sleep(0.3)

def prompt_user(window, prompt, location):
    guess = window.input_string(prompt, location[0], location[1])
    location[1] += window.get_font_height()
    return guess

def display_line(window, string, location):
    window.draw_string(string, location[0], location[1])
    window.update()
    sleep(0.3)
    location[1] += window.get_font_height()

def display_outcome(window, outcome):
    font_height = window.get_font_height()
    y = (window.get_height() - (7 * font_height)) // 2
    for line in outcome:
        x = (window.get_width() - window.get_string_width(line)) // 2
        if line == outcome[-1]:
            window.input_string(line, x, y)
        else:
            window.draw_string(line, x, y)
            window.update()
            sleep(0.3)
            y += 2 * font_height

def end_game(window, guess, password):
    window.clear()
    if guess == password:
        outcome = [guess, 'SUCCESS!', 'ACCESS GRANTED', 'PRESS ENTER TO CONTINUE']
    else:
        outcome = [guess, 'FAILURE', 'ACCESS DENIED', 'PRESS ENTER TO EXIT']
    display_outcome(window, outcome)

main()