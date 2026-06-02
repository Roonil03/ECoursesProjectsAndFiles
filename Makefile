# Memory Game - React

This Makefile provides basic commands for running the Memory Game project.

.PHONY: install run clean help

# Default target
help:
	@echo "Available commands:"
	@echo "  make install - Install Node dependencies (npm install)"
	@echo "  make run     - Run the React development server (npm run dev)"
	@echo "  make clean   - Remove node_modules and build artifacts"

install:
	npm install

run:
	@echo "Starting Stellar Memory Game Server"
	@echo "Once the server is running, open your web browser and go to:"
	@echo "   👉 http://localhost:5173"
	@echo ""
	@echo "To stop the server, press Ctrl+C"
	@echo "=========================================================="
	npm run dev

clean:
	rm -rf node_modules dist
	@echo "Cleaned node_modules and dist directories."
