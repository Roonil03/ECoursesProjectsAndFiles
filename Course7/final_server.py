"""
Flask web application module for emotion detection using Watson NLP.

Provides endpoints to render the home page and process emotion detection
on submitted text input, returning a formatted response.
"""

from flask import Flask, request, render_template
from EmotionDetection.emotion_detection import emotion_detector

app = Flask(__name__)


@app.route('/')
def index():
    """Render the home page with the form for text input."""
    return render_template('index.html')


@app.route('/emotionDetector', methods=['POST'])
def emotionDetector():
    """
    Process POST request with text input to detect emotions.
    Returns formatted string with emotion scores and dominant emotion,
    or error message if input is invalid.
    """
    text = request.form.get('text', '')
    if not text:
        return "Error: No text provided", 400

    result = emotion_detector(text)
    if result['dominant_emotion'] is None:
        return "Invalid text! Please try again!"

    response_str = (
        f"For the given statement, the system response is "
        f"'anger': {result['anger']}, 'disgust': {result['disgust']}, "
        f"'fear': {result['fear']}, 'joy': {result['joy']} and 'sadness': {result['sadness']}. "
        f"The dominant emotion is {result['dominant_emotion']}."
    )
    return response_str


if __name__ == '__main__':
    app.run(host='localhost', port=5000)
