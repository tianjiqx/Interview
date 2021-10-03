package main

import (
	"bytes"
	"encoding/gob"
	"fmt"
	"log"
)

type kv struct {
	Key      string
	Val      string
	extr1    string // not sered
	Extr2    int32
	IsDelete bool
}

func main() {
	fmt.Println("vim-go")

	k := "key1"
	v := "value1"

	var buf bytes.Buffer
	if err := gob.NewEncoder(&buf).Encode(kv{Key: k, Val: v, IsDelete: true, extr1: "xxx", Extr2: 3}); err != nil {
		log.Fatal(err)
	}

	fmt.Printf("buf: %s \n", buf.String())

	fmt.Println("vim-go")

	data := buf.String()
	//data := []byte(buf.String())

	var dataKv kv
	dec := gob.NewDecoder(bytes.NewBufferString(data))
	if err := dec.Decode(&dataKv); err != nil {
		log.Fatalf("raftexample: could not decode message (%v)", err)
	}

	// string can't use nil
	/*
		if dataKv.Val == nil {
			log.Println("we can encoding nil")
		} else {
			log.Printf("we can't encoding , val = %v", dataKv.Val)
		}
	*/

	if dataKv.IsDelete {
		log.Printf("is delete  key: %v", dataKv.Key)
	} else {
		log.Printf("%v :  %v  extr2 %v ", dataKv.Key, dataKv.Val, dataKv.Extr2)

	}

}
