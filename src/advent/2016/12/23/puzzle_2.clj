(ns advent.2016.12.23.puzzle-2
  (:require [advent.2016.12.12.puzzle-1 :as prev]
            [advent.2016.12.23.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(comment
  "This takes a really long time to run -- I let it run overnight to get the
   solution. I did read up on the problem on the Reddit thread, which was
   enlightening. Basically, this problem is an exercise in optimizing assembly
   code. The program is doing really inefficient multiplication by adding and
   subtracting numbers in multiple registers. So to optimize the assembly code,
   I would look for patterns where the program is basically doing
   multiplication, and rewrite those to a `mul` instruction, which would do
   multiplication.")

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-23-01-input"))]
    (->> rdr
         line-seq
         (mapv prev/parse-instruction)
         (p1/run-instructions {"a" 12})
         prn)))
