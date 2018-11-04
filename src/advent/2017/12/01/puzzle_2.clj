(ns advent.2017.12.01.puzzle-2
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn solve
  [digits]
  (let [halfway  (/ (count digits) 2)
        partners (->> digits cycle (drop halfway))]
    (->> (map vector digits partners)
         (filter (fn [[a b]] (= a b)))
         (map #(-> % first (Character/digit 10)))
         (apply +))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-01-01-input"))]
    (-> rdr slurp str/trim solve prn)))

