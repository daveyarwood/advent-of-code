(ns advent.2016.12.23.puzzle-1
  (:require [advent.2016.12.12.puzzle-1 :as prev]
            [clojure.java.io            :as io]))

(defn run-instructions
  [initial-state instructions]
  (loop [i 0, registers initial-state, instructions instructions]
    (if (>= i (count instructions))
      registers
      (let [[instruction & args] (nth instructions i)]
        (case instruction
          "cpy" (recur (inc i)
                       (let [[x y] args
                             value (if (number? x) x (get registers x))]
                         (assoc registers y value))
                       instructions)
          "inc" (recur (inc i)
                       (update registers (first args) inc)
                       instructions)
          "dec" (recur (inc i)
                       (update registers (first args) dec)
                       instructions)
          "jnz" (recur (let [[x y] args
                             value-1 (if (number? x) x (get registers x))
                             value-2 (if (number? y) y (get registers y))]
                         (if (zero? value-1)
                           (inc i)
                           (+ i value-2)))
                       registers
                       instructions)
          "tgl" (recur (inc i)
                       registers
                       (let [[x]   args
                             value (if (number? x) x (get registers x))
                             i     (+ i value)]
                         (if (<= 0 i (dec (count instructions)))
                           (let [[cmd & args] (nth instructions i)
                                 instruction  (case (count args)
                                                1 (if (= "inc" cmd)
                                                    (cons "dec" args)
                                                    (cons "inc" args))
                                                2 (if (= "jnz" cmd)
                                                    (cons "cpy" args)
                                                    (cons "jnz" args)))]
                             (concat (take i instructions)
                                     [instruction]
                                     (drop (inc i) instructions)))
                           instructions))))))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-23-01-input"))]
    (->> rdr
         line-seq
         (mapv prev/parse-instruction)
         (run-instructions {"a" 7})
         prn)))
