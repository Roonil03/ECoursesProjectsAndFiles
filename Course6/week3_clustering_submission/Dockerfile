FROM python:3.9-slim
RUN apt-get update && apt-get install -y default-jre && rm -rf /var/lib/apt/lists/*
RUN pip install pyspark pandas numpy matplotlib
WORKDIR /app
COPY . /app
CMD ["python", "temp_runtime.py"]
