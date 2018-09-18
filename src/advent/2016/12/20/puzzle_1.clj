(ns advent.2016.12.20.puzzle-1
  (:require [clojure.edn     :as edn]
            [clojure.java.io :as io]))

(defn parse-range
  [line]
  (->> line
       (re-matches #"(\d+)-(\d+)")
       rest
       (map edn/read-string)))

(defn lowest-number-not-in-a-range
  [ranges]
  (loop [previous-result nil]
    (let [result (reduce (fn [n [lower-bound upper-bound]]
                           (if (<= lower-bound n upper-bound)
                             (inc upper-bound)
                             n))
                         (or previous-result 0)
                         ranges)]
      (if (= previous-result result)
        result
        (recur result)))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-20-01-input"))]
    (->> rdr line-seq (map parse-range) lowest-number-not-in-a-range prn)))


