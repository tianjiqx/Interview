package main

import (
	"fmt"
	"time"
)

func timer_run() {

	c := time.Tick(2 * time.Second)
	/*
		for next := range c {
			fmt.Printf("%v \n", next)
			fmt.Printf("current time: %v \n", time.Now())
		}
	*/
	for range c {
		fmt.Printf("current time: %v \n", time.Now())
	}

}

func main() {

	// sleep test  ok
	/*
		for {
			fmt.Printf("start %v \n", time.Now())
			time.Sleep(2 * time.Second)
			fmt.Printf("end %v \n", time.Now())
		}
	*/

	// 2.
	/*
		go timer_run()
		// dead loop, make timer_run
		for {
		}
		/*
			var wg sync.WaitGroup
			wg.Add(1)
			wg.Done()  in go
			wg.Wait()
	*/

	// 3.
	t := time.NewTicker(time.Second * 2)

	go func() {
		for {
			select {
			case <-t.C:
				fmt.Printf("current time: %v \n", time.Now())
			}
		} // end for
	}()

	time.Sleep(10 * time.Second)

	t.Stop()
}
