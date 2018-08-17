(ns advent.2016.12.12.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn maybe-parse-int
  [x]
  (try
    (Integer/parseInt x)
    (catch NumberFormatException _
      x)))

(defn parse-instruction
  [line]
  (as-> line ?
    (str/split ? #" ")
    (let [[instruction & args] ?]
      (apply vector instruction (map maybe-parse-int args)))))

(defn run-instructions
  [initial-state instructions]
  (loop [i 0, registers initial-state]
    (if (>= i (count instructions))
      registers
      (let [[instruction & args] (get instructions i)]
        (case instruction
          "cpy" (recur (inc i) (let [[x y] args
                                     value (if (number? x) x (get registers x))]
                                 (assoc registers y value)))
          "inc" (recur (inc i) (update registers (first args) inc))
          "dec" (recur (inc i) (update registers (first args) dec))
          "jnz" (recur (let [[x y] args
                             value (if (number? x) x (get registers x))]
                          (if (zero? value) (inc i) (+ i y)))
                       registers))))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-12-01-input"))]
    (->> rdr
         line-seq
         (mapv parse-instruction)
         (run-instructions {"a" 0 "b" 0 "c" 0 "d" 0})
         prn)))
