(ns advent.2015.12.05.puzzle-2
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(def contains-pair-twice?
  (memoize
    (fn [s pair]
      (let [index     (str/index-of s (apply str pair))
            remaining (partition 2 1 (subs s (+ index 2)))]
        (some #{pair} remaining)))))

(defn nice?
  [s]
  (let [pairs   (partition 2 1 s)
        triples (partition 3 1 s)]
    (and (some (partial contains-pair-twice? s) pairs)
         (some (fn [[a b c]] (= a c)) triples))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-05-01-input"))]
    (->> rdr line-seq (filter nice?) count prn)))

