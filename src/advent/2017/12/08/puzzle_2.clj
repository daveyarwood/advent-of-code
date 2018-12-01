(ns advent.2017.12.08.puzzle-2
  (:require [advent.2017.12.08.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn execute-instruction
  [[registers highest-value] instruction]
  (let [result (p1/execute-instruction registers instruction)]
    [result (max highest-value (apply max 0 (vals registers)))]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-08-01-input"))]
    (->> rdr
         line-seq
         (map p1/parse-instruction)
         (reduce execute-instruction [{} 0])
         second
         prn)))
