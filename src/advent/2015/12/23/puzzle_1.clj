(ns advent.2015.12.23.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn parse-instruction
  [line]
  (as-> line ?
    (str/replace ? #"[+,]" "")
    (str/split ? #" ")
    (let [[instruction & args] ?]
      (case instruction
        "jmp" ["jmp" (Integer/parseInt (first args))]
        "jie" ["jie" (first args) (Integer/parseInt (second args))]
        "jio" ["jio" (first args) (Integer/parseInt (second args))]
        (apply vector instruction args)))))

(defn run-instructions
  [initial-state instructions]
  (loop [i 0, registers initial-state]
    (if (>= i (count instructions))
      registers
      (let [[instruction & args] (get instructions i)]
        (case instruction
          "hlf" (recur (inc i) (update registers (first args) #(/ % 2)))
          "tpl" (recur (inc i) (update registers (first args) #(* % 3)))
          "inc" (recur (inc i) (update registers (first args) inc))
          "jmp" (recur (+ i (first args)) registers)
          "jie" (recur (if (even? (get registers (first args)))
                         (+ i (second args))
                         (inc i))
                       registers)
          "jio" (recur (if (= 1 (get registers (first args)))
                         (+ i (second args))
                         (inc i))
                       registers))))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-23-01-input"))]
    (->> rdr
         line-seq
         (mapv parse-instruction)
         (run-instructions {"a" 0 "b" 0})
         prn)))
