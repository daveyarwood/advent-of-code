(ns advent.2015.12.15.puzzle-2
  (:require [advent.2015.12.15.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-15-01-input"))]
    (let [ingredients (->> rdr line-seq (map p1/parse-ingredient))]
      (->> (for [proportions (p1/proportions (count ingredients) 100)]
             (p1/score-and-calories ingredients proportions))
           (filter #(= 500 (second %)))
           (map first)
           (apply max)
           prn))))

