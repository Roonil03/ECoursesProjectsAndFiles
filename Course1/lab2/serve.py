import http.server
import socketserver
import webbrowser

PORT = 5500
Handler = http.server.SimpleHTTPRequestHandler

print(f" Starting Local Server on Port: {PORT}")
print(f" Target URL: http://localhost:{PORT}")

try:
    with socketserver.TCPServer(("", PORT), Handler) as httpd:
        print("[Status] Server is now LIVE. Press CTRL + C to stop.")
        webbrowser.open(f"http://localhost:{PORT}")
        httpd.serve_forever()
except KeyboardInterrupt:
    print("\n[Status] Server has been offline (stopped) successfully.")
except Exception as e:
    print(f"\n[Error] Could not start server on port {PORT}: {e}")