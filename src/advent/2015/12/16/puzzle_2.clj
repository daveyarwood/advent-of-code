(ns advent.2015.12.16.puzzle-2
  (:require [advent.2015.12.16.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn -main
  []
  (with-open [gift-rdr (io/reader (io/resource "2015-12-16-01-gift"))
              sues-rdr (io/reader (io/resource "2015-12-16-01-sues"))]
    (let [gift-traces (->> gift-rdr line-seq p1/parse-traces)
          sue-facts   (->> sues-rdr line-seq (map p1/parse-sue-facts))]
      (->> sue-facts
           (filter (fn [[_ facts]]
                     (every? (fn [[k v]]
                               (condp contains? k
                                 #{"cats" "trees"}
                                 (> v (get gift-traces k))
                                 #{"pomeranians" "goldfish"}
                                 (< v (get gift-traces k))
                                 ;; else
                                 (= v (get gift-traces k))))
                             facts)))
           prn))))
