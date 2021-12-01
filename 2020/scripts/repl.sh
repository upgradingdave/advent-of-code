#! /bin/bash

clj -M:repl -m nrepl.cmdline --middleware "[cider.nrepl/cider-middleware]"
