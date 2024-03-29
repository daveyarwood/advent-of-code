(ns advent.2015.12.01.puzzle-1
  (:require [clojure.java.io :as    io]
            [advent.io       :refer [char-seq]]))

(defn follow-instruction
  [floor instruction]
  (case instruction
    \( (inc floor)
    \) (dec floor)
    floor))

(defn follow-instructions
  [instructions]
  (reduce follow-instruction 0 instructions))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-01-01-input"))]
    (-> rdr char-seq follow-instructions prn)))
