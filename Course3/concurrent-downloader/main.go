package main

import (
	"fmt"
	"io"
	"net/http"
	"net/url"
	"os"
	"path"
	"sync"
)

type Result struct {
	URL      string
	FileName string
	Err      error
}

type progressWriter struct {
	fileName    string
	total       int64
	written     int64
	lastPct     int
	lastWritten int64
}

func (pw *progressWriter) Write(p []byte) (int, error) {
	n := len(p)
	pw.written += int64(n)

	if pw.total > 0 {
		pct := int(float64(pw.written) / float64(pw.total) * 100)
		if pct-pw.lastPct >= 10 {
			fmt.Printf("[%s] %d%%\n", pw.fileName, pct)
			pw.lastPct = pct
		}
	} else {
		if pw.written-pw.lastWritten >= 1024*1024 {
			fmt.Printf("[%s] %d MB downloaded\n", pw.fileName, pw.written/(1024*1024))
			pw.lastWritten = pw.written
		}
	}
	return n, nil
}

func downloadFile(fileURL string, wg *sync.WaitGroup, results chan<- Result) {
	defer wg.Done()

	parsedURL, err := url.Parse(fileURL)
	if err != nil {
		results <- Result{URL: fileURL, Err: fmt.Errorf("invalid URL: %v", err)}
		return
	}

	fileName := path.Base(parsedURL.Path)
	if fileName == "/" || fileName == "." || fileName == "" {
		fileName = "downloaded_file"
	}

	resp, err := http.Get(fileURL)
	if err != nil {
		results <- Result{URL: fileURL, Err: fmt.Errorf("network error: %v", err)}
		return
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		results <- Result{URL: fileURL, Err: fmt.Errorf("bad status: %s", resp.Status)}
		return
	}

	out, err := os.Create(fileName)
	if err != nil {
		results <- Result{URL: fileURL, Err: fmt.Errorf("file access error: %v", err)}
		return
	}
	defer out.Close()

	pw := &progressWriter{
		fileName: fileName,
		total:    resp.ContentLength,
	}

	_, err = io.Copy(out, io.TeeReader(resp.Body, pw))
	if err != nil {
		results <- Result{URL: fileURL, Err: fmt.Errorf("download error: %v", err)}
		return
	}

	if pw.total > 0 && pw.lastPct < 100 {
		fmt.Printf("[%s] 100%%\n", pw.fileName)
	} else if pw.total <= 0 {
		fmt.Printf("[%s] Download complete (%d bytes)\n", pw.fileName, pw.written)
	}

	results <- Result{URL: fileURL, FileName: fileName, Err: nil}
}

func main() {
	if len(os.Args) < 2 {
		fmt.Println("Usage: go run main.go <url1> <url2> ...")
		return
	}

	urls := os.Args[1:]
	var wg sync.WaitGroup
	results := make(chan Result, len(urls))

	for _, u := range urls {
		wg.Add(1)
		go downloadFile(u, &wg, results)
	}

	wg.Wait()
	close(results)

	successCount := 0
	failCount := 0

	fmt.Println("\n--- Download Summary ---")
	for res := range results {
		if res.Err != nil {
			fmt.Printf("FAILED:  %s\n         Reason: %v\n", res.URL, res.Err)
			failCount++
		} else {
			fmt.Printf("SUCCESS: %s -> saved as '%s'\n", res.URL, res.FileName)
			successCount++
		}
	}
	fmt.Printf("\nTotal Successful: %d\n", successCount)
	fmt.Printf("Total Failed: %d\n", failCount)
}
