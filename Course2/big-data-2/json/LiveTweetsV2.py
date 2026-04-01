import json
import sys
import tweepy

class TweetStreamListener(tweepy.Stream):
    def on_status(self, status):
        print(status.created_at.strftime("%Y-%m-%d %H:%M:%S") + " " + status.text[:70] + "\n")
        return True

    def on_error(self, status_code):
        print("Error:", status_code)
        return False

auth = []
with open("auth", "r") as f:
    auth = [line.strip() for line in f]

try:
    listener = TweetStreamListener()
    auth_key = tweepy.OAuthHandler(auth[0], auth[1])
    auth_key.set_access_token(auth[2], auth[3])

    api = tweepy.API(auth_key)
    live_twitter_stream = tweepy.Stream(auth=auth_key, listener=listener)
    live_twitter_stream.filter(track=[sys.argv[1]], is_async=True)
except KeyboardInterrupt:
    print("\nStopping the Twitter stream.")
