package main

import (
	"fmt"
	"sync"
)

type Chopstick struct{ sync.Mutex }

type Philosopher struct {
	id              int
	leftCS, rightCS *Chopstick
}

func host(requestChannel chan int, finishChannel chan int, wg *sync.WaitGroup) {
	defer wg.Done()
	eatingCount := 0
	for {
		if eatingCount < 2 {
			select {
			case id := <-requestChannel:
				if id == 0 {
					return
				}
				eatingCount++
			case <-finishChannel:
				eatingCount--
			}
		} else {
			<-finishChannel
			eatingCount--
		}
	}
}

func (p Philosopher) eat(requestChannel chan int, finishChannel chan int, wg *sync.WaitGroup) {
	defer wg.Done()
	for i := 0; i < 3; i++ {
		requestChannel <- p.id
		p.leftCS.Lock()
		p.rightCS.Lock()
		fmt.Printf("starting to eat %d\n", p.id)
		fmt.Printf("finishing eating %d\n", p.id)
		p.rightCS.Unlock()
		p.leftCS.Unlock()
		finishChannel <- p.id
	}
}

func main() {
	chopsticks := make([]*Chopstick, 5)
	for i := 0; i < 5; i++ {
		chopsticks[i] = new(Chopstick)
	}
	philosophers := make([]*Philosopher, 5)
	for i := 0; i < 5; i++ {
		philosophers[i] = &Philosopher{
			id:      i + 1,
			leftCS:  chopsticks[i],
			rightCS: chopsticks[(i+1)%5],
		}
	}
	requestChannel := make(chan int)
	finishChannel := make(chan int)
	var wg sync.WaitGroup
	wg.Add(1)
	go host(requestChannel, finishChannel, &wg)
	for i := 0; i < 5; i++ {
		wg.Add(1)
		go philosophers[i].eat(requestChannel, finishChannel, &wg)
	}
	wg.Wait()
	requestChannel <- 0
	wg.Add(1)
	wg.Wait()
}
